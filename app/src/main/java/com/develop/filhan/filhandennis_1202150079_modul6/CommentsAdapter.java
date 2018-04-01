package com.develop.filhan.filhandennis_1202150079_modul6;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ASUS on 01/04/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<CommentModel> commentList;

    public CommentsAdapter(List<CommentModel> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentModel comment = commentList.get(position);
        holder.bindTo(comment);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        private TextView lblUser, lblIsi;

        public CommentViewHolder(View itemView) {
            super(itemView);

            lblUser=(TextView)itemView.findViewById(R.id.lblCommentUser);
            lblIsi=(TextView)itemView.findViewById(R.id.lblCommentTXT);
        }

        public void bindTo(CommentModel item){
            CommentModel curr = item;
            lblUser.setText(item.getUser());
            lblIsi.setText(item.getIsi());
        }
    }
}
