package com.example.umborno.di;

import android.content.Context;

import com.example.umborno.MainActivity;
import com.example.umborno.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {

    @Provides
    static PermissionHelper providePermissionHelper(MainActivity activity){
        return new PermissionHelper(activity);
    }

}
