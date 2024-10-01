package com.dt181g.laboration_3.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.dt181g.laboration_3.view.ViewConfig.INIT_COORD_SQUARES_X;
import static com.dt181g.laboration_3.view.ViewConfig.INIT_COORD_SQUARES_Y;
import static com.dt181g.laboration_3.view.ViewConfig.INIT_OFFSET_RECURSIVE_X;
import static com.dt181g.laboration_3.view.ViewConfig.INIT_OFFSET_RECURSIVE_Y;
import static com.dt181g.laboration_3.view.ViewConfig.MOUSE_PANEL_BG;
import static com.dt181g.laboration_3.view.ViewConfig.MOUSE_PANEL_FG;
import static com.dt181g.laboration_3.view.ViewConfig.MOUSE_PANEL_HEIGHT;
import static com.dt181g.laboration_3.view.ViewConfig.MOUSE_PANEL_SQUARE_LENGTH;
import static com.dt181g.laboration_3.view.ViewConfig.MOUSE_PANEL_WIDTH;
import static com.dt181g.laboration_3.view.ViewConfig.RECURSIVE_ENDPOINT_OFFSET;

/**
 * A component with mouse operations for adding and removing squares.
 * This sample class is built upon to facilitate building of polygons.
 * Major changes are documented in place. Original code available @ <a href="https://horstmann.com/corejava/corejava.zip">...</a> v01ch10.
 * The original class was named MouseComponent.
 * @version 1.35 2018-04-10
 * @author Cay Horstmann
 * @author Henrik Oskarsson
 */
public class EditorPanel extends JPanel
{
    private static final int SQUARE_LENGTH = MOUSE_PANEL_SQUARE_LENGTH;
    private static final int WIDTH = MOUSE_PANEL_WIDTH;
    private static final int HEIGHT = MOUSE_PANEL_HEIGHT;
    private ArrayList<Rectangle2D> squares;
    private Rectangle2D current; // the square containing the mouse cursor
    private boolean isPolygon; // NEW. Defines panel usage, true = polygon, false = recursive blueprint

    /**
     * Editor panel setup.
     * @param isPoly is used to decide behaviour of editor (polygon vs recursive). Should probably be governed by a subclassing..
     * @param name name of the panel for label.
     */
    public EditorPanel(Boolean isPoly, String name)
    {
        add(new JLabel(name));
        setBackground(MOUSE_PANEL_BG);
        setForeground(MOUSE_PANEL_FG);

        squares = new ArrayList<>();

        current = null;
        isPolygon = isPoly;
        initSquares();

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());

        setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Override to communicate preferred size to parent component.
     * @return a Dimension representing the preferred panel size.
     */
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(ViewConfig.MOUSE_PANEL_WIDTH, ViewConfig.MOUSE_PANEL_HEIGHT);
    }

    /**
     * Added method to insert some start values.
     */
    private void initSquares() {
        if (isPolygon) {
            IntStream.range(0, INIT_COORD_SQUARES_X.length).forEach(i ->
                    add(new Point2D.Float(INIT_COORD_SQUARES_X[i], INIT_COORD_SQUARES_Y[i])));

        } else {
            for (int i = 0; i < INIT_OFFSET_RECURSIVE_X.length; i++) {
                add(new Point2D.Float(WIDTH/2 + INIT_OFFSET_RECURSIVE_X[i], HEIGHT/2 + INIT_OFFSET_RECURSIVE_Y[i]));
            }
        }
    }

    /**
     * Added method for generating a polygon.
     * @return returns a polygon generated by using the squares as reference.
     */
    public Polygon generatePolygon() {
        var temp = new Polygon();
        squares.forEach(s -> temp.addPoint((int)s.getCenterX(), (int)s.getCenterY()));
        return temp;
    }

    /**
     * Added method for generating a list of points from squares.
     * Also adds a predefined start and endpoint.
     * @return list of points for each square.
     */
    private List<Point2D.Float> generatePoints() {
        List<Point2D.Float> temp = new ArrayList<>();
        var centerX = WIDTH/2;
        var centerY = HEIGHT/2;
        temp.add(new Point2D.Float((centerX-RECURSIVE_ENDPOINT_OFFSET), centerY));
        squares.stream().map( s -> new Point2D.Float((float) s.getCenterX(), (float) s.getCenterY()) ).forEach(point -> temp.add(point));
        temp.add(new Point2D.Float((centerX+RECURSIVE_ENDPOINT_OFFSET), centerY));
        return temp;
    }

    /**
     * Added method for generating a list of points.
     * Also adds a predefined start and endpoint.
     * @return list of points for each square.
     */
    public List<Point2D.Float> generateTransformedPoints() {
        List<Point2D.Float> lst = generatePoints();
        List<Point2D.Float> temp = new ArrayList<>();
        var lastIndex = lst.size()-1;
        for (int i = 1; i < lastIndex; i++) {
        temp.add(RenderPanel.calculateRelativePoint(lst.get(0), lst.get(lastIndex), lst.get(i)));
        }

        return temp;
    }

    /**
     * Not edited.
     * @param g
     */
    public void paintComponent(Graphics g)
    {
        var g2 = (Graphics2D) g;

        // draw all squares
        for (Rectangle2D r : squares) {
            g2.draw(r);

        }

        /* Drawing of polygon and recursive outline. This is a new feature. */
        var poly = generatePolygon();
        if(isPolygon){
            g2.draw(poly);
        }
        else {
            var points = generatePoints();
            for (int i = 0; i < points.size()-1; i++) {
                g2.draw(new Line2D.Float(points.get(i), points.get(i+1)));
            }
        }
    }

    /**
     * Finds the first square containing a point.
     * @param p a point
     * @return the first square that contains p
     */
    public Rectangle2D find(Point2D p)
    {
        for (Rectangle2D r : squares)
        {
            if (r.contains(p)) return r;
        }

        return null;
    }

    /**
     * Adds a square to the collection.
     * @param p the center of the square
     */
    public void add(Point2D p)
    {
        double x = p.getX();
        double y = p.getY();

        current = new Rectangle2D.Double(x - SQUARE_LENGTH / 2, y - SQUARE_LENGTH / 2,
                SQUARE_LENGTH, SQUARE_LENGTH);
        squares.add(current);
        repaint();
    }

    /**
     * Removes a square from the collection.
     * @param s the square to remove
     */
    public void remove(Rectangle2D s)
    {
        if (s == null) return;
        if (s == current) current = null;
        squares.remove(s);
        repaint();
    }

    private class MouseHandler extends MouseAdapter
    {
        /**
         * Not edited.
         * @param event
         */
        public void mousePressed(MouseEvent event)
        {
            // add a new square if the cursor isn't inside a square
            current = find(event.getPoint());
            if (current == null) add(event.getPoint());
        }

        /**
         * Not edited.
         * @param event
         */
        public void mouseClicked(MouseEvent event)
        {
            // remove the current square if double clicked
            current = find(event.getPoint());
            if (current != null && event.getClickCount() >= 2) remove(current);
        }
    }

    private class MouseMotionHandler implements MouseMotionListener
    {
        /**
         * Not edited.
         * @param event
         */
        public void mouseMoved(MouseEvent event)
        {
            // set the mouse cursor to cross hairs if it is inside a rectangle

            if (find(event.getPoint()) == null) setCursor(Cursor.getDefaultCursor());
            else setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

        /**
         * Not edited.
         * @param event
         */
        public void mouseDragged(MouseEvent event)
        {
            if (current != null)
            {
                int x = event.getX();
                int y = event.getY();

                // drag the current rectangle to center it at (x, y)
                current.setFrame(x - SQUARE_LENGTH / 2, y - SQUARE_LENGTH / 2, SQUARE_LENGTH, SQUARE_LENGTH);
                repaint();
            }
        }
    }
}
