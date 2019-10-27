package com.example.umborno.ui;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.util.PreferenceHelper;
import com.example.umborno.viewmodel.WeatherViewModel;
import com.example.umborno.util.LocationHelper;
import com.example.umborno.util.LocationResultHandler;
import com.example.umborno.util.PermissionHelper;
import com.example.umborno.R;
import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;
import com.example.umborno.util.Utilities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class WeatherFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener, LocationResultHandler {
    private static final String TAG = "WeatherFragment";
    NavController navController;
    private TextView longitudeValue, latitudeValue,currentWeatherText;
    public static String lon_value,lat_value,current_weather_text;
    private Button checkDetails;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    public WeatherViewModelProviderFactory factory;
    private WeatherViewModel weatherViewModel;
    private boolean isLoading;
    @Inject
    public FusedLocationProviderClient fusedLocationProviderClient;
    @Inject
    public PermissionHelper permissionHelper;

    private LocationHelper locationHelper;
    @Inject
    public SharedPreferences.Editor sharedPreferencesEditor;

    private LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(isLoading) return;
            Location location = locationResult.getLastLocation();
            Log.d(TAG, "onLocationRetrieved: "+ location.getLongitude() + " "+location.getLatitude());
            onLocationUpdated(location);
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            if(locationAvailability.isLocationAvailable()){
                Log.d(TAG, "onLocationAvailability: true");
            }else{
                Log.d(TAG, "onLocationAvailability: false");
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        checkGpsEnabled();
        weatherViewModel = ViewModelProviders.of(this,factory).get(WeatherViewModel.class);
        weatherViewModel.getCurrentWeatherLiveData().observe(this, new Observer<List<CurrentWeather>>() {
            @Override
            public void onChanged(List<CurrentWeather> currentWeathers) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(isLoading);
                if(currentWeathers!=null && !currentWeathers.isEmpty()){
                    onCurrentWeatherUpdated(currentWeathers.get(0));
                }
            }
        });

    }

    private void onCurrentWeatherUpdated(CurrentWeather currentWeather){
        current_weather_text = currentWeather.getWeatherText();
        String location_id_key =currentWeather.getKey();
        currentWeatherText.setText(current_weather_text);
        sharedPreferencesEditor.putString(PreferenceHelper.CURRENT_WEATHER_KEY,current_weather_text);
        sharedPreferencesEditor.putString(PreferenceHelper.LOCATION_ID_KEY,location_id_key);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.check_details).setOnClickListener(this);
        longitudeValue = view.findViewById(R.id.locationLog);
        latitudeValue = view.findViewById(R.id.locationLat);
        currentWeatherText = view.findViewById(R.id.current_weather_text);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if(savedInstanceState!=null){
            lon_value = savedInstanceState.getString(PreferenceHelper.LON_KEY);
            lat_value = savedInstanceState.getString(PreferenceHelper.LAT_KEY);
            current_weather_text = savedInstanceState.getString(PreferenceHelper.CURRENT_WEATHER_KEY);
        }

        longitudeValue.setText(lon_value);
        latitudeValue.setText(lat_value);
        currentWeatherText.setText(current_weather_text);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString(PreferenceHelper.LON_KEY,lon_value);
        outState.putString(PreferenceHelper.LAT_KEY,lat_value);
        outState.putString(PreferenceHelper.CURRENT_WEATHER_KEY,current_weather_text);
        super.onSaveInstanceState(outState);
    }

    private void checkGpsEnabled() {
        if(Utilities.isLocationProviderEnabled(getContext())){
            checkLocationPermission();
        }else{
            Utilities.enableLocationProvider(getContext(),getString(R.string.gps_title),getString(R.string.gps_msg));
        }
    }

    private void checkLocationPermission(){
        String permission = PermissionHelper.PERMISSION_ACCESS_FINE_LOCATION;
        if(permissionHelper.checkIfPermissionGranted(permission)){
            isLoading = true;
            startLocationUpdates();
        }else{
            String rationalMsg =getString(R.string.rational_msg);
            permissionHelper.requestPermissionFor(permission,rationalMsg,PermissionHelper.CODE_ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationUpdates(){
        if(locationHelper==null){
            locationHelper = LocationHelper.instance(this,locationCallback,fusedLocationProviderClient,this);
        }else{
            locationHelper.requestCurrentLocation();
        }
    }

    @Override
    public void onRefresh() {
        if(isLoading){
            Toast.makeText(getContext(),"Data is loading",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        Log.d(TAG, "onRefresh: ");
        checkGpsEnabled();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: "+navController.getGraph().getNavigatorName());
        navController.navigate(R.id.action_weatherFragment_to_detailFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case PermissionHelper.CODE_ACCESS_FINE_LOCATION:
                if (grantResults.length <= 0) {
                    Log.i(TAG, "onRequestPermission Cancelled");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    //todo
                    Log.d(TAG, "onRequestPermissionsResult: not granted");
                }
        }
    }

    @Override
    public void onLocationRetrieved(Location location) {
        Log.d(TAG, "onLocationRetrieved from last location: "+location.getLatitude() + " "+location.getLongitude());
        onLocationUpdated(location);
    }

    private void onLocationUpdated(Location location){
        lon_value = String.format(Locale.CHINA,"lon: %f",location.getLongitude());
        lat_value = String.format(Locale.CHINA,"lat: %f",location.getLatitude());
        longitudeValue.setText(lon_value);
        latitudeValue.setText(lat_value);
        sharedPreferencesEditor.putString(PreferenceHelper.LON_KEY,lon_value);
        sharedPreferencesEditor.putString(PreferenceHelper.LAT_KEY,lat_value);
        weatherViewModel.setLocMutableLiveData(location.getLongitude(),location.getLatitude());
    }

}
