package com.neolabs.main;

import com.neolabs.screen.GameScreen;
import com.neolabs.screen.MenuScreen;
import com.neolabs.utils.IAndroidActivity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainGame extends Game {

    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    public SpriteBatch batch;
    private IAndroidActivity activity;
    public BitmapFont font;
    private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
    private final Skin uiSkin;

    public MainGame(IAndroidActivity activity) {
        this.activity = activity;
        this.uiSkin = new Skin();
    }


    @Override
    public void create() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Russo_One.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = Gdx.graphics.getHeight() / 18; // Размер шрифта. Я сделал его исходя из размеров экрана. Правда коряво, но вы сами можете поиграться, как вам угодно.
        param.characters = FONT_CHARACTERS; // Наши символы
        param.flip = false;
        font = generator.generateFont(param); // Генерируем шрифт
        font.setColor(Color.WHITE); // Цвет белый
        generator.dispose(); // Уничтожаем наш генератор за ненадобностью.
        batch = new SpriteBatch();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("game-menu.atlas"));


        uiSkin.addRegions(buttonAtlas);
        this.setScreen(new MenuScreen(this, uiSkin/*, batch*/));


    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {

    }

    public IAndroidActivity getActivity() {
        return activity;
    }

    public void showMenu() {
        setScreen(new MenuScreen(this, uiSkin));
    }
}
