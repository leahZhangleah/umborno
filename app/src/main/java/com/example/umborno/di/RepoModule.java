package com.example.umborno.di;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.http.AppExecutors;
import com.example.umborno.http.ReminderRepository;
import com.example.umborno.http.RemoteDataSource;
import com.example.umborno.http.SearchRepository;
import com.example.umborno.http.WeatherRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {
    @Provides
    @PerMainActivity
    WeatherRepository provideWeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, AppExecutors executor){
        return new WeatherRepository(remoteDataSource, localDataSource, executor);
    }

    @Provides
    @PerMainActivity
    ReminderRepository provideReminderRepository(LocalDataSource localDataSource,AppExecutors executors){
        return new ReminderRepository(localDataSource,executors);
    }


    @Provides
    @PerMainActivity
    SearchRepository provideSearchRepository(RemoteDataSource remoteDataSource){
        return new SearchRepository(remoteDataSource);
    }


}
