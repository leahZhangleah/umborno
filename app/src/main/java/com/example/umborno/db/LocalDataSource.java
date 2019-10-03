package com.example.umborno.db;

import androidx.lifecycle.LiveData;

import com.example.umborno.model.CurrentWeather;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class LocalDataSource {

    private final WeatherDao weatherDao;
    public static final long EXPIRATION_INTERVAL = TimeUnit.MINUTES.toMillis(30); // 30 minutes

    @Inject
    public LocalDataSource(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public void save(CurrentWeather currentWeather){
        weatherDao.save(currentWeather);
    }

    public boolean shouldFetch(CurrentWeather currentWeather){
        long fetchedTime = Long.valueOf(currentWeather.getDt());
        long currentTime = System.currentTimeMillis();
        return (currentTime - fetchedTime) >= EXPIRATION_INTERVAL;
    }

    public LiveData<CurrentWeather> getCurrentWeather(){
        return weatherDao.getCurrentWeather();
    }
}
