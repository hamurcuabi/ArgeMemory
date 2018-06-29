package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.TaskManModel;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskManPopUpAdapter extends RecyclerView.Adapter<TaskManPopUpAdapter.MyviewHolder>
        implements Serializable {

    ArrayList<TaskManModel> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;

    public TaskManPopUpAdapter(Context context, ArrayList<TaskManModel> data) {
        layoutInflater = LayoutInflater.from(context);
        this.datalist = data;
        this.mContentxt=context;

    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.taskmanpopuprecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

        TaskManModel clicked = datalist.get(position);
        holder.setData(clicked, position);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView txtFullName;


        public MyviewHolder(View itemView) {
            super(itemView);
            txtFullName=itemView.findViewById(R.id.tvFullName);

        }

        @SuppressLint("NewApi")
        public void setData(final TaskManModel clicked, int position) {
            this.txtFullName.setText(clicked.getMemberName());

        }
    }
}
