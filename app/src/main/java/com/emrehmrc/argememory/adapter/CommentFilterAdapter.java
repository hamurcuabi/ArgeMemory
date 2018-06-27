package com.emrehmrc.argememory.adapter;

import android.widget.Filter;

import com.emrehmrc.argememory.model.ShareCommentModel;
import com.emrehmrc.argememory.model.ShareModel;

import java.util.ArrayList;

public class CommentFilterAdapter extends Filter {
    CommentAdapter adapter;
    ArrayList<ShareCommentModel> filterList;

    public CommentFilterAdapter(CommentAdapter adapter, ArrayList<ShareCommentModel> filterList) {
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
            ArrayList<ShareCommentModel> filtered = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                //CHECK
                if (filterList.get(i).getComment().toUpperCase().contains(constraint)) {
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

        adapter.datalist = (ArrayList<ShareCommentModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}