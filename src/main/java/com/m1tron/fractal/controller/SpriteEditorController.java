package com.m1tron.fractal.controller;

import com.m1tron.fractal.model.Model;
import com.m1tron.fractal.view.EditorView;

import javax.swing.WindowConstants;


/**
 * Controller class that handles events triggered by user and carries out the necessary modification/updates to view and model.
 * @author Henrik Oskarsson
 */
public class SpriteEditorController {
    private EditorView view;
    private Model model;

    /**
     * Controller constructor initializes program by creating and binding a model and a view to itself.
     */
    public SpriteEditorController() {
        view = new EditorView();
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
        model = new Model();

        /* View controller hooks */
        view.getStoreButton().addActionListener( (e)-> saveAction());
        view.getLoadButton().addActionListener( (e)-> loadAction());
        view.getPolyBlueprintPanel().setMouseDragCallback(this::polyAction);
        view.getRecursiveBlueprintPanel().setMouseDragCallback(this::recursiveAction);

        /* Reset Button hooks */
        view.getRecursiveButton().addActionListener( (e)-> view.getRecursiveBlueprintPanel().initSquares());
        view.getPolyButton().addActionListener( (e)-> view.getPolyBlueprintPanel().initSquares());

        /* Push default figure to render */
        polyAction();
        recursiveAction();
    }

    /**
     * Event handling of action when Polygon update button is clicked.
     */
    public void polyAction() {
        view.updatePoly(view.getPolygonEditorPolygon());
    }

    /**
     * Event handling of action when recursive blueprint update button is clicked.
     */
    public void recursiveAction() {
        view.updateRecursive(view.getRecursiveEditorPoints());
    }

    /**
     * Event handling of action when save button is clicked.
     */
    public void saveAction() {
        model.storeSprite(model.createSprite(view.getPolygonEditorPolygon(), view.getRecursiveEditorPoints()));
    }

    /**
     * Event handling of action when load button is clicked.
     */
    public void loadAction() {
        var s = model.readSprite();
        if(s == null) {
            view.displayError("Could not load file")    ;
        }
        else {
            view.updatePoly(s.polygon());
            view.updateRecursive(s.recursivePoints());
        }
    }
}
