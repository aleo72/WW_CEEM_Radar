/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar;

import ua.edu.odeku.ceem.mapRadar.settings.Property;

import java.util.Locale;

/**
 * Вход программы
 *
 * Created by Aleo on 01.02.14.
 */
public class CeemRadar {

    public static void main(String[] args){

        Locale.setDefault(Property.CURRENT_LOCALE_LANGUAGE().toLocale());

        CeemRadarApplication.main(args);
    }
}
