package ua.edu.odeku.ceem.mapRadar.layers.geoName;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.GeographicText;
import gov.nasa.worldwind.render.UserFacingText;
import gov.nasa.worldwind.util.Logging;
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;

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
    protected PriorityBlockingQueue<Runnable> requestQ = new PriorityBlockingQueue<Runnable>(64);

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
        int index = -1;
        for (GeoNames geoNames : this.geoNamesSet.getGeoNamesList()) {
            index += 1;
            if (!isGeoNamesVisible(dc, geoNames)) {
                continue;
            }

            double minDistSquared = geoNames.getMinDisplayDistance() * geoNames.getMinDisplayDistance();
            double maxDistSquared = geoNames.getMaxDisplayDistance() * geoNames.getMaxDisplayDistance();

            if (isSectorVisible(dc, geoNames.getMaskingSector(), minDistSquared, maxDistSquared)) {
                ArrayList<Tile> baseTiles = new ArrayList<Tile>();
                NavigationTile navigationTile = this.navTiles.get(index);
                List<NavigationTile> list = navigationTile.navTilesVisible(dc, minDistSquared, maxDistSquared);

                for (NavigationTile navTile : list) {
                    baseTiles.addAll(navTile.getTiles());
                }

                for (Tile tile : baseTiles){
                    try{
                        drawOrRequestTile(dc, tile, minDistSquared, maxDistSquared);
                    } catch (Exception e){
                        Logging.logger().log(Level.FINE,
                                Logging.getMessage("layers.GeoNameLayer.ExceptionRenderingTile"),
                                e);
                    }
                }
            }
        }
        this.sendRequests();
        this.requestQ.clear();
    }

    protected void drawOrRequestTile(DrawContext dc, Tile tile, double minDistSquared, double maxDistSquared) {
        if(!isTileVisible(dc, tile, minDistSquared, maxDistSquared))
            return;

        Iterable<GeographicText> renderIter = tile.makeIterable(dc);
        dc.getDeclutteringTextRenderer().render(dc, renderIter);
    }

    protected static boolean isTileVisible(DrawContext drawContext, Tile tile, double minDistSquared, double maxDistSquared) {
        if (!tile.getSector().intersects(drawContext.getVisibleSector())){
            return false;
        }

        View view = drawContext.getView();
        Position eyePos = view.getEyePosition();
        if(eyePos == null){
            return false;
        }
        Angle lat = clampAngle(eyePos.getLatitude(), tile.getSector().getMinLatitude(), tile.getSector().getMaxLatitude());
        Angle lon = clampAngle(eyePos.getLongitude(), tile.getSector().getMinLongitude(),
                tile.getSector().getMaxLongitude());
        Vec4 p = drawContext.getGlobe().computePointFromPosition(lat, lon, 0d);
        double distSquared = drawContext.getView().getEyePoint().distanceToSquared3(p);

        return !(minDistSquared > distSquared || maxDistSquared < distSquared);
    }

    private void sendRequests() {
        Runnable task = this.requestQ.poll();
        while (task != null){
            if(!WorldWind.getTaskService().isFull()){
                WorldWind.getTaskService().addTask(task);
            }
            task = this.requestQ.poll();
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

    protected static Angle clampAngle(Angle a, Angle min, Angle max) {
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

    protected Tile[] buildTiles(GeoNames geoNames, NavigationTile navTile)
    {
        final Angle dLat = geoNames.tileDelta.getLatitude();
        final Angle dLon = geoNames.tileDelta.getLongitude();

        // Determine the row and column offset from the global tiling origin for the southwest tile corner
        int firstRow = Tile.computeRow(dLat, navTile.navSector.getMinLatitude());
        int firstCol = Tile.computeColumn(dLon, navTile.navSector.getMinLongitude());
        int lastRow = Tile.computeRow(dLat, navTile.navSector.getMaxLatitude().subtract(dLat));
        int lastCol = Tile.computeColumn(dLon, navTile.navSector.getMaxLongitude().subtract(dLon));

        int nLatTiles = lastRow - firstRow + 1;
        int nLonTiles = lastCol - firstCol + 1;

        Tile[] tiles = new Tile[nLatTiles * nLonTiles];

        Angle p1 = Tile.computeRowLatitude(firstRow, dLat);
        for (int row = 0; row <= lastRow - firstRow; row++)
        {
            Angle p2;
            p2 = p1.add(dLat);

            Angle t1 = Tile.computeColumnLongitude(firstCol, dLon);
            for (int col = 0; col <= lastCol - firstCol; col++)
            {
                Angle t2;
                t2 = t1.add(dLon);
                //Need offset row and column to correspond to total ro/col numbering
                tiles[col + row * nLonTiles] = new Tile(geoNames, new Sector(p1, p2, t1, t2),
                        row + firstRow, col + firstCol);
                t1 = t2;
            }
            p1 = p2;
        }

        return tiles;
    }

    protected static boolean isNameVisible(DrawContext dc, GeoNames geoNames, Position position) {
        double elevation = dc.getVerticalExaggeration() * position.getElevation();
        Vec4 namePoint = dc.getGlobe().computePointFromPosition(position.getLatitude(), position.getLongitude(), elevation);
        Vec4 eyeVec = dc.getView().getEyePoint();

        double dist = eyeVec.distanceTo3(namePoint);
        return dist >= geoNames.getMinDisplayDistance() && dist <= geoNames.getMaxDisplayDistance();
    }

    protected class NavigationTile {
        String id;
        protected GeoNames geoNames;
        public Sector navSector;
        protected List<NavigationTile> subNavTiles = new ArrayList<NavigationTile>();
        protected int level;

        NavigationTile(GeoNames geoNames, Sector sector, int levels, String id){
            this.geoNames = geoNames;
            this.navSector = sector;
            this.level = levels;
            this.id = id;
        }

        public List<NavigationTile> navTilesVisible(DrawContext dc, double minDistSquared, double maxDistSquared) {
            ArrayList<NavigationTile> navList = new ArrayList<NavigationTile>();
            if (this.isNavSectorVisible(dc, minDistSquared, maxDistSquared)){
                if (this.level > 0 && !this.hasSubTiles()){
                    this.buildSubNavTiles();
                }

                if(this.hasSubTiles()){
                    for (NavigationTile navigationTile : subNavTiles){
                        navList.addAll(navigationTile.navTilesVisible(dc, minDistSquared, maxDistSquared));
                    }
                }  else {
                    navList.add(this);
                }
            }
            return navList;
        }

        private void buildSubNavTiles() {
            if(level > 0){
                Sector[] sectors = this.navSector.subdivide();
                for(int i = 0; i < sectors.length; i++){
                    subNavTiles.add(new NavigationTile(geoNames, sectors[i], level - 1, this.id + "." + i));
                }
            }
        }

        public boolean hasSubTiles() {
            return !subNavTiles.isEmpty();
        }

        private boolean isNavSectorVisible(DrawContext drawContext, double minDistSquared, double maxDistSquared) {
            if (!navSector.intersects(drawContext.getVisibleSector())){
                return false;
            }
            View view = drawContext.getView();
            Position eyePos = view.getEyePosition();
            if (eyePos == null){
                return false;
            }
            if (Double.isNaN(eyePos.getLatitude().getDegrees()) || Double.isNaN(eyePos.getLongitude().getDegrees())){
                return false;
            }
            Angle lat = clampAngle(eyePos.getLatitude(), navSector.getMinLatitude(), navSector.getMaxLatitude());
            Angle lon = clampAngle(eyePos.getLongitude(), navSector.getMinLongitude(), navSector.getMaxLongitude());

            Vec4 p = drawContext.getGlobe().computePointFromPosition(lat, lon, 0d);
            double distSquare = drawContext.getView().getEyePoint().distanceTo3(p);

            return !(minDistSquared > distSquare || maxDistSquared < distSquare);
        }

        public List<Tile> getTiles() {
            /**
             * Здесь необходимо произвести считывание данных из database.
             * От кеширования отказался так как может потребоваться динамически менять имена,
             * тем более имен много и в базе они будут компактней.
             */
            Tile[] tiles = buildTiles(this.geoNames, this);
            return Arrays.asList(tiles);
            /*
             if (tileKeys.isEmpty())
            {
                Tile[] tiles = buildTiles(this.placeNameService, this);
                //load tileKeys
                for (Tile t : tiles)
                {
                    tileKeys.add(t.getFileCachePath());
                    WorldWind.getMemoryCache(Tile.class.getName()).add(t.getFileCachePath(), t);
                }
                return Arrays.asList(tiles);
            }
            else
            {
                List<Tile> dataTiles = new ArrayList<Tile>();
                for (String s : tileKeys)
                {
                    Tile t = (Tile) WorldWind.getMemoryCache(Tile.class.getName()).getObject(s);
                    if (t != null)
                    {
                        dataTiles.add(t);
                    }
                }
                return dataTiles;
            }
             */
        }

    }

    protected static class Tile {
        protected final GeoNames geoNames;
        protected final Sector sector;
        protected final int row;
        protected final int column;

        Tile(GeoNames geoNames, Sector sector, int row, int column){
            this.geoNames = geoNames;
            this.sector = sector;
            this.row = row;
            this.column = column;
        }

        protected Sector getSector(){
            return sector;
        }

        public Iterable<GeographicText> makeIterable(DrawContext dc) {
            double maxDisplayDistance = this.geoNames.getMaxDisplayDistance();
            List<GeoName> listGeoName = geoNames.getGeoNamesFromDB();
            ArrayList<GeographicText> list = new ArrayList<GeographicText>(listGeoName.size());
            for(GeoName geoName : listGeoName){
                CharSequence str = geoName.getName();
                Position position = Position.fromDegrees(geoName.getLat(), geoName.getLon(), 0d);
                GeographicText text = new UserFacingText(str, position);
                text.setFont(this.geoNames.font);
                text.setColor(this.geoNames.getColor());
                text.setBackgroundColor(this.geoNames.getBackgroundColor());
                text.setVisible(isNameVisible(dc, this.geoNames, position));
                text.setPriority(maxDisplayDistance);
                list.add(text);
            }
            return list;
        }

        static int computeRow(Angle delta, Angle latitude)
        {
            if (delta == null || latitude == null)
            {
                String msg = Logging.getMessage("nullValue.AngleIsNull");
                Logging.logger().severe(msg);
                throw new IllegalArgumentException(msg);
            }
            return (int) ((latitude.getDegrees() + 90d) / delta.getDegrees());
        }

        static int computeColumn(Angle delta, Angle longitude)
        {
            if (delta == null || longitude == null)
            {
                String msg = Logging.getMessage("nullValue.AngleIsNull");
                Logging.logger().severe(msg);
                throw new IllegalArgumentException(msg);
            }
            return (int) ((longitude.getDegrees() + 180d) / delta.getDegrees());
        }

        static Angle computeRowLatitude(int row, Angle delta)
        {
            if (delta == null)
            {
                String msg = Logging.getMessage("nullValue.AngleIsNull");
                Logging.logger().severe(msg);
                throw new IllegalArgumentException(msg);
            }
            return Angle.fromDegrees(-90d + delta.getDegrees() * row);
        }

        static Angle computeColumnLongitude(int column, Angle delta)
        {
            if (delta == null)
            {
                String msg = Logging.getMessage("nullValue.AngleIsNull");
                Logging.logger().severe(msg);
                throw new IllegalArgumentException(msg);
            }
            return Angle.fromDegrees(-180 + delta.getDegrees() * column);
        }
    }
}
