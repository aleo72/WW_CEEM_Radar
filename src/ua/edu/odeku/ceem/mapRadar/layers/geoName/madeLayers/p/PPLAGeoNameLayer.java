/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.p;

import gov.nasa.worldwind.geom.Sector;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.GeoNameLayer;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.GeoNames;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.GeoNamesSet;

import java.awt.*;

/**
 * User: Aleo skype: aleo72
 * Date: 17.11.13
 * Time: 22:58
 */
public class PPLAGeoNameLayer extends GeoNameLayer {

    public PPLAGeoNameLayer() {
        super(createGeoNamesSet());
    }

    private static GeoNamesSet createGeoNamesSet() {
        GeoNamesSet geoNamesSet = new GeoNamesSet();

        Font font = Font.decode("Arial-11");

        String geoClass = "P", geoCode = "PPLA";
        GeoNames geoNames = new GeoNames(null, geoClass, geoCode, Sector.FULL_SPHERE, GRID_36x72, font);
        geoNames.setMinDisplayDistance(0d);
        geoNames.setMaxDisplayDistance(LEVEL_E);
        geoNames.setColor(Color.lightGray);
        geoNamesSet.addGeoNames(geoNames, true);


        return geoNamesSet;
    }
}
