/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.db.models;

import javax.persistence.*;

/**
 * User: Aleo skype: aleo72
 * Date: 24.11.13
 * Time: 21:05
 */
@Entity
@Table(name = "PROGRAM_SETTINGS")
public class Settings {

    public Settings(){

    }

    public Settings(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "KEY", nullable = false, length = 5000, unique = true)
    private String key;

    @Column(name = "VALUE", nullable = true)
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!obj.getClass().getName().equals(Settings.class.getName()))
            return false;

        Settings other = (Settings) obj;

        return !(other.key == null || this.key == null) && this.key.equals(other.key);
    }

    @Override
    public String toString() {
        return this.key + " = " + this.value;
    }
}
