package com.emrehmrc.argememory.activity;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.fragment.FragLoading;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.services.NotificationServices;
import com.emrehmrc.argememory.sqllite.DataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPass;
    CheckBox cbRememberMe;
    Button btnLogin;
    ConnectionClass connectionClass;
    View rootView;
    FragmentManager fragmentManager;
    String memberid;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean remember;
    ActionBar actionBar;
    private static Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        RememberInfo();
        clickListeners();
        isPendingIntent();
        changeLocale("en");

    }
    //Change Locale
    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config

    }

    private void isPendingIntent() {
        Intent i=getIntent();
       if( i.getIntExtra("pending",0)==Utils.PENDING_FROM_NOTİFİATİON){
            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
            finish();
        }
    }

    private void clickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                String query = "select * from MEMBER where EMAIL='" + edtUserName.getText
                        ().toString() + "'" +
                        " and " +
                        "PASSWORD='" + md5(edtPass.getText().toString()) + "'";
                doLogin.execute(query);
            }
        });
    }

    private void SaveInfoAfterLogin() {

        if (cbRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean(Utils.REMEMBER, true);
            loginPrefsEditor.putString(Utils.USERNAME, edtUserName.getText().toString());
            loginPrefsEditor.putString(Utils.PASSWORD, edtPass.getText().toString());
        } else {
            loginPrefsEditor.clear();
        }
        loginPrefsEditor.commit();
    }

    private void RememberInfo() {
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        remember = loginPreferences.getBoolean(Utils.REMEMBER, false);
        if (remember == true) {
            edtUserName.setText(loginPreferences.getString(Utils.USERNAME, ""));
            edtPass.setText(loginPreferences.getString(Utils.PASSWORD, ""));
            cbRememberMe.setChecked(true);
        }

    }

    private void init() {

        // Getting actionbar
        actionBar = getSupportActionBar();
        // Setting up logo over actionbar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo_ico);
        // setting actionbar title
         actionBar.setTitle(R.string.login);
        // setting actionbar subtitle
       // actionBar.setSubtitle("Giriş");

        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        fragmentManager = getFragmentManager();
        connectionClass = new ConnectionClass();
        rootView = getWindow().getDecorView().getRootView();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Finding search menu item
        final MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Setting hint over search menu
        searchView.setQueryHint("Arama yap....");
        // Calling query listener on search menu
        SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                // Showing text that is entered in search menu
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;

            }
        };

        // Implementing query listener
        searchView.setOnQueryTextListener(textListener);
        return true;
    }

    // Implementing click listeners over all menu icons and displaying there
    // texts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search://do something
                break;
            case R.id.save:
                break;


        }
        return super.onOptionsItemSelected(item);
    }
    private void FragLoadingAdd() {

        FragLoading fragLoading = new FragLoading();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_login, fragLoading, "loading");
        fragmentTransaction.commit();
    }

    private void FragLoadingRemove() {
        FragLoading fragLoading = (FragLoading) fragmentManager.findFragmentByTag("loading");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragLoading != null) {
            fragmentTransaction.remove(fragLoading);
            fragmentTransaction.commit();
        }

    }

    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void GoMain() {
        //Run Services
        if (!IsServiceWorking()) {
            Intent intent = new Intent(getApplicationContext(), NotificationServices.class);
            startService(intent);//Servisi başlatır
            //   stopService(intent);//servisi durdurur
        }
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

    }

    private void addToSql() {

        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        memberid = loginPreferences.getString(Utils.ID, "0");
        DataBase db = new DataBase(getApplicationContext());
        if (db.ListAll().isEmpty()) {

            db.Insert("0", memberid);

        } else {
            db.UpdateMemberId(1, memberid);
        }
    }

    public boolean IsServiceWorking() {//Servis Çalışıyor mu kontrol eden fonksiyon

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationServices.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public class DoLogin extends AsyncTask<String, String, String> {
        Boolean isSuccess;
        String msj;
        Integer type;

        @Override
        protected void onPreExecute() {
            type = 1;
            isSuccess = false;
            FragLoadingAdd();
        }

        @Override
        protected void onPostExecute(String r) {

           FragLoadingRemove();
            if (isSuccess) {

                if (cbRememberMe.isChecked()) {
                    SaveInfoAfterLogin();
                }
                GoMain();
                addToSql();
            }
            if (type == 1)
                new CustomToast().Show_Toast(getApplicationContext(), rootView, msj, Utils.ERROR);
            else if (type == 2) new CustomToast().Show_Toast(getApplicationContext(), rootView, msj,
                    Utils.SUCCESS);
            else if (type == 3) new CustomToast().Show_Toast(getApplicationContext(), rootView, msj,
                    Utils.WARNİNG);
            else if (type == 4) new CustomToast().Show_Toast(getApplicationContext(), rootView, msj,
                    Utils.INFO);


        }

        @Override
        protected String doInBackground(String... params) {
            if (edtUserName.getText().toString().trim().equals("") || edtPass.getText().toString
                    ().trim().equals("")) {
                msj = "Kullanıcı Adı ve Şifreyi Giriniz!";
                type = 3;

            } else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        msj = "Bağlantı Hatası";
                        type = 1;
                    } else {

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(params[0]);

                        if (rs.next()) {
                            loginPrefsEditor.putString(Utils.ID, rs.getString("ID"));
                            loginPrefsEditor.putString(Utils.NAME, rs.getString("NAME"));
                            loginPrefsEditor.putString(Utils.EMAIL, rs.getString("EMAIL"));
                            loginPrefsEditor.putString(Utils.COMPANIESID, rs.getString("COMPANIESID"));
                            loginPrefsEditor.putString(Utils.IMAGE, rs.getString("IMAGE"));
                            isSuccess = true;
                            msj = "Başarılı Giriş";
                            type = 2;
                        }
                    }
                } catch (Exception ex) {

                }
            }
            return msj;
        }
    }
}
