package com.example.umborno.di.worker;

import com.example.umborno.schedule.CurrentWeatherWorker;
import com.example.umborno.schedule.CurrentWeatherWorkerFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(CurrentWeatherWorker.class)
    CurrentWeatherWorkerFactory bindWorkerFactory(CurrentWeatherWorker.Factory factory);
}
