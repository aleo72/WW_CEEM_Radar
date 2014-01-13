/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models;

/**
 * Перечисления
 * Created by Aleo on 13.01.14.
 */
public enum Prefix_SI {

    TERA    (12,    "T"),
    GIGA    (9,     "G"),
    MEGA    (6,     "M"),
    KILO    (3,     "k"),
    HECTO   (2,     "h"),
    DECA    (1,     "h"),
    ONE     (0,     ""),
    DECI    (-1,    "d"),
    CENTI   (-2,    "c"),
    MILI    (-3,    "m"),
    MICRO   (-6,    "µ"),
    NANO    (-9,    "n"),
    PICO    (-12,   "p");

    public static final Prefix_SI[] array = {
            TERA, GIGA, MEGA, KILO, HECTO, DECA, ONE,
            DECI, CENTI, MILI, MICRO, NANO, PICO
    };

    public final byte degree;
    public final String name;

    private Prefix_SI(int degree, String name){
        this.degree = (byte) degree;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "(10^"+degree+")";
    }


}
