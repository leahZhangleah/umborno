package com.example.umborno.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.umborno.di.RepoModule;
import com.example.umborno.di.viewmodel.ViewModelKey;
import com.example.umborno.viewmodel.ReminderViewModel;
import com.example.umborno.viewmodel.SearchLocationViewModel;
import com.example.umborno.viewmodel.WeatherViewModel;
import com.example.umborno.viewmodel.WeatherViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {RepoModule.class})
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel.class)
    abstract ViewModel bindWeatherViewModel(WeatherViewModel weatherViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(WeatherViewModelProviderFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(ReminderViewModel.class)
    abstract ViewModel bindReminderViewModel(ReminderViewModel reminderViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(SearchLocationViewModel.class)
    abstract ViewModel bindSearchLocationViewModel(SearchLocationViewModel searchLocationViewModel);
}
