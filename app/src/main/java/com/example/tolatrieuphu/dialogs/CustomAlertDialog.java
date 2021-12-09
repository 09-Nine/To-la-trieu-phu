package com.example.tolatrieuphu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tolatrieuphu.R;

public class CustomAlertDialog {
    private static AlertDialog dialog;

    public CustomAlertDialog(Activity activity, String dialogContent,
                             View.OnClickListener okeListener, View.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.my_dialog, null);
        builder.setView(v);
        dialog = builder.create();
        TextView content = v.findViewById(R.id.dialog_content);
        content.setText(dialogContent);
        if (cancelListener == null) {
            v.findViewById(R.id.cancel).setOnClickListener(v1 -> dialog.dismiss());
        }
        v.findViewById(R.id.oke).setOnClickListener(okeListener);
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void showDialog() {
        dialog.show();
    }
}
