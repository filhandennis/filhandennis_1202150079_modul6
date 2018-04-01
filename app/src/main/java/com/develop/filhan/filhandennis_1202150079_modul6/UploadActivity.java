package com.develop.filhan.filhandennis_1202150079_modul6;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;

    private EditText txtTitle, txtCaption;
    private ImageView lblImage;
    private Button btnSelect, btnUpload;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        auth=FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase=mFirebaseDatabase.getReference("photos");

        txtTitle=(EditText)findViewById(R.id.txtUploadTitle);
        txtCaption=(EditText)findViewById(R.id.txtUploadCaption);
        lblImage=(ImageView)findViewById(R.id.lblUploadImage);
        btnSelect=(Button)findViewById(R.id.btnUploadChoose);
        btnUpload=(Button)findViewById(R.id.btnUploadSave);
        loading=new ProgressDialog(this);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                lblImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upload(){
        if(filePath!=null) {
            loading.setMessage("Wait a Sec ...");
            loading.show();
            //getting the storage reference
            StorageReference sRef = storageReference.child("upload/" + System.currentTimeMillis() + "." + getFileExtension(filePath));
            sRef.putFile(filePath)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loading.dismiss();

                    String user = auth.getCurrentUser().getEmail();
                    String title = txtTitle.getText().toString();
                    String caption = txtCaption.getText().toString();
                    String imgUrl = taskSnapshot.getDownloadUrl().toString();
                    PhotoModel model = new PhotoModel(user, title, caption, imgUrl, 0);

                    String uploadId = mDatabase.push().getKey();
                    Log.d("FIREBASE::KEYID","PUSHID: "+uploadId);
                    model.setId(uploadId);
                    mDatabase.child(uploadId).setValue(model);

                    Toast.makeText(UploadActivity.this, "Upload Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    loading.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            })
            ;
        }
    }

}
