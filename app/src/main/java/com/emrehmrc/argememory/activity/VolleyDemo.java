package com.emrehmrc.argememory.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.interfaces.DefaultActivitiy;
import com.emrehmrc.argememory.services.NotificationServices;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VolleyDemo extends AppCompatActivity {

    String name, country;
    Button btnvolley;
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imagebox;
    ConnectionClass connectionClass;
    // End Layouts buttons, imageview extra

    // Declaring connection variables and array,string to store data in them
    byte[] byteArray;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_demo);
        final View view = getWindow().getDecorView().getRootView();
        btnvolley=findViewById(R.id.btnvolley);


        /*
        StringRequest request=new StringRequest(Request.Method.GET, "https://www.google.com", new
                Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              new CustomToast().Show_Toast(getApplicationContext(),view,response.substring(0,
                      25),Utils.INFO);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);


        String url="http://api.apixu.com/v1/current" +
                ".json?key=5fc397d694e3486d8c591545180606&q=Ankara";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject object=response.getJSONObject("location");
                             name=object.getString("name");
                             country=object.getString("country");
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

        if (IsServiceWorking()) {

            Intent intent = new Intent(getApplicationContext(), NotificationServices.class);
            startService(intent);//Servisi başlatır

            stopService(intent);//servisi durdurur
        } */
        connectionClass = new ConnectionClass();
        btnvolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opening the Gallery and selecting media
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&& !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE );
                    // this will jump to onActivity Function after selecting image
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No activity found to perform this task", Toast
                            .LENGTH_SHORT).show();
                }
                // End Opening the Gallery and selecting media
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK  && null != data)
        {
            // getting the selected image, setting in imageview and converting it to byte and base 64

            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            Toast.makeText(getApplicationContext(), selectedImage.toString(), Toast.LENGTH_LONG)
                    .show();
            InputStream imageStream;
            try
            {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            }
            catch (FileNotFoundException e)
            {
                System.out.println(e.getMessage().toString());
            }
            if (originBitmap != null)
            {
              //  this.imagebox.setImageBitmap(originBitmap);
                Log.w("Image Setted in", "Done Loading Image");
                try
                {
                //    Bitmap image = ((BitmapDrawable) imagebox.getDrawable()).getBitmap();
                    Bitmap image = originBitmap;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // Calling the background process so that application wont slow down
                    UploadImage uploadImage = new UploadImage();
                    uploadImage.execute("");
                    //End Calling the background process so that application wont slow down
                }
                catch (Exception e)
                {
                    Log.w("OOooooooooo","exception");
                }
                Toast.makeText(getApplicationContext(), "Conversion Done",Toast.LENGTH_SHORT).show();
            }
            // End getting the selected image, setting in imageview and converting it to byte and base 64
        }
        else
        {
            System.out.println("Error Occured");
        }
    }



    public class UploadImage extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPostExecute(String r)
        {
            // After successful insertion of image

            Toast.makeText(VolleyDemo.this , "Image Inserted Succesfully" , Toast.LENGTH_LONG).show();
            // End After successful insertion of image
        }
        @Override
        protected String doInBackground(String... params)
        {
            // Inserting in the database
            String msg = "unknown";
            try
            {
                Connection con = connectionClass.CONN();
                String commands = "Insert into EmreDenemeUpload (IMAGE) values ('" +
                        encodedImage + "')";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                msg = "Inserted Successfully";
            }
            catch (SQLException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 1:", msg);
            }
            catch (IOError ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 2:", msg);
            }
            catch (AndroidRuntimeException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 3:", msg);
            }
            catch (NullPointerException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 4:", msg);
            }
            catch (Exception ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 5:", msg);
            }
            System.out.println(msg);
            return "";
            //End Inserting in the database
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

}
