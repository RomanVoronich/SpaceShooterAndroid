package com.neolabs.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Pool;
import com.google.gson.Gson;

public class Bullet extends Actor {
    private String id;

    public String getId() {
        return id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    private String playerId;
    private int life;
    private double angle;

    private transient Texture texture;
    private transient Sprite sprite;

    public Bullet() {
        this.angle = getAngle();
        this.playerId = getPlayerId();
    }

    public Bullet(Texture texture) {
        this();
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.setBounds(getX() - getWidth(), getY() - getHeight(), getWidth() * getScaleX(), getHeight() * getScaleY());
        sprite.draw(batch);
    }

    @Override
    public boolean remove() {
        try {
            removeAction(Actions.action(MoveToAction.class));
            super.remove();
            System.out.println("REMOVE BULLET");
            return true;
        } catch (Exception e) {
            System.out.println("Actor remove" + e);
            return true;
        }
    }

//    public void setCenterOrigin() {
//        setOrigin(getWidth() / 2, getHeight() / 2);
//    }

    public void animateMovement(float x, float y, float angle, float speed, Pool<MoveToAction> pool) {
        MoveToAction move = Actions.action(MoveToAction.class);
        move.setPool(pool);
        move.setPosition(x, y);
        move.setDuration(speed);
        move.setInterpolation(Interpolation.linear);

        RotateToAction rotate = Actions.action(RotateToAction.class);
        rotate.setPool(pool);
        rotate.setRotation(angle+90);
        rotate.setInterpolation(Interpolation.linear);
        addAction(Actions.parallel(move,rotate));
//        addAction(Actions.parallel(move));
    }

}