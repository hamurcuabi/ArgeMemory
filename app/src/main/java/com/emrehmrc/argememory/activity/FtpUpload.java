package com.emrehmrc.argememory.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.connection.ConnectionClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class FtpUpload extends Activity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1995;
    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST = "ftp.zyonetim.com.tr";
    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "pzyoKyq57";
    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS = "zyo87Ffg";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static int TAKE_PICTURE = 1;
    Bitmap bitmapcam = null;
    Button btn, btncam, btnUpload;
    ImageView ımageView;
    File imageFilecam, imageFilegaleri;
    ConnectionClass connectionClass;
    ProgressBar bpupload;
    private String pictureFilePath;
    private Uri outputFileUri;
    private int serverResponseCode = 0;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp_upload);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ımageView = findViewById(R.id.imgCamera);
        btn = findViewById(R.id.galeri);
        btncam = findViewById(R.id.btncam);
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setVisibility(View.GONE);
        bpupload = findViewById(R.id.pbupload);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openGalery();
            }
        });
        btncam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });
        btnUpload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadFile(imageFilecam);

            }
        });
        connectionClass = new ConnectionClass();
        bpupload.setVisibility(View.GONE);

    }

    private void openGalery() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

        }

    }

    private void TakePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, TAKE_PICTURE);

    }

    public void uploadFile(File fileName) {
        FTPClient client = new FTPClient();
        try {

            client.connect(FTP_HOST, 21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/httpdocs/images/deneme/");
            client.upload(fileName, new MyTransferListener());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /***************/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            try {
                Uri selectedImageURI = data.getData();

                imageFilegaleri = new File(getRealPathFromURI(selectedImageURI));
                Toast.makeText(getApplicationContext(), getRealPathFromURI(selectedImageURI), Toast.LENGTH_LONG).show();
                //  uploadFile(imageFile);
            } catch (Exception ex) {
            }

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(getRealPathFromURI(outputFileUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmapcam, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmapcam, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmapcam, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmapcam;
            }
            ımageView.setImageBitmap(rotatedBitmap);


        }
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            try {

                bitmapcam = MediaStore.Images.Media.getBitmap(getContentResolver(), outputFileUri);
                // ımageView.setImageBitmap(bitmapcam);
                //  Uri selectedImageURI = data.getData();
                imageFilecam = new File(getRealPathFromURI(outputFileUri));
                Toast.makeText(getApplicationContext(), getRealPathFromURI(outputFileUri), Toast.LENGTH_LONG)
                        .show();
                //uploadFile(imageFilecam);
                btnUpload.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e1) {
                btnUpload.setVisibility(View.GONE);
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                btnUpload.setVisibility(View.GONE);
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(getRealPathFromURI(outputFileUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmapcam, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmapcam, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmapcam, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmapcam;
            }
            ımageView.setImageBitmap(rotatedBitmap);
            //Picasso.get().load(rotatedBitmap).resize(96,96).into(ımageView);


        } else {
            btnUpload.setVisibility(View.GONE);
            System.out.println("Error Occured");
        }

    }

    private boolean checkAndRequestPermissions() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void insertImage(Connection conn, String img) {
        int len;
        String query;
        PreparedStatement pstmt;

        try {
            File file = new File(img);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();

            query = ("insert into EmreDenemeUpload (IMAGE) VALUES('" + file.getAbsolutePath() + "')");
            pstmt = conn.prepareStatement(query);
            //pstmt.setString(1, file.getName());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            btn.setVisibility(View.GONE);
            // Transfer started
            // Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            // Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT)
            //     .show();
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {

            btn.setVisibility(View.VISIBLE);
            // Transfer completed

            Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
        }

        public void aborted() {

            btn.setVisibility(View.VISIBLE);
            // Transfer aborted
            Toast.makeText(getBaseContext(), " transfer aborted , please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
        }

        public void failed() {

            btn.setVisibility(View.VISIBLE);
            // Transfer failed
            System.out.println(" failed ...");
        }

    }


}