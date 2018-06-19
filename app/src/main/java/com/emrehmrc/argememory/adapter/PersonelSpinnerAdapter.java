package com.emrehmrc.argememory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.model.PersonelModel;

import java.util.ArrayList;

public class PersonelSpinnerAdapter extends BaseAdapter {
    ArrayList<PersonelModel> datalist;
    Context context;
    LayoutInflater inflter;

    public PersonelSpinnerAdapter(ArrayList<PersonelModel> datalist, Context context) {
        this.datalist = datalist;
        this.context = context;
        this.inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.personelpopuprecycler, null);
        TextView names =convertView.findViewById(R.id.txtNamePers);
        CheckBox checkBox=convertView.findViewById(R.id.cbPers);
        names.setText(datalist.get(position).getName());
        checkBox.setChecked(true);
        checkBox.setEnabled(false);
        return convertView;
    }
}
