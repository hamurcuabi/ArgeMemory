package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emrehmrc.argememory.R;

import java.util.ArrayList;

public class ShareTagPopUpAdapter extends RecyclerView.Adapter<ShareTagPopUpAdapter.MyviewHolder>
      {

    ArrayList<String> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;

    public ShareTagPopUpAdapter(Context context, ArrayList<String> data) {
        layoutInflater = LayoutInflater.from(context);
        this.datalist = data;
        this.mContentxt=context;

    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.tagpopuprecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

        String clicked = datalist.get(position);
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
            txtFullName=(TextView)itemView.findViewById(R.id.tvFullName);

        }

        @SuppressLint("NewApi")
        public void setData(final String clicked, int position) {
            this.txtFullName.setText(clicked);

        }
    }
}
