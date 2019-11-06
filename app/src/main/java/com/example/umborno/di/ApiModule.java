package com.example.umborno.di;

import android.app.Application;

import com.example.umborno.http.AccuWeatherApiInterface;
import com.example.umborno.http.ApiInterface;
import com.example.umborno.http.ConnectivityInterceptor;
import com.example.umborno.http.LiveDataCallAdapterFactory;
import com.example.umborno.http.RequestInterceptor;
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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {
    //Network injection
    //public static String  BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String ACCU_WEATHER_BASE_URL = "http://dataservice.accuweather.com/";


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
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.cache(cache)
                .addInterceptor(logging)
                .addNetworkInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor);
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    Gson provideGson(){
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient){
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(ACCU_WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    /*@Provides
    @Singleton
    ApiInterface provideApiInterface(Retrofit retrofit){
        return retrofit.create(ApiInterface.class);
    }*/

    @Provides
    @Singleton
    AccuWeatherApiInterface provideAccuWeatherApiInterface(Retrofit retrofit){
        return retrofit.create(AccuWeatherApiInterface.class);

    }
}
