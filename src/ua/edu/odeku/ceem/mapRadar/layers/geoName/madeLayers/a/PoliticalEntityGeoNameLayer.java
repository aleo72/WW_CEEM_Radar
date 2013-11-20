/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers.a;

import gov.nasa.worldwind.geom.Sector;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.GeoNameLayer;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.GeoNames;
import ua.edu.odeku.ceem.mapRadar.layers.geoName.GeoNamesSet;

import java.awt.*;

/**
 * User: Aleo skype: aleo72
 * Date: 17.11.13
 * Time: 22:18
 */
public class PoliticalEntityGeoNameLayer extends GeoNameLayer {

    public PoliticalEntityGeoNameLayer() {
        super(createGeoNamesSet());
    }

    private static GeoNamesSet createGeoNamesSet() {
        GeoNamesSet geoNamesSet = new GeoNamesSet();
        Font font = Font.decode("Arial-BOLDITALIC-12");


        String geoClass = "A", geoCode = "PCLI";
        GeoNames geoNames = new GeoNames(null, geoClass, geoCode, Sector.FULL_SPHERE, GRID_4x8, font);
        geoNames.setMinDisplayDistance(LEVEL_G);
        geoNames.setMaxDisplayDistance(LEVEL_D);
        geoNamesSet.addGeoNames(geoNames, true);
        return geoNamesSet;
    }
}
