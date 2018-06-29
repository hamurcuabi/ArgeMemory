package com.emrehmrc.argememory.popup;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.custom_ui.CustomToast;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.SingletonShare;
import com.emrehmrc.argememory.soap.AddTagPopupInsertSoap;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static android.content.Context.MODE_PRIVATE;

public class SAddTagPopup extends AppCompatDialogFragment {

    EditText edtNewTag;
    Button btnOk, btnCancel;
    boolean isok;
    String companiesid = "";
    View rootView;
    DialogListener dialogListener;
    private SharedPreferences loginPreferences;
    AddTagPopupInsertSoap tagPopupInsertSoap;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must be implemnted dialoglistener " +
                    "interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.tag_add_layout, null);
        builder.setView(view);

        loginPreferences = getActivity().getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        rootView = getActivity().getWindow().getDecorView().getRootView();


        edtNewTag = view.findViewById(R.id.edtNewTag);
        btnOk = view.findViewById(R.id.btnAddTag);
        btnCancel = view.findViewById(R.id.btnCancelTag);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.isClosed(false);
                dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtNewTag.getText().toString().trim().isEmpty()) {
                    SingletonShare singletonShare = SingletonShare.getInstance();
                    singletonShare.setNewTag(edtNewTag.getText().toString());

                    InsertTag insertTag = new InsertTag();
                    insertTag.execute("");
                    dialogListener.isClosed(true);
                } else {
                    new CustomToast().Show_Toast(getActivity(), rootView, "Yazı Alanı Boş Bırakılamaz", Utils.ERROR);
                }

            }
        });
        return builder.create();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public interface DialogListener {
        void isClosed(boolean isclosed);
    }

    private class InsertTag extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            isok = false;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String r) {

            if (isok) {
                new CustomToast().Show_Toast(getActivity(), rootView, "Başarıyla Eklendi", Utils.SUCCESS);
                dismiss();

            } else {
                new CustomToast().Show_Toast(getActivity(), rootView, "Hata Oluştu", Utils.ERROR);
                dismiss();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                isok=tagPopupInsertSoap.insertTag(companiesid,edtNewTag.getText().toString());

            } catch (Exception ex) {

                isok=false;
            }

            return "";
        }


    }
}
