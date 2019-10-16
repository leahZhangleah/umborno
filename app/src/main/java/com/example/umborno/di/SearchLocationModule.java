package com.example.umborno.di;

import com.example.umborno.ui.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchLocationModule {
    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
