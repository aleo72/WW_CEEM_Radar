package ua.edu.odeku.ceem.mapRadar.layers.geoName;

import gov.nasa.worldwind.WWObjectImpl;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.util.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Aleo skype: aleo72
 * Date: 12.11.13
 * Time: 22:57
 */
public class GeoNamesSet extends WWObjectImpl {

    private final List<GeoNames> geoNamesList = new ArrayList<GeoNames>(20);
    private long expiryTime = 0;

    public void addGeoNames(GeoNames geoNames, boolean replace){

        if(geoNames != null){
            for(int i = 0; i < this.geoNamesList.size(); i++){
                final GeoNames other = this.geoNamesList.get(i);
                if(geoNames.geoNameClass.equals(other.geoNameClass) && geoNames.geoNameCode.equals(other.geoNameCode)){
                    if(replace){
                        this.geoNamesList.set(i, geoNames);
                        return;
                    } else {
                        return;
                    }
                }
            }

            this.geoNamesList.add(geoNames);
        } else {
            String message = Logging.getMessage("nullValue.GeoNamesIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
    }

    public GeoNamesSet copy(){
        GeoNamesSet copy = new GeoNamesSet();
        copy.setValues(this);

        for (int i = 0; i < this.geoNamesList.size(); i++){
            copy.geoNamesList.add(i, this.geoNamesList.get(i).copy());
        }

        copy.expiryTime = expiryTime;

        return copy;
    }

    public final int getGeoNamesSetSize(){
        return this.geoNamesList.size();
    }

    public final GeoNames getGeoNames(int index){
        return this.geoNamesList.get(index);
    }

    public List<GeoNames> getGeoNamesList(){
        return geoNamesList;
    }

    public GeoNames getGeoNames(String class_code){

        for (GeoNames geoNames : this.geoNamesList) {
            if ((geoNames.geoNameClass + "_" + geoNames.geoNameCode).equalsIgnoreCase(class_code)) {
                return geoNames;
            }
        }
        return null;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }
}
