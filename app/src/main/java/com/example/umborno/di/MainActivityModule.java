package com.example.umborno.di;

import com.example.umborno.LocationHelper;
import com.example.umborno.MainActivity;
import com.example.umborno.PermissionHelper;
import com.example.umborno.WeatherFragment;
import com.google.android.gms.location.FusedLocationProviderClient;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    //WeatherFragmentSubcomponent is child of MainActivitySubcomponent, so we add it here

    //create a WeatherFragmentSubcomponent and use FragmentModule.class as it's corresponding module
    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract WeatherFragment contributeWeatherFragment();

    @Provides
    static PermissionHelper providePermissionHelper(MainActivity activity){
        return new PermissionHelper(activity);
    }

    @Provides
    static FusedLocationProviderClient providerClient(MainActivity activity){
        return new FusedLocationProviderClient(activity);
    }

    @Binds
    abstract LocationHelper.LocationRetrievedCallback provideLocationCallback(MainActivity activity);

}
