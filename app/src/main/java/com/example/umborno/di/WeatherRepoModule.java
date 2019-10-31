package com.example.umborno.di;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.http.AppExecutors;
import com.example.umborno.http.RemoteDataSource;
import com.example.umborno.http.WeatherRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherRepoModule {
    @Provides
    WeatherRepository provideWeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, AppExecutors executor){
        return new WeatherRepository(remoteDataSource, localDataSource, executor);
    }
}
