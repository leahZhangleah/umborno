package com.example.umborno.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;

import java.util.List;

import io.reactivex.Maybe;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {
    @Insert(onConflict = REPLACE)
    public void save(CurrentWeather currentWeather);

    @Query("SELECT * FROM currentweather WHERE `key`=:locationKey")
    public Maybe<List<CurrentWeather>> getCurrentWeather(String locationKey);

    @Insert(onConflict = REPLACE)
    public void save(Reminder reminder);

    @Query("select * from reminder")
    public List<Reminder> getReminders();

    /*@Update
    public void update(Reminder reminder);

    @Delete
    public void delete(Reminder reminder);

    @Delete
    public void deleteAllReminders();
*/
    //todo
    @Query("select * from locationkey")
    public Maybe<LocationKey> getLocationKey();

    @Insert(onConflict = REPLACE)
    public void save(LocationKey locationKey);
}
