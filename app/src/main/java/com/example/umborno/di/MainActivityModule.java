package com.example.umborno.di;

import com.example.umborno.di.viewmodel.ReminderViewModelModule;
import com.example.umborno.di.viewmodel.WeatherViewModelModule;
import com.example.umborno.ui.AddReminderFragment;
import com.example.umborno.ui.AlertFragment;
import com.example.umborno.ui.ReminderFragment;
import com.example.umborno.ui.RepeatFragment;
import com.example.umborno.ui.SearchFragment;
import com.example.umborno.ui.WeatherFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    //WeatherFragmentSubcomponent is child of MainActivitySubcomponent, so we add it here

    //create a WeatherFragmentSubcomponent and use RepoModule.class as it's corresponding module
    @ContributesAndroidInjector(modules = {LocationModule.class, WeatherViewModelModule.class})
    abstract WeatherFragment contributeWeatherFragment();

    @ContributesAndroidInjector(modules = {ReminderModule.class,ReminderViewModelModule.class})
    abstract ReminderFragment contributeReminderFragment();

    @ContributesAndroidInjector(modules = {ReminderModule.class,ReminderViewModelModule.class})
    abstract AddReminderFragment contributeAddReminderFragment();

    @ContributesAndroidInjector(modules = {ReminderModule.class,ReminderViewModelModule.class})
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector(modules = {ReminderModule.class,ReminderViewModelModule.class})
    abstract RepeatFragment contributeRepeatFragment();

    @ContributesAndroidInjector(modules = {ReminderModule.class, ReminderViewModelModule.class})
    abstract AlertFragment contributeAlertFragment();


}
