package com.neolabs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class ShopItem extends Actor implements EventListener {

    private static int N = 0;
    private int currentN;

    private Texture texMenuItem;
    private TextureRegion tex;

    private BitmapFont font;

    public ShopItem(BitmapFont font,int skin_level, float _width, float _height) {
        texMenuItem = new Texture("skin/"+skin_level+".png");
        tex = new TextureRegion(texMenuItem, 0, 0, 128, 128);
        this.font = font;
        setWidth(_width);
        setHeight(_height);
        // Глобальная нумерация кнопок выбора
        N += 1;
        currentN = N;
        addListener(this);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        font.draw(batch, String.valueOf(currentN), getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }


    @Override
    public boolean handle(Event event) {
        if (event instanceof InputEvent) {
            InputEvent inputEvent = ((InputEvent) event);
            if (inputEvent.getType() == InputEvent.Type.touchDown) {
                setWidth(getWidth() + 10);
                setHeight(getHeight() + 10);
            }
            if (inputEvent.getType() == InputEvent.Type.exit) {
                setWidth(getWidth() - 10);
                setHeight(getHeight() - 10);
            }
        }
        return true;
    }

}