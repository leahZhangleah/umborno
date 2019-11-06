package com.example.umborno.di.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.umborno.di.WeatherRepoModule;
import com.example.umborno.viewmodel.WeatherViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {WeatherRepoModule.class,ViewModelFactoryModule.class})
public abstract class WeatherViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel.class)
    abstract ViewModel bindWeatherViewModel(WeatherViewModel weatherViewModel);
}
