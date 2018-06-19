package com.emrehmrc.argememory.popup;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.activity.ShareActivity;
import com.emrehmrc.argememory.adapter.DepartmentPopupAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.DepartmentModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DepartmentPopup extends AppCompatActivity {

    RecyclerView recyclerView;
    DepartmentPopupAdapter adapter;
    ArrayList<DepartmentModel> datalist;
    String companiesid = "";
    ConnectionClass connectionClass;
    String z;
    Boolean isSuccess;
    ArrayList<DepartmentModel> selectedList;
    Button btnOk;
    ProgressBar pbDep;
    TextView txtall;
    boolean isall;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_popup);
        init();
        setClickListeners();
        FillDepartment fillDepartment = new FillDepartment();
        String query = "select d.ID,d.NAME from DEPARTMANT as d ,COMPANIES as c where c" +
                ".ID='" + companiesid + "'";
        fillDepartment.execute(query);

    }

    private void setClickListeners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < datalist.size(); i++) {

                    if (datalist.get(i).isOk()) {
                        selectedList.add(datalist.get(i));
                    }
                }
                if (selectedList.isEmpty()) {
                    selectedList.add(new DepartmentModel(false, "BOŞ GİRİLEMEZ", "-1"));
                }
                //send data to personel
                Intent intent = new Intent(getApplicationContext(), PersonelPopup.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",selectedList);
                intent.putExtra("deplist",args);
                startActivity(intent);
            }
        });
        txtall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isall) {
                    for (int i = 0; i < datalist.size(); i++) {
                        datalist.get(i).setOk(false);
                    }
                    isall = false;
                } else if (!isall) {
                    for (int i = 0; i < datalist.size(); i++) {
                        datalist.get(i).setOk(true);
                    }
                    isall = true;
                }

                adapter = new DepartmentPopupAdapter(datalist, getApplicationContext());
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                txtall.setText(getText(R.string.all) + "(" + datalist.size() + ")");


            }
        });
    }

    private void init() {
        connectionClass = new ConnectionClass();
        recyclerView = findViewById(R.id.popupdepartmans);
        datalist = new ArrayList<>();
        selectedList = new ArrayList<>();
        btnOk = findViewById(R.id.btnOk);
        pbDep = findViewById(R.id.pbDep);
        txtall = findViewById(R.id.txtall);
        isall = false;
        txtall.setText(getText(R.string.all) + "(" + datalist.size() + ")");
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
    }


    private class FillDepartment extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            z = "";
            isSuccess = false;
            datalist = new ArrayList<>();
            // datalist.add(new DepartmentModel(false, getString(R.string.all), ""));
            pbDep.setVisibility(View.VISIBLE);


        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbDep.setVisibility(View.GONE);
            adapter = new DepartmentPopupAdapter(datalist, getApplicationContext());
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            txtall.setText(getText(R.string.all) + "(" + datalist.size() + ")");


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
                        DepartmentModel temp = new DepartmentModel();
                        temp.setOk(false);
                        temp.setId(rs.getString("ID"));
                        temp.setText(rs.getString("NAME"));
                        datalist.add(temp);
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
