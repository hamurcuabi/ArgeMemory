package com.emrehmrc.argememory.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.ExpandListAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.Utils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import in.ishankhanna.tinglingsquares.TinglingSquaresView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingnew);
        final TinglingSquaresView tsv = (TinglingSquaresView) findViewById(R.id.tinglingSquaresView);
        tsv.runAnimation(0);

    }


}
