package com.example.umborno.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.umborno.LocationService;
import com.example.umborno.R;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private LocationService locationService;
    private boolean shouldUnbind = false;

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        setupNavigation();

        //bind to location service
        //bindLocationService();

    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

   @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.nav_host_fragment),drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        int id = menuItem.getItemId();
        switch (id){
            case R.id.main:
                navController.navigate(R.id.weatherFragment);
                break;
            case R.id.reminder:
                navController.navigate(R.id.reminderFragment);
                break;
            case R.id.more_cities:
                navController.navigate(R.id.addCityFragment);
                break;
            case R.id.settings:
                navController.navigate(R.id.settingsFragment);
                break;
        }
        return false;
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
    /*class MyLocationRunnable implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "location runnable");
            locationHelper.requestCurrentLocation();
        }
    }*/

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


}


























