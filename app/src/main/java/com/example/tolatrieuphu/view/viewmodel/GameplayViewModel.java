package com.example.tolatrieuphu.view.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.manager.DatabaseManager;
import com.example.tolatrieuphu.model.Question;

public class GameplayViewModel extends BaseViewModel{
    private MutableLiveData<Question> currentQuestionLiveData;
    private Question currentQuestion;
    private DatabaseManager databaseManager;
    private int level;

    public GameplayViewModel() {
        level = 0;
        databaseManager = App.getInstance().getDatabaseManager();
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

    public MutableLiveData<Question> getCurrentQuestionLiveData() {
        return currentQuestionLiveData;
    }

    public int getTrueCase() {
        return currentQuestion.getTrueCase();
    }

    public int getLevel() {
        return level;
    }
}
