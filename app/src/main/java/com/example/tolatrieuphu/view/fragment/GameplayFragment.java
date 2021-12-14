package com.example.tolatrieuphu.view.fragment;


import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

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
import com.example.tolatrieuphu.dialogs.GameOverDialog;
import com.example.tolatrieuphu.dialogs.HelpAudienceDialog;
import com.example.tolatrieuphu.dialogs.HelpCallDialog;
import com.example.tolatrieuphu.interfaces.GameOverListener;
import com.example.tolatrieuphu.manager.MediaManager;
import com.example.tolatrieuphu.model.Player;
import com.example.tolatrieuphu.model.Question;
import com.example.tolatrieuphu.view.viewmodel.GameplayViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameplayFragment extends BaseFragment<GameplayFragmentBinding, GameplayViewModel> {
    private MoneyAdapter moneyAdapter;
    private MediaManager mediaManager;
    private CustomAlertDialog readyDialog, stopDialog;
    private HelpCallDialog helpCallDialog;
    private HelpAudienceDialog helpAudienceDialog;
    private GameOverDialog gameOverDialog;
    private DrawerLayout.DrawerListener readyListener;
    private boolean isPlaying;
    private Handler handler;
    private CountDownTimer countDownTimer = null;
    private String money;
    private List<TextView> answerButtons;
    private long milliLeft;

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

        mViewModel.reset();
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

    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timerText.setText(String.valueOf(millisUntilFinished/1000));
                milliLeft = millisUntilFinished;
                if (!isPlaying){
                    cancelTimer();
                }
            }
            @Override
            public void onFinish() {
                mediaManager.playSound(MediaManager.OUT_OF_TIME, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        handleGameOver(false);
                    }
                });
            }
        };
        countDownTimer.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void resumeTimer() {
        startTimer(milliLeft);
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
                if (mViewModel.getLevel() == 6) {
                    mediaManager.playSound(MediaManager.CONGRATULATION_1, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            binding.gameplayDrawer.closeDrawers();
                            mediaManager.playSound(mediaManager.getQuestionSound(mViewModel.getLevel()), null);
                        }
                    });
                } else if (mViewModel.getLevel() == 11) {
                    mediaManager.playSound(MediaManager.CONGRATULATION_2, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            binding.gameplayDrawer.closeDrawers();
                            mediaManager.playSound(mediaManager.getQuestionSound(mViewModel.getLevel()), null);

                        }
                    });
                } else {
                    binding.gameplayDrawer.closeDrawers();
                    mediaManager.playSound(mediaManager.getQuestionSound(mViewModel.getLevel()), null);
                }
                binding.gameplayContainer.setVisibility(View.VISIBLE);
                if (mViewModel.getLevel() == 5 || mViewModel.getLevel() == 10 || mViewModel.getLevel() == 15) {
                    mediaManager.playSound(MediaManager.IMPORTANT, null);
                    mediaManager.playBgMusic(MediaManager.BACKGROUND_MUSIC[1]);
                } else {
                    mediaManager.playBgMusic(MediaManager.BACKGROUND_MUSIC[0]);
                }
                startTimer(Constants.TIME);
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
        binding.btnCall.setOnClickListener(v -> handleHelpCall());
        binding.btnAudience.setOnClickListener(v -> handleHelpAudience());
        binding.btnChange.setOnClickListener(v -> handleHelpChangeQuestion());
        binding.btn5050.setOnClickListener(v -> handleHelp5050());
        binding.btnStop.setOnClickListener(v -> handleStop());
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
                            if (mViewModel.getLevel() == 15) {
                                handleWin();
                            }
                            playGame();
                        }
                    });
                } else {
                    answerBtn.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
                    answerButtons.get(mViewModel.getTrueCase() - 1)
                            .setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
                    mediaManager.playSound(MediaManager.WRONG_SOUND[mViewModel.getTrueCase() - 1], new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            handleGameOver(false);
                        }
                    });
                }
            }
        });
    }

    private void handleHelpCall() {
        cancelTimer();
        helpCallDialog = new HelpCallDialog(requireActivity());
        binding.btnCall.setBackgroundResource(R.drawable.atp__activity_player_button_image_help_call_x);
        binding.btnCall.setClickable(false);
        helpCallDialog.setTrueCase(mViewModel.getTrueCase());
        mediaManager.playSound(MediaManager.HELP_CALL_FIRST, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                helpCallDialog.showDialog();
                helpCallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        resumeTimer();
                    }
                });
                mediaManager.playSound(MediaManager.HELP_CALL_SECOND, null);
            }
        });
    }

    private void handleHelpChangeQuestion() {
        binding.btnChange.setBackgroundResource(R.drawable.atp__activity_player_button_image_help_change_question_x);
        binding.btnChange.setClickable(false);
        for (TextView a : answerButtons) {
           a.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
        }
        mViewModel.changeQuestion();
    }

    private void handleHelpAudience(){
        cancelTimer();
        helpAudienceDialog = new HelpAudienceDialog(requireActivity());
        binding.btnAudience.setBackgroundResource(R.drawable.atp__activity_player_button_image_help_audience_x);
        binding.btnAudience.setClickable(false);
        helpAudienceDialog.setTrueCase(mViewModel.getTrueCase());
        mediaManager.playSound(MediaManager.HELP_AUDIENCE, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                helpAudienceDialog.showDialog();
                helpAudienceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        resumeTimer();
                    }
                });
                mediaManager.playSound(MediaManager.HELP_AUDIENCE_WAIT, null);
            }
        });
    }

    private void handleHelp5050() {
        binding.btn5050.setBackgroundResource(R.drawable.atp__activity_player_button_image_help_5050_x);
        binding.btn5050.setClickable(false);
        mediaManager.playSound(MediaManager.HELP_5050, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                List<Integer> ans = new ArrayList<>();
                ans.add(1);
                ans.add(2);
                ans.add(3);
                ans.add(4);
                ans.remove(Integer.valueOf(mViewModel.getTrueCase()));
                Collections.shuffle(ans);
                answerButtons.get(ans.get(0) - 1).setBackgroundResource(R.drawable.null_image);
                answerButtons.get(ans.get(0) - 1).setText("");
                ans.remove(0);
                Collections.shuffle(ans);
                answerButtons.get(ans.get(0) - 1).setBackgroundResource(R.drawable.null_image);
                answerButtons.get(ans.get(0) - 1).setText("");
            }
        });

    }

    private void handleStop() {
         stopDialog =  new CustomAlertDialog(requireActivity(), "Bạn chắc chắn muốn dừng cuộc chơi tại đây",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopDialog.dismissDialog();
                        handleGameOver(true);
                    }
                }, null);
         stopDialog.showDialog();
    }

    private void handleGameOver(boolean iStop) {
        gameOverDialog = new GameOverDialog(requireActivity());
        isPlaying = false;
        gameOverDialog.setMoney(mViewModel.getLevel(), iStop);
        gameOverDialog.setGameOverListener(new GameOverListener() {
            @Override
            public void overListener(String name, int money) {
                mViewModel.writePlayer(new Player(name, money));
                gotoHome();
            }
        });
        mediaManager.playSound(MediaManager.LOSE_SOUND, null);
        gameOverDialog.showDialog();
    }

    private void handleWin() {
        gameOverDialog = new GameOverDialog(requireActivity());
        isPlaying = false;
        gameOverDialog.setMoney(16, false);
        gameOverDialog.setGameOverListener(new GameOverListener() {
            @Override
            public void overListener(String name, int money) {
                mViewModel.writePlayer(new Player(name, money));
                gotoHome();
            }
        });
        mediaManager.playSound(MediaManager.BEST_PLAYER, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                gameOverDialog.showDialog();
            }
        });
    }

    private void gotoHome() {
        mediaManager.resetMedia();
        mediaManager.stopBgMusic();
        callBack.callBack(Constants.KEY_SHOW_HOME, null);
    }

}
