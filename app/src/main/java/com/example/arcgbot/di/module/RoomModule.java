package com.example.arcgbot.di.module;

import android.app.Application;

import androidx.room.Room;

import com.example.arcgbot.database.AppDataBase;
import com.example.arcgbot.database.dao.CompleteGameDao;
import com.example.arcgbot.database.dao.GameCountDao;
import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.ScreenDao;

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

    @Singleton
    @Provides
    ScreenDao provideScreenDao(AppDataBase appDataBase) {
        return appDataBase.screenDao();
    }

    @Singleton
    @Provides
    GameDao provideGameTypeDao(AppDataBase appDataBase) {
        return appDataBase.gameTypeDao();
    }


    @Singleton
    @Provides
    GameCountDao provideGameCountDao(AppDataBase appDataBase) {
        return appDataBase.gameCountDao();
    }

    @Singleton
    @Provides
    CompleteGameDao provideCompleteGameDao(AppDataBase appDataBase) {
        return appDataBase.completeGameDao();
    }


}
