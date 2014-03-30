/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models;

import org.codehaus.jackson.impl.Indenter;

/**
 * Перечисления
 * Created by Aleo on 13.01.14.
 */
public enum Prefix_SI {

    TERA(12, "T"),
    GIGA(9, "G"),
    MEGA(6, "M"),
    KILO(3, "k"),
    HECTO(2, "h"),
    DECA(1, "h"),
    ONE(0, ""),
    DECI(-1, "d"),
    CENTI(-2, "c"),
    MILI(-3, "m"),
    MICRO(-6, "µ"),
    NANO(-9, "n"),
    PICO(-12, "p");

    public static final Prefix_SI[] array = {
            TERA, GIGA, MEGA, KILO, HECTO, DECA, ONE,
            DECI, CENTI, MILI, MICRO, NANO, PICO
    };

    public final byte degree;
    public final String name;

    private Prefix_SI(int degree, String name) {
        this.degree = (byte) degree;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "(10^" + degree + ")";
    }

    public double pow() {
        return Math.pow(10, degree);
    }

    /**
     * Передается числовое значение префикса, и если оно существует в Prefix_SI то оно вернется
     *
     * @param value значение которое необходимо перевести в Prefix_SI
     */
    public static Prefix_SI prefixSiIfExist(int value) {
        for (Prefix_SI prefixSi : Prefix_SI.array) {
            if (prefixSi.degree == value)
                return prefixSi;
        }
        return null;
    }

    /**
     * Существует ли данный цифровой префикс как Prefix_SI
     *
     * @param value числовое значение
     * @return true если оно существует
     */
    public static boolean isExist(int value) {
        return prefixSiIfExist(value) != null;
    }

    public static Prefix_SI nearestExistValue(int value) {
        Prefix_SI prefixSi = prefixSiIfExist(value);
        if (prefixSi == null){
            int min = Integer.MAX_VALUE;
            for(Prefix_SI prefix_si : Prefix_SI.array){

                if(value > 0 && prefix_si.degree < 0) continue;
                if(value < 0 && prefix_si.degree > 0) continue;

                int diff = Math.abs(Math.abs(prefix_si.degree) - Math.abs(value));
                if(diff < min){
                    min = diff;
                    prefixSi = prefix_si;
                }
            }
        }
        return prefixSi;
    }
}
