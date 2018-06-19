package com.emrehmrc.argememory.fragment;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.DepartmentPopupAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.interfaces.ShareInterface;
import com.emrehmrc.argememory.model.DepartmentModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DepartmentsFrag extends Fragment {
    private static final String TAG = "Department";

    ProgressBar pbLoading;
    DepartmentPopupAdapter adapter;
    ArrayList<DepartmentModel> datalist;
    String companiesid = "";
    ConnectionClass connectionClass;
    String z;
    Boolean isSuccess;
    ArrayList<DepartmentModel> selectedList;
    boolean isall;
    private Button btnOk;
    private RecyclerView rcy;
    private TextView txtSelectAll;
    private SharedPreferences loginPreferences;
    ShareInterface shareInterface;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.departments_frag, container, false);

        //Inıt componenets
        btnOk = view.findViewById(R.id.btnfragtab);
        pbLoading = view.findViewById(R.id.pbfragtab);
        rcy = view.findViewById(R.id.rcyfragtab);
        txtSelectAll = view.findViewById(R.id.txtfragtab);
        //set listeners
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedList=new ArrayList<>();
                for (int i = 1; i < datalist.size(); i++) {

                    if (datalist.get(i).isOk()) {
                        selectedList.add(datalist.get(i));
                    }

                }
                if (selectedList.isEmpty()) {
                    selectedList.add(new DepartmentModel(false, "BOŞ GİRİLEMEZ", "-1"));
                }
                ShareInterface shareInterface = (ShareInterface) getActivity();
                shareInterface.dialogDep(selectedList);
                getActivity().getFragmentManager().popBackStack();


            }
        });
        txtSelectAll.setOnClickListener(new View.OnClickListener() {
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


                adapter = new DepartmentPopupAdapter(datalist, getActivity().getApplicationContext());
                rcy.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rcy.setLayoutManager(linearLayoutManager);
                txtSelectAll.setText(getText(R.string.all) + "(" + datalist.size() + ")");
            }
        });
        //other
        connectionClass = new ConnectionClass();

        //data
        loginPreferences = getActivity().getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        FillDepartment fillDepartment = new FillDepartment();
        String query = "select d.ID,d.NAME from DEPARTMANT as d ,COMPANIES as c where c" +
                ".ID='" + companiesid + "' ";
        fillDepartment.execute(query);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareInterface = (ShareInterface) getActivity();
    }

    private class FillDepartment extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            z = "";
            isSuccess = false;
            datalist = new ArrayList<>();
            pbLoading.setVisibility(View.VISIBLE);


        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbLoading.setVisibility(View.GONE);
            adapter = new DepartmentPopupAdapter(datalist, getActivity());
            rcy.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rcy.setLayoutManager(linearLayoutManager);
            txtSelectAll.setText(getText(R.string.all) + "(" + datalist.size() + ")");
            shareInterface.dialogDep(selectedList);


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
