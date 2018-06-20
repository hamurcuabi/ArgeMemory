package com.emrehmrc.argememory.adapter;

import android.widget.Filter;

import com.emrehmrc.argememory.model.TagModel;

import java.util.ArrayList;

public class TagFilterAdapter extends Filter {
    TagPopupAdapter adapter;
    ArrayList<TagModel> filterList;

    public TagFilterAdapter(TagPopupAdapter adapter, ArrayList<TagModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<TagModel> filtered = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                //CHECK
                if (filterList.get(i).getTag().toUpperCase().contains(constraint)) {
                    //ADD DATA TO FILTERED DATA
                    filtered.add(filterList.get(i));
                }
            }

            results.count = filtered.size();
            results.values = filtered;
        } else {
            results.count = filterList.size();
            results.values = filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.datalist = (ArrayList<TagModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}