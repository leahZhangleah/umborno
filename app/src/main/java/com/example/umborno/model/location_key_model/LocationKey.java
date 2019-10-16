package com.example.umborno.model.location_key_model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class LocationKey {
    @NonNull
    @PrimaryKey
    private String Key;
    @ColumnInfo(name = "english_name")
    private String EnglishName;
    @Embedded
    private GeoPositionBean GeoPosition;



    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }


    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String EnglishName) {
        this.EnglishName = EnglishName;
    }


    public GeoPositionBean getGeoPosition() {
        return GeoPosition;
    }

    public void setGeoPosition(GeoPositionBean GeoPosition) {
        this.GeoPosition = GeoPosition;
    }


}
