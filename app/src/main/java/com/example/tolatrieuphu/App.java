package com.example.tolatrieuphu;

import android.app.Application;

import com.example.tolatrieuphu.manager.DatabaseManager;
import com.example.tolatrieuphu.manager.LocalDataManager;
import com.example.tolatrieuphu.manager.MediaManager;

public class App extends Application {
    private static App instance;
    private MediaManager mediaManager;
    private DatabaseManager databaseManager;
    private LocalDataManager localDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mediaManager = new MediaManager(this);
        databaseManager = new DatabaseManager(this);
        localDataManager = new LocalDataManager(this);
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public LocalDataManager getLocalDataManager() {
        return localDataManager;
    }

    public static App getInstance() {
        return instance;
    }
}
