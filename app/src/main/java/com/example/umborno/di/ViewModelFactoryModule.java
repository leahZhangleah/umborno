package com.example.umborno.di;

import androidx.lifecycle.ViewModelProvider;

import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(WeatherViewModelProviderFactory factory);
}
