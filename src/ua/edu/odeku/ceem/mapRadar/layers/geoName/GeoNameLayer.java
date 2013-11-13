package ua.edu.odeku.ceem.mapRadar.layers.geoName;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.render.DrawContext;

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


    public GeoNameLayer(GeoNamesSet geoNameCitesSet){

    }

    @Override
    protected void doRender(DrawContext dc) {

    }
}
