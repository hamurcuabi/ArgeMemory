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
import com.emrehmrc.argememory.adapter.ShareMemberPopUpAdapter;
import com.emrehmrc.argememory.adapter.ShareTagPopUpAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ShareTagPopup extends AppCompatActivity {

    RecyclerView recyclerView;
    ShareTagPopUpAdapter adapter;
    ArrayList<String> datalist;
    String id = "";
    ConnectionClass connectionClass;
    ProgressBar pbL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_man_pop_up);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Intent intent = getIntent();
        pbL = findViewById(R.id.pbL);

        id = intent.getExtras().getString("id");
        connectionClass = new ConnectionClass();

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        recyclerView = findViewById(R.id.popuptaskmans);
        datalist = new ArrayList<>();
        MainTasks mainTasks = new MainTasks();
        String query = "  select TAGNAME from VW_SHARETAG where SHAREID='"+id+"'";
        mainTasks.execute(query);


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

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            adapter = new ShareTagPopUpAdapter(getApplicationContext(), datalist);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            pbL.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Bağlantı Hatası";
                } else {


                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(params[0]);

                    while (rs.next()) {
                        String gecici = (rs.getString("TAGNAME"));
                        datalist.add(gecici);
                        isSuccess = true;
                    }


                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }
    }
}

