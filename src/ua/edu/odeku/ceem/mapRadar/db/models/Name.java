package ua.edu.odeku.ceem.mapRadar.db.models;

import javax.persistence.*;

/**
 * User: Aleo skype: aleo72
 * Date: 09.11.13
 * Time: 20:29
 */
@Entity
@Table(name = "NAME", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "LAT", "LON"}))
public class Name {

    public Name() {  }

    public Name(String name, String asciiname,
                double lat, double lon,
                char featureClass, String featureCode) {
        this.name = name;
        this.asciiname = asciiname;
        this.lat = lat;
        this.lon = lon;
        this.featureClass = featureClass;
        this.featureCode =featureCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
     *  see http://www.geonames.org/export/codes.html, char(1)
     *
     *  A country, state, region,...
     *  H stream, lake, ...
     *  L parks,area, ...
     *  P city, village,...
     *  R road, railroad
     *  S spot, building, farm
     *  T mountain,hill,rock,...
     *  U undersea
     *  V forest,heath,...
     */
    @Column(name = "FEATURE_CLASS", nullable = false)
    private char featureClass;

    /**
     *  see http://www.geonames.org/export/codes.html, varchar(10)
     */
    @Column(name = "FEATURE_CODE", nullable = false)
    private String featureCode;

    /**
     * ISO-3166 2-letter country code, 2 characters
     */
    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @Column(name = "ELEVATION", nullable = true)
    private Integer elevation;

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
        if (alternatenames != null){
            return alternatenames.split(",");
        }
        return new String[0];
    }

    public void setAlternatenames(String alternatenames) {
        this.alternatenames = alternatenames;
    }

    public void setAlternatenames(String[] alternatenames) {
        if (alternatenames != null ){
            StringBuilder sb = new StringBuilder(alternatenames.length * 20);
            String split = "";
            for (String name : alternatenames){
                sb.append(split);
                sb.append(name);
                if(split.length() == 0){
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

    public void setFeatureClass(char featureClass) {
        this.featureClass = featureClass;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }
}
