package com.emrehmrc.argememory.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.ExpandListAdapter;
import com.emrehmrc.argememory.adapter.MainTaskAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MainTaskModel;
import com.emrehmrc.argememory.popup.DepartmentPopup;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener {
    private static Locale myLocale;
    public List<String> list_parent;
    public ExpandListAdapter expand_adapter;
    public HashMap<String, List<String>> list_child;
    public ExpandableListView expandlist_view;
    public List<String> parent_list_1, parent_list_2;
    ConnectionClass connectionClass;
    CompactCalendarView compactCalendar;
    RecyclerView recyclerView;
    HashSet<String> hset;
    ArrayList<MainTaskModel> datalist;
    MainTaskAdapter mainTaskAdapter;
    TextView txtCountTask;
    ArrayList<String> memberList;
    MainTaskModel gecici;
    String memberid, imgPath;
    ImageView imgAvatar;
    NavigationView navigationView;
    View headerView;
    TextView txtUsername, txtUsermail;
    ActionBar actionBar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    View rootView;
    String eng, tr;
    boolean lang = false;
    private SimpleDateFormat dateDefault;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        defaultinit();
        init();
        ExpandListPrepare();
        clicklisteners();
        getMainTasks();

    }


    private void getMainTasks() {
        MainTasks mainTasks = new MainTasks();
        String query = "select  * from VW_MAINTASKLIST where ID in\n" +
                "(select ID from VW_TASKMEMBER where  (PUBLISHERID='" + memberid + "' or " +
                "MEMBERID='" + memberid + "') and ISFINISH='0') order by (ID)";

        mainTasks.execute(query);
    }

    private void defaultinit() {
        //Default gelenler
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        //default gelenler
    }

    private void clicklisteners() {
        txtCountTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainTasks();
            }
        });
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+03"));
                long oneday = 86400000l;

                MainTasks mainTasks = new MainTasks();


                String q = "select * from VW_MAINTASKLIST where DATESTART  >=  '" + sdf.format(new
                        Date(dateClicked.getTime())) + "' and " +
                        "DATESTART  <  '" + sdf.format(new Date(dateClicked.getTime() + oneday))
                        + "' and ID in (select ID from VW_TASKMEMBER where  (PUBLISHERID='" +
                        memberid + "' or MEMBERID='" + memberid + "') and ISFINISH='0') order by (ID)";
                mainTasks.execute(q);


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                getSupportActionBar().setTitle(sdf.format(firstDayOfNewMonth));
            }
        });

        expandlist_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void ExpandListPrepare() {
        list_parent = new ArrayList<>();  // başlıklarımızı listemelek için oluşturduk
        list_child = new HashMap<>(); // başlıklara bağlı elemenları tutmak için oluşturduk
        list_parent.add("GALATASARAY");  // ilk başlığı giriyoruz
        list_parent.add("FENERBAHCE");   // ikinci başlığı giriyoruz
        parent_list_1 = new ArrayList<>();  // ilk başlık için alt elemanları tanımlıyoruz
        parent_list_1.add("Muslera");
        parent_list_1.add("Sabri");
        parent_list_1.add("Chejdou");
        parent_list_2 = new ArrayList<>(); // ikinci başlık için alt elemanları tanımlıyoruz
        parent_list_2.add("Volkan Demirel");
        parent_list_2.add("Gökhan Gönül");
        list_child.put(list_parent.get(0), parent_list_1); // ilk başlığımızı ve onların
        // elemanlarını HashMap sınıfında tutuyoruz
        list_child.put(list_parent.get(1), parent_list_2); // ikinci başlığımızı ve onların elemanlarını HashMap sınıfında tutuyoruz
        expand_adapter = new ExpandListAdapter(getApplicationContext(), list_parent, list_child);
        expandlist_view.setAdapter(expand_adapter);
        expandlist_view.setClickable(true);

    }

    private void init() {

        // Getting actionbar
        actionBar = getSupportActionBar();
        // Setting up logo over actionbar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo_ico);
        // setting actionbar title
        actionBar.setTitle(R.string.pano);
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");
        expandlist_view = findViewById(R.id.expand_listview);
        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        txtCountTask = findViewById(R.id.txtCountTasks);
        recyclerView = findViewById(R.id.recyclerview);
        imgAvatar = headerView.findViewById(R.id.imgProfil);
        txtUsername = headerView.findViewById(R.id.txtUsername);
        txtUsermail = headerView.findViewById(R.id.txtUsermail);

        connectionClass = new ConnectionClass();
        dateDefault = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());

        datalist = new ArrayList<>();
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        memberid = loginPreferences.getString(Utils.ID, "");
        imgPath = loginPreferences.getString(Utils.IMAGE, "");
        txtUsername.setText(loginPreferences.getString(Utils.NAME, ""));
        txtUsermail.setText(loginPreferences.getString(Utils.EMAIL, ""));

        if (!imgPath.equals("")) {
            Picasso.get().load("http://argememory.com/assets/images/" + imgPath + ".jpg").resize
                    (64, 64).into
                    (imgAvatar);
        } else {
            imgAvatar.setImageResource(R.mipmap.ic_launcher_round);
        }
        rootView = getWindow().getDecorView().getRootView();
        eng = "en";
        tr = "";

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

                mainTaskAdapter.getFilter().filter(newText);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(getApplicationContext(), DepartmentPopup.class);
            startActivity(i);


        } else if (id == R.id.nav_lang) {

            if (lang) {
                changeLocale(tr);
                new CustomToast().Show_Toast(getApplicationContext(),rootView,"Türkçe Dili " +
                        "Seçildi",Utils.INFO);
                lang=false;
                
            } else {
                changeLocale(eng);
                new CustomToast().Show_Toast(getApplicationContext(),rootView,"İngilizce Dili " +
                        "Seçildi",Utils.INFO);
                lang=true;
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public class MainTasks extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            memberList = new ArrayList<>();
            hset = new HashSet<>();
            datalist.clear();
            recyclerView.setAdapter(null);

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            mainTaskAdapter = new MainTaskAdapter(getApplicationContext(), datalist);
            recyclerView.setAdapter(mainTaskAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //  String dateInString = "2018-04-27";
            txtCountTask.setText("GÖREV LİSTESİ(" + datalist.size() + ")");
            for (int i = 0; i < datalist.size(); i++) {
                Date date = null;
                String dateInString = datalist.get(i).getTaskDate();
                try {
                    date = dateDefault.parse(dateInString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Event ev1 = new Event(Color.RED, date.getTime(), "TASK");
                compactCalendar.addEvent(ev1);

            }


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

                        gecici = new MainTaskModel();
                        gecici.setTaskId(rs.getString("ID"));
                        gecici.setTaskDescription(rs.getString("DESCRIPTION"));
                        gecici.setTaskCreater(rs.getString("PUBLISHERNAME"));
                        gecici.setTaskDate(rs.getString("DATESTART"));
                        gecici.setTaskTag(rs.getString("NAME"));
                        gecici.setTaskCountMan(rs.getString("TOTALMEMBER").toString());
                        datalist.add(gecici);
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
