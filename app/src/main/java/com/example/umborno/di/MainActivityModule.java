package com.example.umborno.di;

import com.example.umborno.ui.AddReminderFragment;
import com.example.umborno.ui.ReminderFragment;
import com.example.umborno.ui.SearchFragment;
import com.example.umborno.ui.WeatherFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    //WeatherFragmentSubcomponent is child of MainActivitySubcomponent, so we add it here

    //create a WeatherFragmentSubcomponent and use RepoModule.class as it's corresponding module
    @ContributesAndroidInjector()
    abstract WeatherFragment contributeWeatherFragment();

    @ContributesAndroidInjector()
    abstract ReminderFragment contributeReminderFragment();

    @ContributesAndroidInjector()
    abstract AddReminderFragment bindAddReminderFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();


}
