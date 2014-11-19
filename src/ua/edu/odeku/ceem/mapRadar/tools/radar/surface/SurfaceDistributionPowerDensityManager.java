/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.surface;

import com.google.common.primitives.Doubles;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.util.BufferFactory;
import gov.nasa.worldwind.util.BufferWrapper;
import scala.Array;
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame;
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar;
import ua.edu.odeku.ceem.mapRadar.utils.CeemUtilsFunctions;
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Aleo on 20.09.2014.
 */
public class SurfaceDistributionPowerDensityManager {

    protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;
    protected static final double POLAR_RADIUS = AppCeemRadarFrame.wwd().getModel().getGlobe().getPolarRadius();
    protected static final double EQUATORIAL_RADIUS = AppCeemRadarFrame.wwd().getModel().getGlobe().getEquatorialRadius();
    protected static final double GLOBE_RADIUS = (POLAR_RADIUS + EQUATORIAL_RADIUS) / 2.0;

    private static final Angle NORTH = Angle.fromDegrees(0);
    private static final Angle EAST = Angle.fromDegrees(90);
    private static final Angle SOUTH = Angle.fromDegrees(180);
    private static final Angle WEST = Angle.fromDegrees(270);

    private static final int step = 100;

    private static final RenderableLayer renderableLayer = new RenderableLayer() {{
        setPickEnabled(false);
        setName("DistributionPowerDensityManager layer");
    }};

    public static final String SHOW_TYPE_ISOLINE = "ISOLINE";
    public static final String SHOW_TYPE_2D = "2D";
    public static final String SHOW_TYPE_3D = "3D";

    public static void show(String typeShow, int elevation, Radar[] radars) {
        VisibleUtils.insertBeforeCompass(AppCeemRadarFrame.wwd(), renderableLayer);
        renderableLayer.clearList();

        AnalyticSurface distributionPowerDensity = null;
        if(typeShow.equals(SHOW_TYPE_3D)) {
            distributionPowerDensity =
                    createDistribution3DPowerDensity(AppCeemRadarFrame.wwd().getModel().getGlobe().getElevationModel(), step,
                            elevation, radars);
        } else if(typeShow.equals(SHOW_TYPE_2D)) {
            distributionPowerDensity =
                    createDistribution2DPowerDensity(AppCeemRadarFrame.wwd().getModel().getGlobe().getElevationModel(), step,
                            elevation, radars);
        }

        distributionPowerDensity.setClientLayer(renderableLayer);
        renderableLayer.addRenderable(distributionPowerDensity);
        AppCeemRadarFrame.wwd().redraw();
    }

    public static void hiden() {
        renderableLayer.removeAllRenderables();
        AppCeemRadarFrame.wwd().getModel().getLayers().remove(renderableLayer);
        AppCeemRadarFrame.wwd().redraw();
    }



    /**
     * Метод создает сектор который покроет все действие радаров.
     * Алгоритм:
     * проходим все радары. Для каждого находим 4 значения:
     * 1. Позиция на севере максимального действия
     * 2. На востоке
     * 3. На юге.
     * 4. На западе
     *
     * @param radars
     * @return
     */
    private static Sector createSectorForAllRadar(Radar[] radars, double researchHeight) {
        double minLat = 90.0;
        double minLon = 180.0;
        double maxLat = 0;
        double maxLon = -180;

        for (Radar radar : radars) {
            double dist = radar.radiusOnElevation(researchHeight);

            LatLon[] positions = new LatLon[]{
                    destinationPoint(radar.latLon(), NORTH, dist),
                    destinationPoint(radar.latLon(), EAST, dist),
                    destinationPoint(radar.latLon(), SOUTH, dist),
                    destinationPoint(radar.latLon(), WEST, dist)
            };


            for (LatLon pos : positions) {
                minLat = Math.min(minLat, pos.latitude.degrees);
                minLon = Math.min(minLon, pos.longitude.degrees);
                maxLat = Math.max(maxLat, pos.latitude.degrees);
                maxLon = Math.max(maxLon, pos.longitude.degrees);
            }
        }
        return new Sector(Angle.fromDegreesLatitude(minLat), Angle.fromDegreesLatitude(maxLat),
                Angle.fromDegreesLongitude(minLon), Angle.fromDegreesLongitude(maxLon));
    }

