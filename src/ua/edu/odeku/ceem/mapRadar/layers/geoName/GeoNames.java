package ua.edu.odeku.ceem.mapRadar.layers.geoName;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.Tile;

import java.awt.*;

/**
 * User: Aleo skype: aleo72
 * Date: 12.11.13
 * Time: 22:56
 */
public class GeoNames {

    protected static final Sector TILING_SECTOR = Sector.FULL_SPHERE;
    private static final int MAX_ABSENT_TILE_TRIES = 2;
    private static final int MIN_ABSENT_TILE_CHECK_INTERVAL = 10000;

    public final String geoNameClass;
    public final String geoNameCode;

    public final LatLon tileDelta;
    public final Font font;

    private boolean enabled;
    private java.awt.Color color;
    private java.awt.Color backgroundColor;
    private double minDisplayDistance;
    private double maxDisplayDistance;

    private Sector maskingSector = null;

    private int numColumns;


    public GeoNames(String geoNameClass, String geoNameCode, LatLon tileDelta, Font font) {
        this.tileDelta = tileDelta;
        this.geoNameClass = geoNameClass;
        this.geoNameCode = geoNameCode;
        this.font = font;

        this.enabled = true;
        this.color = Color.WHITE;
        this.minDisplayDistance = Double.MIN_VALUE;
        this.maxDisplayDistance = Double.MAX_VALUE;

        String message = this.validate();
        if (message != null)
        {
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        this.numColumns = this.numColumnsInLevel();
    }

    private int numColumnsInLevel() {
        int firstCol = Tile.computeColumn(this.tileDelta.getLongitude(), TILING_SECTOR.getMinLongitude(), Angle.NEG180);
        int lastCol = Tile.computeColumn(this.tileDelta.getLongitude(), TILING_SECTOR.getMaxLongitude().subtract(this.tileDelta.getLongitude()), Angle.NEG180);

        return lastCol - firstCol + 1;
    }

    private String validate() {
        String msg = "";
        if (this.geoNameClass == null)
        {
            msg += Logging.getMessage("nullValue.GeoNameClassIsNull") + ", ";
        }
        if (this.geoNameCode == null)
        {
            msg += Logging.getMessage("nullValue.GeoNameCodeIsNull") + ", ";
        }
        if (this.tileDelta == null)
        {
            msg += Logging.getMessage("nullValue.TileDeltaIsNull") + ", ";
        }
        if (this.font == null)
        {
            msg += Logging.getMessage("nullValue.FontIsNull") + ", ";
        }

        if (msg.length() == 0)
        {
            return null;
        }

        return msg;
    }

    public final GeoNames copy(){
        GeoNames copy = new GeoNames(this.geoNameClass, this.geoNameCode, this.tileDelta, this.font);
        return copy;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBackgroundColor() {
        if (this.backgroundColor == null)
            this.backgroundColor = suggestBackgroundColor(this.color);
        return this.backgroundColor;
    }

    private Color suggestBackgroundColor(Color foreground) {
        float[] compArray = new float[4];
        Color.RGBtoHSB(foreground.getRed(), foreground.getGreen(), foreground.getBlue(), compArray);
        int colorValue = compArray[2] < 0.5f ? 255 : 0;
        int alphaValue = foreground.getAlpha();
        return new Color(colorValue, colorValue, colorValue, alphaValue);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getMinDisplayDistance() {
        return minDisplayDistance;
    }

    public void setMinDisplayDistance(double minDisplayDistance) {
        this.minDisplayDistance = minDisplayDistance;
    }

    public double getMaxDisplayDistance() {
        return maxDisplayDistance;
    }

    public void setMaxDisplayDistance(double maxDisplayDistance) {
        this.maxDisplayDistance = maxDisplayDistance;
    }

    public int getNumColumns() {
        return numColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;

        final GeoNames other = (GeoNames) o;

        if(this.geoNameClass != null ? !this.geoNameClass.equals(other.geoNameClass) : other.geoNameClass != null){
            return false;
        }
        if (this.geoNameCode != null ? !this.geoNameCode.equals(other.geoNameCode) : other.geoNameCode != null){
            return false;
        }
        return true;
    }

    public Sector getMaskingSector() {
        return maskingSector;
    }

    public void setMaskingSector(Sector maskingSector) {
        this.maskingSector = maskingSector;
    }
}
