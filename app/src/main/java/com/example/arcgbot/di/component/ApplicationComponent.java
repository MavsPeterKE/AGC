package com.example.arcgbot.di.component;

import android.app.Application;

import com.example.arcgbot.di.module.ActivityBindingModule;
import com.example.arcgbot.di.module.ApplicationModule;
import com.example.arcgbot.di.module.ContextModule;
import com.example.arcgbot.di.module.FragmentBindingModule;
import com.example.arcgbot.di.module.RetrofitModule;
import com.example.arcgbot.di.module.RoomModule;
import com.example.arcgbot.di.module.ViewModelModule;
import com.example.arcgbot.utils.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

@Singleton
@Component(modules = {ContextModule.class, ApplicationModule.class, FragmentBindingModule.class,
        AndroidSupportInjectionModule.class, ActivityBindingModule.class, ViewModelModule.class,
        RetrofitModule.class, RoomModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(BaseApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}