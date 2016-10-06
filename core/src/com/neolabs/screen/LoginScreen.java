package com.neolabs.screen;

import com.neolabs.api.WebApi;
import com.neolabs.api.WebCallback;
import com.neolabs.main.MainGame;
import com.neolabs.model.PlayerInfo;
import com.neolabs.prefs.GamePreferences;
import com.neolabs.response.PlayerInfoResponse;
import com.neolabs.utils.Constants;
import com.neolabs.utils.IAndroidActivity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginScreen implements Screen, EventListener {
    final MainGame game;

    private Stage stage;
    private ImageButton vk, fb, g, u;
    private Table table;
    //    private SpriteBatch batch;
    private IAndroidActivity activity;
    private Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    public BitmapFont font;

    public LoginScreen(MainGame game) {
        this.game = game;
    }


    private void createStage() {
        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, game.batch);
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("social.atlas"));
        skin.addRegions(buttonAtlas);

        ImageButton.ImageButtonStyle vkStyle = new ImageButton.ImageButtonStyle();
        vkStyle.up = skin.getDrawable("icon-vk");
        vkStyle.down = skin.getDrawable("icon-vk");

        ImageButton.ImageButtonStyle fbStyle = new ImageButton.ImageButtonStyle();
        fbStyle.up = skin.getDrawable("icon-fb");
        fbStyle.down = skin.getDrawable("icon-fb");

        ImageButton.ImageButtonStyle gStyle = new ImageButton.ImageButtonStyle();
        gStyle.up = skin.getDrawable("icon-googleplus");
        gStyle.down = skin.getDrawable("icon-googleplus");

        ImageButton.ImageButtonStyle userStyle = new ImageButton.ImageButtonStyle();
        userStyle.up = skin.getDrawable("icon-user");
        userStyle.down = skin.getDrawable("icon-user");

        vk = new ImageButton(vkStyle);
        vk.setName("vk");
        fb = new ImageButton(fbStyle);
        fb.setName("fb");
        g = new ImageButton(gStyle);
        g.setName("g");
        u = new ImageButton(userStyle);
        u.setName("user");

        vk.addListener(this);
        fb.addListener(this);
        g.addListener(this);
        u.addListener(this);

        table = new Table();
        table.setFillParent(true);
        table.add(vk);
        table.add(fb);
        table.row();
        table.add(g);
        table.add(u);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
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
    public void show() {
        createStage();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof InputEvent) {
            InputEvent inputEvent = ((InputEvent) event);
            if (inputEvent.getType() == InputEvent.Type.touchDown) {
                String name = inputEvent.getTarget().getName();
                if (name.equals("vk")) {
                    System.out.println("VK click");
                    game.getActivity().loginSocial(5);//vk
                }
                if (name.equals("fb")) {
                    System.out.println("FB click");
                    game.getActivity().loginSocial(4);//facebook
                }
                if (name.equals("g")) {
                    System.out.println("GOOGLE click");
                    game.getActivity().loginSocial(3);//google plus
                }

                if (name.equals("user")) {
                    WebApi.getInstance().getPlayerInfo(game.getActivity().getAndroidId(), new WebCallback<PlayerInfoResponse>(new Callback<PlayerInfoResponse>() {
                        @Override
                        public void success(PlayerInfoResponse resultPlayerInfo, Response response) {
                            try {
                                GamePreferences.setPlayerId(resultPlayerInfo.data.getPlayer_id());
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

            }
        }
        return false;
    }

    public void openGameScreen() {
        GameScreen gameScreen = new GameScreen(game/*, batch*/);
        game.setScreen(gameScreen);
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input(String text) {
            if (text.isEmpty()) {
                double rand = Math.floor(Math.random() * 10000.0);
                prefs.putString("name", "user_" + rand);
            } else {
                prefs.putString("name", text);
            }
            prefs.flush();
            openGameScreen();
        }

        @Override
        public void canceled() {
        }

    }


}