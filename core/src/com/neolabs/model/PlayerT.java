package com.neolabs.model;

import com.neolabs.model.Target;
import com.badlogic.gdx.Gdx;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerT {


    private String id;
    private String name;
    private int screenWidth;
    private int screenHeight;

    public PlayerT(JSONObject player) throws JSONException {
        this.id = (String) player.getString("id");
        this.name = (String) player.getString("name");
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
    }
}