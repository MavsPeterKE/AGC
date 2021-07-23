package com.example.arcgbot.di.module;

import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.view.activity.HomeActivity;
import com.example.arcgbot.view.activity.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract HomeActivity bindHomeActivity();


    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract GameActivity bindGameActivity();
}
