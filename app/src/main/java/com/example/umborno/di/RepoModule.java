package com.example.umborno.di;

import android.app.Activity;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.http.AppExecutors;
import com.example.umborno.http.ReminderRepository;
import com.example.umborno.http.RemoteDataSource;
import com.example.umborno.http.SearchRepository;
import com.example.umborno.http.WeatherRepository;
import com.example.umborno.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module()
public class RepoModule {
    @Provides
    @PerActivity
    WeatherRepository provideWeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, AppExecutors executor){
        return new WeatherRepository(remoteDataSource, localDataSource, executor);
    }

    @Provides
    @PerActivity
    ReminderRepository provideReminderRepository(LocalDataSource localDataSource,AppExecutors executors){
        return new ReminderRepository(localDataSource,executors);
    }


    @Provides
    @PerActivity
    SearchRepository provideSearchRepository(RemoteDataSource remoteDataSource){
        return new SearchRepository(remoteDataSource);
    }


}
