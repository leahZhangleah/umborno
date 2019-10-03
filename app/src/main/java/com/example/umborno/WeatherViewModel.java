package com.example.umborno;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.umborno.http.Resource;
import com.example.umborno.http.WeatherRepository;
import com.example.umborno.model.CurrentWeather;

import javax.inject.Inject;

public class WeatherViewModel extends ViewModel {
    private static final String TAG = "WeatherViewModel";
    //SavedStateHandle savedStateHandle;
    private LiveData<Resource<CurrentWeather>> currentWeatherLiveData;
    private MutableLiveData<Loc> locMutableLiveData;
    //private WeatherRepository weatherRepository;

    @Inject
    public WeatherViewModel(final WeatherRepository weatherRepository) {
        //this.weatherRepository = weatherRepository;
        locMutableLiveData = new MutableLiveData<>();
        currentWeatherLiveData = Transformations.switchMap(locMutableLiveData, new Function<Loc, LiveData<Resource<CurrentWeather>>>() {
            @Override
            public LiveData<Resource<CurrentWeather>> apply(Loc location) {
                return weatherRepository.requestCurrentWeather(location.longitude, location.latitude);
            }
        });
    }


    public LiveData<Resource<CurrentWeather>> getCurrentWeather() {
        return this.currentWeatherLiveData;
    }


    public void setLocMutableLiveData(final double lon, final double lat){

        float longitude = (float) lon;
        float latitude = (float) lat;
        Log.d(TAG, "setLocMutableLiveData: lon "+longitude+" lat: "+latitude);
        Loc loc = new Loc(longitude,latitude);
        locMutableLiveData.postValue(loc);
    }
}
