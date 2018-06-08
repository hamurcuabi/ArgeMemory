package com.emrehmrc.argememory.custom_ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.helper.Utils;

public class CustomToast {
    View layout;

    // Custom Toast Method
    public void Show_Toast(Context context, View view, String msj, Integer type) {


        // Layout Inflater for inflating custom view
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the layout over view

        if (type == Utils.ERROR) {
            layout = inflater.inflate(R.layout.danger_custom_toast, (ViewGroup) view.findViewById(R.id.toast_root));
        } else if (type == Utils.SUCCESS) {
            layout = inflater.inflate(R.layout.success_custom_toast, (ViewGroup) view
                    .findViewById(R.id.toast_root));
        } else if (type == Utils.WARNİNG) {
            layout = inflater.inflate(R.layout.warning_custom_toast, (ViewGroup) view
                    .findViewById(R.id.toast_root));
        } else if (type == Utils.INFO) {
            layout = inflater.inflate(R.layout.info_custom_toast, (ViewGroup) view
                    .findViewById(R.id.toast_root));
        }


        // Get TextView id and set error
        TextView text = layout.findViewById(R.id.toast_msj);
        text.setText(msj);

        Toast toast = new Toast(context);// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        // Toast
        // gravity
        // and
        // Fill
        // Horizoontal
        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast
        toast.show();// Finally show toast
    }
}

