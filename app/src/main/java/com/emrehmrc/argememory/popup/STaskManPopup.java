package com.emrehmrc.argememory.popup;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.TaskManPopUpAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.CustomExceptionHandler;
import com.emrehmrc.argememory.model.TaskManModel;
import com.emrehmrc.argememory.soap.TaskMembersSoap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class STaskManPopup extends AppCompatActivity {

    RecyclerView recyclerView;
    TaskManPopUpAdapter adapter;
    ArrayList<TaskManModel> datalist;
    String id = "";
    ProgressBar pbL;
    TaskMembersSoap taskMembersSoap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        setContentView(R.layout.activity_task_man_pop_up);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        pbL=findViewById(R.id.pbL);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        recyclerView = findViewById(R.id.popuptaskmans);
        datalist = new ArrayList<>();
        MainTasks mainTasks = new MainTasks();
                mainTasks.execute("");


    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class MainTasks extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            pbL.setVisibility(View.VISIBLE);
            datalist = new ArrayList<>();


        }

        @Override
        protected void onPostExecute(String r) {

            adapter = new TaskManPopUpAdapter(getApplicationContext(), datalist);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            pbL.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {

            try {

             datalist=taskMembersSoap.taskMembers(id);
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }
    }
}

