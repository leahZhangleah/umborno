package com.example.umborno.di;

import android.content.Context;

import com.example.umborno.LocationHelper;
import com.example.umborno.MainActivity;
import com.example.umborno.PermissionHelper;
import com.example.umborno.WeatherFragment;
import com.google.android.gms.location.FusedLocationProviderClient;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    //WeatherFragmentSubcomponent is child of MainActivitySubcomponent, so we add it here

    //create a WeatherFragmentSubcomponent and use FragmentModule.class as it's corresponding module
    @ContributesAndroidInjector(modules = {FragmentModule.class,LocationModule.class})
    abstract WeatherFragment contributeWeatherFragment();

}
