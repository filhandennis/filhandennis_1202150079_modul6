package com.develop.filhan.filhandennis_1202150079_modul6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    public static String EXTRA_ITEM = "EXTRA_VIEW_ITEM";

    //Attibut Komponen View
    private TextView lblUser, lblTitle, lblCaption, lblLike;
    private ImageView lblImg;
    private EditText txtComment;
    private Button btnPost;
    private ProgressBar progressBar;
    private RecyclerView listComments;
    private ListView listViewComments;

    //Attribut List
    private List<CommentModel> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //Inisialisasi Komponen View
        lblUser=(TextView)findViewById(R.id.lblViewUsername);
        lblTitle=(TextView)findViewById(R.id.lblViewTitle);
        lblCaption=(TextView)findViewById(R.id.lblViewCaption);
        lblLike=(TextView)findViewById(R.id.lblViewLike);
        lblImg=(ImageView) findViewById(R.id.lblViewImage);
        txtComment=(EditText)findViewById(R.id.txtViewComment);
        progressBar=(ProgressBar) findViewById(R.id.progressViewImage);
        btnPost=(Button)findViewById(R.id.btnComment);
        listComments=(RecyclerView) findViewById(R.id.commentsRecycler);
        listComments.setLayoutManager(new LinearLayoutManager(this));
        listViewComments=(ListView)findViewById(R.id.commentsList);

        //Pengecekan Item yang dikirimkan intent Lain
        Intent ini = getIntent();
        if(ini.getParcelableExtra(EXTRA_ITEM)!=null){
            final PhotoModel data = ini.getParcelableExtra(EXTRA_ITEM);
            setTitle(data.getTitle());
            lblUser.setText(data.getUser());
            lblTitle.setText(data.getTitle());
            lblCaption.setText(data.getCaption());
            lblLike.setText(""+data.getLike());
            Glide.with(this).load(data.getImgUrl()).error(R.drawable.ic_broken_image_black_24dp)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(lblImg);
            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertComment(data.getId());
                }
            });

            commentsList=new ArrayList<>();
            loadComment(data.getId());
        }
    }

    /*
    * Method yang digunakan untuk menambahkan komentar
    * */
    public void insertComment(String id){
        String comment = txtComment.getText().toString();
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("photos").child(id).child("comments");
        //DatabaseReference commentpost = FirebaseDatabase.getInstance().getReference().child("photos").child(id);
        String postId = post.push().getKey();
        CommentModel item = new CommentModel(user, comment);
        item.setId(postId);
        post.child(postId).setValue(item);
        txtComment.setText("");
        Toast.makeText(this, "Comment Inserted", Toast.LENGTH_SHORT).show();
    }

    /*
    * Method yang digunakan untuk mengambil data komen berdasarkan post yg dibuka.
    * */
    public void loadComment(String id){
        final List<CommentModel> comments = new ArrayList<>();
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("photos").child(id).child("comments");

        //digunakan untuk mengambil Firebase Data berdasarkan referensi yang ada
        // kemudian diubah menjadi list yang dapat digunakan pada List
        commentsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    CommentModel comment = postSnapshot.getValue(CommentModel.class);
                    comments.add(comment);
                    list.add(""+comment.getUser()+" -- "+comment.getIsi());
                }
                Log.d("FIREBASE::COMMENTS","num:"+comments.size());
                commentsList=comments;

                CommentsAdapter dataAdapter = new CommentsAdapter(comments);
                listComments.setAdapter(dataAdapter);

                ArrayAdapter listAdapter = new ArrayAdapter(ViewActivity.this, android.R.layout.simple_list_item_1, list);
                listViewComments.setAdapter(listAdapter);

                lblLike.setText(""+comments.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
