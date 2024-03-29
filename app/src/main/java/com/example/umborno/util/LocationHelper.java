package com.example.umborno.util;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

public class LocationHelper implements LifecycleObserver {
    private static final String TAG = "LocationHelper";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;

    private static LocationHelper mInstance;
    private LocationCallback locationCallback;
    private LocationResultHandler resultHandler;
    private boolean isRequestingLocationUpdates;
    private Location currentLocation;

    private LocationHelper(LifecycleOwner lifecycleOwner,LocationCallback locationCallback,
                           FusedLocationProviderClient fusedLocationProviderClient,LocationResultHandler resultHandler) {
        lifecycleOwner.getLifecycle().addObserver(this);
        this.locationCallback = locationCallback;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.resultHandler = resultHandler;
    }

    public static LocationHelper instance(LifecycleOwner lifecycleOwner,LocationCallback locationCallback,
                                          FusedLocationProviderClient fusedLocationProviderClient,LocationResultHandler resultHandler){
        if(mInstance==null){
            Log.d(TAG, "instance: is null");
            return new LocationHelper(lifecycleOwner,locationCallback,fusedLocationProviderClient,resultHandler);
        }
        Log.d(TAG, "instance: is not null");
        return mInstance;
    }

   private LocationRequest createLocationSetting(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(TimeUnit.HOURS.toMillis(1));
        locationRequest.setFastestInterval(TimeUnit.MINUTES.toMillis(30));
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void requestCurrentLocation(){
        Log.d(TAG, "requestCurrentLocation: ");
        getLocation();
    }

    @SuppressLint("MissingPermission")
     public void getLocation(){
        Log.d(TAG, "getLocation: ");
        if(fusedLocationProviderClient!=null){
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if(task.isSuccessful() && task.getResult()!=null){
                                currentLocation = task.getResult();
                                //Intent intent = getIntent();
                                //intent.putExtra(LocationUpdatesBroadcastReceiver.LON_KEY,location.getLongitude());
                                //intent.putExtra(LocationUpdatesBroadcastReceiver.LAT_KEY,location.getLatitude());
                                //context.sendBroadcast(intent);
                                resultHandler.onLocationRetrieved(currentLocation);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }else{
            Log.d(TAG, "getLocation: fused client is null");
        }


    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void requestLocationUpdates(){
        if(!isRequestingLocationUpdates&&fusedLocationProviderClient!=null){
            Log.d(TAG, "onComplete: request location updates");
            fusedLocationProviderClient.requestLocationUpdates(createLocationSetting(),locationCallback,null);
            isRequestingLocationUpdates = true;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void stopLocationUpdates(){
        if(fusedLocationProviderClient!=null && isRequestingLocationUpdates){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            isRequestingLocationUpdates = false;
        }
    }



    /*
    todo: steps in using fusedLocationProvider
    1.check location permission
        2. if permission is granted, we can request for last known location
    OR  2. if we want regular location updates,


            OnCreate
            1. retrieve values from Bundle if any including shouldRequestUpdate (boolean value) FLAG, and maybe last received location etc.
            2. create
                1. locationCallback(called after requestLocationUpdates to get updated location data) variable
                2. and LocationRequest, LocationSettingsRequest(for setting up before requesting location updates) variable
            OnResume
            1. checkPermission
                1. if yes, start location updates. Call settingsClient.checklocationSetings(pass in LocationSettingsRequest variable), return a task
                2. add onSuccessListener(fusedLocationClient.requestLocationUpdates with  locationCallback variable) and onFailureListener on task to handle different situation
            OnPause
            1.remove locationUpdates correspondingly
            */


    //an alternative for android location api
   /* @SuppressLint("MissingPermission")
    private void getLastLocationFromFused(){
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(context, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            Location location = task.getResult();

                        }else{
                            Log.w(TAG, "getLastlocation: exception", task.getException());
                            //todo show snackbar msg
                        }
                    }
                });
    }

   protected void createLocationSetting(){
       final LocationRequest locationRequest = LocationRequest.create();
       locationRequest.setInterval(10000); //preferred location request interval
       locationRequest.setFastestInterval(5000); //fastest location request interval, can't be faster than this
       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

       LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
       SettingsClient client = LocationServices.getSettingsClient(context);
       Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

       final LocationCallback locationCallback = new LocationCallback();
       task.addOnSuccessListener((Executor) this, new OnSuccessListener<LocationSettingsResponse>() {
           @SuppressLint("MissingPermission")
           @Override
           public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
               // All location settings are satisfied. The client can initialize
               // location requests here.
               // ..
               fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper());
           }
       }).addOnFailureListener((Executor) this, new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               if(e instanceof ResolvableApiException){
                   // Location settings are not satisfied, but this can be fixed
                   // by showing the user a dialog.
                   try{
                       // Show the dialog by calling startResolutionForResult(),
                       // and check the result in onActivityResult().
                       ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                       //todo this method will invoke onActivityResult, we can pass a listener into this method and handle it in mainactivity
                       resolvableApiException.startResolutionForResult(context,CODE_LOCATION_SETTINGS);
                   } catch (IntentSender.SendIntentException e1) {
                       e1.printStackTrace();
                   }
               }
           }
       });

   }

    private Intent getIntent(){
        Intent intent = new Intent();
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return intent;
    }
   */


}























