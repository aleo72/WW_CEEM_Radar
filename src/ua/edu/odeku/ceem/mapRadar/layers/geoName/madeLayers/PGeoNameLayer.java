/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName.madeLayers;

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
public class PGeoNameLayer extends GeoNameLayer {

    public PGeoNameLayer() {
        super(createGeoNamesSet());
    }

    private static final String geoNameClass = "P";

    private static GeoNamesSet createGeoNamesSet() {
        GeoNamesSet geoNamesSet = new GeoNamesSet();
        GeoNames geoNames;

        geoNames = new GeoNames(null, geoNameClass, "PPLC", Sector.FULL_SPHERE, GRID_36x72, Font.decode("Arial-BOLD-12"));
        geoNames.setMinDisplayDistance(0d);
        geoNames.setMaxDisplayDistance(LEVEL_D);
        geoNames.setColor(new Color(247, 70, 70));
        geoNamesSet.addGeoNames(geoNames, true);

        geoNames = new GeoNames(null, geoNameClass, "PPLA", Sector.FULL_SPHERE, GRID_36x72, Font.decode("Arial-BOLD-14"));
        geoNames.setMinDisplayDistance(0d);
        geoNames.setMaxDisplayDistance(LEVEL_E);
        geoNames.setColor(Color.LIGHT_GRAY);
        geoNamesSet.addGeoNames(geoNames, true);

        geoNames = new GeoNames(null, geoNameClass, "PPLA2", Sector.FULL_SPHERE, GRID_36x72, Font.decode("Arial-11"));
        geoNames.setMinDisplayDistance(0d);
        geoNames.setMaxDisplayDistance(LEVEL_G);
        geoNames.setColor(Color.LIGHT_GRAY);
        geoNamesSet.addGeoNames(geoNames, true);

        geoNames = new GeoNames(null, geoNameClass, "PPL", Sector.FULL_SPHERE, GRID_1x1, Font.decode("Arial-14"));
        geoNames.setMinDisplayDistance(0d);
        geoNames.setMaxDisplayDistance(LEVEL_I);
        geoNames.setColor(Color.WHITE);
        geoNamesSet.addGeoNames(geoNames, true);

        return geoNamesSet;
    }
}
