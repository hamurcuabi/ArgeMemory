package com.emrehmrc.argememory.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.ShareAdapter;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.ShareModel;
import com.emrehmrc.argememory.popup.UpdateDescpPopup;
import com.emrehmrc.argememory.soap.ShowAllShareSoap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SShowAllShareActivity extends AppCompatActivity implements UpdateDescpPopup
        .DialogListenerDescp {

    Button btnDatePicker, btnDatePicker2;
    EditText txtDate, txtTime;
    RecyclerView recyclerView;
    ShareAdapter adapter;
    ArrayList<ShareModel> datalist;
    String companiesid = "";
    String z, qdate, qclock, qdate2;
    Boolean isSuccess;
    ArrayList<ShareModel> selectedList;
    ProgressBar pbDep;
    boolean isall;
    ActionBar actionBar;
    View rootView;
    TextView txtOk, txtAll;
    FragmentManager manager;
    FloatingActionButton fab;
    ShowAllShareSoap soap;
    private int mYear, mMonth, mDay;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_share);

        init();
        setClickListeners();
        fillAll();
        manager = getSupportFragmentManager();
        //


    }

    private void fillAll() {
        FillShare fillShare = new FillShare();
        fillShare.execute(companiesid);
    }

    private void setClickListeners() {
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        btnDatePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getDate();
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SShowAllShareActivity
                        .this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtTime.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                qdate2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                btnDatePicker2.setText(qdate2);
                                if (!qdate.trim().isEmpty()) {
                                    fillShareByDate();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fillAll();


            }
        });
        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fillAll();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fillShareByDate() {
        FillShareByDate fillShare = new FillShareByDate();
        fillShare.execute(companiesid, qdate, qdate2);
    }


    private void getDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SShowAllShareActivity
                .this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(year + "-" + dayOfMonth + "-" + (monthOfYear + 1));
                        qdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        btnDatePicker.setText(qdate);
                        if (!qdate2.trim().isEmpty()) {
                            fillShareByDate();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void init() {
        actionBar = getSupportActionBar();
        // Setting up logo over actionbar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo_ico);
        // setting actionbar title
        actionBar.setTitle(getString(R.string.sharing));
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");
        recyclerView = findViewById(R.id.allsharerecycler);
        datalist = new ArrayList<>();
        selectedList = new ArrayList<>();
        btnDatePicker = findViewById(R.id.btn_date);
        btnDatePicker2 = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        pbDep = findViewById(R.id.pbDep);
        txtAll = findViewById(R.id.txtAll);
        txtDate.setEnabled(false);
        txtTime.setEnabled(false);
        isall = false;
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        rootView = getWindow().getDecorView().getRootView();
        txtOk = findViewById(R.id.txtOK);
        qdate2 = "";
        qdate = "";
        qclock = "";
        fab = findViewById(R.id.fabAllShare);
        soap = new ShowAllShareSoap();


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
    public void isClosed(boolean isclosed) {
        if (isclosed) {
           fillAll();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       // fillAll();
    }

    private class FillShare extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            z = "";
            isSuccess = false;
            datalist = new ArrayList<>();
            pbDep.setVisibility(View.VISIBLE);
            selectedList = new ArrayList<>();
            selectedList.clear();
            datalist.clear();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            if (datalist.size() > 0) {
                txtAll.setText(getString(R.string.txtsharing) + "(" + datalist.size() + ")");
                pbDep.setVisibility(View.GONE);
                adapter = new ShareAdapter(getApplicationContext(), datalist, manager);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);

            }


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                datalist = soap.allShare(params[0]);

            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }


    }

    private class FillShareByDate extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            z = "";
            isSuccess = false;
            datalist = new ArrayList<>();
            pbDep.setVisibility(View.VISIBLE);
            selectedList = new ArrayList<>();
            selectedList.clear();
            datalist.clear();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            if (datalist.size() > 0) {
                txtAll.setText(getString(R.string.txtsharing) + "(" + datalist.size() + ")");
                pbDep.setVisibility(View.GONE);
                adapter = new ShareAdapter(getApplicationContext(), datalist, manager);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);

            }


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                datalist = soap.allShareByDate(params[0], params[1], params[2]);

            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }


    }

}