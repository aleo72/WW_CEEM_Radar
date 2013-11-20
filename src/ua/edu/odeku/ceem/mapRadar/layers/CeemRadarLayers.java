/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.layers;

import gov.nasa.worldwind.WorldWindow;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.AGeoNameLayer;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.PGeoNameLayer;

/**
 * User: Aleo skype: aleo72
 * Date: 17.11.13
 * Time: 23:09
 */
public abstract class CeemRadarLayers {

    public static void insertCeemRadarLayers(WorldWindow worldWindow) {
        worldWindow.getModel().getLayers().add(new AGeoNameLayer());
        worldWindow.getModel().getLayers().add(new PGeoNameLayer());
    }
}
