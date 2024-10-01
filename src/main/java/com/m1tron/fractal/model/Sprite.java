package com.m1tron.fractal.model;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

/**
 * Sprite model. The editor lets you build and store these.
 * @param polygon A sprite has a polygon template.
 * @param recursivePoints The points needed to recursively redraw every line of the polygon.
 * @author Henrik Oskarsson
 */
public record Sprite(Polygon polygon, List<Point2D.Float> recursivePoints) implements Serializable {
}