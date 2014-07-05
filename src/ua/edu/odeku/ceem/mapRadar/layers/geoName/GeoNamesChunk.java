/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.GeographicText;
import gov.nasa.worldwind.render.UserFacingText;
import ua.edu.odeku.ceem.mapRadar.db.model.GeoName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Aleo skype: aleo72
 * Date: 19.11.13
 * Time: 14:58
 */
public class GeoNamesChunk {

    protected final GeoNames geoNames;
    protected List<GeoName> geoNameList;

    public GeoNamesChunk(GeoNames geoNames) {
        this.geoNames = geoNames;
        geoNameList = getGeoNameList();
    }

    public List<GeoName> getGeoNameList(){
        if (geoNameList == null){
            geoNameList = Arrays.asList(ua.edu.odeku.ceem.mapRadar.db.model.GeoNames.list(null, geoNames.geoNameCountry, geoNames.geoNameClass, geoNames.geoNameCode));
        }
        return geoNameList;
    }

    public Iterable<GeographicText> makeIterable(DrawContext dc) {
        double maxDisplayDistance = this.geoNames.getMaxDisplayDistance();
        ArrayList<GeographicText> list = new ArrayList<GeographicText>();

        for(GeoName geoName : getGeoNameList()){
            CharSequence str = getText(geoName);
            Position pos = getPosition(geoName);
            GeographicText text = new UserFacingText(str, pos);
            text.setFont(this.geoNames.font);
            text.setColor(this.geoNames.getColor());
            text.setBackgroundColor(this.geoNames.getBackgroundColor());
            text.setVisible(GeoNameLayer.isNameVisible(dc, this.geoNames, pos));
            text.setPriority(maxDisplayDistance);
            list.add(text);
        }
        return list;
    }

    private Position getPosition(GeoName geo) {
        return Position.fromDegrees(geo.lat(), geo.lon(), 0);
    }

    private CharSequence getText(GeoName geoName) {
        if(geoName.translateName() != null && !geoName.translateName().isEmpty()){
            return geoName.name() + "(" + geoName.translateName() + ")";
        } else {
            return geoName.name();
        }
    }
}
