package com.example.umborno.model.current_weather_model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CurrentWeather {

    @NonNull
    @PrimaryKey
    private String key;
    @Ignore
    private String LocalObservationDateTime;
    @ColumnInfo(name = "epoch_time")
    private int EpochTime;
    @ColumnInfo(name = "weather_text")
    private String WeatherText;
    @ColumnInfo(name = "weather_icon")
    private int WeatherIcon;
    @ColumnInfo(name = "has_precipitation")
    private boolean HasPrecipitation;
    @ColumnInfo(name = "precipitation_type")
    private String PrecipitationType;
    @Embedded
    private LocalSourceBean LocalSource;
    @ColumnInfo(name = "is_daytime")
    private boolean IsDayTime;
    @Embedded
    private TemperatureBean Temperature;
    @Ignore
    private String MobileLink;
    @Ignore
    private String Link;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocalObservationDateTime() {
        return LocalObservationDateTime;
    }

    public void setLocalObservationDateTime(String LocalObservationDateTime) {
        this.LocalObservationDateTime = LocalObservationDateTime;
    }

    public int getEpochTime() {
        return EpochTime;
    }

    public void setEpochTime(int EpochTime) {
        this.EpochTime = EpochTime;
    }

    public String getWeatherText() {
        return WeatherText;
    }

    public void setWeatherText(String WeatherText) {
        this.WeatherText = WeatherText;
    }

    public int getWeatherIcon() {
        return WeatherIcon;
    }

    public void setWeatherIcon(int WeatherIcon) {
        this.WeatherIcon = WeatherIcon;
    }

    public boolean isHasPrecipitation() {
        return HasPrecipitation;
    }

    public void setHasPrecipitation(boolean HasPrecipitation) {
        this.HasPrecipitation = HasPrecipitation;
    }

    public String getPrecipitationType() {
        return PrecipitationType;
    }

    public void setPrecipitationType(String PrecipitationType) {
        this.PrecipitationType = PrecipitationType;
    }

    public LocalSourceBean getLocalSource() {
        return LocalSource;
    }

    public void setLocalSource(LocalSourceBean LocalSource) {
        this.LocalSource = LocalSource;
    }

    public boolean isIsDayTime() {
        return IsDayTime;
    }

    public void setIsDayTime(boolean IsDayTime) {
        this.IsDayTime = IsDayTime;
    }

    public TemperatureBean getTemperature() {
        return Temperature;
    }

    public void setTemperature(TemperatureBean Temperature) {
        this.Temperature = Temperature;
    }

    public String getMobileLink() {
        return MobileLink;
    }

    public void setMobileLink(String MobileLink) {
        this.MobileLink = MobileLink;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }
}
