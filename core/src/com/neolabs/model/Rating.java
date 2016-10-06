package com.neolabs.model;

/**
 * Created by elenavlasova on 16.05.16.
 */
public class Rating {
    public String player_id;
    public String nickname;
    public int score;

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
