package com.example.umborno.di;

import android.app.Application;

import androidx.room.Room;

import com.example.umborno.db.WeatherDao;
import com.example.umborno.db.WeatherDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    //Database injection
    @Provides
    @Singleton
    WeatherDatabase provideDatabase(Application application){
        return Room.databaseBuilder(application,WeatherDatabase.class,"weather_db").build();
    }

    @Provides
    @Singleton
    WeatherDao provideWeatherDao(WeatherDatabase weatherDatabase){
        return weatherDatabase.getWeatherDao();
    }

}
