/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.utils.geometry;

/**
 * User: Aleo skype: aleo72
 * Date: 20.11.13
 * Time: 21:57
 */
public final class LatitudeLongitudeUtils {

    public static boolean isValidChar(String data){
        if(data == null || data.isEmpty())
            return false;
        else {
            boolean res = true;

            for (char c : data.trim().toCharArray()){
                if(!(Character.isDigit(c) || c == '.' || c == '-')){
                    res = false;
                    break;
                }
            }
            return res;
        }
    }

    public static boolean isValidLatitude(String data){
        if(isValidChar(data)){
            double d = Double.parseDouble(data);
            if( d >= -90.0 && d <= 90.0 )
                return true;
        }
        return false;
    }

    public static boolean isValidLongitude(String data){
        if(isValidChar(data)){
            double d = Double.parseDouble(data);
            if( d >= -180.0 && d <= 180.0 )
                return true;
        }
        return false;
    }
}