    private static AnalyticSurface createDistribution3DPowerDensity(ElevationModel em, int step, double roof, Radar[] radars) {
        Sector sector = createSectorForAllRadar(radars, roof);
        LatLon[][] coordinates = createSectorCoordinates(sector, step);
        double[][] elevation = createElevationSector(coordinates, em, roof);
        double[][] gridPower = gridPower(coordinates, radars, roof);
        double[][] value = mergeElevationAndPower(elevation, gridPower);
        double[] flatValue = flatDoubleArray(value);

        AnalyticSurface distributionPowerDensity = new AnalyticSurface(sector, roof, coordinates.length, coordinates[0].length);

        AnalyticSurfaceAttributes attr = new AnalyticSurfaceAttributes();
        attr.setDrawShadow(true);
        attr.setInteriorOpacity(1.0);
        attr.setOutlineWidth(3);

        distributionPowerDensity.setSurfaceAttributes(attr);

        distributionPowerDensity.setValues(
                AnalyticSurface.createColorGradientValues(
                        createBufferWrapper(flatValue),
                        0.0, //Double.MAX_VALUE,
                        Doubles.min(flatValue) / 25_000,
                        Doubles.max(flatValue),
                        HUE_BLUE,
                        HUE_RED
                )
        );
        return distributionPowerDensity;
    }

    private static AnalyticSurface createDistribution2DPowerDensity(ElevationModel em, int step, double roof, Radar[] radars) {
        Sector sector = createSectorForAllRadar(radars, roof);
        LatLon[][] coordinates = createSectorCoordinates(sector, step);
        double[][] elevation = createElevationSector(coordinates, em, roof);
        double[][] gridPower = gridPower(coordinates, radars, roof);
        double[][] value = mergeElevationAndPower(elevation, gridPower);
        double[] flatValue = flatDoubleArray(value);

        AnalyticSurface distributionPowerDensity = new AnalyticSurface(sector, roof, coordinates.length, coordinates[0].length);
        distributionPowerDensity.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);


        AnalyticSurfaceAttributes attr = new AnalyticSurfaceAttributes();
        attr.setDrawShadow(false);
        attr.setInteriorOpacity(0.6);
        attr.setOutlineWidth(3);

        distributionPowerDensity.setSurfaceAttributes(attr);

        distributionPowerDensity.setValues(
                AnalyticSurface.createColorGradientValues(
                        createBufferWrapper(flatValue),
                        0.0, //Double.MAX_VALUE,
                        Doubles.min(flatValue) / 10_000,
                        Doubles.max(flatValue) ,
                        HUE_BLUE,
                        HUE_RED
                )
        );

