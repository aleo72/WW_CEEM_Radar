/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: Aleo skype: aleo72
 * Date: 20.11.13
 * Time: 20:29
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel(BufferedImage image){
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0, 0, null);
    }
}
