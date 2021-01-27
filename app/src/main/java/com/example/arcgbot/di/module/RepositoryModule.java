package com.example.arcgbot.di.module;

import com.example.arcgbot.repository.UserRepository;
import com.example.arcgbot.retrofit.RetrofitService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RetrofitModule.class, RoomModule.class})
public class RepositoryModule {

    @Singleton
    @Provides
    ExecutorService providesExecutorService() {
        return Executors.newSingleThreadExecutor();
    }


    @Provides
    UserRepository provideAppAuthRepository(RetrofitService retrofitService) {
        return new UserRepository(retrofitService);
    }


}