        return distributionPowerDensity;
    }


    private static BufferWrapper createBufferWrapper(double[] value) {
        int numValue = value.length;
        BufferFactory bufferFactory = new BufferFactory.DoubleBufferFactory();
        BufferWrapper bufferWrapper = bufferFactory.newBuffer(numValue);
        bufferWrapper.putDouble(0, value, 0, numValue);
        return bufferWrapper;
    }

    private static double[] flatDoubleArray(double[][] array) {
        double[] ret = new double[array.length * array[0].length];
        for (int i = 0; i < array.length; i++) {
            try{
                System.arraycopy(
                        array[i],
                        0,
                        ret,
                        i * array.length,
                        array[i].length
                );
            } catch (Exception e) {
                System.out.println(i);
            }
        }
        return ret;
//        return CeemUtilsFunctions.flatArray(array);
    }


    /**
     * Метод должен совместить рельеф и
     *
     * @param elevation
     * @param power
     * @return
     */
    private static double[][] mergeElevationAndPower(double[][] elevation, double[][] power) {
        // сделаем границы. Обойдем матрицу и везде где есть elevation мощьность сделаем Double.NaN
        for(int i = 0; i < power.length; i++){
            for(int j = 0; j < power[i].length; j++){
                if (elevation[i][j] > 0) {
                    power[i][j] = Double.NaN;
                }
            }
        }



        // Нормализуем
        for(int i = 0; i < power.length; i++){
            for(int j = 0; j < power[i].length; j++){
                if (power[i][j] == Double.NaN) {
                    power[i][j] = 1000;
                }
            }
        }
        return power; //TODO
    }

    /**
     * Метод должен создать сетку мощности
     *
     * @param coordinates соординаты
     * @param radars      радары
     * @return сетка мощности
     */
    private static double[][] gridPower(LatLon[][] coordinates, Radar[] radars, double elevation) {
        double[][] res = new double[coordinates.length][];
        for (int i = 0; i < coordinates.length; i++) {
            res[i] = new double[coordinates[i].length];
            for (int j = 0; j < coordinates[i].length; j++) {
                if(coordinates[i][j] != null){
                    res[i][j] = findMax(powerFromRadar(coordinates[i][j], radars, elevation));
                } else {
                    res[i][j] = 0;
                }

//                if (res[i][j] < 0) {
//                    res[i][j] = 0.0;
//                }
            }
        }
        return res;
    }

    private static double[] powerFromRadar(LatLon pos, Radar[] radars, double elevation) {
        double[] res = new double[radars.length];
        double minLength = Double.MAX_VALUE;
        for (int i = 0; i < radars.length; i++) {
            double length = LatLon.ellipsoidalDistance(pos, radars[i].latLon(), Earth.WGS84_EQUATORIAL_RADIUS, Earth.WGS84_POLAR_RADIUS);
            res[i] = pos != null ? radars[i].power(length, elevation) : Radar.OpacityValuePower();
            minLength = Math.min(minLength, length);
        }
        if(minLength < 1_000) {
            int x = 0;
        }
        return res;
    }

    private static double findMax(double[] doubles) {
        double res = Radar.OpacityValuePower();
        for (double d : doubles) {
            res = Math.max(res, d);
        }
        return res;
    }

    private static int[] findPosition(LatLon[][] mapCoordinates, LatLon position) {
        double latitude = position.getLatitude().degrees;
        double longitude = position.getLongitude().degrees;

        for (int i = 0; i < mapCoordinates.length; i++) {
            for (int j = 0; j < mapCoordinates[i].length; j++) {
                if (latitude == mapCoordinates[i][j].latitude.degrees && longitude == mapCoordinates[i][j].longitude.degrees) { // Если точно совпало
                    return new int[]{i, j};
                } else { // Попробуем проверить, вдруг оно приблезительно подходит, исходя из соседних координат

                    // TODO Возможно неправильный поиск
                    if (i > 1 && i < mapCoordinates.length - 1
                            && j > 1 && j < mapCoordinates[i].length - 1) { // если проверяем внутри в координаты

                        if (latitude >= mapCoordinates[i - 1][j - 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j - 1].longitude.degrees

                                && longitude >= mapCoordinates[i - 1][j].longitude.degrees

                                && latitude <= mapCoordinates[i - 1][j + 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j + 1].longitude.degrees

                                && latitude <= mapCoordinates[i][j + 1].latitude.degrees

                                && latitude <= mapCoordinates[i + 1][j + 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j + 1].longitude.degrees

                                && longitude <= mapCoordinates[i + 1][j].longitude.degrees

                                && latitude >= mapCoordinates[i + 1][j - 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j - 1].longitude.degrees

                                && latitude >= mapCoordinates[i][j - 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i == 0 && j == 0) {
                        if (latitude <= mapCoordinates[i][j + 1].latitude.degrees

                                && latitude <= mapCoordinates[i + 1][j + 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j + 1].longitude.degrees

                                && longitude <= mapCoordinates[i + 1][j].longitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i == mapCoordinates.length - 1 && j == mapCoordinates[i].length) {
                        if (latitude >= mapCoordinates[i - 1][j - 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j - 1].longitude.degrees

                                && longitude >= mapCoordinates[i - 1][j].longitude.degrees

                                && latitude >= mapCoordinates[i][j - 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i == 0 && j == mapCoordinates[i].length - 1) {
                        if (longitude <= mapCoordinates[i + 1][j].longitude.degrees

                                && latitude >= mapCoordinates[i + 1][j - 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j - 1].longitude.degrees

                                && latitude >= mapCoordinates[i][j - 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i == mapCoordinates.length - 1 && j == 0) {
                        if (longitude >= mapCoordinates[i - 1][j].longitude.degrees

                                && latitude <= mapCoordinates[i - 1][j + 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j + 1].longitude.degrees

                                && latitude <= mapCoordinates[i][j + 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i == 0 && j != 0 && j != mapCoordinates[i].length - 1) {
                        if (latitude <= mapCoordinates[i][j + 1].latitude.degrees

                                && latitude <= mapCoordinates[i + 1][j + 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j + 1].longitude.degrees

                                && longitude <= mapCoordinates[i + 1][j].longitude.degrees

                                && latitude >= mapCoordinates[i + 1][j - 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j - 1].longitude.degrees

                                && latitude >= mapCoordinates[i][j - 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i == mapCoordinates.length - 1 && j != 0 && j != mapCoordinates[i].length - 1) {
                        if (latitude >= mapCoordinates[i - 1][j - 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j - 1].longitude.degrees

                                && longitude >= mapCoordinates[i - 1][j].longitude.degrees

                                && latitude <= mapCoordinates[i - 1][j + 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j + 1].longitude.degrees

                                && latitude <= mapCoordinates[i][j + 1].latitude.degrees

                                && latitude >= mapCoordinates[i][j - 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i != 0 && i != mapCoordinates.length - 1 && j == 0) {
                        if (longitude >= mapCoordinates[i - 1][j].longitude.degrees

                                && latitude <= mapCoordinates[i - 1][j + 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j + 1].longitude.degrees

                                && latitude <= mapCoordinates[i][j + 1].latitude.degrees

                                && latitude <= mapCoordinates[i + 1][j + 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j + 1].longitude.degrees

                                && longitude <= mapCoordinates[i + 1][j].longitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                    if (i != 0 && i != mapCoordinates.length - 1 && j == mapCoordinates[i].length - 1) {
                        if (latitude >= mapCoordinates[i - 1][j - 1].latitude.degrees
                                && longitude >= mapCoordinates[i - 1][j - 1].longitude.degrees

                                && longitude >= mapCoordinates[i - 1][j].longitude.degrees

                                && longitude <= mapCoordinates[i + 1][j].longitude.degrees

                                && latitude >= mapCoordinates[i + 1][j - 1].latitude.degrees
                                && longitude <= mapCoordinates[i + 1][j - 1].longitude.degrees

                                & latitude >= mapCoordinates[i][j - 1].latitude.degrees) {
                            return new int[]{i, j};
                        }
                    }

                }

            }

        }

        return null;
    }

    /**
     * Метод возращает карту координат, с учетом шага
     *
     * @param sector сектор у которого надо веруть карту координат
     * @param step   шаг в координатах
     * @return карта координат
     */
    private static LatLon[][] createSectorCoordinates(Sector sector, int step) {
        LinkedList<LinkedList<LatLon>> array = new LinkedList<LinkedList<LatLon>>();
        LatLon[] cornersSector = sector.getCorners();
        LatLon northWestCorner = cornersSector[3];
        for (int i = 0; true; i++) {
            LatLon pos = destinationPoint(northWestCorner, SOUTH, step * i);
            if (sector.getMinLatitude().degrees > pos.latitude.degrees) {
                break;
            }
            LinkedList<LatLon> innerArray = new LinkedList<LatLon>();
            for (int j = 0; true; j++) {
                LatLon next = destinationPoint(pos, EAST, step * j);
                if (sector.getMaxLongitude().degrees < next.longitude.degrees) {
                    break;
                }
                innerArray.add(next);
            }
            array.add(innerArray);
        }
        return listOfListToMatrix(array);
    }

    private static LatLon[][] listOfListToMatrix(LinkedList<LinkedList<LatLon>> listOfList) {
        int maxWidth = 0, maxHeight = listOfList.size();
        for (LinkedList<LatLon> list : listOfList) {
            maxWidth = Math.max(list.size(), maxWidth);
        }

        LatLon[][] matrix = new LatLon[maxHeight][maxWidth];
        Iterator<LinkedList<LatLon>> iteratorList = listOfList.iterator();
        for (int i = 0; iteratorList.hasNext(); i++) {
            LinkedList<LatLon> list = iteratorList.next();
            Iterator<LatLon> iter = list.iterator();
            for (int initIndex = (maxWidth - list.size()) / 2; iter.hasNext(); initIndex++) {
                matrix[i][initIndex] = iter.next();
            }
        }
        return matrix;
    }

    /**
     * Метод возращает карту возвышености по карте координат
     *
     * @param sectorCoordinates карта координат
     * @param em                модель возвышеностей
     * @return карту где указаны возвышености
     */
    private static double[][] createElevationSector(LatLon[][] sectorCoordinates, ElevationModel em, double roof) {
        double[][] sectorElevation = new double[sectorCoordinates.length][];
        for (int i = 0, sectorCoordinatesLength = sectorCoordinates.length; i < sectorCoordinatesLength; i++) {
            LatLon[] line = sectorCoordinates[i];
            sectorElevation[i] = new double[line.length];
            for (int j = 0, lineLength = line.length; j < lineLength; j++) {
                LatLon latLon = line[j];
                if (latLon != null) {
                    double elevation = em.getElevation(latLon.latitude, latLon.longitude);
                    sectorElevation[i][j] =  elevation < roof ? 0: elevation;
                }
            }
        }
        return sectorElevation;
    }

    /**
     * Инкрементация широты на указаной долготе. на указаный шаг в метре
     *
     * @param longitudeDegrees долгота в градусах
     * @param latitudeDegrees  широта в градусах
     * @param step             шаг в метрах
     * @return новый градус по указаной долготе
     */
    private static double incLatitude(double longitudeDegrees, double latitudeDegrees, int step) {
        return latitudeDegrees + createLatitudeDegreesStep(longitudeDegrees, step);
    }

    /**
     * Высчитывает сколько надо увеличить градус широты что бы приклюсовать эти метры
     *
     * @param longitudeDegrees долгота в градусах
     * @param meter            шаг в метрах
     * @return шаг градусов широты для заданой долготы
     */
    private static double createLatitudeDegreesStep(double longitudeDegrees, int meter) {
        return secondLatitudeInMeter(longitudeDegrees) / meter * Earth.NKOEF;
    }

    /**
     * Метод инкрементирует долготу, на указаный шаг в метрах
     *
     * @param longitudeDegrees долгота в градусах
     * @param step             шаг итерирования в метрах
     * @return следующая долгота в градусах
     */
    private static double incLongitude(double longitudeDegrees, int step) {
        return longitudeDegrees + createLongitudeDegreesStep(step);
    }

    /**
     * Указывает на сколько надо увеличивать градусы что бы получить прибавку в метрах по долготе
     *
     * @param meter прибавку в метрах
     * @return увелечение в градусах
     */
    private static double createLongitudeDegreesStep(int meter) {
        return SECOND_LONGITUDE_IN_METER / meter * Earth.NKOEF;
    }


    private static final double LENGTH_MERIDIAN = calculatePerimeterEllipse(
            AppCeemRadarFrame.wwd().getModel().getGlobe().getEquatorialRadius(),
            AppCeemRadarFrame.wwd().getModel().getGlobe().getPolarRadius()
    );

    private static final double LENGTH_EQUATORIAL = 2 * Math.PI * AppCeemRadarFrame.wwd().getModel().getGlobe().getEquatorialRadius();

    /**
     * Константа указывающая сколько метров в одной секунде
     */
    public static final double SECOND_LONGITUDE_IN_METER = LENGTH_MERIDIAN / 360.0 / 60 / 60 / 1000;

    private static final double SECOND_LATITUDE_IN_METER = LENGTH_EQUATORIAL / 360.0 / 60 / 60 / 1000;

    /**
     * Возрадает значение сколько метров в одной секунде широты на заданой долготе
     *
     * @param longitudeDegrees долгота в градусах
     * @return количество метров в одной секунде
     */
    private static double secondLatitudeInMeter(double longitudeDegrees) {
        return Math.cos(longitudeDegrees) * SECOND_LATITUDE_IN_METER;
    }

    private static double calculatePerimeterEllipse(double a, double b) {
        return 4 * ((Math.PI * a * b + (a - b)) / (a + b));
    }

    /**
     * Returns the destination point from  point having travelled the given distance on the
     * given initial bearing (bearing normally varies around path followed).
     */
    private static LatLon destinationPoint(LatLon pos, Angle azimuth, double dist) {
        if (dist == 0) {
            return new LatLon(pos);
        }
        double θ = azimuth.radians;
        double δ = dist / GLOBE_RADIUS;

        double φ1 = pos.latitude.radians;
        double λ1 = pos.longitude.radians;

        double φ2 = Math.asin(Math.sin(φ1) * Math.cos(δ) +
                Math.cos(φ1) * Math.sin(δ) * Math.cos(θ));
        double λ2 = λ1 + Math.atan2(Math.sin(θ) * Math.sin(δ) * Math.cos(φ1),
                Math.cos(δ) - Math.sin(φ1) * Math.sin(φ2));
        λ2 = (λ2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI; // normalise to -180..+180º
        return LatLon.fromRadians(φ2, λ2);
    }
}
