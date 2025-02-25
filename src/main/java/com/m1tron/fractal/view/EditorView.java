package com.m1tron.fractal.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * The editor view present the user with two editor panels where the user can create a blueprint for a sprite.
 * When done the user can issue a command (through the controller) to render the blueprint.
 * Functionality also exists for saving a completed blueprint.
 * @author Henrik Oskarsson
 */
public class EditorView extends JFrame {

    private JLabel infoLabel =
            new JLabel("<html><h2>Fractal Sprite Maker 2000 Mk2 </h2>" +
                    "<h3>Default blueprints are already set in the right panel." +
                    "<br>To edit the sprite you can interact with blueprints.</h3> " +
                    "<br> - Right-click adds a vertex." +
                    "<br> - Double-clicking a Square removes it." +
                    "<br> - Drag to move squares." +
            "<br> - Use Save/Load-buttons to read or store creations to current directory</html>");

    /* Panel references */
    private BlackPaddedPanel mainPanel;
    private RenderPanel renderPanel;
    private BlackPaddedPanel sidepanel;

    /* Sub panel reference */
    private EditorPanel polyPanel = new EditorPanel(true, ViewConfig.POLY_PANEL_STRING);
    private JButton polyButton = new JButton(ViewConfig.POLY_BUTTON_STRING);
    private EditorPanel recursivePanel = new EditorPanel(false, ViewConfig.RECURSIVE_PANEL_STRING);
    private JButton recursiveButton = new JButton(ViewConfig.RECURSIVE_BUTTON_STRING);
    private JPanel buttonPanel = new BlackPaddedPanel();
    private JButton storeButton = new JButton(ViewConfig.STORE_BUTTON_STRING);
    private JButton loadButton = new JButton(ViewConfig.LOAD_BUTTON_STRING);


    /**
     * Constructor of EditorView. Initializes sub panels, and connects buttons to the controller.
     */
    public EditorView() {

        /* Main panel */
        mainPanel = new BlackPaddedPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);


        /* Render panel set up */
        renderPanel = new RenderPanel();
        mainPanel.add(renderPanel, BorderLayout.CENTER);
        renderPanel.add(infoLabel, BorderLayout.NORTH);
        infoLabel.setForeground(Color.RED);

        /* Side panel set up */
        sidepanel = new BlackPaddedPanel();
        mainPanel.add(sidepanel, BorderLayout.EAST);

        sidepanel.setForeground(ViewConfig.MOUSE_PANEL_FG);
        sidepanel.setFont(ViewConfig.SIDE_PANEL_FONT);
        sidepanel.setBackground(ViewConfig.MOUSE_PANEL_BG);
        sidepanel.setBorder(new EtchedBorder());
        sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));

        /* Button panel set up */
        sidepanel.add(buttonPanel);
        buttonPanel.setBackground(sidepanel.getBackground());
        buttonPanel.add(storeButton);
        buttonPanel.add(loadButton);

        /* Other side panel buttons set up */
        polyPanel.add(polyButton);
        sidepanel.add(polyPanel);
        recursivePanel.add(recursiveButton);
        sidepanel.add(recursivePanel);



        pack();
    }

    /**
     * Used by controller when it has a new polygon to render.
     * @param p polygon to be rendered.
     */
    public void updatePoly(Polygon p) {
        renderPanel.setPoly(p);
    }

    /**
     * Used by controller when it has a new recursive blueprint to render.
     * @param p recursive blueprint to be rendered.
     */
    public void updateRecursive(List<Point2D.Float> p) {
        renderPanel.setRelativePoints(p);
    }

    /**
     * Getter for load button.
     * @return returns load button.
     */
    public JButton getLoadButton() {
        return loadButton;
    }

    /**
     * Getter for store button.
     * @return returns store button.
     */
    public JButton getStoreButton() {
        return storeButton;
    }

    /**
     * Getter for poly button.
     * @return returns poly button.
     */
    public JButton getPolyButton() {
        return polyButton;
    }

    /**
     * Getter for set recursive button.
     * @return returns set recursive button.
     */
    public JButton getRecursiveButton() {
        return recursiveButton;
    }

    /**
     * Getter for set recursive button.
     * @return returns set recursive button.
     */
    public EditorPanel getRecursiveBlueprintPanel() {
        return recursivePanel;
    }

    /**
     * Getter for set recursive button.
     * @return returns set recursive button.
     */
    public EditorPanel getPolyBlueprintPanel() {
        return polyPanel;
    }

    /**
     * Getter for recursive panel points.
     * @return returns a list of recursive points.
     */
    public List<Point2D.Float> getRecursiveEditorPoints() {
        return recursivePanel.generateTransformedPoints();
    }

    /**
     * Getter for polygon panel polygon.
     * @return returns the polygon editor's polygon.
     */
    public Polygon getPolygonEditorPolygon() {
        return polyPanel.generatePolygon();
    }

    /**
     * Method for displaying error.
     * @param error the error message.
     */
    public void displayError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
