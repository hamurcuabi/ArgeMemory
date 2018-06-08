package com.emrehmrc.argememory.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.fragment.FragLoading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class FileActivity extends Activity {


    static final String FTP_HOST = "ftp.zyonetim.com.tr";
    static final String FTP_USER = "pzyoKyq57";
    static final String FTP_PASS = "zyo87Ffg";
    ImageView viewImage;
    Button btnSelectPhoto, btnUpload;
    File fCamera, fGalery;
    boolean isCamera = false;
    UUID uuıd;
    String photoName;
    ProgressBar loading;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @SuppressLint("ResourceType")
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        btnUpload = findViewById(R.id.btnUpload);
        viewImage = findViewById(R.id.viewImage);
        loading=findViewById(R.id.loading);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCamera) uploadFile(fCamera);
                else uploadFile(fGalery);
            }
        });

    }

    private void selectImage() {
        uuıd = UUID.randomUUID();
        photoName = uuıd.toString() + ".jpg";
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(FileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    isCamera = true;
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fCamera = new File(android.os.Environment.getExternalStorageDirectory(),
                            photoName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fCamera));
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery"))

                {
                    isCamera = false;
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    public void uploadFile(File fileName) {

        FTPClient client = new FTPClient();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                fGalery = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : fGalery.listFiles()) {
                    if (temp.getName().equals(photoName)) {
                        fGalery = temp;
                        break;

                    }
                }

                try {

                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(fGalery.getAbsolutePath(),
                            bitmapOptions);
                    viewImage.setImageBitmap(bitmap);

                    String path = android.os.Environment.getExternalStorageDirectory() + "";

                    // fGalery.delete();

                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("TAG", picturePath + "");
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(picturePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(thumbnail, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(thumbnail, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(thumbnail, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = thumbnail;
                }
                viewImage.setImageBitmap(rotatedBitmap);

            }

        }

    }

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

           btnUpload.setEnabled(false);
           btnSelectPhoto.setEnabled(false);
            Log.e("TAG","start");
        }

        public void transferred(int length) {

            Log.e("TAG","transferred");
        }

        public void completed() {

            Log.e("TAG","completed");
            btnUpload.setEnabled(true);
            btnSelectPhoto.setEnabled(true);
            // Transfer completed
            Toast.makeText(getBaseContext(), " Completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );

        }

        public void aborted() {

            Log.e("TAG","aborted");
            // Transfer aborted
            Toast.makeText(getBaseContext(), " transfer aborted , please try again...", Toast.LENGTH_SHORT).show();

        }

        public void failed() {

            Log.e("TAG","failed");
            // Transfer failed
        }

    }

}