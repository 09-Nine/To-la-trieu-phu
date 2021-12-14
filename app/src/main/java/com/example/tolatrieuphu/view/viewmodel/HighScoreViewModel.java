package com.example.tolatrieuphu.view.viewmodel;


import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.manager.LocalDataManager;
import com.example.tolatrieuphu.model.Player;

import java.util.List;

public class HighScoreViewModel extends BaseViewModel{
    private List<Player> highScorePlayer;
    private final LocalDataManager localDataManager;

    public HighScoreViewModel() {
        localDataManager = App.getInstance().getLocalDataManager();
    }

    public void getData() {
        highScorePlayer = localDataManager.getListPlayer();
    }

    public List<Player> getHighScorePlayer() {
        return highScorePlayer;
    }

}
