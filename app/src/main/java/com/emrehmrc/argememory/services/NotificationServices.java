package com.emrehmrc.argememory.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.activity.LoginActivity;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.MySingleton;
import com.emrehmrc.argememory.helper.Utils;
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
    final static int PERIOD = 10000;
    final static int DELAY = 0;
    Context context;
    Notification notification;
    Timer timer;
    float dolar = 0, dolarlast = 0;
    SharedPreferences preferences, sharedpreferences;
    ConnectionClass connectionClass;
    int taskCount = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        context = getApplicationContext();
        //Toast.makeText(this, "Servis Çalıştı", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getMainTasks();
            }
        }, DELAY, PERIOD);
        connectionClass = new ConnectionClass();
    }

    private void sendNotification(String msj) {
        long when = System.currentTimeMillis();//notificationın ne zaman gösterileceği
        String baslik = "Dolar Yükseldi!!";//notification başlık
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("pending", Utils.PENDING_FROM_NOTİFİATİON);
        PendingIntent pending = PendingIntent.getActivity(context, 1, intent, 0);

        //Notificationa tıklanınca açılacak activityi belirliyoruz
        notification = new Notification(R.mipmap.ic_launcher, "Yeni Bildirim", when);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            try {
                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                deprecatedMethod.invoke(notification, context, msj, "cevap", pending);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {

            }
        } else {
            // Use new API
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(pending)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(msj);
            notification = builder.build();
        }


        notification.flags |= Notification.FLAG_AUTO_CANCEL;//notificationa tıklanınca notificationın otomatik silinmesi için
        notification.defaults |= Notification.DEFAULT_SOUND;//notification geldiğinde bildirim sesi çalması için
        notification.defaults |= Notification.DEFAULT_VIBRATE;//notification geldiğinde bildirim titremesi için
        nm.notify(0, notification);
    }

    private void getDolar() {
        String url = "http://www.doviz.gen.tr/doviz_json.asp?version=1.2";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dolar = preferences.getFloat("dolar", 0);

                        try {
                            //JSONObject object=response.getJSONObject("dolar");
                            dolarlast = Float.parseFloat((response.getString("dolar")));
                            if (dolar > dolarlast) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putFloat("dolar", dolarlast);
                                editor.commit();
                                sendNotification("Dolar Yükseldi");

                            } else if (dolarlast > dolar) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putFloat("dolar", dolarlast);
                                editor.commit();
                                sendNotification("Dolar Düştü");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void getMainTasks() {
        DataBase db = new DataBase(getApplicationContext());
        String memberid = db.SelectByIdMemberId(1);
        MainTasks mainTasks = new MainTasks();
        String query = "select  * from VW_MAINTASKLIST where ID in" +
                "(select ID from VW_TASKMEMBER where  (PUBLISHERID='" + memberid + "' or " +
                "MEMBERID='" + memberid + "') and ISFINISH='0') order by (ID)";

        mainTasks.execute(query);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MainTasks extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            taskCount = 0;

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            DataBase db = new DataBase(getApplicationContext());
            String old = db.SelectById(1);
            if(old.equals(""))old=String.valueOf(0);

            try {
                if (taskCount > Integer.parseInt(old)) {
                    db.Update(1, String.valueOf(taskCount));
                    sendNotification("Yeni Bir Göreviniz Var!");

                }
            } catch (Exception ex) {
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

                        taskCount++;
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