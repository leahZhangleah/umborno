package com.example.umborno.schedule;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class SimpleWorkerFactory extends WorkerFactory {

    private final Map<Class<? extends ListenableWorker>, Provider<CurrentWeatherWorkerFactory>> workersFactories;

    @Inject
    public SimpleWorkerFactory(Map<Class<? extends ListenableWorker>, Provider<CurrentWeatherWorkerFactory>> workersFactories){
        this.workersFactories = workersFactories;
    }
    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext, @NonNull String workerClassName, @NonNull WorkerParameters workerParameters) {
        Provider<CurrentWeatherWorkerFactory> factoryProvider = null;
        for(Map.Entry<Class<? extends ListenableWorker>,Provider<CurrentWeatherWorkerFactory>> entry: workersFactories.entrySet()){
            try {
                if(Class.forName(workerClassName).isAssignableFrom(entry.getKey())){
                    factoryProvider = entry.getValue();
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("unknown worker class " + workerClassName);
            }
        }
        return factoryProvider.get().create(appContext,workerParameters);
    }
}






















