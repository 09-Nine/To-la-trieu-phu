package com.example.tolatrieuphu.view.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.manager.DatabaseManager;
import com.example.tolatrieuphu.manager.LocalDataManager;
import com.example.tolatrieuphu.model.Player;
import com.example.tolatrieuphu.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameplayViewModel extends BaseViewModel{
    private MutableLiveData<Question> currentQuestionLiveData;
    private Question currentQuestion;
    private List<Player> listPlayer;
    private final DatabaseManager databaseManager;
    private final LocalDataManager localDataManager;
    private int level;

    public GameplayViewModel() {
        reset();
        databaseManager = App.getInstance().getDatabaseManager();
        localDataManager = App.getInstance().getLocalDataManager();
        currentQuestionLiveData = new MutableLiveData<>();
    }

    public boolean checkAnswer(int answer) {
        return currentQuestion.getTrueCase() == answer;
    }

    public void nextQuestion(){
        level++;
        currentQuestion = databaseManager.getQuestion(level);
        currentQuestionLiveData.postValue(currentQuestion);
    }

    public void changeQuestion() {
        currentQuestion = databaseManager.getQuestion(level);
        currentQuestionLiveData.postValue(currentQuestion);
    }

    public MutableLiveData<Question> getCurrentQuestionLiveData() {
        return currentQuestionLiveData;
    }

    public void writePlayer(Player p) {
        listPlayer = localDataManager.getListPlayer();
        listPlayer.add(p);
        Collections.sort(listPlayer, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return Integer.compare(o2.getMoneyInt(), o1.getMoneyInt());
            }
        });
        localDataManager.setListPlayer(listPlayer);
    }

    public int getTrueCase() {
        return currentQuestion.getTrueCase();
    }

    public int getLevel() {
        return level;
    }

    public void reset() {
        level = 0;
    }
}
