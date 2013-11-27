/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.testing.dome;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils;

/**
 * User: Aleo Bakalov
 * Date: 27.11.13
 * Time: 13:47
 */
public class DomeView {

    public static void add(WorldWindow wwd){
        Position odessa = Position.fromDegrees(46.477, 30.733, 1e3);

        ShapeAttributes normalAttributes = new BasicShapeAttributes();
        normalAttributes.setOutlineOpacity(0.6);
        normalAttributes.setInteriorOpacity(0.4);
        normalAttributes.setOutlineMaterial(Material.WHITE);

        ShapeAttributes highlightAttributes = new BasicShapeAttributes(normalAttributes);
        highlightAttributes.setOutlineOpacity(0.3);
        highlightAttributes.setInteriorOpacity(0.6);

        Dome dome = new Dome();
        dome.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        dome.setPosition(odessa);
        dome.setAzimuth(Angle.fromDegrees(0));
        dome.setElevationAngle(Angle.fromDegrees(0));
        dome.setAttributes(normalAttributes);
        dome.setHighlightAttributes(highlightAttributes);
        dome.setGainOffset(640);
        dome.setGainScale(10);

        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(dome);
        layer.setName("Dome");
        VisibleUtils.insertBeforeCompass(wwd, layer);
    }
}
