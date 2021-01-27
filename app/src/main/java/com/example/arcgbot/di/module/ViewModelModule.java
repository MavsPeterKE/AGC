package com.example.arcgbot.di.module;

import androidx.lifecycle.ViewModel;

import com.example.arcgbot.di.util.ViewModelKey;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.example.arcgbot.viewmodels.GameCountViewModel;
import com.example.arcgbot.viewmodels.LoginViewModel;
import com.example.arcgbot.viewmodels.ScreensViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameCountViewModel.class)
    abstract ViewModel bindGameCountViewModel(GameCountViewModel gameCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ScreensViewModel.class)
    abstract ViewModel bindScreensViewModel(ScreensViewModel screensViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EODViewModel.class)
    abstract ViewModel bindEODViewModel(EODViewModel screensViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);
}
