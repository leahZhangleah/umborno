package com.example.umborno.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.umborno.model.CurrentWeather;
import com.example.umborno.model.Reminder;

@Database(entities = {CurrentWeather.class, Reminder.class} ,version = 1,exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}
