package com.example.umborno;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class PermissionHelper {
    private static final String TAG = "PermissionHelper";
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final String PACKAGE_NAME = "umborno";
    private Activity context;

    private static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE
    };

    interface LocationPermissionGrant {
        void onPermissionGranted();
        void onPermissionNotGranted();
    }

    public PermissionHelper(Activity context) {
        this.context = context;
    }

    //before checking this, make sure all required permissions have been declared in manifest
    public void requestPermissionFor(String permission, String rationalMsg, int requestCode){
        Log.d(TAG, "requestPermissionFor: ");
        boolean rationale = ActivityCompat.shouldShowRequestPermissionRationale(context,permission);
        String[] permissions = {permission};
        if(rationale){
            showRationaleDialog(rationalMsg,permissions,requestCode);
        }else{
            ActivityCompat.requestPermissions(context,permissions, requestCode);
        }
    }

    public boolean checkIfPermissionGranted(String permission){
        Log.d(TAG, "checkIfPermissionGranted: ");
        int permissionState = ActivityCompat.checkSelfPermission(context,permission);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    //explanation dialog
    private void showRationaleDialog(String message, final String[] permissions, final int requestCode){
        Log.d(TAG, "showRationaleDialog: ");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(context,permissions, requestCode);
                    }
                });
        dialogBuilder.show();
    }

    //deal with permission request result callback
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults, LocationPermissionGrant locationPermissionGrant){
        Log.d(TAG, "onRequestPermissionResult");
        switch (requestCode){
            case CODE_ACCESS_FINE_LOCATION:
                if(grantResults.length<=0){
                    Log.i(TAG, "onRequestPermission Cancelled");
                } else if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGrant.onPermissionGranted();
                }else{
                   //todo
                    locationPermissionGrant.onPermissionNotGranted();
                }
        }
    }



}





















