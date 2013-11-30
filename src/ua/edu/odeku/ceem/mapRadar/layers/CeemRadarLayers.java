/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.layers;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.AGeoNameLayer;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.PGeoNameLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Aleo skype: aleo72
 * Date: 17.11.13
 * Time: 23:09
 */
public abstract class CeemRadarLayers {

    public static void insertGeoNameLayers(WorldWindow worldWindow, JMenu menu) {

        {
            final Layer layer = new AGeoNameLayer();
            final JCheckBoxMenuItem item = new JCheckBoxMenuItem("GeoName A", false);
            layer.setEnabled(false);
            worldWindow.getModel().getLayers().add(layer);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                    layer.setEnabled(item.isSelected());
                }
            });

            menu.add(item);
        }

        {
            final Layer layer = new PGeoNameLayer();
            final JCheckBoxMenuItem item = new JCheckBoxMenuItem("GeoName P", false);
            layer.setEnabled(false);
            worldWindow.getModel().getLayers().add(layer);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                    layer.setEnabled(item.isSelected());
                }
            });

            menu.add(item);
        }

    }
}
