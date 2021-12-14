package com.example.tolatrieuphu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.manager.MediaManager;

public class HelpCallDialog {
    private final AlertDialog dialog;
    private final View v;
    private String trueCaseText;

    public HelpCallDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        v = inflater.inflate(R.layout.help_call_dialog, null);
        builder.setView(v);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        LinearLayout callDoctor = v.findViewById(R.id.call_doctor);
        LinearLayout callProfessor = v.findViewById(R.id.call_professor);
        LinearLayout callEngineer = v.findViewById(R.id.call_engineer);
        LinearLayout callReporter = v.findViewById(R.id.call_reporter);
        callDoctor.setOnClickListener(v -> handleCallClick(callDoctor));
        callProfessor.setOnClickListener(v -> handleCallClick(callProfessor));
        callEngineer.setOnClickListener(v -> handleCallClick(callEngineer));
        callReporter.setOnClickListener(v -> handleCallClick(callReporter));
    }

    private void handleCallClick(LinearLayout layout){
        LinearLayout ans_layout = v.findViewById(R.id.chosen_call);
        GridLayout gridLayout = v.findViewById(R.id.grid_layout);
        ImageView imageView = (ImageView) layout.getChildAt(0);
        TextView textView = (TextView) layout.getChildAt(1);
        layout.removeAllViews();
        gridLayout.removeAllViews();
        gridLayout.addView(ans_layout);
        ans_layout.addView(imageView, 0);
        ans_layout.addView(textView, 1);
        TextView chosenAnswer =  ans_layout.findViewById(R.id.chosen_call_answer);
        chosenAnswer.setText(trueCaseText);
        App.getInstance().getMediaManager().playSound(MediaManager.CALL_SOUND, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ans_layout.setVisibility(View.VISIBLE);
                v.findViewById(R.id.call_close_btn).setOnClickListener(v -> dismissDialog());
            }
        });
    }

    public void setTrueCase(int trueCase) {
        switch (trueCase) {
            case 1:
                trueCaseText = "Theo tôi đáp án đúng là A";
                break;
            case 2:
                trueCaseText = "Theo tôi đáp án đúng là B";
                break;
            case 3:
                trueCaseText = "Theo tôi đáp án đúng là C";
                break;
            case 4:
                trueCaseText = "Theo tôi đáp án đúng là D";
                break;
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        dialog.setOnDismissListener(onDismissListener);
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void showDialog() {
        dialog.show();
    }
}
