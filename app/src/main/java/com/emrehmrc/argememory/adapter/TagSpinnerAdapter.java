package com.emrehmrc.argememory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.TagModel;

import java.util.ArrayList;

public class TagSpinnerAdapter extends BaseAdapter {
    ArrayList<TagModel> datalist;
    Context context;
    LayoutInflater inflter;

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    public TagSpinnerAdapter(ArrayList<TagModel> datalist, Context context) {
        this.datalist = datalist;
        this.context = context;
        this.inflter=LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.personelpopuprecycler, null);
        TextView names = convertView.findViewById(R.id.txtNamePers);
        CheckBox checkBox = convertView.findViewById(R.id.cbPers);
        names.setText(datalist.get(position).getTag());
        checkBox.setChecked(true);
        checkBox.setEnabled(false);
        return convertView;
    }
}
