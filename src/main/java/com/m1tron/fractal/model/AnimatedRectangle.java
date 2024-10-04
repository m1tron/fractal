package com.m1tron.fractal.model;

import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;

/**
 * Child class that can store animation information on squares
 * @author Henrik Oskarsson
 */
public class AnimatedRectangle extends Rectangle2D.Double {
    public static int currentStep = 0;

    boolean animated = false;
    double offsetX = 0.0;
    double offsetY = 0.0;
    int steps = 30;
    int lastStep = currentStep;
    Consumer<AnimatedRectangle> animator = AnimatedRectangle::circleAnimator;

    public double getAnimatedX() {
        if (lastStep != currentStep) {
            animator.accept(this);
        }
        var tempX = super.getCenterX();
        return tempX + offsetX;
    }

    public double getAnimatedY() {
        if (lastStep == currentStep) {
            animator.accept(this);
        }
        var tempY = super.getCenterY();
        return tempY + offsetY;
    }

    static void circleAnimator(AnimatedRectangle ar) {
        var maxSteps = ar.steps;
        var omega = (2*Math.PI) * (currentStep%maxSteps);
        ar.offsetX = Math.cos(omega);
        ar.offsetY = Math.sin(omega);
    }

    public static void stepAnimation() {
        currentStep++;
    }
}
