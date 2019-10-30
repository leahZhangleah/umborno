package com.example.umborno.db;

import android.util.Log;

import com.example.umborno.model.reminder_model.Reminder;
import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class LocalDataSource {
    private static final String TAG = "LocalDataSource";
    private final WeatherDao weatherDao;
    public static final long EXPIRATION_INTERVAL = TimeUnit.HOURS.toMillis(2); // 2 HOURS

    @Inject
    public LocalDataSource(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public void save(CurrentWeather currentWeather){
        weatherDao.save(currentWeather);
    }

    public boolean shouldFetch(CurrentWeather currentWeather){
        long fetchedTime = currentWeather.getEpochTime(); // granuality to tens of milliseconds
        long currentTime = System.currentTimeMillis() / 1000L;
        long time_difference = currentTime - fetchedTime;
        Log.d(TAG, "current time "+currentTime);
        if(time_difference >=EXPIRATION_INTERVAL){
            Log.d(TAG, "shouldFetch: true. the time difference is "+time_difference);
            return true;
        }else{
            Log.d(TAG, "shouldFetch: false. the time difference is "+time_difference);
            return false;
        }

    }

    public Maybe<List<CurrentWeather>> getCurrentWeather(String key){
        return weatherDao.getCurrentWeather(key);
    }


    public List<Reminder> getReminders(){
        return weatherDao.getReminders();
    }

    public void addReminder(Reminder reminder){
        weatherDao.save(reminder);
    }

    public void save(LocationKey locationKey){
        weatherDao.save(locationKey);
    }

    public Maybe<LocationKey> getLocationKey(){
        return weatherDao.getLocationKey();
    }

    /*public void updateReminder(Reminder reminder){
        weatherDao.update(reminder);
    }

    public void deleteReminder(Reminder reminder){
        weatherDao.delete(reminder);
    }

    public void deleteAllReminders(){
        weatherDao.deleteAllReminders();
    }*/

}
