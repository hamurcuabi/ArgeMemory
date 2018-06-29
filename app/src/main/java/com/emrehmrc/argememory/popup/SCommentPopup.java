package com.emrehmrc.argememory.popup;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.CommentAdapter;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.ShareCommentModel;
import com.emrehmrc.argememory.soap.CommentPopupFillCommentSoap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class SCommentPopup extends AppCompatActivity {

    RecyclerView recyclerView;
    CommentAdapter adapter;
    ArrayList<ShareCommentModel> datalist;
    EditText edtNewTag;
    TextView txtComment;
    Button btnOk, btnCancel;
    boolean isok;
    String companiesid = "", memberid;
    View rootView;
    ProgressBar pbL;
    CommentPopupFillCommentSoap commentPopupFillCommentSoap;
    private SharedPreferences loginPreferences;
    private String shareid;
    String date, clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_descp);

        Intent intent = getIntent();
        shareid = intent.getExtras().getString("id");

        rootView = getWindow().getDecorView().getRootView();
        edtNewTag = findViewById(R.id.edtNewTag);
        btnOk = findViewById(R.id.btnAddTag);
        btnCancel = findViewById(R.id.btnCancelTag);
        recyclerView = findViewById(R.id.rcyComments);
        txtComment = findViewById(R.id.txtNewTag);
        pbL = findViewById(R.id.pbC);
        datalist = new ArrayList<>();
        commentPopupFillCommentSoap = new CommentPopupFillCommentSoap();
        FillComments fillComments = new FillComments();
        fillComments.execute("");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtNewTag.getText().toString().trim().isEmpty()) {

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
                    date = df.format(Calendar.getInstance().getTime());
                    clock = df2.format(Calendar.getInstance().getTime());
                    loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
                    companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
                    memberid = loginPreferences.getString(Utils.ID, "");

                    InsertComment ınsertComment = new InsertComment();
                    ınsertComment.execute("");
                } else {
                    new CustomToast().Show_Toast(getApplicationContext(), rootView, "Yazı Alanı Boş Bırakılamaz",
                            Utils.ERROR);
                }

            }
        });
    }


    private class InsertComment extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            isok = false;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            if (isok) {
                new CustomToast().Show_Toast(getApplicationContext(), rootView, "Başarıyla Güncellendi",
                        Utils.SUCCESS);
                finish();

            } else {
                new CustomToast().Show_Toast(getApplicationContext(), rootView, "Hata Oluştu", Utils.ERROR);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                isok = commentPopupFillCommentSoap.insertComment(companiesid,shareid,memberid,
                        edtNewTag.getText().toString().trim(),date,clock);
            } catch (Exception ex) {
                isok = false;
            }
            return "";
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class FillComments extends AsyncTask<String, String, String> {
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

            if (datalist.size() > 0) {
                txtComment.setText(getString(R.string.commnets) + "(" + datalist.size() + ")");
                adapter = new CommentAdapter(getApplicationContext(), datalist);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);

            }
            pbL.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                datalist = commentPopupFillCommentSoap.getComment(shareid);
                isSuccess = true;
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }
    }
}

