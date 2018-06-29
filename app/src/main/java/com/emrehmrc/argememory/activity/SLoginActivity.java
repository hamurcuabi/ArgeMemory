package com.emrehmrc.argememory.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.fragment.LoadingFrag;
import com.emrehmrc.argememory.helper.CustomExceptionHandler;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MemberModel;
import com.emrehmrc.argememory.services.NotificationServices;
import com.emrehmrc.argememory.soap.MemberLoginSoap;
import com.emrehmrc.argememory.sqllite.DataBase;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SLoginActivity extends AppCompatActivity {


    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1111;
    private static Locale myLocale;
    EditText edtUserName, edtPass;
    CheckBox cbRememberMe;
    Button btnLogin;
    View rootView;
    FragmentManager fragmentManager;
    ActionBar actionBar;
    MemberModel memberModel;
    MemberLoginSoap memberLoginSoap;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean remember;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        setContentView(R.layout.activity_login);
        changeLocale("tr");
        init();
        RememberInfo();
        clickListeners();
        isPendingIntent();
        checkAndRequestPermissions();
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
        Intent i = getIntent();
        if (i.getIntExtra("pending", 0) == Utils.PENDING_FROM_NOTİFİATİON) {
            startActivity(new Intent(getApplicationContext(), SMainActivityNavDrawer.class));
            finish();
        }
    }

    private void clickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
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
        rootView = getWindow().getDecorView().getRootView();
        memberLoginSoap = new MemberLoginSoap();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search://do something
                break;
            case R.id.save:
                takeScreenshot();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void FragLoadingAdd() {

        LoadingFrag loadingFrag = new LoadingFrag();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_login, loadingFrag, "loading");
        fragmentTransaction.commit();
    }

    private void FragLoadingRemove() {
        LoadingFrag loadingFrag = (LoadingFrag) fragmentManager.findFragmentByTag("loading");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (loadingFrag != null) {
            fragmentTransaction.remove(loadingFrag);
            fragmentTransaction.commit();
        }

    }

    private void GoMain() {
        finish();
        if (!IsServiceWorking()) {
            Intent intent = new Intent(getApplicationContext(), NotificationServices.class);
            startService(intent);//Servisi başlatır
        }
        Intent intent = new Intent(getApplicationContext(), SMainActivityNavDrawer.class);
        startActivity(intent);
    }

    //Take SS
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imageFile);
            new CustomToast().Show_Toast(getApplicationContext(), rootView, "Ekran Görünütüsü " +
                            "Alındı.Tıklayarak Paylaşabilirsiniz!",
                    Utils.INFO);


        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
    //Take SS

    public boolean IsServiceWorking() {//Servis Çalışıyor mu kontrol eden fonksiyon

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationServices.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAndRequestPermissions() {
        int permissionINTERNET = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionREAD_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionINTERNET != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionREAD_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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

                if (memberModel != null) {
                    loginPrefsEditor.putString(Utils.ID, memberModel.getId());
                    loginPrefsEditor.putString(Utils.NAME, memberModel.getName());
                    loginPrefsEditor.putString(Utils.EMAIL, memberModel.getEmail());
                    loginPrefsEditor.putString(Utils.COMPANIESID, memberModel.getCompID());
                    loginPrefsEditor.putString(Utils.IMAGE, memberModel.getImage());
                    type = 2;
                }


                if (cbRememberMe.isChecked()) {
                    SaveInfoAfterLogin();
                }
                GoMain();

            }
            if (type == 1)
                new CustomToast().Show_Toast(getApplicationContext(), rootView, msj, Utils.ERROR);
                //   else if (type == 2) new CustomToast().Show_Toast(getApplicationContext(), rootView,    msj, Utils.SUCCESS);
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
                    memberModel = memberLoginSoap.loginMember(edtUserName.getText().toString().trim(), edtPass.getText().toString().trim());
                    isSuccess = true;
                    msj = "Başarılı Giriş";
                    type = 2;
                } catch (Exception ex) {
                    msj = "Kullanıcı Adı/Şifre Hatalı!";
                    isSuccess = true;
                    type = 1;
                }
            }
            return msj;
        }


    }
}

