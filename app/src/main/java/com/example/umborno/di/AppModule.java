package com.example.umborno.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.umborno.MainActivity;
import com.example.umborno.db.LocalDataSource;
import com.example.umborno.db.WeatherDao;
import com.example.umborno.db.WeatherDatabase;
import com.example.umborno.http.ApiInterface;
import com.example.umborno.http.AppExecutors;
import com.example.umborno.http.ConnectivityInterceptor;
import com.example.umborno.http.LiveDataCallAdapterFactory;
import com.example.umborno.http.RemoteDataSource;
import com.example.umborno.http.RequestInterceptor;
import com.example.umborno.http.WeatherRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {ViewModelModule.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application){
        return application;
    }

    @Provides
    @Singleton
    static FusedLocationProviderClient providerClient(Context context){
        return LocationServices.getFusedLocationProviderClient(context);
    }
    //Database injection
    @Provides
    @Singleton
    WeatherDatabase provideDatabase(Application application){
        return Room.databaseBuilder(application,WeatherDatabase.class,"weather_db").build();
    }

    @Provides
    @Singleton
    WeatherDao provideWeatherDao(WeatherDatabase weatherDatabase){
        return weatherDatabase.getWeatherDao();
    }

    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, AppExecutors executor){
        return new WeatherRepository(remoteDataSource, localDataSource, executor);
    }

    //Network injection
    public static String  BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "a4bee604b2ebdeba98d1f9ff77faf245";

    @Provides
    @Singleton
    Cache provideCache(Application application){
        long cacheSize = 10 * 1024 * 1024; // 10MB
        File httpCacheDirectory = new File(application.getCacheDir(),"http_cache");
        return new Cache(httpCacheDirectory,cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, ConnectivityInterceptor connectivityInterceptor, RequestInterceptor requestInterceptor){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.cache(cache)
                .addInterceptor(logging)
                .addNetworkInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor);
        return httpClient.build();
    }

    @Provides
    @Singleton
    Gson provideGson(){
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson,OkHttpClient okHttpClient){
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ApiInterface provideApiInterface(Retrofit retrofit){
        return retrofit.create(ApiInterface.class);
    }

}






















