package com.example.arcgbot.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arcgbot.database.dao.CompleteGameDao;
import com.example.arcgbot.database.dao.GameCountDao;
import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.ScreenDao;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.models.LoginModel;
import com.example.arcgbot.retrofit.RetrofitService;
import com.example.arcgbot.retrofit.responseStructures.APIListResponse;
import com.example.arcgbot.retrofit.responseStructures.GameStructure;
import com.example.arcgbot.retrofit.responseStructures.LoginStructure;
import com.example.arcgbot.retrofit.responseStructures.ScreenStructure;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
    private GameCountDao gameCountDao;
    private CompleteGameDao completeGameDao;

    @Inject
    public GameRepository(RetrofitService retrofitService, ScreenDao screenDao, GameDao gameDao,
                          GameCountDao gameCountDao, CompleteGameDao completeGameDao,ExecutorService executorService) {
        this.retrofitService = retrofitService;
        disposable = new CompositeDisposable();
        this.executorService = executorService;
        this.screenDao = screenDao;
        this.gameDao = gameDao;
        this.gameCountDao = gameCountDao;
        this.completeGameDao = completeGameDao;

    }

    public void clearGameData(){
        executorService.submit(() -> {
            gameCountDao.clearData();
            completeGameDao.clearData();
        });
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
            long []x = gameDao.insert(gameTypeList);
            int j = 12;
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

    public LiveData<List<GameView>> getScreensLiveData(){
        return screenDao.getAllScreens();
    }

    public LiveData<List<GameType>> getGamesLiveData(){
        return gameDao.getGameTypes();
    }

    public LiveData<GameView> getGamesLiveDataById(long gameId){
        return screenDao.getGameViewById(gameId);
    }

    public void updateSelectedGame(GameType gameType) {
        executorService.submit(() -> {
            gameDao.deselectAllGame();
            gameDao.updateSelected(true,gameType.getId());


        });
    }

    public void updateGameCount(GameCount gameCount) {
        if (gameCount.getGamesCount() == 0){
            gameCount.setGamesCount(1);
        }
        executorService.submit(() -> {
            long insert = gameCountDao.insert(gameCount);
            screenDao.updateActiveScreen(gameCount.getScreenId());
            gameDao.deselectGames();
            Log.e("updateGameCount:_ ",insert +" inserted" );
        });

    }

    public void updateGameCountValue(long gameId,int count,int bonus){
        executorService.submit(() -> {
            int update = gameCountDao.updateGameCount(gameId, count,bonus);
            Log.e("updateGameCountValue: ", update+"");
        });

    }

    public void resetActiveScreen(long id) {
        executorService.submit(() -> screenDao.resetActiveScreen(id));
    }

    public void detachGameFromScreen(GameModel gameModel) {
        CompletedGame completedGame = new CompletedGame();
        completedGame.setDuration(gameModel.startTime+" - "+gameModel.endTime);
        completedGame.setGamesCount(Integer.parseInt(gameModel.GameCount));
        completedGame.setScreenLable(gameModel.screenLable + " - "+ gameModel.players);
        completedGame.setEndTimeSeconds(Utils.getSeconds(gameModel.endTime));
        completedGame.setPayableAmount(Double.parseDouble(gameModel.payableAmount));
        completedGame.setBonusAmount(Double.parseDouble(gameModel.bonusAmount));
        executorService.submit(() -> {
            int x = gameCountDao.updateCompletedGames(gameModel.gameId);
            Log.e("detachGameFromScreen: ",x +"  deleted" );
            long y = completeGameDao.insert(completedGame);
            Log.e("insert__: ",x +" GCount " +completedGame.getGamesCount());
        });

    }

    public LiveData<List<CompletedGame>> getCompletedGames(){
        return completeGameDao.getAllCompletedGames();
    }

    public LiveData<Integer> getGameTotal() {
       return completeGameDao.getTotalGamesPlayed();
    }

    public LiveData<Double> getTotalRevenue() {
      return completeGameDao.getTotalAmountPlayed();
    }

    public GameCount getSelectedGameScreen(long gameId) {
        Callable<GameCount> callable = () -> gameCountDao.getGameById(gameId);
        GameCount gameCount = null;
        Future<GameCount> future = executorService.submit(callable);
        try {
            gameCount = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gameCount;
    }
}



