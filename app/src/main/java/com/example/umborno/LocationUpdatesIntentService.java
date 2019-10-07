package com.example.umborno;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class LocationUpdatesIntentService extends IntentService {
    private static final String TAG = "LocationIntentService";


    public FusedLocationProviderClient fusedLocationProviderClient;

    public LocationUpdatesIntentService() {
        super("LocationUpdatesIntentService");
        AndroidInjection.inject(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @SuppressLint("MissingPermission")
    public void getLocation(){
        Log.d(TAG, "getLocation: ");
        if(fusedLocationProviderClient!=null){
            //fusedLocationProviderClient.requestLocationUpdates(createLocationSetting(),locationCallback,null);

        }else{
            Log.d(TAG, "getLocation: fused client is null");
        }

    }


}
