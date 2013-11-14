package ua.edu.odeku.ceem.mapRadar.layers.geoName;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.util.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Aleo skype: aleo72
 * Date: 12.11.13
 * Time: 22:00
 */
public class GeoNameLayer extends AbstractLayer {

    public static final double LEVEL_A = 0x1 << 25; // 33,554 km
    public static final double LEVEL_B = 0x1 << 24; // 16,777 km
    public static final double LEVEL_C = 0x1 << 23; // 8,388 km
    public static final double LEVEL_D = 0x1 << 22; // 4,194 km
    public static final double LEVEL_E = 0x1 << 21; // 2,097 km
    public static final double LEVEL_F = 0x1 << 20; // 1,048 km
    public static final double LEVEL_G = 0x1 << 19; // 524 km
    public static final double LEVEL_H = 0x1 << 18; // 262 km
    public static final double LEVEL_I = 0x1 << 17; // 131 km
    public static final double LEVEL_J = 0x1 << 16; // 65 km
    public static final double LEVEL_K = 0x1 << 15; // 32 km
    public static final double LEVEL_L = 0x1 << 14; // 16 km
    public static final double LEVEL_M = 0x1 << 13; // 8 km
    public static final double LEVEL_N = 0x1 << 12; // 4 km
    public static final double LEVEL_O = 0x1 << 11; // 2 km
    public static final double LEVEL_P = 0x1 << 10; // 1 km
    public static final LatLon GRID_1x1 = new LatLon(Angle.fromDegrees(180d), Angle.fromDegrees(360d));
    public static final LatLon GRID_4x8 = new LatLon(Angle.fromDegrees(45d), Angle.fromDegrees(45d));
    public static final LatLon GRID_8x16 = new LatLon(Angle.fromDegrees(22.5d), Angle.fromDegrees(22.5d));
    public static final LatLon GRID_16x32 = new LatLon(Angle.fromDegrees(11.25d), Angle.fromDegrees(11.25d));
    public static final LatLon GRID_36x72 = new LatLon(Angle.fromDegrees(5d), Angle.fromDegrees(5d));
    public static final LatLon GRID_72x144 = new LatLon(Angle.fromDegrees(2.5d), Angle.fromDegrees(2.5d));
    public static final LatLon GRID_144x288 = new LatLon(Angle.fromDegrees(1.25d), Angle.fromDegrees(1.25d));
    public static final LatLon GRID_288x576 = new LatLon(Angle.fromDegrees(0.625d), Angle.fromDegrees(0.625d));
    public static final LatLon GRID_576x1152 = new LatLon(Angle.fromDegrees(0.3125d), Angle.fromDegrees(0.3125d));
    public static final LatLon GRID_1152x2304 = new LatLon(Angle.fromDegrees(0.1563d), Angle.fromDegrees(0.1563d));

    protected final GeoNamesSet geoNamesSet;
    protected Vec4 referencePoint;
    protected List<NavigationTile> navTiles = new ArrayList<NavigationTile>();

    public GeoNameLayer(GeoNamesSet geoNamesSet){

        if (geoNamesSet == null)
        {
            String message = Logging.getMessage("nullValue.GeoNamesSetIsNull");
            Logging.logger().fine(message);
            throw new IllegalArgumentException(message);
        }

        this.geoNamesSet = geoNamesSet.copy();
        for (GeoNames geoNames : geoNamesSet.getGeoNamesList()){
            int calc = (int) (GeoNames.TILING_SECTOR.getDeltaLatDegrees() / geoNames.tileDelta.getLatitude().getDegrees());

            int numLevels = (int) Math.log(calc);
            navTiles.add( new NavigationTile(geoNames, GeoNames.TILING_SECTOR, numLevels, "top"));
        }
    }

    @Override
    protected void doRender(DrawContext dc) {
        this.referencePoint = this.computeReferencePoint(dc);

        for(GeoNames geoNames : this.geoNamesSet.getGeoNamesList()){
            if(!isGeoNamesVisible(dc, geoNames)){
                continue;
            }

            double minDistSquared = geoNames.getMinDisplayDistance() * geoNames.getMinDisplayDistance();
            double maxDistSquared = geoNames.getMaxDisplayDistance() * geoNames.getMaxDisplayDistance();

            if(isSectorVisible(dc, geoNames.getMaskingSector(), minDistSquared, maxDistSquared)){
                // TODO do....
            }
        }
    }

    private boolean isSectorVisible(DrawContext drawContext, Sector sector, double minDistSquared, double maxDistSquared) {
        View view = drawContext.getView();

        Position eyePos = view.getEyePosition();
        if(eyePos == null){
            return false;
        }
        Angle lat = clampAngle(eyePos.getLatitude(), sector.getMinLatitude(), sector.getMaxLatitude());
        Angle lon = clampAngle(eyePos.getLongitude(), sector.getMinLongitude(), sector.getMaxLongitude());
        Vec4 p = drawContext.getGlobe().computePointFromPosition(lat, lon, 0d);
        double distSquared = drawContext.getView().getEyePoint().distanceToSquared3(p);

        return !(minDistSquared > distSquared || maxDistSquared < distSquared);
    }

    private Angle clampAngle(Angle a, Angle min, Angle max) {
        double degrees = a.degrees;
        double minDegrees = min.degrees;
        double maxDegrees = max.degrees;
        return Angle.fromDegrees(degrees < minDegrees ? minDegrees : (degrees > maxDegrees ? maxDegrees : degrees));
    }

    private boolean isGeoNamesVisible(DrawContext drawContext, GeoNames geoNames) {
        return geoNames.isEnabled()
                && (drawContext.getVisibleSector() != null)
                && geoNames.getMaskingSector().intersects(drawContext.getVisibleSector());
    }

    protected Vec4 computeReferencePoint(DrawContext drawContext){
        if(drawContext.getViewportCenterPosition() != null){
            return drawContext.getGlobe().computePointFromPosition(drawContext.getViewportCenterPosition());
        }

        java.awt.geom.Rectangle2D viewport = drawContext.getView().getViewport();
        int x = (int) viewport.getWidth() / 2;
        for(int y = (int) (0.5 * viewport.getHeight()); y >= 0; y--){
            Position pos = drawContext.getView().computePositionFromScreenPoint(x, y);
            if(pos == null){
                continue;
            }
            return drawContext.getGlobe().computePointFromPosition(pos.getLatitude(), pos.getLongitude(), 0d);
        }

        return null;
    }

    protected class NavigationTile {
        String id;
        protected GeoNames geoNames;
        public Sector navSector;
        protected List<NavigationTile> subNavTiles = new ArrayList<NavigationTile>();
        protected List<String> tileKeys = new ArrayList<String>();
        protected int level;

        NavigationTile(GeoNames geoNames, Sector sector, int levels, String id){
            this.geoNames = geoNames;
            this.navSector = sector;
            this.level = levels;
            this.id = id;
        }

    }
}
