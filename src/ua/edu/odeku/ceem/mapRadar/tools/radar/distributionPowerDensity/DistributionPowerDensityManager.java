/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.util.BufferWrapper;
import gov.nasa.worldwind.util.PropertyAccessor;
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame;
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame$;
import ua.edu.odeku.ceem.mapRadar.tools.radar.models.Radar;

import java.util.LinkedList;

/**
 * Created by Aleo on 20.09.2014.
 */
public class DistributionPowerDensityManager {

    public static void main(String[] args) {

//        createBufferWrapper(new Sector())

    }

    public static BufferWrapper createBufferWrapper(Sector sector, ElevationModel em, int step, double roof, Radar[] radars) {
        LatLon[][] coordinates = createSectorCoordinates(sector, step);
        double[][] elevation = createElevationSector(coordinates, em);
        double[][] gridPower = gridPower(coordinates, radars);



        return null;
    }


    /**
     * Метод должен совместить рельеф и
     * @param elevation
     * @param power
     * @return
     */
    private double[][] mergeElevationAndPower(double[][] elevation, double[][] power){
        return power; //TODO
    }

    /**
     * Метод должен создать сетку мощности
     * @param coordinates соординаты
     * @param radars радары
     * @return сетка мощности
     */
    public static double[][] gridPower(LatLon[][] coordinates, Radar[] radars) {
        double[][] res = new double[coordinates.length][];
        for (int i = 0; i < coordinates.length; i++){
            res[i] = new double[coordinates[i].length];

            for(int j = 0; j < coordinates[i].length; j++){
                res[i][j] = findMax(powerFromRadar(coordinates[i][j], radars));
            }
        }
        return res;
    }

    private static double[] powerFromRadar(LatLon pos, Radar[] radars) {
        double[] res = new double[radars.length];
        for (int i = 0; i < radars.length; i++){
            res[i] = radars[i].power(LatLon.ellipsoidalDistance(pos, radars[i].latLon(), Earth.WGS84_EQUATORIAL_RADIUS, Earth.WGS84_POLAR_RADIUS));
        }
        return res;
    }

    private static double findMax(double[] doubles){
        double res = Double.MIN_VALUE;
        for (double d: doubles){
            res = Math.max(res, d);
        }
        return res;
    }

    public static int[] findPosition(LatLon[][] mapCoordinates, LatLon position) {
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
        for (int i = 0; true; i++) { // longitude
            double longitude = incLongitude(sector.getMinLongitude().degrees, i * step);
            if (longitude > sector.getMaxLongitude().degrees) {
                break;
            }
            LinkedList<LatLon> innerArray = new LinkedList<LatLon>();
            for (int j = 0; true; j++) { // latitude
                double latitude = incLatitude(longitude, sector.getMinLatitude().degrees, i * step);
                if (latitude > sector.getMaxLatitude().degrees) {
                    break;
                }
                innerArray.add(LatLon.fromDegrees(latitude, longitude));
            }
            array.add(innerArray);
        }

        LatLon[][] sectorCoordinates = new LatLon[array.size()][];
        int i = 0;
        for (LinkedList<LatLon> listData : array) {
            sectorCoordinates[i++] = listData.toArray(new LatLon[listData.size()]);
        }
        return sectorCoordinates;
    }

    /**
     * Метод возращает карту возвышености по карте координат
     *
     * @param sectorCoordinates карта координат
     * @param em                модель возвышеностей
     * @return карту где указаны возвышености
     */
    private static double[][] createElevationSector(LatLon[][] sectorCoordinates, ElevationModel em) {
        double[][] sectorElevation = new double[sectorCoordinates.length][];
        int i = 0;
        for (LatLon[] line : sectorCoordinates) {
            int j = 0;
            sectorElevation[i] = new double[line.length];
            for (LatLon latLon : line) {
                sectorElevation[i][j++] = em.getElevation(latLon.latitude, latLon.longitude);
            }
            i++;
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
    public static double incLatitude(double longitudeDegrees, double latitudeDegrees, int step) {
        return latitudeDegrees + createLatitudeDegreesStep(longitudeDegrees, step);
    }

    /**
     * Высчитывает сколько надо увеличить градус широты что бы приклюсовать эти метры
     *
     * @param longitudeDegrees долгота в градусах
     * @param meter            шаг в метрах
     * @return шаг градусов широты для заданой долготы
     */
    public static double createLatitudeDegreesStep(double longitudeDegrees, int meter) {
        return secondLatitudeInMeter(longitudeDegrees) / meter * Earth.NKOEF;
    }

    /**
     * Метод инкрементирует долготу, на указаный шаг в метрах
     *
     * @param longitudeDegrees долгота в градусах
     * @param step             шаг итерирования в метрах
     * @return следующая долгота в градусах
     */
    public static double incLongitude(double longitudeDegrees, int step) {
        return longitudeDegrees + createLongitudeDegreesStep(step);
    }

    /**
     * Указывает на сколько надо увеличивать градусы что бы получить прибавку в метрах по долготе
     *
     * @param meter прибавку в метрах
     * @return увелечение в градусах
     */
    public static double createLongitudeDegreesStep(int meter) {
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
    public static double secondLatitudeInMeter(double longitudeDegrees) {
        return Math.cos(longitudeDegrees) * SECOND_LATITUDE_IN_METER;
    }

    private static double calculatePerimeterEllipse(double a, double b) {
        return 4 * ((Math.PI * a * b + (a - b)) / (a + b));
    }
}
