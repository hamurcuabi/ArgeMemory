package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.PersonelModel;

import java.util.ArrayList;

public class PersonelPopupAdapter extends RecyclerView.Adapter<PersonelPopupAdapter.MyviewHolder> {
    ArrayList<PersonelModel> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;

    public PersonelPopupAdapter(ArrayList<PersonelModel> datalist, Context mContentxt) {
        this.datalist = datalist;
        this.mContentxt = mContentxt;
        layoutInflater = LayoutInflater.from(mContentxt);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.personelpopuprecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.setData( datalist.get(position),position);
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

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView txtFullName;
        CheckBox checkBox;


        public MyviewHolder(View itemView) {
            super(itemView);
            txtFullName=itemView.findViewById(R.id.txtNamePers);
            checkBox=itemView.findViewById(R.id.cbPers);

        }

        @SuppressLint("NewApi")
        public void setData(final PersonelModel clicked, int position) {
            this.txtFullName.setText(clicked.getName());
            this.checkBox.setChecked(clicked.isOk());

        }
    }
}
