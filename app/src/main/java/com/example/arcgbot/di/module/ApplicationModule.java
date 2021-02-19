package com.example.arcgbot.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module(includes = {ViewModelModule.class, RetrofitModule.class,RepositoryModule.class})
public class ApplicationModule {

    @Singleton
    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

}
