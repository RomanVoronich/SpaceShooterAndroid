package com.neolabs.prefs;


import com.neolabs.utils.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {
    private static Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);

    public static String  getPlayerId(){
        return prefs.getString("player_id");
    }
    public static void setPlayerId(String player_id){
        prefs.putString("player_id", player_id);
        prefs.flush();
    }

}
