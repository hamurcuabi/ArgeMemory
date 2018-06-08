package com.emrehmrc.argememory.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emrehmrc.argememory.R;

import in.ishankhanna.tinglingsquares.TinglingSquaresView;

public class FragLoading extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_loading,container,false);
        return view;
    }
}
