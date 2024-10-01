package com.m1tron.fractal;

import com.m1tron.fractal.controller.SpriteEditorController;

import javax.swing.SwingUtilities;

/**
 * The main starting point for Fractal
 * @author Henrik Oskarsson
 */
public final class FractalMain {
    private FractalMain() { // Utility classes should not have a public or default constructor
        throw new IllegalStateException("Utility class");
    }

    /**
     * Entry point for the Sprite Editor!
     * @param args command arguments not in use.
     */
    public static void main(final String... args) {
        System.out.println("Sprite fractal editor");
        SwingUtilities.invokeLater(() -> new SpriteEditorController());
    }
}
