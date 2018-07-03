package com.emrehmrc.argememory.popup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.soap.CrashReportEmailSoap;

import okhttp3.internal.Util;

public class SCrashPopup extends AppCompatActivity {

    Button btnClose;
    String error;
    CrashReportEmailSoap crashReportEmailSoap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_popup);
        Intent intent = getIntent();
        error = intent.getExtras().getString("error");
        crashReportEmailSoap=new CrashReportEmailSoap();
        btnClose=findViewById(R.id.btnSendNo);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new SendMail().execute("");

    }
    private class SendMail extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {
            try {
                crashReportEmailSoap.sendMail(error, Utils.DEVELOPER_EMAIL);
               crashReportEmailSoap.sendMail(error, Utils.MAIN_DEVELOPER_EMAIL);

            }
            catch (Exception ex){

            }
            return  "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
