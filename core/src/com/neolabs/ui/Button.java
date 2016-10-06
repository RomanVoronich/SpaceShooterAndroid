package com.neolabs.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Button extends Actor {

    public Button(){

    }

    public Button(float x, float y, float width, Skin skin, String name, String position_horizontal, String position_vertical) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = skin.getDrawable(name);
        style.down = skin.getDrawable(name);
        ImageButton btn = new ImageButton(style);
        btn.setName(name);
        float w = btn.getWidth();
        float h = btn.getHeight();
        float ratio = w / h;
        float new_width = w/100 * width;
        float new_height = (w/ratio)/100 * width;
        btn.setWidth(new_width);
        btn.setHeight(new_height);
//      position_horizontal = left, right, center
//      position_horizontal = top, center, bottom
        float  new_x = x;
        float  new_y = y;
//      положение кнопки
        if (position_horizontal.equals("left")) {
            new_x = x;
        } else if (position_horizontal.equals("center")) {
            new_x = x - btn.getWidth() / 2;

        } else if (position_horizontal.equals("right")) {
            new_x = x - btn.getWidth();
        }

        if (position_vertical.equals("top")) {
            new_y = y;
        } else if (position_vertical.equals("center")) {
            new_y = y - btn.getHeight() / 2;

        } else if (position_vertical.equals("bottom")) {
            new_y = y - btn.getHeight();
        }
        btn.setPosition( new_x, new_y);
        return;
    }
}
