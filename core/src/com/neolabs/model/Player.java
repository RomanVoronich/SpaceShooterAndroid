package com.neolabs.model;

import com.neolabs.api.SocketApi;
import com.neolabs.ui.TouchpadFire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Pool;
import com.google.gson.Gson;

public class Player extends Actor {
    private String id;
    private int life;
    private int bulet;
    private int score;
    private String name;
    private Target target;
    private transient Gson gson;
    private transient Texture texture;
    private transient Sprite sprite;
    private long dateLastBullet;
    private BitmapFont font;

    private int height = Gdx.graphics.getHeight();
    private int width = Gdx.graphics.getWidth();

    public Player() {
        int rand = (int) Math.floor(Math.random() * 10000);
        this.id = String.valueOf(rand);
        this.target = new Target();
        this.life = 0;
        this.bulet = 0;
        // TODO get name from game screen
        this.name = getName();
        this.dateLastBullet = System.currentTimeMillis();
        this.gson = new Gson();
    }

    public Player(Texture texture, BitmapFont font) {
        this();
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
        this.font = font;
    }

    public String getId() {
        return id;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetGson() {
        return gson.toJson(this.target);
    }

    public void updateController(Touchpad leftJoystick, TouchpadFire rightJoystick) {
        if (leftJoystick.getKnobPercentY() == 0 && leftJoystick.getKnobPercentX() == 0) {
            this.target.setAngle(0);
            this.target.setSpeed(0);
        } else {
            double angle = Math.atan2(leftJoystick.getKnobPercentY(), leftJoystick.getKnobPercentX());
            this.target.setAngle(angle);
            this.target.setSpeed(1);
        }
        if (rightJoystick.getKnobPercentY() == 0 && rightJoystick.getKnobPercentX() == 0) {
//            this.target.setRotationSpeed(0);
        } else {
            double target = Math.atan2(rightJoystick.getKnobPercentY(), rightJoystick.getKnobPercentX());
            /*if (target < 0)
                target = Math.PI + Math.abs(Math.PI + target);

            double temp1 = target;
            double temp2 = 2 * Math.PI + target;

            if (Math.abs(temp1) < Math.abs(temp2))
                target = temp1;
            else
                target = temp2;
*/
            System.out.println("target degrees:" + Double.toString(Math.toDegrees(target)) + "  target:" + target);
//            this.target.setRotation((float)Math.toDegrees(target));
//            (float)Math.toDegrees(target);
            this.target.setRotation(target);
//            this.target.setRotationSpeed(1);
            if (dateLastBullet + 500 < System.currentTimeMillis()) {
                dateLastBullet = System.currentTimeMillis();
                Bullet bullet = new Bullet();
                bullet.setAngle(target);
                bullet.setPlayerId(this.getId());
                bullet.setX(this.getX());
                bullet.setY(this.getY());
                SocketApi.getInstance().getSocket().emit("fire", gson.toJson(bullet));
            }
        }
    }

    public int getLife() {
        return life;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.setOriginCenter();
        sprite.setSize(width / 15, width / 15);
        sprite.setBounds(getX() - getWidth(), getY() - getHeight(), getWidth() * getScaleX(), getHeight() * getScaleY());
        sprite.setRotation(getRotation());
        sprite.draw(batch);
        font.draw(batch, getName(), getX() - getWidth(), getY());
        font.draw(batch, String.valueOf(getLife()), getX() - getWidth(), getY() + getScaleY() * 10);
    }

    public void setCenterOrigin() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public void animateMovement(float x, float y, float angle, float speed, Pool<MoveToAction> pool) {
        MoveToAction move = Actions.action(MoveToAction.class);
        move.setPool(pool);
        move.setPosition(x, y);
        move.setDuration(2.5f);
        move.setInterpolation(Interpolation.linear);

        RotateToAction rotate = Actions.action(RotateToAction.class);
        rotate.setPool(pool);
        rotate.setRotation(angle + 90);
        rotate.setDuration(0.15f);
        rotate.setInterpolation(Interpolation.linear);
        addAction(Actions.parallel(move, rotate));
    }
}