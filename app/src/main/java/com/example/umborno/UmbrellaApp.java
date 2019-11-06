package com.example.umborno;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.work.Configuration;
import androidx.work.WorkManager;
import androidx.work.WorkerFactory;

import com.example.umborno.di.AppComponent;
import com.example.umborno.di.DaggerAppComponent;
import com.example.umborno.schedule.SimpleWorkerFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class UmbrellaApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    public SimpleWorkerFactory factory;


    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();

    }

    private void initDagger(){
        AppComponent appComponent =DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        //set custom factory for workmanager
        Configuration configuration = new Configuration.Builder()
                .setWorkerFactory(factory)
                .build();
        WorkManager.initialize(this,configuration);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
