package com.emrehmrc.argememory.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.activity.LoginActivity;
import com.emrehmrc.argememory.activity.SLoginActivity;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.MySingleton;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.soap.InsertTagSoap;
import com.emrehmrc.argememory.soap.NotifCountSoap;
import com.emrehmrc.argememory.sqllite.DataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationServices extends Service {
    final static int PERIOD = 30000;
    final static int DELAY = 0;
    Context context;
    Notification notification;
    Timer timer;
    float dolar = 0, dolarlast = 0;
    SharedPreferences preferences, sharedpreferences;
    int notfCount=0;
    String memberid="",msg="";
    NotifCountSoap notifCountSoap;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        memberid = preferences.getString(Utils.ID, "");
        context = getApplicationContext();
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getNotifications();
            }
        }, DELAY, PERIOD);

       notifCountSoap=new NotifCountSoap();
    }

    private void getNotifications() {
        Notifications notifications=new Notifications();
        notifications.execute("");
    }

    private void sendNotification(String msj) {
        long when = System.currentTimeMillis();//notificationın ne zaman gösterileceği
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, SLoginActivity.class);
        intent.putExtra("pending", Utils.PENDING_FROM_NOTİFİATİON);
        PendingIntent pending = PendingIntent.getActivity(context, 1, intent, 0);
        notification = new Notification(R.mipmap.ic_launcher, "Yeni Bildirim", when);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            try {
                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                deprecatedMethod.invoke(notification, context, msj, "cevap", pending);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {

            }
        } else {
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(pending)
                    .setContentText(" BİLDİRİM ("+notfCount+")")
                    .setSmallIcon(R.drawable.argenotfif)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.logo_ico))
                    .setContentTitle(msj);
            notification = builder.build();
        }


        notification.flags |= Notification.FLAG_AUTO_CANCEL;//notificationa tıklanınca notificationın otomatik silinmesi için
        notification.defaults |= Notification.DEFAULT_SOUND;//notification geldiğinde bildirim sesi çalması için
        notification.defaults |= Notification.DEFAULT_VIBRATE;//notification geldiğinde bildirim titremesi için
        nm.notify(0, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();

    }
    private class Notifications extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {
           if(notfCount>0)sendNotification(" AR-GE ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                notfCount= Integer.valueOf(notifCountSoap.countNotifString(memberid));
            } catch (Exception ex) {
                Log.e("HATA",ex.getMessage());
               notfCount=0;
            }

            return "";
        }
    }
}
