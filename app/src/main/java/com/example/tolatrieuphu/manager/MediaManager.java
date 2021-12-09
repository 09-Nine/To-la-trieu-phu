package com.example.tolatrieuphu.manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.model.Question;

import java.util.Random;

public class MediaManager implements MediaPlayer.OnCompletionListener{
    private Context mContext;
    private MediaPlayer bgMedia;
    private MediaPlayer mediaPlayer;

    public static final int HOME_BG_MUSIC = R.raw.bgmusic;
    public static final int[] ANS_A = {R.raw.ans_a, R.raw.ans_a2};
    public static final int[] ANS_B = {R.raw.ans_b, R.raw.ans_b2};
    public static final int[] ANS_C = {R.raw.ans_c, R.raw.ans_c2};
    public static final int[] ANS_D = {R.raw.ans_d, R.raw.ans_d2};
    public static final int[] TRUE_A = {R.raw.true_a, R.raw.true_a2};
    public static final int[] TRUE_B = {R.raw.true_b, R.raw.true_b2, R.raw.true_b3};
    public static final int[] TRUE_C = {R.raw.true_c, R.raw.true_c2, R.raw.true_c3};
    public static final int[] TRUE_D = {R.raw.true_d2, R.raw.true_d3};
    public static final int[] LOSE_A = {R.raw.lose_a, R.raw.lose_a2};
    public static final int[] LOSE_B = {R.raw.lose_b, R.raw.lose_b2};
    public static final int[] LOSE_C = {R.raw.lose_c, R.raw.lose_c2};
    public static final int[] LOSE_D = {R.raw.lose_d, R.raw.lose_d2};
    public static final int[][] ANS_SOUND = {ANS_A, ANS_B, ANS_C, ANS_D};
    public static final int[][] TRUE_SOUND = {TRUE_A, TRUE_B, TRUE_C, TRUE_D};
    public static final int[][] WRONG_SOUND = {LOSE_A, LOSE_B, LOSE_C, LOSE_D};
    public static final int[] ANS_NOW = {R.raw.ans_now1, R.raw.ans_now2, R.raw.ans_now3};
    public static final int[] BACKGROUND_MUSIC = {R.raw.background_music,
            R.raw.background_music_b, R.raw.background_music_c};
    public static final int[] BEST_PLAYER = {R.raw.best_player};
    public static final int[] CALL_SOUND = {R.raw.call};
    public static final int[] CONGRATULATION_1 = {R.raw.chuc_mung_vuot_moc_01_0, R.raw.chuc_mung_vuot_moc_01_1};
    public static final int[] CONGRATULATION_2 = {R.raw.chuc_mung_vuot_moc_02_0};
    public static final int[] GO_FIND = {R.raw.gofind, R.raw.gofind_b};
    public static final int[] HELP_CALL = {R.raw.help_call, R.raw.help_callb};
    public static final int[] HELP_AUDIENCE = {R.raw.khan_gia};
    public static final int[] HELP_AUDIENCE_WAIT = {R.raw.hoi_y_kien_chuyen_gia_01b};
    public static final int[] HELP_5050 = {R.raw.sound5050, R.raw.sound5050_2};
    public static final int[] IMPORTANT = {R.raw.important};
    public static final int[] LOSE_SOUND = {R.raw.lose, R.raw.lose2};
    public static final int[] RULE_SOUND = {R.raw.luatchoi_b, R.raw.luatchoi_c};
    public static final int[] OUT_OF_TIME = {R.raw.out_of_time};
    public static final int[] QUEST_1 = {R.raw.ques1, R.raw.ques1_b};
    public static final int[] QUEST_2 = {R.raw.ques2, R.raw.ques2_b};
    public static final int[] QUEST_3 = {R.raw.ques3, R.raw.ques3_b};
    public static final int[] QUEST_4 = {R.raw.ques4, R.raw.ques4_b};
    public static final int[] QUEST_5 = {R.raw.ques5, R.raw.ques5_b};
    public static final int[] QUEST_6 = {R.raw.ques6};
    public static final int[] QUEST_7 = {R.raw.ques7, R.raw.ques7_b};
    public static final int[] QUEST_8 = {R.raw.ques8, R.raw.ques8_b};
    public static final int[] QUEST_9 = {R.raw.ques9, R.raw.ques9_b};
    public static final int[] QUEST_10 = {R.raw.ques10};
    public static final int[] QUEST_11 = {R.raw.ques11};
    public static final int[] QUEST_12 = {R.raw.ques12};
    public static final int[] QUEST_13 = {R.raw.ques13};
    public static final int[] QUEST_14 = {R.raw.ques14};
    public static final int[] QUEST_15 = {R.raw.ques15};
    public static final int[] READY_SOUND = {R.raw.ready, R.raw.ready_b, R.raw.ready_c};
    public static final int[] TOUCH_SOUND = {R.raw.touch_sound};


    public MediaManager(Context Context) {
        mContext = Context;
    }

    public void playBgMusic(int soundId) {
        bgMedia = MediaPlayer.create(mContext, soundId);
        bgMedia.setLooping(true);
        bgMedia.start();
    }

    public void stopBgMusic() {
        bgMedia.release();
        bgMedia = null;
    }


    public void playSound(int[] soundId, MediaPlayer.OnCompletionListener completionListener) {
        if (mediaPlayer != null) {
            resetMedia();
        }
        mediaPlayer = MediaPlayer.create(mContext, soundId[new Random().nextInt(soundId.length)]);
        if (completionListener == null) {
            mediaPlayer.setOnCompletionListener(this);
        }
        mediaPlayer.setOnCompletionListener(completionListener);
        mediaPlayer.start();
    }

    public void resetMedia() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public boolean isMediaPlaying() {
        return mediaPlayer != null;
    }

    public int[] getQuestionSound(Question question) {
        switch (question.getLevel()) {
            case 1: return QUEST_1;
            case 2: return QUEST_2;
            case 3: return QUEST_3;
            case 4: return QUEST_4;
            case 5: return QUEST_5;
            case 6: return QUEST_6;
            case 7: return QUEST_7;
            case 8: return QUEST_8;
            case 9: return QUEST_9;
            case 10: return QUEST_10;
            case 11: return QUEST_11;
            case 12: return QUEST_12;
            case 13: return QUEST_13;
            case 14: return QUEST_14;
            case 15: return QUEST_15;
            default: return null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        resetMedia();
    }
}
