package com.neolabs.screen;

import com.neolabs.main.MainGame;
import com.neolabs.ui.Button;
import com.neolabs.utils.Calculate;
import com.neolabs.utils.IAndroidActivity;
import com.neolabs.utils.IPurchaseFinished;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MenuScreen implements Screen, EventListener, ApplicationListener {
    final MainGame game;

    private Stage stage;
//    кастомные кнопки
    private Button logo_game;
    //    кнопки
    private ImageButton play, rating, settings, free, level;
    //    Кнопки покупок
    private ImageButton buy_coins, buy_skin, buy_speed, buy_life, buy_bullet;
    //    изображения
    private Image game_name, player_name;
    //    инпуты
    private Input input_name;

    private Dialog dialog;
    private Table table;
    private IAndroidActivity activity;
    private Preferences prefs = Gdx.app.getPreferences("PlayerSettings");
    public BitmapFont font;
    private final Skin uiSkin;
    private final Skin dialogSkin;

    public MenuScreen(MainGame game, Skin uiSkin) {
        this.game = game;
        this.uiSkin = uiSkin;
        this.dialogSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    /**
     * Отрисуем кнопку
     *
     * @param x
     * @param y
     * @param width
     * @param skin
     * @param name
     */
    private void newButton(float x, float y, int width, Skin skin, String name, String position_horizontal, String position_vertical) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = skin.getDrawable(name);
        style.down = skin.getDrawable(name);
        ImageButton btn = new ImageButton(style);
        btn.setName(name);
        float new_x = x;
        float new_y = y;
        float w = btn.getWidth();
        float h = btn.getHeight();
        float ratio = w / h;
        btn.setWidth(width);
        btn.setHeight(width / ratio);
//        btn.setPosition(x - btn.getWidth() / 2, y - btn.getHeight() / 2);

        if (position_horizontal.equals("left")) {
            new_x = x;
        } else if (position_horizontal.equals("center")) {
            new_x = x - btn.getWidth() / 2;
        } else if (position_horizontal.equals("right")) {
            new_x = x - btn.getWidth();
        }

        if (position_vertical.equals("top")) {
            new_y = y - btn.getHeight();
        } else if (position_vertical.equals("center")) {
            new_y = y - btn.getHeight() / 2;
        } else if (position_vertical.equals("bottom")) {
            new_y = y;
        }
        btn.setPosition( new_x, new_y);
        btn.addListener(this);
        stage.addActor(btn);
    }

    /**
     * Отрисуем инпут
     *
     * @param x
     * @param y
     * @param width
     * @param name
     */
    private void newInput(float x, float y, int width, String name) {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.BLACK;
        style.font = game.font;
        final TextField inputName = new TextField(name, style);
        float w = inputName.getWidth();
        float h = inputName.getHeight();
        float ratio = w / h;
        inputName.setSize(width, width / ratio);
        inputName.setPosition(x - inputName.getWidth() / 2, y - inputName.getHeight() / 2);
        inputName.setAlignment(Align.center);
        inputName.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                String txtVal = inputName.getText();
                prefs.putString("name", txtVal);
                System.out.println("txtVal: " + txtVal);
            }
        });
        stage.addActor(inputName);
    }

    /**
     * Основной метод создания нашего меню - кнопки инпуты и др
     */
    private void createStage() {
        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, game.batch);

