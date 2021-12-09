package com.example.tolatrieuphu.view.fragment;


import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.adapters.MoneyAdapter;
import com.example.tolatrieuphu.databinding.GameplayFragmentBinding;
import com.example.tolatrieuphu.dialogs.CustomAlertDialog;
import com.example.tolatrieuphu.manager.MediaManager;
import com.example.tolatrieuphu.model.Question;
import com.example.tolatrieuphu.view.viewmodel.GameplayViewModel;

import java.util.ArrayList;
import java.util.List;


public class GameplayFragment extends BaseFragment<GameplayFragmentBinding, GameplayViewModel> {
    private MoneyAdapter moneyAdapter;
    private MediaManager mediaManager;
    private CustomAlertDialog readyDialog;
    private DrawerLayout.DrawerListener readyListener, changeQuestion;
    private boolean isPlaying;
    private Handler handler;
    private CountDownTimer countDownTimer = null;
    private String money;
    private List<TextView> answerButtons;

    @Override
    protected Class<GameplayViewModel> getViewModelClass() {
        return GameplayViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gameplay_fragment;
    }

    @Override
    protected void initViews() {
        answerButtons = new ArrayList<>();
        answerButtons.add(binding.answerA);
        answerButtons.add(binding.answerB);
        answerButtons.add(binding.answerC);
        answerButtons.add(binding.answerD);
        money = "0";

        mediaManager = App.getInstance().getMediaManager();
        binding.gameplayContainer.setVisibility(View.GONE);
        handler = new Handler();
        initDrawer();
        binding.gameplayDrawer.addDrawerListener(readyListener);
        mediaManager.playSound(MediaManager.RULE_SOUND, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                readyDialog = new CustomAlertDialog(requireActivity(), "Bạn đã sẵn sàng chơi với chúng tôi ?",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                readyDialog.dismissDialog();
                                binding.gameplayDrawer.closeDrawers();
                            }
                        }, v -> gotoHome());
                mediaManager.playSound(MediaManager.READY_SOUND, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        readyDialog.showDialog();
                    }
                });

            }
        });
        initMoneyRecycleView();
        setButtonClickListener();

        mViewModel.getCurrentQuestionLiveData().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                mediaManager.playSound(mediaManager.getQuestionSound(question), null);
                setQuestion(question);
            }
        });
    }

    private void initDrawer() {
        binding.gameplayDrawer.openDrawer(GravityCompat.START);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) binding.playerMoneyContainer.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        params.width = displayMetrics.widthPixels;
        binding.playerMoneyContainer.setLayoutParams(params);
        binding.playerMoneyContainer.requestLayout();
        binding.playerMoneyContainer.findViewById(R.id.hide_drawer)
                .setOnClickListener(v -> binding.gameplayDrawer.closeDrawers());
        readyListener = new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mediaManager.playSound(MediaManager.GO_FIND, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playGame();
                    }
                });
                binding.gameplayDrawer.removeDrawerListener(readyListener);
            }
        };
    }

    private void initMoneyRecycleView() {
        RecyclerView moneyRecycleView = binding.playerMoneyContainer.findViewById(R.id.money_recycle_view);
        moneyRecycleView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        moneyRecycleView.setLayoutManager(mLayoutManager);
        moneyAdapter = new MoneyAdapter(Constants.MONEY_MILESTONE);
        moneyRecycleView.setAdapter(moneyAdapter);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timerText.setText(String.valueOf(millisUntilFinished/1000));
                if (!isPlaying){
                    cancelTimer();
                }
            }
            @Override
            public void onFinish() {
                Toast.makeText(requireActivity(), "Time out", Toast.LENGTH_SHORT).show();
            }
        };
        countDownTimer.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void setQuestion(Question question) {
        String quesLevel = "Câu " + question.getLevel();
        binding.quesLevel.setText(quesLevel);
        binding.questionContent.setText(question.getQuestion());
        String answerAText = "A: " + question.getCaseA();
        String answerBText = "B: " + question.getCaseB();
        String answerCText = "C: " + question.getCaseC();
        String answerDText = "D: " + question.getCaseD();
        binding.answerA.setText(answerAText);
        binding.answerB.setText(answerBText);
        binding.answerC.setText(answerCText);
        binding.answerD.setText(answerDText);
    }

    private void playGame() {
        isPlaying = true;
        cancelTimer();
        setAnswerClickable(true);
        resetButtonBackground();
        binding.playerCurrentMoney.setText(money);
        mViewModel.nextQuestion();
        binding.gameplayDrawer.openDrawer(GravityCompat.START);
        moneyAdapter.setCurrentMoney(mViewModel.getLevel() - 1);
        moneyAdapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.gameplayDrawer.closeDrawers();
                binding.gameplayContainer.setVisibility(View.VISIBLE);
                if (mViewModel.getLevel() == 5 || mViewModel.getLevel() == 10 || mViewModel.getLevel() == 15) {
                    mediaManager.playSound(MediaManager.IMPORTANT, null);
                    mediaManager.playBgMusic(MediaManager.BACKGROUND_MUSIC[1]);
                } else {
                    mediaManager.playBgMusic(MediaManager.BACKGROUND_MUSIC[0]);
                }
                startTimer();
            }
        },1500);
    }

    private void resetButtonBackground() {
        for (TextView a : answerButtons) {
           a.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
        }
    }

    private void setButtonClickListener(){
        for (TextView a : answerButtons) {
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleChooseAnswer(a, answerButtons.indexOf(a));
                }
            });
        }
    }

    private void setAnswerClickable(Boolean b) {
        binding.answerA.setClickable(b);
        binding.answerB.setClickable(b);
        binding.answerC.setClickable(b);
        binding.answerD.setClickable(b);
        binding.btnStop.setClickable(b);
        binding.btn5050.setClickable(b);
        binding.btnAudience.setClickable(b);
        binding.btnCall.setClickable(b);
        binding.btnChange.setClickable(b);
    }

    private void handleChooseAnswer(TextView answerBtn, int pos) {
        mediaManager.stopBgMusic();
        answerBtn.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_selected);
        setAnswerClickable(false);
        isPlaying = false;
        mediaManager.playSound(MediaManager.ANS_SOUND[pos], new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mViewModel.checkAnswer(pos + 1)){
                    answerBtn.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                    mediaManager.playSound(MediaManager.TRUE_SOUND[pos], new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            money = Constants.MONEY_MILESTONE[mViewModel.getLevel()-1];
                            playGame();
                        }
                    });
                } else {
                    answerBtn.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
                    mediaManager.playSound(MediaManager.WRONG_SOUND[mViewModel.getTrueCase() - 1], null);
                    answerButtons.get(mViewModel.getTrueCase() - 1)
                            .setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                }
            }
        });
    }

    private void gotoHome() {
        mediaManager.resetMedia();
        mediaManager.stopBgMusic();
        callBack.callBack(Constants.KEY_SHOW_HOME, null);
    }

}
