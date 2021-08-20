package com.example.arcgbot.di.module;

import com.example.arcgbot.database.dao.CompleteGameDao;
import com.example.arcgbot.database.dao.CustomerDao;
import com.example.arcgbot.database.dao.CustomerVisitDao;
import com.example.arcgbot.database.dao.GameCountDao;
import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.ScreenDao;
import com.example.arcgbot.repository.GameRepository;
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
    ExecutorService provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }


    @Provides
    UserRepository provideAuthRepository(RetrofitService retrofitService) {
        return new UserRepository(retrofitService);
    }

    @Provides
    GameRepository provideGameRepository(RetrofitService retrofitService, ScreenDao screenDao,
                                         GameDao gameDao, GameCountDao gameCountDao,
                                         CompleteGameDao completeGameDao, CustomerDao customerDao,
                                         CustomerVisitDao customerVisitDao, ExecutorService executorService) {
        return new GameRepository(retrofitService,screenDao,gameDao,gameCountDao,completeGameDao,
                customerDao,customerVisitDao,executorService);
    }


}
