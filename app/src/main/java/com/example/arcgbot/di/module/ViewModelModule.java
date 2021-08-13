package com.example.arcgbot.di.module;

import androidx.lifecycle.ViewModel;

import com.example.arcgbot.di.util.ViewModelKey;
import com.example.arcgbot.viewmodels.CustomerViewModel;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.example.arcgbot.viewmodels.GameCountViewModel;
import com.example.arcgbot.viewmodels.GameItemViewModel;
import com.example.arcgbot.viewmodels.LoginViewModel;
import com.example.arcgbot.viewmodels.ScreensViewModel;
import com.example.arcgbot.viewmodels.SearchItemViewModel;

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

    @Binds
    @IntoMap
    @ViewModelKey(SearchItemViewModel.class)
    abstract ViewModel bindSearchItemViewModel(SearchItemViewModel searchItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GameItemViewModel.class)
    abstract ViewModel bindGameItemViewModel(GameItemViewModel gameItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CustomerViewModel.class)
    abstract ViewModel bindCustomerViewModel(CustomerViewModel customerViewModel);
}
