package com.emrehmrc.argememory.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.DepartmentSpinnerAdapter;
import com.emrehmrc.argememory.adapter.PersonelSpinnerAdapter;
import com.emrehmrc.argememory.adapter.TagSpinnerAdapter;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.CustomExceptionHandler;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.interfaces.ShareInterface;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.PersonelModel;
import com.emrehmrc.argememory.model.SingletonShare;
import com.emrehmrc.argememory.model.TagModel;
import com.emrehmrc.argememory.popup.SDepartmentPopup;
import com.emrehmrc.argememory.popup.SPersonelPopup;
import com.emrehmrc.argememory.popup.STagPopup;
import com.emrehmrc.argememory.soap.InsertPersSoap;
import com.emrehmrc.argememory.soap.InsertShareSoap;
import com.emrehmrc.argememory.soap.InsertTagSoap;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SShareActivity extends AppCompatActivity {

    ActionBar actionBar;
    Button btnSave, btnCancel;
    EditText edtTask;
    String companiesid, memberid;
    TextView txtDep, txtPers, txtTag;
    Spinner spnDep, spnPers, spnTag;
    ArrayList<PersonelModel> persList;
    ArrayList<TagModel> tagList;
    ArrayList<DepartmentModel> depList;
    ShareInterface shareInterface;
    boolean isok;
    View rootView;
    UUID uuıdshare, uuıdmember, uuidtag;
    ProgressBar pbloading;
    InsertShareSoap soapShare;
    InsertPersSoap soapPers;
    InsertTagSoap soapTag;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        setContentView(R.layout.activity_share);

        init();
        setClickListeners();
        fillSpinners();
    }

    private void fillSpinners() {
        SingletonShare share = SingletonShare.getInstance();
        depList = share.getDepList();
        persList = share.getPersList();
        tagList = share.getTagList();


        //dep
        DepartmentSpinnerAdapter departmentSpinnerAdapter = new DepartmentSpinnerAdapter(this.depList,
                getApplicationContext());
        spnDep.setAdapter(departmentSpinnerAdapter);
        //pers
        PersonelSpinnerAdapter personelSpinnerAdapter = new PersonelSpinnerAdapter(this.persList,
                getApplicationContext());
        spnPers.setAdapter(personelSpinnerAdapter);
        //tag
        TagSpinnerAdapter tagSpinnerAdapter = new TagSpinnerAdapter(this.tagList,
                getApplicationContext());
        spnTag.setAdapter(tagSpinnerAdapter);

    }

    private void setClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtTask.getText().toString().trim().isEmpty()) {
                    if (persList.size() > 0) {
                        insertShare();
                        SingletonShare singletonShare = SingletonShare.getInstance();
                        singletonShare.setNull();
                    } else {
                        new CustomToast().Show_Toast(getApplicationContext(), rootView,
                                "EN AZ 1 PERSONEL SEÇİLMELİ!",
                                Utils.WARNİNG);
                    }

                } else {
                    new CustomToast().Show_Toast(getApplicationContext(), rootView, "YAZI " +
                                    "ALANI BOŞ BIRAKILAMAZ!",
                            Utils.WARNİNG);
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SingletonShare singletonShare = SingletonShare.getInstance();
                singletonShare.setNull();
                finish();
            }
        });
        txtDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SDepartmentPopup.class);
                startActivity(i);
            }
        });
        txtPers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SPersonelPopup.class);
                startActivity(i);
            }
        });
        txtTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), STagPopup.class);
                startActivity(i);
            }
        });
    }

    private void insertShare() {

        uuıdshare = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat mdclock = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat mddate = new SimpleDateFormat("yyyy-MM-dd");
        String clock = mdclock.format(calendar.getTime());
        String day = mddate.format(calendar.getTime());

        InsertShare share = new InsertShare();
        share.execute(companiesid, memberid, edtTask.getText().toString().trim(), day,
                clock,uuıdshare.toString());

    }

    private void insertPers() {
        for (int i = 0; i < persList.size(); i++) {
            InsertPers share = new InsertPers();
            share.execute(uuıdshare.toString(), persList.get(i).getId());
        }
    }

    private void insertTag() {

        if(tagList.size()>0) {
            for (int i = 0; i < tagList.size(); i++) {
                InsertTag share = new InsertTag();
                share.execute(uuıdshare.toString(), tagList.get(i).getId());
            }
        }
        else {
            tagList.clear();
            persList.clear();
            edtTask.setText("");
            spnPers.setAdapter(null);
            spnTag.setAdapter(null);
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
        actionBar.setTitle(R.string.share);
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        memberid = loginPreferences.getString(Utils.ID, "");
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        edtTask = findViewById(R.id.edtTask);
        txtDep = findViewById(R.id.txtDepartment);
        txtPers = findViewById(R.id.txtPersonel);
        txtTag = findViewById(R.id.txtTag);
        spnDep = findViewById(R.id.spnDep);
        spnPers = findViewById(R.id.spnPers);
        spnTag = findViewById(R.id.spnTag);
        pbloading = findViewById(R.id.pbInsertShare);
        depList = new ArrayList<>();
        tagList = new ArrayList<>();
        persList = new ArrayList<>();
        rootView = getWindow().getDecorView().getRootView();
        //edit text done on keyboard
        soapShare = new InsertShareSoap();
        soapPers = new InsertPersSoap();
        soapTag = new InsertTagSoap();


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

    @Override
    protected void onResume() {
        super.onResume();
        fillSpinners();
    }

    private class InsertShare extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            isok = false;
            pbloading.setVisibility(View.VISIBLE);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbloading.setVisibility(View.GONE);
            if (isok) {

                new CustomToast().Show_Toast(getApplicationContext(), rootView, "Başarıyla " +
                      "Eklendi", Utils.SUCCESS);
                insertPers();

            } else {
                new CustomToast().Show_Toast(getApplicationContext(), rootView, "HATA OLUŞTU",
                        Utils.ERROR);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                isok = soapShare.insertShare(params[0], params[1], params[2], params[3],
                        params[4],params[5]);

            } catch (Exception ex) {
                isok = false;
                Log.e("E", ex.getMessage());
            }
            return "";
        }


    }

    private class InsertPers extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pbloading.setVisibility(View.VISIBLE);
            isok = false;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            pbloading.setVisibility(View.GONE);
            if (isok) {
            //    new CustomToast().Show_Toast(getApplicationContext(), rootView, "Başarıyla " +
                       // "Eklendi", Utils.SUCCESS);
                insertTag();

            } else {
                new CustomToast().Show_Toast(getApplicationContext(), rootView, "HATA OLUŞTU",
                        Utils.ERROR);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                isok = soapPers.insertPers(params[0], params[1]);
            } catch (Exception ex) {
                Log.e("E", ex.getMessage());
            }

            return "";
        }


    }

    private class InsertTag extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pbloading.setVisibility(View.VISIBLE);
            isok = false;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbloading.setVisibility(View.GONE);
            if (isok) {
             //   new CustomToast().Show_Toast(getApplicationContext(), rootView, "Başarıyla " +
                 //       "Eklendi", Utils.SUCCESS);

            } else {
                new CustomToast().Show_Toast(getApplicationContext(), rootView, "HATA OLUŞTU",
                        Utils.ERROR);
            }


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                isok = soapTag.insertTag(params[0], params[1]);
            } catch (Exception ex) {
                Log.e("E", ex.getMessage());
            }

            return "";
        }


    }
}
