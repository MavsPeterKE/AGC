package com.example.arcgbot.di.module;


import com.example.arcgbot.view.fragment.FragmentCustomers;
import com.example.arcgbot.view.fragment.FragmentEOD;
import com.example.arcgbot.view.fragment.FragmentGameCount;
import com.example.arcgbot.view.fragment.FragmentGameItem;
import com.example.arcgbot.view.fragment.FragmentScreens;
import com.example.arcgbot.view.fragment.FragmentSearch;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract FragmentGameCount bindGameCountFragment();

    @ContributesAndroidInjector
    abstract FragmentScreens bindFragmentScreens();

    @ContributesAndroidInjector
    abstract FragmentEOD bindFragmentEOD();

    @ContributesAndroidInjector
    abstract FragmentSearch bindFragmentSearch();

    @ContributesAndroidInjector
    abstract FragmentGameItem bindFragmentGameItem();

    @ContributesAndroidInjector
    abstract FragmentCustomers bindFragmentCustomers();

}
