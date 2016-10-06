package com.neolabs.screen;

import com.neolabs.main.MainGame;
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


public class MenuScreenOld implements Screen, EventListener {
    final MainGame game;

    private Stage stage;
    private ImageButton play, rating, store;
    private Table table;
    private IAndroidActivity activity;
    private Preferences prefs = Gdx.app.getPreferences("PlayerSettings");
    public BitmapFont font;

    public MenuScreenOld(MainGame game) {
        this.game = game;
    }

    private void createStage() {
        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, game.batch);
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("MenuPack.txt"));
        skin.addRegions(buttonAtlas);

        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.up = skin.getDrawable("Play");
        playStyle.down = skin.getDrawable("Play");

        ImageButton.ImageButtonStyle ratingStyle = new ImageButton.ImageButtonStyle();
        ratingStyle.up = skin.getDrawable("Rating");
        ratingStyle.down = skin.getDrawable("Rating");

        ImageButton.ImageButtonStyle storeStyle = new ImageButton.ImageButtonStyle();
        storeStyle.up = skin.getDrawable("Store");
        storeStyle.down = skin.getDrawable("Store");

        play = new ImageButton(playStyle);
        play.setName("Play");
        rating = new ImageButton(ratingStyle);
        rating.setName("Rating");
        store = new ImageButton(storeStyle);
        store.setName("Store");

        play.addListener(this);
        rating.addListener(this);
        store.addListener(this);

        table = new Table();
        table.setFillParent(true);
        table.add(play);
        table.row();
        table.add(store);
        table.row();
        table.add(rating);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.137f, 0.188f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



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
                if (name.equals("Play")) {
                    if (prefs.getString("name").isEmpty()) {
                        MyTextInputListener listener = new MyTextInputListener();
                        Gdx.input.getTextInput(listener, "Enter your name", "", "Enter your name");
                    } else {
                        openGameScreen();
                    }
                }
                if (name.equals("Store")) {
                    //TODO store
//                    LoginScreen loginScreen = new LoginScreen(game);
//                    game.setScreen(loginScreen);
                     ShopScreen shopScreen = new ShopScreen(game);
                     game.setScreen(shopScreen);
                }
                if (name.equals("Rating")) {
                    RatingScreen ratingScreen = new RatingScreen(game);
                    game.setScreen(ratingScreen);
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