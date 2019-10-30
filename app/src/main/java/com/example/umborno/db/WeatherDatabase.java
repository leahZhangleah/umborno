package com.example.umborno.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;

@Database(entities = {CurrentWeather.class, Reminder.class, LocationKey.class} ,version = 1,exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}
