package com.dt181g.laboration_3.view;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

/**
 * Extension of JPanel to leverage settings that will apply to all panels.
 * @author Henrik Oskarsson
 */
public class BlackPaddedPanel extends JPanel {
    private static final int PADDING = ViewConfig.FONT_SIZE;

    /**
     * Here goes config that should apply to all panels.
     */
    public BlackPaddedPanel() {
        setBackground(Color.BLACK);
        setBorder(new EmptyBorder(PADDING,PADDING,PADDING,PADDING));
    }
}
