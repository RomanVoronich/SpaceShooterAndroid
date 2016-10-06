package com.neolabs.screen;

import com.neolabs.api.WebApi;
import com.neolabs.api.WebCallback;
import com.neolabs.main.MainGame;
import com.neolabs.model.Rating;
import com.neolabs.response.RatingResponse;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
//import retrofit.client.Response;
//import retrofit.http.GET;
//import retrofit.http.POST;

public class RatingScreen implements Screen {
    final MainGame game;
    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle;
    private ImageButton userRating;
    private TextButton userRatingText;

    public RatingScreen(MainGame game) {
        this.game = game;
    }

    private void createStage() {

        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, game.batch);
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("social.atlas"));
        skin.addRegions(buttonAtlas);
        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;
        textButtonStyle.up = skin.getDrawable("icon-user");
        textButtonStyle.down = skin.getDrawable("icon-user");
        textButtonStyle.checked = skin.getDrawable("icon-user");

        labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;

        table = new Table();
        table.setFillParent(true);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        WebApi.getInstance().getRating(new WebCallback<RatingResponse>(new Callback<RatingResponse>() {
            @Override
            public void success(RatingResponse resultRating, Response response) {
                try {
                    System.out.println("resultRating" + resultRating.data);
                    for (Rating player : resultRating.data) {
                        userRatingText = new TextButton(player.getNickname() + " " + player.getScore(), textButtonStyle);
                        table.add(userRatingText);
                        System.out.println();
                        System.out.println(player.getPlayer_id());
                        System.out.println(player.getScore());
                    }
                } catch (Exception e) {
                    System.out.println("[ERROR] getPlayerInfo" + e);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("[ERROR] getPlayerInfo" + error);
            }
        }));

    }

    @Override
    public void show() {
        createStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.137f, 0.188f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.showMenu();
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
