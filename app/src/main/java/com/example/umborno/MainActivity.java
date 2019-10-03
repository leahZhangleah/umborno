package com.example.umborno;

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.umborno.http.Resource;
import com.example.umborno.http.Status;
import com.example.umborno.model.CurrentWeather;
import com.example.umborno.schedule.MyJobService;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        LocationHelper.LocationRetrievedCallback, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private TextView longitudeValue, latitudeValue;
    private Button pauseOrResume;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationService locationService;
    private boolean shouldUnbind = false;
    @Inject
    WeatherViewModelProviderFactory factory;
    private WeatherViewModel weatherViewModel;
    private boolean isLoading = true;

    @Inject
    public LocationHelper locationHelper;
    @Inject
    public PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        longitudeValue = findViewById(R.id.locationLog);
        latitudeValue = findViewById(R.id.locationLat);
        pauseOrResume = findViewById(R.id.stopOrStart);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        weatherViewModel = ViewModelProviders.of(this,factory).get(WeatherViewModel.class);
        weatherViewModel.getCurrentWeather().observe(this, new Observer<Resource<CurrentWeather>>() {
            @Override
            public void onChanged(Resource<CurrentWeather> currentWeatherResource) {
                if(currentWeatherResource.getStatus()!= Status.LOADING){
                    swipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }
                Log.d(TAG, "resource status: "+currentWeatherResource.getStatus());
                Log.d(TAG, "resource msg: "+currentWeatherResource.getMsg());
                Log.d(TAG, "weather name: "+currentWeatherResource.getData());
            }
        });
        new Thread(new MyLocationRunnable()).start();

        //bind to location service
        //bindLocationService();

    }

    @Override
    public void onLocationRetrieved(double lon, double lat) {
        Log.d(TAG, "onLocationRetrieved: "+ lon + " "+lat);
        weatherViewModel.setLocMutableLiveData(lon,lat);
    }

    @Override
    public void onRefresh() {
        if(isLoading) return;
        new Thread(new MyLocationRunnable()).start();
    }

    /*
    schedule a job
     */
    /*
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void scheduleJob(View v){
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo info = new JobInfo.Builder(100,componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(10*60*1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "scheduled Job: ");
        }else{
            Log.d(TAG, "failed Job schedule: ");
        }
    }
    
    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(100);
        Log.d(TAG, "cancelJob: ");
    }*/

    //To create a custom runnable as static instead of having runnable directly inside thread is to avoid
    //having reference from runnable to our activity, so when our activity is destroyed or has configuration
    //change, but our runnable is still running, we can then avoid memory leak, since now there's no reference
    class MyLocationRunnable implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "location runnable");
            locationHelper.requestCurrentLocation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    /*------
    For local service
    ----------*/

    /*
    intent service is more suitable here cuz we only want one time running when user opens the app, we don't want it to run after user leaves the activity,
    but we may need it if we enable create reminder function in app, which means we want to fetch new data even if user is not using it
    */
    /*private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationService = ((LocationService.LocationBinder) service).getService();
            //todo we can directly use methods or data from location service
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
        }
    };

    private void bindLocationService(){
        Intent intent = new Intent(this,LocationService.class);
        boolean bound =bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
        if(!bound){
            Log.e(TAG, "bindLocationService: the requested service does not esixt or is not allowed to be bound");
        }else{
            shouldUnbind = true;
        }

        //todo start service and stop service in corresponding lifecycle
    }

    private void unbindLocationService(){
        if(shouldUnbind){
            unbindService(mConnection);
            shouldUnbind=false;
        }
    }*/


    /*------
    For remote service
    ----------*/
    /*
    Messenger mService = null;

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LocationService.MSG_SET_VALUE:
                    Log.d(TAG, "Recieve value from location service: "+msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Log.d(TAG, "onServiceConnected: ");
            try{
                //register our messenger with the service's messenger list to let it know where to return value to
                Message msg = Message.obtain(null,LocationService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

                //send some initial value
                msg = Message.obtain(null,LocationService.MSG_SET_VALUE,this.hashCode(),0);
                mService.send(msg);
            }catch (RemoteException e){

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService=null;
        }
    };

    private void bindLocationService(){
        Intent intent = new Intent(this,LocationService.class);
        boolean bound =bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
        if(!bound){
            Log.e(TAG, "bindLocationService: the requested service does not exist or is not allowed to be bound");
        }else{
            shouldUnbind = true;
        }

        //todo start service and stop service in corresponding lifecycle
    }

    private void unbindLocationService(){
        if(shouldUnbind){
            if(mService!=null){
                try{
                    Message msg = Message.obtain(null,LocationService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }catch (RemoteException e){

                }
            }
            unbindService(mConnection);
            shouldUnbind=false;
        }
    }*/




    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        //unbindLocationService();
    }

    //callback for PermissionHelper when we try to request permissions from user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode){
            case PermissionHelper.CODE_ACCESS_FINE_LOCATION:
                permissionHelper.onRequestPermissionResult(requestCode,permissions,grantResults,locationHelper);
        }


    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}


























