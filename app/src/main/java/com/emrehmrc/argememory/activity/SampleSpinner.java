package com.emrehmrc.argememory.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.emrehmrc.argememory.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class SampleSpinner extends AppCompatActivity {

    SearchableSpinner searchableSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_spinner);
        searchableSpinner=findViewById(R.id.spinner);
        searchableSpinner.setTitle("Select Item");
        searchableSpinner.setPositiveButton("OK");


    }
}
