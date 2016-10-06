package com.neolabs.model;

import com.neolabs.utils.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PlayerInfo {

    private Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    public String nickname;
    public String g_id;
    public String fb_id;
    public String vk_id;
    public String player_id;
    public int score;
    public int id;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {

        nickname = nickname;
    }

    public String getG_id() {

        return g_id;
    }

    public void setG_id(String g_id) {
        prefs.putString("g_id", g_id);
        this.g_id = g_id;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getVk_id() {
        return vk_id;
    }

    public void setVk_id(String vk_id) {
        this.vk_id = vk_id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public static void setPlayer_id(String player_id) {
        player_id = player_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerInfo(){
        this.nickname = nickname;
        this.g_id = g_id;
        this.fb_id = fb_id;
        this.vk_id = vk_id;
        this.player_id = player_id;
        this.score = score;
        this.id = id;
    }

}
