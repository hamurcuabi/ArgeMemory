package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.ShareCommentModel;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyviewHolder> implements Filterable {

    ArrayList<ShareCommentModel> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;
    CommentFilterAdapter filter;


    public CommentAdapter(Context context, ArrayList<ShareCommentModel> data) {
        layoutInflater = LayoutInflater.from(context);
        this.datalist = data;
        this.mContentxt = context;
    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //   View v = layoutInflater.inflate(R.layout.shareallrecycler, parent, false);
        View v = LayoutInflater.from(mContentxt).inflate(R.layout.commentrecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        ShareCommentModel clicked = datalist.get(position);
        holder.setData(clicked, position);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CommentFilterAdapter(this, datalist);
        }

        return filter;
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtOwner, txtDescrp;


        public MyviewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.tvTaskDate);
            txtOwner = itemView.findViewById(R.id.tvTaskCreater);
            txtDescrp = itemView.findViewById(R.id.tvDescription);


        }

        @SuppressLint("NewApi")
        public void setData(final ShareCommentModel clicked, int position) {
            this.txtDate.setText(clicked.getDate().substring(0, 16));
            this.txtOwner.setText(clicked.getCommenter());
            this.txtDescrp.setText(clicked.getComment());

        }
    }
}
