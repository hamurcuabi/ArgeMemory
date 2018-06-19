package com.emrehmrc.argememory.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emrehmrc.argememory.R;

public class TagFrag extends Fragment {
    private static final String TAG = "Tag";
    ProgressBar pbLoading;
    private Button btnOk;
    private RecyclerView rcy;
    private TextView txtSelectAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tag_frag,container,false);

        //Inıt componenets
        btnOk=view.findViewById(R.id.btnfragtab);
        pbLoading=view.findViewById(R.id.pbfragtab);
        rcy=view.findViewById(R.id.rcyfragtab);
        txtSelectAll=view.findViewById(R.id.txtfragtab);
        //set listeners
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buton
            }
        });
    txtSelectAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //text
        }
    });

        return view;
    }
}
