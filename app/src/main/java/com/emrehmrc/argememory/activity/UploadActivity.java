package com.emrehmrc.argememory.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.ShareAdapter;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.Utility;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.ShareModel;
import com.emrehmrc.argememory.model.SingletonShare;
import com.emrehmrc.argememory.rest.CancelableCallback;
import com.emrehmrc.argememory.rest.RestClient;
import com.emrehmrc.argememory.soap.InsertShareFileSoap;
import com.emrehmrc.argememory.soap.ShowAllShareSoap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_CAMERA_IMAGE = 1;
    public RestClient restClient;
    Bitmap bm;
    ImageView imgPhoto;
    ProgressDialog mProgress;
    ArrayList<String> filePaths, photoPaths, docPaths;
    private String imagePath = "";
    private File savedFileDestination;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private Button  btnUpload, btnFile, btnGaleri;
    InsertShareFileSoap soap;
    private SharedPreferences loginPreferences;
    String memberid;
    SingletonShare singletonShare;
    ArrayList<ShareModel> datalist;
    ShowAllShareSoap sharesoap;
    View rootView;
    FloatingActionButton fab;
    ///
    TextView txtDate, txtOwner, txtTag, txtTotalMember, txtDescrp, txtTagImage, txtDescpS,
            txtComment;
    ImageButton imgMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        soap=new InsertShareFileSoap();
        sharesoap=new ShowAllShareSoap();
        restClient = new RestClient();
        datalist=new ArrayList<>();
        btnGaleri = findViewById(R.id.btnGalleri);
        btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerBuilder.getInstance().setMaxCount(1)
                        .setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.LibAppTheme)
                        .pickPhoto(UploadActivity.this);
            }
        });
        txtDate = findViewById(R.id.tvTaskDate);
        txtOwner = findViewById(R.id.tvTaskCreater);
        txtTag = findViewById(R.id.tvTaskTag);
        txtTotalMember = findViewById(R.id.tvTaskTotalMan);
        txtDescrp = findViewById(R.id.tvDescription);
        imgMembers = findViewById(R.id.imgTaskMans);
        txtTagImage = findViewById(R.id.txtTagS);
        txtDescpS = findViewById(R.id.txtDescpS);
        txtComment=findViewById(R.id.txtCommnet);
        fab=findViewById(R.id.fabAllShare);
        singletonShare=SingletonShare.getInstance();
        rootView=getWindow().getDecorView().getRootView();


        filePaths = new ArrayList<>();
        //Remove this section if you don't want camera code (End)
        btnFile = findViewById(R.id.btnUploadZip);

        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new UploadSync(UploadActivity.this).execute(imagePath);
                FilePickerBuilder.getInstance().setMaxCount(1)
                        .setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.LibAppTheme)
                        .pickFile(UploadActivity.this);
            }
        });


        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        new FillOneShare().execute(singletonShare.getFileSharedId());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initiateProgressDialog() {
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Uploading files...");
        mProgress.setCancelable(true);

        //setButton is depreciated, it's tell us is not good to cancel when something is running at the back. :). Think about it.
        //When cancel button clicked, it will ignore the response from server
        //You could see this from video
        mProgress.setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CancelableCallback.cancelAll();
                return;
            }
        });
        mProgress.show();
    }


    private void uploadImage() {
        if (savedFileDestination == null) {
            new CustomToast().Show_Toast(getApplicationContext(),rootView,"Dosya Seçimi " +
                    "yapılmadı!!",Utils.WARNİNG);
            return;
        }

        TypedFile typedFile = new TypedFile("multipart/form-data", savedFileDestination);
        initiateProgressDialog();

        restClient.getService().upload(typedFile, new CancelableCallback<Response>
                () {
            @Override
            public void onSuccess(Response response, Response response2) {
                mProgress.dismiss();

            //    Toast.makeText(UploadActivity.this, "Upload successfully", Toast.LENGTH_LONG)
                    //    .show();
                new CustomToast().Show_Toast(getApplicationContext(),rootView,"Başarıyla Yüklendi",
                        Utils.SUCCESS);
                Log.e("Upload", "success");
                loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
                memberid = loginPreferences.getString(Utils.ID, "");
                InsertFileDatabase database=new InsertFileDatabase();
                database.execute("");


            }

            @Override
            public void onFailure(RetrofitError error) {
                mProgress.dismiss();
                Toast.makeText(UploadActivity.this, "Upload failed", Toast.LENGTH_LONG).show();
                Log.e("Upload", error.getMessage().toString());
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (CancelableCallback.runningProcess() == false) finish();
    }

    /*
        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            //For the purpose of when the device orientation change
            savedInstanceState.putString("destination", savedFileDestination.getName());
            super.onSaveInstanceState(savedInstanceState);
        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            //For the purpose of when the device orientation change

            String sPath = savedInstanceState.getString("destination");
            savedFileDestination = new File(sPath);
        }
        //Remove this section if you don't want camera code (End)
    */
    //File Activity methodes here////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filePaths = new ArrayList<>();
                    filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filePaths = new ArrayList<>();
                    filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                }
                break;

        }

        if(filePaths.size()>0){
            File f = new File(filePaths.get(0));
            savedFileDestination = f;
        }


    }

    private class InsertFileDatabase extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            finish();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                 soap.insertShareFile(singletonShare.getFileSharedId(),memberid,filePaths.get(0),
                         "android");

            } catch (Exception ex) {
            Log.e("E", ex.getMessage());
            }
            return "";
        }


    }
    private class FillOneShare extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            datalist.clear();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            if(datalist.size()>0){
                txtDate.setText(datalist.get(0).getShareDate().substring(0, 16));
                txtOwner.setText(datalist.get(0).getShareOwner());
                txtDescrp.setText(datalist.get(0).getShareDescp());
                txtTag.setText(datalist.get(0).getShareTag() + " Adet Etiketli");
                txtTotalMember.setText(datalist.get(0).getShareCountMember());
                txtComment.setText("  "+datalist.get(0).getShareCountComment());
            }


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                datalist = sharesoap.shareById(params[0]);
            } catch (Exception ex) {

            }

            return "";
        }


    }


}
