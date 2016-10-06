package com.neolabs.utils;


import com.badlogic.gdx.Gdx;

public class Calculate {
    private static float width = (float) Gdx.graphics.getWidth()/100;
    private static float height =(float) Gdx.graphics.getHeight()/100;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRatio(){
        return width/height;

    }

    public static int getWidthImage(int percentWidth) {
        return (int) width*percentWidth;
    }

    /**
     *
     * @param percentTop
     * @return
     */
    public static float getPositionY(int percentTop) {
        return height*percentTop;
    }

    /**
     *
     * @param percentLeft
     * @return
     */
    public static float getPositionX(int percentLeft) {
        return width*percentLeft;
    }
}
