package com.example.umborno.di;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.http.AppExecutors;
import com.example.umborno.http.ReminderRepository;
import com.example.umborno.http.RemoteDataSource;
import com.example.umborno.http.SearchRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderRepoModule {
    @Provides
    ReminderRepository provideReminderRepository(LocalDataSource localDataSource, AppExecutors executors){
        return new ReminderRepository(localDataSource,executors);
    }


    @Provides
    SearchRepository provideSearchRepository(RemoteDataSource remoteDataSource){
        return new SearchRepository(remoteDataSource);
    }
}
