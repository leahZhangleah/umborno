package com.example.umborno.model.current_weather_model;

import androidx.room.ColumnInfo;

public class LocalSourceBean {
    private int Id;
    private String Name;
    @ColumnInfo(name = "weather_code")
    private String WeatherCode;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getWeatherCode() {
        return WeatherCode;
    }

    public void setWeatherCode(String WeatherCode) {
        this.WeatherCode = WeatherCode;
    }
}
