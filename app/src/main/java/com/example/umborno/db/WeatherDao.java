package com.example.umborno.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.umborno.model.Coord;
import com.example.umborno.model.CurrentWeather;
import com.example.umborno.model.Reminder;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {
    @Insert(onConflict = REPLACE)
    public void save(CurrentWeather currentWeather);

    @Query("select * from currentweather")
    public LiveData<CurrentWeather> getCurrentWeather();

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
}
