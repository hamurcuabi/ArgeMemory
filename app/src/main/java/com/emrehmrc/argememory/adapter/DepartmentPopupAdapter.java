package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.SingletonShare;

import java.util.ArrayList;

public class DepartmentPopupAdapter extends RecyclerView.Adapter<DepartmentPopupAdapter
        .MyviewHolder> implements Filterable {

    ArrayList<DepartmentModel> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;
    DepartmentFilterAdapter filter;


    public DepartmentPopupAdapter(ArrayList<DepartmentModel> datalist, Context mContentxt) {
        this.datalist = datalist;
        this.mContentxt = mContentxt;
        layoutInflater = LayoutInflater.from(mContentxt);
        // setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.departmentpopuprecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {

      //  holder.setIsRecyclable(false);
        holder.setData(datalist.get(position), position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datalist.get(position).setOk(!datalist.get(position).isOk());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new DepartmentFilterAdapter(this, datalist);
        }

        return filter;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView txtFullName;
        CheckBox checkBox;


        public MyviewHolder(View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.tvFullName);
            checkBox = itemView.findViewById(R.id.cbdep);

        }

        @SuppressLint("NewApi")
        public void setData(final DepartmentModel clicked, int position) {
            this.txtFullName.setText(clicked.getText());
            this.checkBox.setChecked(clicked.isOk());


        }
    }
}
