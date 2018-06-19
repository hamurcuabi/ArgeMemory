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
import com.emrehmrc.argememory.adapter.PersonelPopupAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.PersonelModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PersonelPopup extends AppCompatActivity {


    RecyclerView recyclerView;
    PersonelPopupAdapter adapter;
    ArrayList<PersonelModel> datalist;
    String companiesid = "";
    ConnectionClass connectionClass;
    String z;
    Boolean isSuccess;
    ArrayList<PersonelModel> selectedList;
    Button btnOk;
    ProgressBar pbPers;
    TextView txtall;
    boolean isall;
    ArrayList<DepartmentModel> depList;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_popup);
        init();
        setClickListeners();
        //get data
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("deplist");
        depList = (ArrayList<DepartmentModel>) args.getSerializable("ARRAYLIST");
        fillPersonel();

    }

    private void fillPersonel() {

        if (depList != null) {
            for (int i = 0; i < depList.size(); i++) {
                FillPersonel fillPersonel = new FillPersonel();
                String query = "select m.ID,m.FULLNAME from VW_MEMBERDETAIL as m where m" +
                        ".DEPARTMENTID='" + depList.get(i).getId() + "' and ISACTIVE='1'";
                fillPersonel.execute(query);

            }
        }


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
                    selectedList.add(new PersonelModel("-1", "BOŞ GİRİLEMEZ", false));
                }

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


                adapter = new PersonelPopupAdapter(datalist, getApplicationContext());
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
        recyclerView = findViewById(R.id.popuppersonel);
        selectedList = new ArrayList<>();
        btnOk = findViewById(R.id.btnOkPers);
        pbPers = findViewById(R.id.pbPers);
        txtall = findViewById(R.id.txtallPers);
        isall = false;
        datalist = new ArrayList<>();
    }

    private class FillPersonel extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            z = "";
            isSuccess = false;
            pbPers.setVisibility(View.VISIBLE);

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbPers.setVisibility(View.GONE);
            adapter = new PersonelPopupAdapter(datalist, getApplicationContext());
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
                        PersonelModel temp = new PersonelModel();
                        temp.setOk(false);
                        temp.setId(rs.getString("ID"));
                        temp.setName(rs.getString("FULLNAME"));
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
