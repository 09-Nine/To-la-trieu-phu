package com.example.tolatrieuphu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.tolatrieuphu.R;

public class InfoDialog {
    private final AlertDialog dialog;

    public InfoDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.home_info_dialog, null);
        builder.setView(v);
        dialog = builder.create();
        v.findViewById(R.id.info_dialog_cancel).setOnClickListener(v1 -> dialog.dismiss());
    }

    public void showDialog() {
        dialog.show();
    }

}
