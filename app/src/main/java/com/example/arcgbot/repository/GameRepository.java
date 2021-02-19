package com.example.arcgbot.repository;

import androidx.lifecycle.LiveData;

import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.ScreenDao;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.models.LoginModel;
import com.example.arcgbot.retrofit.RetrofitService;
import com.example.arcgbot.retrofit.responseStructures.APIListResponse;
import com.example.arcgbot.retrofit.responseStructures.GameStructure;
import com.example.arcgbot.retrofit.responseStructures.LoginStructure;
import com.example.arcgbot.retrofit.responseStructures.ScreenStructure;
import com.example.arcgbot.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class GameRepository {
    private PublishSubject<LoginModel> loginSubject;
    private CompositeDisposable disposable;
    private RetrofitService retrofitService;
    private ExecutorService executorService;
    private ScreenDao screenDao;
    private GameDao gameDao;

    @Inject
    public GameRepository(RetrofitService retrofitService, ScreenDao screenDao, GameDao gameDao,ExecutorService executorService) {
        this.retrofitService = retrofitService;
        disposable = new CompositeDisposable();
        this.executorService = executorService;
        this.screenDao = screenDao;
        this.gameDao = gameDao;

    }

    /**
     * Start Sync Screens from server
     */
    public void startScreensApiRequest() {
        loginSubject = PublishSubject.create();
        disposable.add(retrofitService.getScreens()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<APIListResponse<ScreenStructure>>() {
                    @Override
                    public void onSuccess(APIListResponse<ScreenStructure> screenStructures) {
                        int count = screenStructures.data.size();
                        List<Screen> screenList = new ArrayList<>();
                        for (ScreenStructure  structure : screenStructures.data){
                            Screen screen = new Screen();
                            screen.setId(structure.id);
                            screen.setScreenLable(structure.label);
                            screen.setActive(false);
                            screenList.add(screen);
                        }
                        setScreensToDB(screenList);
                    }

                    @Override
                    public void onError(Throwable e) {
                       int x = 6;
                    }
                }));
    }

    public void startGamesSync() {
        disposable.add(retrofitService.getGames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<APIListResponse<GameStructure>>() {
                    @Override
                    public void onSuccess(APIListResponse<GameStructure> gameStructureAPIListResponse) {
                        List<GameType> gameTypeList = new ArrayList<>();
                        for (GameStructure  structure : gameStructureAPIListResponse.data){
                            GameType gameType = new GameType();
                            gameType.setId(structure.id);
                            gameType.setCharges(structure.unitPrice);
                            gameType.setGameName(structure.name);
                            gameTypeList.add(gameType);
                        }
                        setGameToDB(gameTypeList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        int x = 6;
                    }
                }));
    }

    private void setGameToDB(List<GameType> gameTypeList) {
        executorService.submit(() -> {
            gameDao.insert(gameTypeList);
        });
    }

    private void setScreensToDB(List<Screen> screenList) {
        executorService.submit(() -> {
            screenDao.insert(screenList);
        });

        startGamesSync();
    }

    @NotNull
    private LoginModel getLoginResponseModel(LoginStructure userStructureAPIResponse) {
        String errorMsg = userStructureAPIResponse.error!=null?userStructureAPIResponse.error: Constants.SUCCESS;
        LoginModel loginResponseModel = new LoginModel();
        loginResponseModel.message = errorMsg;
        loginResponseModel.loginStructure = userStructureAPIResponse;
        return loginResponseModel;
    }

    public PublishSubject<LoginModel> getLoginSubject() {
        return loginSubject;
    }

    public LiveData<List<Screen>> getScreensLiveData(){
        return screenDao.getAllScreens();
    }

    public LiveData<List<GameType>> getGamesLiveData(){
        return gameDao.getGameTypes();
    }
}



