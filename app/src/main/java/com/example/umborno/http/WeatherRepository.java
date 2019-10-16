package com.example.umborno.http;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class WeatherRepository {
    private static final String TAG = "WeatherRepository";
    private ApiInterface apiInterface;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private AppExecutors executor;

    @Inject
    public WeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, AppExecutors executor) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.executor = executor;
    }


    public Maybe<LocationKey> requestLocationKey(float lon, float lat){
        Log.d(TAG, "requestLocationKey: ");
       return new LocationBoundResource<LocationKey>(){

           @Override
           protected void onFetchFailed(Throwable throwable) {

           }

           @NonNull
           @Override
           protected Maybe<LocationKey> loadFromDB() {
               return localDataSource.getLocationKey();
           }

           @NonNull
           @Override
           protected Maybe<LocationKey> createCall() {
               return remoteDataSource.getLocationKey(lat,lon);
           }

           @Override
           protected void saveCallResult(@NonNull LocationKey item) {
               localDataSource.save(item);
           }

           @Override
           protected boolean shouldFetch(@Nullable LocationKey resultType) {
               return false;
           }
       }.getResult();
    }

    public Maybe<List<CurrentWeather>> requestCurrentWeather(String key){
        Log.d(TAG, "requestCurrentWeather: ");
        return new LocationBoundResource<List<CurrentWeather>>(){

            @Override
            protected void onFetchFailed(Throwable throwable) {

            }

            @NonNull
            @Override
            protected Maybe<List<CurrentWeather>> loadFromDB() {
                return localDataSource.getCurrentWeather(key);
            }

            @NonNull
            @Override
            protected Maybe<List<CurrentWeather>> createCall() {
                return remoteDataSource.getCurrentWeather(key);
            }

            @Override
            protected void saveCallResult(@NonNull List<CurrentWeather> item) {
                for(CurrentWeather currentWeather:item){
                    currentWeather.setKey(key);
                    localDataSource.save(currentWeather);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CurrentWeather> currentWeathers) {
                if(currentWeathers!=null && !currentWeathers.isEmpty()){
                    CurrentWeather currentWeather = currentWeathers.get(0);
                    if(currentWeather!=null){
                        //.d(TAG, "shouldFetch: db data  lon:" + currentWeather.getCoord().getLon()+ " lat: "+currentWeather.getCoord().getLat());
                        //Log.d(TAG, "shouldFetch: new lat lon: "+lat + ": "+lon);
                        Log.d(TAG, currentWeather.getEpochTime()+"");
                        return localDataSource.shouldFetch(currentWeather); //if data in db is timed out
                    }
                }
                return true;
            }
        }.getResult();

    }

}























