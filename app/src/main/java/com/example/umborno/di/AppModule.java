package com.example.umborno.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

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

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {DbModule.class,ApiModule.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application){
        return application;
    }


}






















