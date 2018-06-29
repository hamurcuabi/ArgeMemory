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
import android.support.design.widget.FloatingActionButton;
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
import com.emrehmrc.argememory.adapter.TagPopupAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.LinearLayoutManagerWithSmoothScroller;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.SingletonShare;
import com.emrehmrc.argememory.model.TagModel;
import com.emrehmrc.argememory.soap.TagPopupFillTagSoap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;


public class STagPopup extends AppCompatActivity implements AddTagPopup.DialogListener {

    RecyclerView recyclerView;
    TagPopupAdapter adapter;
    ArrayList<TagModel> datalist;
    String companiesid = "";
    ConnectionClass connectionClass;
    String z;
    Boolean isSuccess;
    ArrayList<TagModel> selectedList;
    Button btnOk, btnCancel;
    ProgressBar pbDep;
    TextView txtall;
    boolean isall;
    ActionBar actionBar;
    FloatingActionButton fabTag;
    View rootView;
    TagPopupFillTagSoap tagPopupFillTagSoap;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_popup);
        init();
        setClickListeners();
        fillTag(0);

    }

    public void fillTag(int dialog) {
        String d = String.valueOf(dialog);
        FillTag fillTag = new FillTag();
        fillTag.execute("", d);
    }

    private void setClickListeners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < datalist.size(); i++) {

                    if (datalist.get(i).isOk()) {
                        selectedList.add(datalist.get(i));
                    }
                }
                if (selectedList.isEmpty()) {
                    new CustomToast().Show_Toast(getApplicationContext(), rootView, "BOŞ " +
                            "GEÇİLDİ", Utils.WARNİNG);

                }
                SingletonShare share = SingletonShare.getInstance();
                share.setTagList(selectedList);
                finish();

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

                adapter = new TagPopupAdapter(datalist, getApplicationContext());
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                txtall.setText(getText(R.string.all) + "(" + datalist.size() + ")");


            }
        });
        fabTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTag();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        actionBar.setTitle("3-" + getString(R.string.tag_activity));
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");
        connectionClass = new ConnectionClass();
        recyclerView = findViewById(R.id.popupdepartmans);
        datalist = new ArrayList<>();
        selectedList = new ArrayList<>();
        btnOk = findViewById(R.id.btnOk);
        pbDep = findViewById(R.id.pbDep);
        txtall = findViewById(R.id.txtall);
        fabTag = findViewById(R.id.fabaddtag);
        btnCancel = findViewById(R.id.btnCancelTagPop);
        isall = false;
        txtall.setText(getText(R.string.all) + "(" + datalist.size() + ")");
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        rootView = getWindow().getDecorView().getRootView();
        tagPopupFillTagSoap = new TagPopupFillTagSoap();
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
    private void openAddTag() {
        AddTagPopup addTagPopup = new AddTagPopup();
        addTagPopup.show(getSupportFragmentManager(), "Etiket Ekleme");
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
                adapter.getFilter().filter(newText);
                return false;

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
                takeScreenshot();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void isClosed(boolean isclosed) {
        if (isclosed) {
            fillTag(1);

        }
    }


    private class FillTag extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            z = "";
            isSuccess = false;
            datalist = new ArrayList<>();
            pbDep.setVisibility(View.VISIBLE);
            datalist.clear();

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            pbDep.setVisibility(View.GONE);
            adapter = new TagPopupAdapter(datalist, getApplicationContext());
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getApplicationContext()));

            if (z.equals("1")) {
                recyclerView.smoothScrollToPosition(datalist.size() - 1);
                datalist.get(datalist.size() - 1).setOk(true);
                recyclerView.smoothScrollToPosition(datalist.size() - 1);

            }
            txtall.setText(getText(R.string.all) + "(" + datalist.size() + ")");


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                datalist = tagPopupFillTagSoap.fillTag(companiesid);
                isSuccess = true;
                z = params[1];


            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
            }

            return z;
        }


    }
}
