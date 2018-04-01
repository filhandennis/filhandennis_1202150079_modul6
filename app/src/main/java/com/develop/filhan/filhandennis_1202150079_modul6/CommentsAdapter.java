package com.develop.filhan.filhandennis_1202150079_modul6;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter Comment
 *
 * digunakan untuk mengubah list menjadi item data
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    // List digunakan untuk menampung data Komentar
    private List<CommentModel> commentList;

    //Konstruktor membutuhkan List sebagaidata
    public CommentsAdapter(List<CommentModel> commentList) {
        this.commentList = commentList;
    }

    /*
    * ViewHolder sebagai Layout
    * */
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    /*
    * Method untuk tingkah laku si data
    * */
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentModel comment = commentList.get(position);
        holder.bindTo(comment);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /*
    * Class ViewHolder digunakan untuk keperluan Layout
    * */
    class CommentViewHolder extends RecyclerView.ViewHolder{

        private TextView lblUser, lblIsi;

        public CommentViewHolder(View itemView) {
            super(itemView);

            lblUser=(TextView)itemView.findViewById(R.id.lblCommentUser);
            lblIsi=(TextView)itemView.findViewById(R.id.lblCommentTXT);
        }

        //Method
        public void bindTo(CommentModel item){
            CommentModel curr = item;
            lblUser.setText(item.getUser());
            lblIsi.setText(item.getIsi());
        }
    }
}
