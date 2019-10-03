package com.example.umborno.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.umborno.model.Coord;
import com.example.umborno.model.CurrentWeather;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {
    @Insert(onConflict = REPLACE)
    public void save(CurrentWeather currentWeather);

    @Query("select * from currentweather")
    public LiveData<CurrentWeather> getCurrentWeather();

}
