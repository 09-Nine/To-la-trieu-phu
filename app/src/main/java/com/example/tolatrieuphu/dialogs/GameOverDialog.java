package com.example.tolatrieuphu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.interfaces.GameOverListener;

import java.util.Locale;

public class GameOverDialog {
    private final AlertDialog dialog;
    private final TextView scoreText;
    private GameOverListener gameOverListener;
    private int m;
    private final EditText playerName;

    public GameOverDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.game_over_dialog, null);
        builder.setView(v);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        scoreText = v.findViewById(R.id.score_text);
        playerName = v.findViewById(R.id.player_name_text);
        v.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = playerName.getText().toString();
                gameOverListener.overListener(nameText, m);
                dismissDialog();
            }
        });
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void showDialog() {
        dialog.show();
    }

    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    public void setMoney(int level, boolean isStop) {
        if (!isStop) {
            switch (level) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    m = 0;
                    break;
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    m = Constants.MONEY_MILESTONE_INT[5];
                    break;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    m = Constants.MONEY_MILESTONE_INT[10];
                    break;
                case 16:
                    m = Constants.MONEY_MILESTONE_INT[15];
                    break;
            }
        } else {
            m = Constants.MONEY_MILESTONE_INT[level - 1];
        }

        scoreText.setText(String.format(Locale.US, "%,d VND", m));
    }
}
