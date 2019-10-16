package com.example.umborno.viewmodel;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.umborno.Loc;
import com.example.umborno.http.Resource;
import com.example.umborno.http.WeatherRepository;
import com.example.umborno.model.current_weather_model.CurrentWeather;
import com.example.umborno.model.location_key_model.LocationKey;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherViewModel extends ViewModel {
    private static final String TAG = "WeatherViewModel";
    //SavedStateHandle savedStateHandle;
    private LiveData<List<CurrentWeather>> currentWeatherLiveData;
    private MutableLiveData<Loc> locMutableLiveData;
    //private WeatherRepository weatherRepository;

    @Inject
    public WeatherViewModel(final WeatherRepository weatherRepository) {
        //this.weatherRepository = weatherRepository;
        locMutableLiveData = new MutableLiveData<>();

        currentWeatherLiveData = Transformations.switchMap(locMutableLiveData, new Function<Loc, LiveData<List<CurrentWeather>>>() {
            @Override
            public LiveData<List<CurrentWeather>> apply(Loc location) {
                Maybe<List<CurrentWeather>> weatherMaybe =
                        weatherRepository.requestLocationKey(location.getLongitude(), location.getLatitude())
                        .subscribeOn(Schedulers.io())
                        .concatMap(new io.reactivex.functions.Function<LocationKey, MaybeSource<List<CurrentWeather>>>() {
                            @Override
                            public MaybeSource<List<CurrentWeather>> apply(LocationKey locationKey) throws Exception {
                                String key =locationKey.getKey();
                                Log.d(TAG, "location key: "+key);
                                return weatherRepository.requestCurrentWeather(key)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        });
                return LiveDataReactiveStreams.fromPublisher(weatherMaybe.toFlowable());
            }
        });

    }


    public LiveData<List<CurrentWeather>> getCurrentWeatherLiveData() {
        return currentWeatherLiveData;
    }

    public void setLocMutableLiveData(final double lon, final double lat){
        float longitude = (float) lon;
        float latitude = (float) lat;
        Log.d(TAG, "setLocMutableLiveData: lon "+longitude+" lat: "+latitude);
        Loc loc = new Loc(longitude,latitude);
        locMutableLiveData.postValue(loc);
    }
}
