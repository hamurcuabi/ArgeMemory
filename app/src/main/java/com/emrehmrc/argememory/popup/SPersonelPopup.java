package com.emrehmrc.argememory.popup;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.PersonelPopupAdapter;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.PersonelModel;
import com.emrehmrc.argememory.model.SingletonShare;
import com.emrehmrc.argememory.soap.PersonelPopupFillSoap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class SPersonelPopup extends AppCompatActivity {


    RecyclerView recyclerView;
    PersonelPopupAdapter adapter;
    ArrayList<PersonelModel> datalist;
    String companiesid = "";
    String z;
    Boolean isSuccess;
    ArrayList<PersonelModel> selectedList;
    Button btnOk, btnBack;
    ProgressBar pbPers;
    TextView txtall;
    boolean isall;
    ArrayList<DepartmentModel> depList;
    ActionBar actionBar;
    View rootView;
    PersonelPopupFillSoap personelPopupFillSoap;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_popup);
        init();
        setClickListeners();
        SingletonShare share = SingletonShare.getInstance();
        depList = share.getDepList();
        fillPersonel();

    }


    private void fillPersonel() {

        if (depList != null && depList.size() > 0) {
            for (int i = 0; i < depList.size(); i++) {
                FillPersonel fillPersonel = new FillPersonel();
                fillPersonel.execute(depList.get(i).getId());

            }
        } else {
            FillPersonelByComp fillPersonel = new FillPersonelByComp();
            fillPersonel.execute(companiesid);
        }


    }

    private void setClickListeners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < datalist.size(); i++) {

                    if (datalist.get(i).isOk()) {
                        selectedList.add(datalist.get(i));
                    }

                }
                if (selectedList.isEmpty()) {
                    new CustomToast().Show_Toast(getApplicationContext(), rootView, "BOŞ " +
                            "GEÇİLEMEZ!", Utils.WARNİNG);
                } else {
                    //send to tag
                    SingletonShare share = SingletonShare.getInstance();
                    share.setPersList(selectedList);
                    finish();

                }


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPersonelPopup.super.onBackPressed();
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
        // Getting actionbar
        actionBar = getSupportActionBar();
        // Setting up logo over actionbar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo_ico);
        // setting actionbar title
        actionBar.setTitle("2-" + getString(R.string.pers_activity));
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");

        recyclerView = findViewById(R.id.popuppersonel);
        selectedList = new ArrayList<>();
        btnOk = findViewById(R.id.btnOkPers);
        btnBack = findViewById(R.id.btnBackPers);
        pbPers = findViewById(R.id.pbPers);
        txtall = findViewById(R.id.txtallPers);
        isall = false;
        datalist = new ArrayList<>();
        rootView = getWindow().getDecorView().getRootView();
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        personelPopupFillSoap=new PersonelPopupFillSoap();
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
                adapter.getFilter().filter(newText);
                return false;

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

                    datalist=personelPopupFillSoap.getMemberByDepId(params[0]);
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }


    }
    private class FillPersonelByComp extends AsyncTask<String, String, String> {

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
                datalist=personelPopupFillSoap.getMemberByCompId(params[0]);
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }


    }
}
