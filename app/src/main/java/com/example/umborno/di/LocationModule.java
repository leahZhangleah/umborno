package com.example.umborno.di;

import android.content.Context;

import com.example.umborno.ui.MainActivity;
import com.example.umborno.util.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {

    @Provides
    @PerActivity
    static PermissionHelper providePermissionHelper(MainActivity activity){
        return new PermissionHelper(activity);
    }

    @Provides
    @PerActivity
    static FusedLocationProviderClient providerClient(MainActivity context){
        return LocationServices.getFusedLocationProviderClient(context);
    }

}
