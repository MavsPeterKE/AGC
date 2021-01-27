package com.example.arcgbot.di.module;

import android.app.Application;

import androidx.room.Room;

import com.example.arcgbot.database.AppDataBase;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.arcgbot.utils.Constants.RoomConfigs.DATABASE_NAME;

@Module
public class RoomModule {

    @Singleton
    @Provides
    AppDataBase provideAppDataBase(Application application) {
        return Room.databaseBuilder(application, AppDataBase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

  /*  @Singleton
    @Provides
    ContainerDao provideContainerDao(AppDataBase appDataBase) {
        return appDataBase.containerDao();
    }*/


}
