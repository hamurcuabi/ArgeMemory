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
import com.emrehmrc.argememory.adapter.PersonelPopupAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.interfaces.ShareInterface;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.PersonelModel;
import com.emrehmrc.argememory.popup.PersonelPopup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PersonelFrag extends Fragment implements ShareInterface{
    private static final String TAG = "Personel";
    ProgressBar pbLoading;
    PersonelPopupAdapter adapter;
    ArrayList<PersonelModel> datalist;
    String companiesid = "";
    ConnectionClass connectionClass;
    String z;
    Boolean isSuccess;
    ArrayList<PersonelModel> selectedList;
    boolean isall;
    private Button btnOk;
    private RecyclerView rcy;
    private TextView txtSelectAll;
    private SharedPreferences loginPreferences;
    ShareInterface shareInterface;
    ArrayList<DepartmentModel> depList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.personel_frag, container, false);

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
                    selectedList.add(new PersonelModel("-1", "BOŞ GİRİLEMEZ", false));
                }
                ShareInterface shareInterface = (ShareInterface) getActivity();
                shareInterface.dialogPers(selectedList);
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


                adapter = new PersonelPopupAdapter(datalist, getActivity().getApplicationContext());
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




        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareInterface = (ShareInterface) getActivity();
    }

    @Override
    public void dialogDep(ArrayList<DepartmentModel> depList) {
        if(depList!=null) {
            for (int i = 0; i < depList.size(); i++) {
                FillPersonel fillPersonel = new FillPersonel();
                String query = "select m.ID,m.FULLNAME from VW_MEMBERDETAIL as m where m" +
                        ".DEPARTMENTID='" + depList.get(0).getId() + "'";
                fillPersonel.execute(query);

            }
        }
    }

    @Override
    public void dialogPers(ArrayList<PersonelModel> persList) {

    }

    private class FillPersonel extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            z = "";
            isSuccess = false;
            // datalist.add(new DepartmentModel(false, getString(R.string.all), ""));
            pbLoading.setVisibility(View.VISIBLE);


        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbLoading.setVisibility(View.GONE);
            adapter = new PersonelPopupAdapter(datalist, getActivity().getApplicationContext());
            rcy.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rcy.setLayoutManager(linearLayoutManager);
            txtSelectAll.setText(getText(R.string.all)+"("+datalist.size()+")");


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
