package com.neolabs.model;

public class Target {

    private double angle;
    private double rotation;
    private double speed;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Target() {
        this.angle = getAngle();
        this.speed = getSpeed();
        this.rotation = getRotation();
    }
}
