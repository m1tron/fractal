package com.dt181g.laboration_3.view;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The RenderPanel is used as a canvas for rendering a Sprite.
 * @author Henrik Oskarsson
 */
public class RenderPanel extends BlackPaddedPanel {

    private int recursiveDepth = ViewConfig.RECURSIVE_DEPTH;
    private Polygon polygon = new Polygon();


    public List<Point2D.Float> relativePoints = Arrays.asList(new Point2D.Float(0,0));

    /**
     * CenterPanel set up goes here.
     */
    public RenderPanel() {
        setForeground(ViewConfig.RENDER_PANEL_COLOR);
    }

    /**
     * Overide of parent method to return dimensions. Makes any internal calls by other JComponents get the same value.
     * @return the objects dimensions.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ViewConfig.RENDER_PANEL_WIDTH, ViewConfig.RENDER_PANEL_HEIGHT);
    }

    /**
     * Used to calculate a new point for drawing using relative coordinates p(x,y). The unit vector u(x,y) is
     * calculated as the vector from _start_ to _end_.
     * The inverse operation of calculateRelativePoint().
     * Method is needed to draw a line that can be scaled and rotated to match two previous vectors.
     *
     * @param p_x the relative x coordinate.
     * @param p_y the relative y coordinate.
     * @param start start point
     * @param end end point
     * @return returns a transformed point that can be drawn.
     */
    public Point2D.Float calculateActualPoint(float p_x, float p_y, Point2D.Float start, Point2D.Float end){
        float dist = (float)Point2D.distance(start.x, start.y, end.x, end.y);
        var u = new Point2D.Float((end.x - start.x)/dist, (end.y - start.y)/dist);
        return new Point2D.Float(
                (int)(u.x*dist*p_x  + u.y*dist*p_y) + start.x,
                (int)(u.y*dist*p_x  - u.x*dist*p_y) + start.y);
    }

    /**
     * Transform a point to coordinates using unit vectors U and V. U is the point from _start_ to _end_, and V is orthogonal to U.
     * I.e. if this method is called with the vector from _start_ to _end_ as the vector to transform it would produce (1,0).
     * The orthogonal vector to the above example would produce (0,1).
     * @param start start point of line
     * @param end end point of line
     * @param p point to transform.
     * @return returns the transformed point.
     */
    public static Point2D.Float calculateRelativePoint(Point2D.Float start, Point2D.Float end, Point2D.Float p) {
        float dist = (float)Point2D.distance(start.x, start.y, end.x, end.y);
        // U is unit vector pointing from start to end point.
        var u = new Point2D.Float((end.x - start.x)/dist, (end.y - start.y)/dist);

        // V is the orthogonal unit vector to U. Flipping coordinates and sign produces a 90° rotated vector.
        var v = new Point2D.Float(-u.y, u.x);

        /* Make point P be the vector from start to P */
        p.x = p.x - start.x;
        p.y = p.y - start.y;

        /* Dot product of P⋅U and P⋅V */
        float a = p.x*u.x + p.y*u.y;
        float b = p.x*v.x + p.y*v.y;
        return new Point2D.Float(a/dist, b/dist);
    }


    /**
     * Draw a polygon line by line so that each one can be recursively drawn using drawRecursiveLine().
     * @param g Graphics2D object to draw on.
     */
    public void drawPoly(Graphics2D g) {
        var xPoints = polygon.xpoints;
        var yPoints = polygon.ypoints;
        var points = new ArrayList<Point2D.Float>(polygon.npoints);
        for (int i = 0; i < polygon.npoints; i++) {
            points.add(new Point2D.Float(xPoints[i], yPoints[i]));
        }
        for (int i = 0; i < polygon.npoints-1; i++) {
            drawRecursiveLine(points.get(i), points.get(i+1), recursiveDepth, g);
        }
        if(polygon.npoints > 2) // Connect tail to head
            drawRecursiveLine(points.get(points.size()-1), points.get(0), recursiveDepth, g);
    }

    /**
     * Used for updating the polygon used as a base for rendering.
     * @param p a Polygon.
     */
    public void setPoly(Polygon p){
        polygon = p;
        polygon.translate(200, 200);
        repaint();
    }

    /**
     * Used for updating the recursive blueprint used as a base for rendering.
     * @param points a list of points representing multiple lines used as a recursive blueprint..
     */
    public void setRelativePoints(List<Point2D.Float> points){
        relativePoints = points;
        repaint();
    }

    /**
     * Used for drawing a line, where the line is exchanged for multiple lines recursively by using a recursive blueprint.
     * @param startPoint start point of line.
     * @param endPoint end point of line.
     * @param n recursive depth.
     * @param g Graphics object to draw on.
     */
    public void drawRecursiveLine(Point2D.Float startPoint, Point2D.Float endPoint, int n, Graphics g){
        /* Recursively do this part until n = 0 */
        if(n > 0) {
            /* Substitute the startPoint to endPoint line with multiple lines calculated from relativePoints */
            var newPoints = new ArrayList<Point2D.Float>();
            newPoints.add(startPoint);
            relativePoints.forEach( p -> newPoints.add(calculateActualPoint(p.x, p.y, startPoint, endPoint)));
            newPoints.add(endPoint);

            /* Draw all new sub lines recursively */
            for (int i = 0; i < newPoints.size()-1; i++) {
                drawRecursiveLine(newPoints.get(i), newPoints.get(i+1), n-1, g);
            }
        }
        /* Draw the line normally when recursive depth is reached */
        else {
            g.drawLine((int) startPoint.x, (int) startPoint.y, (int) endPoint.x, (int) endPoint.y);
        }
    }

    /**
     * Draws a polygon on the paintobject.
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        var g2d = (Graphics2D) g; // g is already a Graphics2D object, but needs to be casted for legacy reasons
        drawPoly(g2d);
    }
}
