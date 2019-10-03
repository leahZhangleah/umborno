package com.example.umborno.http;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.umborno.db.LocalDataSource;
import com.example.umborno.db.WeatherDao;
import com.example.umborno.model.Coord;
import com.example.umborno.model.CurrentWeather;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public LiveData<Resource<CurrentWeather>> requestCurrentWeather(final float lon, final float lat){

        return new NetworkBoundResource<CurrentWeather,CurrentWeather>(executor){
            @Override
            protected void onFetchFailed() {
                //todo
            }

            @NonNull
            @Override
            protected LiveData<CurrentWeather> loadFromDB() {
                return localDataSource.getCurrentWeather();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CurrentWeather>> createCall() {
                return remoteDataSource.getCurrentWeather(lat,lon);
            }

            @Override
            protected void saveCallResult(@NonNull CurrentWeather item) {
                localDataSource.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable CurrentWeather currentWeather) {
                if(currentWeather!=null){
                    Log.d(TAG, currentWeather.getSys().getCountry());
                    return localDataSource.shouldFetch(currentWeather); //if data in db is timed out
                }
                return true;
            }
        }.asLiveData();

    }

}























