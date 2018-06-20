package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.MainTaskModel;
import com.emrehmrc.argememory.popup.TaskManPopup;

import java.util.ArrayList;

public class MainTaskAdapter extends RecyclerView.Adapter<MainTaskAdapter.MyviewHolder>  implements Filterable {

    ArrayList<MainTaskModel> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;
    TaskFilterAdapter filter;

    public MainTaskAdapter(Context context, ArrayList<MainTaskModel> data) {
        layoutInflater = LayoutInflater.from(context);
        this.datalist = data;
        this.mContentxt = context;

    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.maintaskrecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

        holder.setIsRecyclable(false);
        MainTaskModel clicked = datalist.get(position);
        holder.setData(clicked, position);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TaskFilterAdapter(this, datalist);
        }

        return filter;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {


        TextView txtDate, txtTaskCreater, txtTag, txtTotalTaskMan, txtDescription;
        ImageButton imgTaskMans;


        public MyviewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.tvTaskDate);
            txtTaskCreater = itemView.findViewById(R.id.tvTaskCreater);
            txtTag = itemView.findViewById(R.id.tvTaskTag);
            txtDescription = itemView.findViewById(R.id.tvDescription);
            txtTotalTaskMan = itemView.findViewById(R.id.tvTaskTotalMan);
            imgTaskMans = itemView.findViewById(R.id.imgTaskMans);


        }

        @SuppressLint("NewApi")
        public void setData(final MainTaskModel clicked, int position) {
            this.txtDate.setText(clicked.getTaskDate().substring(0, 16));
            this.txtTaskCreater.setText(clicked.getTaskCreater());
            String htmldes = Html.fromHtml(clicked.getTaskDescription()).toString();
            this.txtDescription.setText(Html.fromHtml(htmldes));
            this.txtTag.setText(clicked.getTaskTag());
            this.txtTotalTaskMan.setText(clicked.getTaskCountMan());
            imgTaskMans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContentxt, TaskManPopup.class);
                    i.putExtra("id", clicked.getTaskId());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContentxt.startActivity(i);


                }
            });

        }
    }
}
