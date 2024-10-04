package com.m1tron.fractal.view;

import java.awt.Color;
import java.awt.Font;

/**
 * AppConfig class configuration for package constants.
 * Missing javadoc for constants since they should be self-explanatory
 * @author Henrik Oskarsson
 */
public final class ViewConfig {

    private ViewConfig() { throw new IllegalStateException("Utility class"); }

    /* Constants for CenterPanel */
    public static final Color RENDER_PANEL_COLOR = Color.BLUE;
    public static final int RENDER_PANEL_WIDTH = 800;
    public static final int RENDER_PANEL_HEIGHT = 800;
    public static final int RECURSIVE_DEPTH = 5;

    /* Constants for SidePanel */
    public static final Color SIDE_PANEL_FG = Color.YELLOW;
    public static final Color SIDE_PANEL_BG = Color.DARK_GRAY;
    public static final int FONT_SIZE = 24;
    public static final Font SIDE_PANEL_FONT = new Font("Courier New", Font.PLAIN, FONT_SIZE);

    /* Constants for SidePanel buttons */
    public static final String POLY_BUTTON_STRING = "Reset polygon";
    public static final String RECURSIVE_BUTTON_STRING = "Reset recursive";
    public static final String LOAD_BUTTON_STRING = "Load last sprite";
    public static final String STORE_BUTTON_STRING = "Store current sprite";

    /* Constants for MousePanel  */
    public static final String POLY_PANEL_STRING = "Base polygon";
    public static final String RECURSIVE_PANEL_STRING = "Recursive blueprint";

    public static final Color MOUSE_PANEL_FG = SIDE_PANEL_FG;
    public static final Color MOUSE_PANEL_BG = SIDE_PANEL_BG;
    public static final int MOUSE_PANEL_WIDTH = 400;
    public static final int MOUSE_PANEL_HEIGHT = 400;
    public static final int MOUSE_PANEL_SQUARE_LENGTH = 10;

    public static final int[] INIT_COORD_SQUARES_X = {100, 100, 300, 300};
    public static final int[] INIT_COORD_SQUARES_Y = {100, 300, 300, 100};

    public static final int[] INIT_OFFSET_RECURSIVE_X = {-33, 0, +33};
    public static final int[] INIT_OFFSET_RECURSIVE_Y = {0, 84, 0};

    public static final int RECURSIVE_ENDPOINT_OFFSET = 100;



}