//        logo_game = new Button(Calculate.getPositionX(50), Calculate.getPositionY(80), Calculate.getWidthImage(30), uiSkin, "game-name", "left", "center");
//        stage.addActor(logo_game);
        this.newButton(Calculate.getPositionX(50), Calculate.getPositionY(100), Calculate.getWidthImage(30), uiSkin, "game-name", "center", "top");
        this.newButton(Calculate.getPositionX(50), Calculate.getPositionY(5), Calculate.getWidthImage(20), uiSkin, "play", "center", "bottom");
        this.newButton(Calculate.getPositionX(5), Calculate.getPositionY(5), Calculate.getWidthImage(10), uiSkin, "settings", "left", "bottom");
        this.newButton(Calculate.getPositionX(25), Calculate.getPositionY(5), Calculate.getWidthImage(10), uiSkin, "free", "left", "bottom");
        this.newButton(Calculate.getPositionX(5), Calculate.getPositionY(100), Calculate.getWidthImage(15), uiSkin, "shop-life", "left", "top");
        this.newButton(Calculate.getPositionX(5), Calculate.getPositionY(50), Calculate.getWidthImage(15), uiSkin, "shop-speed", "left", "center");
        this.newButton(Calculate.getPositionX(25), Calculate.getPositionY(70), Calculate.getWidthImage(15), uiSkin, "shop-bullet", "left", "center");
        this.newButton(Calculate.getPositionX(70), Calculate.getPositionY(5), Calculate.getWidthImage(15), uiSkin, "shop-skin", "left", "bottom");
        this.newButton(Calculate.getPositionX(100), Calculate.getPositionY(100), Calculate.getWidthImage(20), uiSkin, "shop-coins", "right", "top");
        this.newButton(Calculate.getPositionX(100), Calculate.getPositionY(80), Calculate.getWidthImage(20), uiSkin, "rating", "right", "top");
        this.newButton(Calculate.getPositionX(90), Calculate.getPositionY(30), Calculate.getWidthImage(10), uiSkin, "level", "right", "bottom");
        if (prefs.getString("name").isEmpty()) {
            this.newInput(Calculate.getPositionX(50), Calculate.getPositionY(50), Calculate.getWidthImage(30), "player_name");
        } else {
            this.newInput(Calculate.getPositionX(50), Calculate.getPositionY(50), Calculate.getWidthImage(30), prefs.getString("name"));
        }

        dialogExit();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    /**
     * Диалог выхода из приложения
     */
    public void dialogExit(){
        Label label = new Label("Are you really exit?", dialogSkin);
        label.setWrap(true);
        label.setFontScale(.8f);
        label.setAlignment(Align.center);
        dialog = new Dialog("", dialogSkin, "dialog") {
            protected void result(Object exit) {
                System.out.println("Chosen: " + exit);
                if (exit.equals("true")) {
                    Gdx.app.exit();
                }
            }
        };
        dialog.padTop(50).padBottom(50);
        dialog.getContentTable().add(label).width(850).row();
        dialog.getButtonTable().padTop(50);

        TextButton yesBtn = new TextButton("Yes", dialogSkin);
        dialog.button(yesBtn, true);

        TextButton noBtn = new TextButton("No", dialogSkin);
        dialog.button(noBtn, false);
        dialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
        dialog.invalidateHierarchy();
        dialog.invalidate();
        dialog.layout();
        dialog.hide();

        yesBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {

                System.out.println("exit");
                dialog.hide();
                dialog.cancel();
                dialog.remove();

                return true;
            }

        });

        noBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                System.out.println("no exit");
                dialog.cancel();
                dialog.hide();

                return true;
            }

        });
        stage.addActor(dialog);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            dialog.show(stage);
        }
        stage.act(delta);
        stage.draw();
    }

    public void dialog() {
        new Dialog("confirm exit", dialogSkin) {
            {
                text("Are you really exit");
                button("yes", "goodbye");
                button("no", "glad you stay");
            }

            @Override
            protected void result(final Object object) {
                new Dialog("", dialogSkin) {

                    {
                        text(object.toString());
                        button("OK");
                    }

                }.show(stage);
            }
        }.show(stage);
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {

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
                System.out.println("click " + name);
                if (name.equals("play")) {
                    openGame();
                }

                if (name.equals("settings")) {
                    openSettings();
                }

                if (name.equals("free")) {
                    openFree();
                }

                if (name.equals("rating")) {
                    openRating();
                }

                if (name.equals("shop-skin")) {
                    openSkin();
                }

                if (name.equals("shop-life")) {
                    openBuy("life", "life");
                }

                if (name.equals("shop-speed")) {
                    openBuy("speed", "speed");
                }

                if (name.equals("shop-bullet")) {
                    openBuy("bullet", "bullet");
                }

                if (name.equals("shop-coins")) {
                    openBuy("coins", "coins");
                }


            }
        }
        return false;
    }

    private void openBuy(String sku, String type) {
        game.getActivity().purchase(sku, new IPurchaseFinished() {
            @Override
            public void onIabPurchaseFinished(boolean result, String sku) {
                System.out.println("onIabPurchaseFinished" + result + " SKU" + sku);
            }
        });
    }

    private void openSkin() {
        ShopScreen shopScreen = new ShopScreen(game);
        game.setScreen(shopScreen);
    }

    private void openRating() {
        RatingScreen ratingScreen = new RatingScreen(game);
        game.setScreen(ratingScreen);

    }


    private void openFree() {
        FreeScreen freeScreen = new FreeScreen(game);
        game.setScreen(freeScreen);
    }

    private void openSettings() {
        SettingsScreen settingsScreen = new SettingsScreen(game);
        game.setScreen(settingsScreen);

    }

    public void openGame() {
        GameScreen gameScreen = new GameScreen(game);
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
            openGame();
        }

        @Override
        public void canceled() {
        }

    }


}