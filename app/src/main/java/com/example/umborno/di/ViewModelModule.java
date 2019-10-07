package com.example.umborno.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.umborno.ReminderViewModel;
import com.example.umborno.WeatherViewModel;
import com.example.umborno.WeatherViewModelProviderFactory;

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
}
