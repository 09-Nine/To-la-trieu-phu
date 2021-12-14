package com.example.tolatrieuphu.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocalDataManager {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public LocalDataManager(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(Constants.MY_SHARED_PREFERENCE, mContext.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setListPlayer(List<Player> listPlayer) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(listPlayer).getAsJsonArray();
        String strJsonArray = jsonArray.toString();
        setStringValue(Constants.PREFERENCE_LIST, strJsonArray);
    }

    public List<Player> getListPlayer() {
        String strJsonArray = getStringValue(Constants.PREFERENCE_LIST);
        List<Player> players = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(strJsonArray);
            JSONObject jsonObject;
            Player player;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                player = gson.fromJson(jsonObject.toString(), Player.class);
                players.add(player);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return players;
    }
}
