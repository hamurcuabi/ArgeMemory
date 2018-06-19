package com.emrehmrc.argememory.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.DepartmentSpinnerAdapter;
import com.emrehmrc.argememory.adapter.SectionsPageAdapter;
import com.emrehmrc.argememory.fragment.DepartmentsFrag;
import com.emrehmrc.argememory.fragment.PersonelFrag;
import com.emrehmrc.argememory.fragment.TagFrag;
import com.emrehmrc.argememory.interfaces.GetDataTabsInterface;
import com.emrehmrc.argememory.model.DepartmentModel;

import java.util.ArrayList;

public class ShareTabActivity extends AppCompatActivity {

    private SectionsPageAdapter msectionsPageAdapter;
    private ViewPager viewPager;
    TabLayout tableLayout;
    ArrayList<DepartmentModel> selectedListdep;
    Spinner spndepfrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_tab);

        //init
         spndepfrag=findViewById(R.id.spndepfrag);
        msectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());
        viewPager=findViewById(R.id.container);
        setupViewPager(viewPager);
        tableLayout=findViewById(R.id.tabs);
        tableLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter=new SectionsPageAdapter(getSupportFragmentManager());
       // adapter.addFragment(new DepartmentsFrag(),"dep");
        adapter.addFragment(new PersonelFrag(),"pers");
        adapter.addFragment(new TagFrag(),"tag");
        viewPager.setAdapter(adapter);
    }


}
