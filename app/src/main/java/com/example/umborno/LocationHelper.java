package com.example.umborno;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

public class LocationHelper implements PermissionHelper.LocationPermissionGrant {
    private static final String TAG = "LocationHelper";
    public static final int CODE_LOCATION_SETTINGS = 10;
    private String provider;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PermissionHelper permissionHelper;
    private LocationRetrievedCallback callback;

    @Inject
    public LocationHelper(FusedLocationProviderClient client,PermissionHelper helper,LocationRetrievedCallback callback) {
        Log.d(TAG, "LocationHelper: ");
        this.fusedLocationProviderClient = client;
        this.permissionHelper = helper;
        this.callback = callback;

    }
    

    public void requestCurrentLocation(){
        Log.d(TAG, "requestCurrentLocation: ");
        String permission = PermissionHelper.PERMISSION_ACCESS_FINE_LOCATION;
        if(permissionHelper.checkIfPermissionGranted(permission)){
            getLocation();
        }else{
            String rationalMsg ="UmbOrNo needs to use location service to determine your location";
            permissionHelper.requestPermissionFor(permission,rationalMsg,PermissionHelper.CODE_ACCESS_FINE_LOCATION);
        }
    }


    private Intent getIntent(){
        Intent intent = new Intent();
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return intent;
    }


    @Override
    public void onPermissionGranted() {
        getLocation();
    }

    @Override
    public void onPermissionNotGranted() {

        //todo
    }

    public interface LocationRetrievedCallback{
        void onLocationRetrieved(double lon,double lat);
    }


    @SuppressLint("MissingPermission")
    private void getLocation(){
        Log.d(TAG, "getLocation: ");
        fusedLocationProviderClient.getLastLocation()
            .addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful() && task.getResult()!=null){
                        Location location = task.getResult();
                        //Intent intent = getIntent();
                        //intent.putExtra(LocationUpdatesBroadcastReceiver.LON_KEY,location.getLongitude());
                        //intent.putExtra(LocationUpdatesBroadcastReceiver.LAT_KEY,location.getLatitude());
                        //context.sendBroadcast(intent);
                        callback.onLocationRetrieved(location.getLongitude(),location.getLatitude());
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

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

   }*/


}























