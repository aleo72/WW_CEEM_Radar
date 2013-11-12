package ua.edu.odeku.ceem.mapRadar.db.models;

import org.hibernate.annotations.Index;
import ua.edu.odeku.ceem.mapRadar.db.models.geoNameEnum.*;
import ua.edu.odeku.ceem.mapRadar.exceptions.db.models.GeoNameException;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram;
import ua.edu.odeku.ceem.mapRadar.utils.translate.TranslateManager;

import javax.persistence.*;
import java.util.Locale;

/**
 * User: Aleo skype: aleo72
 * Date: 09.11.13
 * Time: 20:29
 */
@Entity
@Table(name = "GEO_NAME", uniqueConstraints =
    @UniqueConstraint(columnNames = {"NAME", "LAT", "LON", "FEATURE_CLASS"}))
public class GeoName {

    public GeoName() {
    }

    public GeoName(int sourceId,
                   String name, String asciiname,
                   double lat, double lon,
                   char featureClass, String featureCode) {
        this.sourceId = sourceId;
        this.name = name;
        this.asciiname = asciiname;
        this.lat = lat;
        this.lon = lon;
        this.featureClass = featureClass;
        this.featureCode = featureCode;
        if(PropertyProgram.isTranslateGeoName()){
            this.translateName =
                    TranslateManager.getTranslatable(this.countryCode).translate(this.asciiname);
        } else {
            this.translateName = asciiname;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * integer id of record in geonames database
     * Данное поле необходимо для быстрой проверки на нахождения данного названия в таблице.
     */
    @Column(name = "SOURCE_ID", nullable = false, unique = true)
    @Index(name = "SOURCE_ID_INDEX")
    private int sourceId;

    /**
     * name of geographical point (utf8) varchar(200)
     */
    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    /**
     * name of geographical point in plain ascii characters, varchar(200)
     */
    @Column(name = "ASCIINAME", nullable = false, length = 200)
    private String asciiname;

    /**
     * alternatenames, comma separated varchar(5000)
     */
    @Column(name = "ALTERNATENAMES", nullable = true, length = 5000)
    private String alternatenames;

    /**
     * latitude in decimal degrees (wgs84)
     */
    @Column(name = "LAT", nullable = false)
    private double lat;

    /**
     * longitude in decimal degrees (wgs84)
     */
    @Column(name = "LON", nullable = false)
    private double lon;

    /**
     * see http://www.geonames.org/export/codes.html, char(1)
     * <p/>
     * A country, state, region,...
     * H stream, lake, ...
     * L parks,area, ...
     * P city, village,...
     * R road, railroad
     * S spot, building, farm
     * T mountain,hill,rock,...
     * U undersea
     * V forest,heath,...
     */
    @Column(name = "FEATURE_CLASS", nullable = false)
    private char featureClass;

    /**
     * see http://www.geonames.org/export/codes.html, varchar(10)
     */
    @Column(name = "FEATURE_CODE", nullable = true)
    private String featureCode;

    /**
     * ISO-3166 2-letter country code, 2 characters
     */
    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @Column(name = "ELEVATION", nullable = true)
    private Integer elevation;

    @Column(name = "TRANSLATE", nullable = true)
    private String translateName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsciiname() {
        return asciiname;
    }

    public void setAsciiname(String asciiname) {
        this.asciiname = asciiname;
    }

    public String getAlternatenames() {
        return alternatenames;
    }

    public String[] getAlternatenamesArray() {
        if (alternatenames != null) {
            return alternatenames.split(",");
        }
        return new String[0];
    }

    public void setAlternatenames(String alternatenames) {
        this.alternatenames = alternatenames;
    }

    public void setAlternatenames(String[] alternatenames) {
        if (alternatenames != null) {
            StringBuilder sb = new StringBuilder(alternatenames.length * 20);
            String split = "";
            for (String name : alternatenames) {
                sb.append(split);
                sb.append(name);
                if (split.length() == 0) {
                    split = ",";
                }
            }
            this.alternatenames = sb.toString().trim();
        } else {
            this.alternatenames = null;
        }
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public char getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(char featureClass) throws GeoNameException {
        for(FeatureClass feature : FeatureClass.values()){
            if (feature.toString().equals(String.valueOf(featureClass))) {
                this.featureClass = featureClass;
                return;
            }
        }
        throw new GeoNameException("Feature Class not be used '"+featureClass+"'");
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) throws GeoNameException {
        if (featureCode == null || featureCode.trim().isEmpty())
            return;

        for(FeatureClass feature : FeatureClass.values()){
            if (feature.toString().equals(String.valueOf(featureClass))) {
                switch (feature) {
                    case A:
                        for(FeatureCode_A code : FeatureCode_A.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case H:
                        for(FeatureCode_H code : FeatureCode_H.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case L:
                        for(FeatureCode_L code : FeatureCode_L.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case P:
                        for(FeatureCode_P code : FeatureCode_P.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case R:
                        for(FeatureCode_R code : FeatureCode_R.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case S:
                        for(FeatureCode_S code : FeatureCode_S.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case T:
                        for(FeatureCode_T code : FeatureCode_T.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case U:
                        for(FeatureCode_U code : FeatureCode_U.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                    case V:
                        for(FeatureCode_V code : FeatureCode_V.values()){
                            if (code.toString().equals(String.valueOf(featureCode))) {
                                this.featureCode = featureCode;
                                return;
                            }
                        }
                        break;
                }
                break;
            }
        }
        throw new GeoNameException("not valid feature code: " + featureCode);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) throws GeoNameException {
        for (String country : Locale.getISOCountries()){
            if(country.equalsIgnoreCase(countryCode)){
                this.countryCode = countryCode.toUpperCase();
                return;
            }
        }
        throw new GeoNameException("not valid countyCode: "+countryCode);
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTranslateName() {
        return translateName;
    }

    public void setTranslateName(String translateName) {
        this.translateName = translateName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sourceId);
        sb.append(", ");
        sb.append(name);
        sb.append(", ");
        sb.append(asciiname);
        sb.append(", ");
        sb.append(alternatenames);
        sb.append(", ");
        sb.append(lat);
        sb.append(", ");
        sb.append(lon);
        sb.append(", ");
        sb.append(featureClass);
        sb.append(", ");
        sb.append(featureCode);
        sb.append(", ");
        sb.append(countryCode);
        sb.append(", ");
        sb.append(elevation);
        return sb.toString();
    }

    /**
     * Метод преобразует строку в объект.
     * При этом парсит его по указаному стандарту geoName
     * <p/>
     * The main 'geoname' table has the following fields :
     * ---------------------------------------------------
     * geonameid         : integer id of record in geonames database
     * name              : name of geographical point (utf8) varchar(200)
     * asciiname         : name of geographical point in plain ascii characters, varchar(200)
     * alternatenames    : alternatenames, comma separated varchar(5000)
     * latitude          : latitude in decimal degrees (wgs84)
     * longitude         : longitude in decimal degrees (wgs84)
     * feature class     : see http://www.geonames.org/export/codes.html, char(1)
     * feature code      : see http://www.geonames.org/export/codes.html, varchar(10)
     * country code      : ISO-3166 2-letter country code, 2 characters
     * cc2               : alternate country codes, comma separated, ISO-3166 2-letter country code, 60 characters
     * admin1 code       : fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
     * admin2 code       : code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80)
     * admin3 code       : code for third level administrative division, varchar(20)
     * admin4 code       : code for fourth level administrative division, varchar(20)
     * population        : bigint (8 byte int)
     * elevation         : in meters, integer
     * dem               : digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
     * timezone          : the timezone id (see file timeZone.txt) varchar(40)
     * modification date : date of last modification in yyyy-MM-dd format
     *
     * @param line строка для парсинга
     * @return GeoName объект
     */
    public static GeoName createGeoName(String line) throws GeoNameException {
        GeoName geoName = new GeoName();

        String[] columns = line.trim().split("\t");

        try{
            geoName.setSourceId(Integer.parseInt(columns[0]));
            geoName.setName(columns[1]);
            geoName.setAsciiname(columns[2]);
            geoName.setAlternatenames(columns[3]);
            geoName.setLat(Double.parseDouble(columns[4]));
            geoName.setLon(Double.parseDouble(columns[5]));
            geoName.setFeatureClass(columns[6].charAt(0));
            geoName.setFeatureCode(columns[7]);
            geoName.setCountryCode(columns[8]);
            String eleva = columns[15];
            if(eleva != null && !eleva.trim().isEmpty())
                geoName.setElevation(Integer.parseInt(eleva));

            if(PropertyProgram.isTranslateGeoName()){
                geoName.translateName =
                        TranslateManager.getTranslatable(geoName.countryCode).translate(geoName.asciiname);
            } else {
                geoName.translateName = geoName.asciiname;
            }

        } catch (Exception e){
            throw new GeoNameException(ResourceString.get("exception_GeoName")+"\n"+ line + "\n" + e.getMessage());
        }

        return geoName;
    }


}
