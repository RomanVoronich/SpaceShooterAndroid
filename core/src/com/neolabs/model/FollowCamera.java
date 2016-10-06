package com.neolabs.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class FollowCamera extends OrthographicCamera {

    private Rectangle field;

    public FollowCamera(Rectangle field) {
        super();
        this.field = field;
    }


    public void follow(Actor actor, float deltaTime) {
        float lerp = 4f;
        if (actor.getX() > field.getX() && actor.getX() < field.getX() + field.getWidth()) {
            position.x += (actor.getX() - position.x) * lerp * deltaTime;
        } else {
            if (actor.getX() < field.getX())
                position.x += (actor.getX() - position.x) * lerp * deltaTime;
            else
                position.x = field.getX() + field.getWidth();
        }

        if (actor.getY() > field.getY() && actor.getY() < field.getY() + field.getHeight()) {
            position.y += (actor.getY() - position.y) * lerp * deltaTime;
        } else {
            if (actor.getY() < field.getY())
                position.y += (actor.getY() - position.y) * lerp * deltaTime;
            else
                position.y = field.getY() + field.getHeight();
        }
    }

    public void follow2(Actor actor, float deltaTime) {
        float lerp = 1.5f;
        float deltaX = actor.getX() - position.x;
        float deltaY = actor.getY() - position.y;
        float epsX = viewportWidth / 3;
        float epsY = viewportHeight / 3;

        if (deltaX > epsX)
            position.x += ((actor.getX() - position.x) * lerp * deltaTime);

        if (-deltaX > epsX)
            position.x += ((actor.getX() - position.x) * lerp * deltaTime);


        if (deltaY > epsY)
            position.y += ((actor.getY() - position.y) * lerp * deltaTime);

        if (-deltaY > epsY)
            position.y += ((actor.getY() - position.y) * lerp * deltaTime);
    }


}
