package com.emrehmrc.argememory.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.MainTaskAdapter;
import com.emrehmrc.argememory.adapter.NavDrawerListAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.CustomExceptionHandler;
import com.emrehmrc.argememory.helper.OnSwipeTouchListener;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MainTaskModel;
import com.emrehmrc.argememory.model.NavDrawerItem;
import com.emrehmrc.argememory.model.NotifCountModel;
import com.emrehmrc.argememory.services.NotificationServices;
import com.emrehmrc.argememory.soap.MainTaskSoap;
import com.emrehmrc.argememory.soap.NotifCountSoap;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import me.leolin.shortcutbadger.ShortcutBadger;

//


public class SMainActivityNavDrawer extends AppCompatActivity {
    private static Locale myLocale;
    ConnectionClass connectionClass;
    CompactCalendarView compactCalendar;
    RecyclerView recyclerView;
    HashSet<String> hset;
    ArrayList<MainTaskModel> datalist;
    MainTaskAdapter mainTaskAdapter;
    TextView txtCountTask;
    ArrayList<String> memberList;
    String memberid, imgPath;
    ActionBar actionBar;
    View rootView;
    String eng, tr;
    boolean lang = false;
    ProgressBar pbLoading;
    FrameLayout calender;
    LinearLayout layout;
    NotifCountModel notfCount;
    private SimpleDateFormat dateDefault;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    //////////////Navigation/////////////////
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    MainTaskSoap mainTaskSoap;
    NotifCountSoap notifCountSoap;
    String startDate,endDate;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        setContentView(R.layout.activity_nav_drawer);
        init();
        newNav();
        clicklisteners();
        getMainTasks();



    }

    @SuppressLint("ResourceType")
    private void newNav() {

        Notifications notifications=new Notifications();
        notifications.execute("");
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.menuList);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.menuIconList);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.list_slidermenu);


        navDrawerItems = new ArrayList<>();

      //  fillSideMenu();



        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.hamg, //nav menu toggle icon
                R.string.open, // nav drawer open - description for accessibility
                R.string.close // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @SuppressLint("ResourceType")
    private void fillSideMenuWithBadge() {
        //sside bar doldur array den gelenler ile
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5,
                    -1), true, notfCount.getCountNotf()));


        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6,
                -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7,
                -1)));
        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        //Badges
        try {
            ShortcutBadger.applyCount(getApplicationContext(),Integer.parseInt(notfCount.getCountNotf()));
            //for 1.1.4+
        }
        catch (Exception e){}

    }


    private void getMainTasks() {

        MainTasks mainTasks = new MainTasks();
        mainTasks.execute("");

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

                CalendarTask calendarTask = new CalendarTask();
                startDate=sdf.format(new Date(dateClicked.getTime()));
                endDate=sdf.format(new Date(dateClicked.getTime() + oneday));
                calendarTask.execute("");


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                getSupportActionBar().setTitle(sdf.format(firstDayOfNewMonth));
            }
        });

        recyclerView.setOnTouchListener(new OnSwipeTouchListener(SMainActivityNavDrawer.this) {
            public void onSwipeTop() {
/*
               TranslateAnimation animate = new TranslateAnimation(0,0,0,-calender.getHeight());
               animate.setDuration(500);
               animate.setFillAfter(true);
               layout.startAnimation(animate);
                calender.setVisibility(View.INVISIBLE);
                */
// slide-up animation
                Animation slideUp = AnimationUtils.loadAnimation(SMainActivityNavDrawer.this, R.anim.slide_up);

                if (calender.getVisibility() == View.VISIBLE) {
                    calender.setVisibility(View.GONE);
                    layout.startAnimation(slideUp);
                }
                // calender.setVisibility(View.GONE);

            }

            public void onSwipeBottom() {
                /*
                TranslateAnimation animate = new TranslateAnimation(0,0,0,calender.getHeight());
                animate.setDuration(500);
                animate.setFillAfter(true);
                layout.startAnimation(animate);
                calender.setVisibility(View.VISIBLE);*/
                // calender.setVisibility(View.VISIBLE);
                Animation slideDown = AnimationUtils.loadAnimation(SMainActivityNavDrawer.this, R.anim
                        .slide_down);
                if (calender.getVisibility() == View.GONE) {
                    layout.startAnimation(slideDown);
                    calender.setVisibility(View.VISIBLE);

                }

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
        actionBar.setTitle(R.string.pano);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");
        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        txtCountTask = findViewById(R.id.txtCountTasks);
        recyclerView = findViewById(R.id.recyclerview);
        pbLoading = findViewById(R.id.pbLoadMain);
        connectionClass = new ConnectionClass();
        dateDefault = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        datalist = new ArrayList<>();
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        memberid = loginPreferences.getString(Utils.ID, "");
        imgPath = loginPreferences.getString(Utils.IMAGE, "");

        rootView = getWindow().getDecorView().getRootView();
        eng = "en";
        tr = "";
        calender = findViewById(R.id.frame_calender);
        layout = findViewById(R.id.translayout);
        mainTaskSoap=new MainTaskSoap();
        notfCount=new NotifCountModel();
        notifCountSoap=new NotifCountSoap();


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.search://do something
                break;
            case R.id.save:
                takeScreenshot();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                //   fragment = new HomeFragment();
                break;
            case 1:
                //    fragment = new FindPeopleFragment();
                break;
            case 2:
                Intent share = new Intent(getApplicationContext(), SShareActivity.class);
                startActivity(share);
                break;
            case 3:
                Intent getshared = new Intent(getApplicationContext(), SShowAllShareActivity.class);
                startActivity(getshared);
                break;
            case 4:
                //   fragment = new PagesFragment();
                break;
            case 5:
               //bildiirmelr
                notifBuild();
                break;
            case 6:
                if (lang) {
                    changeLocale(tr);
                    new CustomToast().Show_Toast(getApplicationContext(), rootView, "Türkçe Dili " +
                            "Seçildi", Utils.INFO);
                    lang = false;

                } else {
                    changeLocale(eng);
                    new CustomToast().Show_Toast(getApplicationContext(), rootView, "İngilizce Dili " +
                            "Seçildi", Utils.INFO);
                    lang = true;
                }
                break;

            default:
                break;
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        //   setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);



    }

    private  void notifBuild(){

        String msj="";
        if (IsServiceWorking()) {
            msj="Bildirimler Kapatılsın Mı?";
        }
        else {
            msj="Bildirimler Açılsın mı?";
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(SMainActivityNavDrawer.this);
        builder.setTitle("Bildirimler");
        builder.setMessage(msj);
        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if(IsServiceWorking()){
                    Intent intent = new Intent(SMainActivityNavDrawer.this, NotificationServices.class);
                    stopService(intent);//Servisi durdur
                }
                else {
                    Intent intent = new Intent(SMainActivityNavDrawer.this, NotificationServices.class);
                    startService(intent);//Servisi başalt
                }

            }
        });


        builder.show();

    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    public boolean IsServiceWorking() {//Servis Çalışıyor mu kontrol eden fonksiyon

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationServices.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private class MainTasks extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            memberList = new ArrayList<>();
            hset = new HashSet<>();
            datalist.clear();
            recyclerView.setAdapter(null);
            pbLoading.setVisibility(View.VISIBLE);
            compactCalendar.removeAllEvents();

        }


        @Override
        protected void onPostExecute(String r) {

            pbLoading.setVisibility(View.GONE);
            mainTaskAdapter = new MainTaskAdapter(getApplicationContext(), datalist);
            recyclerView.setAdapter(mainTaskAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
           // recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getApplicationContext()));
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
                datalist=mainTaskSoap.mainTask(memberid);
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
                Log.e("HATA",ex.getMessage());
            }
            return z;
        }
    }
    private class CalendarTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            memberList = new ArrayList<>();
            hset = new HashSet<>();
            datalist.clear();
            recyclerView.setAdapter(null);
            pbLoading.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(String r) {

            pbLoading.setVisibility(View.GONE);
            mainTaskAdapter = new MainTaskAdapter(getApplicationContext(), datalist);
            recyclerView.setAdapter(mainTaskAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            // recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getApplicationContext()));
            //  String dateInString = "2018-04-27";
            txtCountTask.setText("GÖREV LİSTESİ(" + datalist.size() + ")");



        }

        @Override
        protected String doInBackground(String... params) {

            try {
                datalist=mainTaskSoap.calendarTask(memberid,startDate,endDate);
            } catch (Exception ex) {
                isSuccess = false;
                z = "Hata";
                Log.e("HATA",ex.getMessage());
            }
            return z;
        }
    }
    private class Notifications extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
            fillSideMenuWithBadge();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                notfCount= notifCountSoap.countNotif(memberid);
            } catch (Exception ex) {
                Log.e("HATA",ex.getMessage());
                notfCount.setCountNotf("12");
            }

            return "";
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
