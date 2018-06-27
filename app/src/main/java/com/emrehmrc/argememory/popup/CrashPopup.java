package com.emrehmrc.argememory.popup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.emrehmrc.argememory.R;

public class CrashPopup extends AppCompatActivity {

    Button btnSend,btnSendNo;
    String error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_popup);
        Intent intent = getIntent();
         error = intent.getExtras().getString("error");
        btnSend=findViewById(R.id.btnSendYes);
        btnSend=findViewById(R.id.btnSendNo);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"emrhamurcu@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "ArGe Android Crash");
                i.putExtra(Intent.EXTRA_TEXT   , error);
                try {
                    startActivity(Intent.createChooser(i, "Mailinizi Seçiniz..."));
                } catch (android.content.ActivityNotFoundException ex) {

                }

            }
        });
        btnSendNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
