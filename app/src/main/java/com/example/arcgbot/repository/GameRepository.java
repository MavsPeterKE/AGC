package com.example.arcgbot.repository;

import android.text.format.DateFormat;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arcgbot.database.dao.CompleteGameDao;
import com.example.arcgbot.database.dao.CustomerDao;
import com.example.arcgbot.database.dao.CustomerVisitDao;
import com.example.arcgbot.database.dao.GameCountDao;
import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.ScreenDao;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.models.GamerModel;
import com.example.arcgbot.models.LoginModel;
import com.example.arcgbot.retrofit.RetrofitService;
import com.example.arcgbot.retrofit.responseStructures.APIListResponse;
import com.example.arcgbot.retrofit.responseStructures.GameStructure;
import com.example.arcgbot.retrofit.responseStructures.LoginStructure;
import com.example.arcgbot.retrofit.responseStructures.ScreenStructure;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import static com.example.arcgbot.utils.Constants.DATE_FORMAT;

@Singleton
public class GameRepository {
    private PublishSubject<LoginModel> loginSubject;
    private CompositeDisposable disposable;
    private RetrofitService retrofitService;
    private ExecutorService executorService;
    private ScreenDao screenDao;
    private GameDao gameDao;
    private GameCountDao gameCountDao;
    private CustomerDao customerDao;
    private CustomerVisitDao customerVisitDao;


    private CompleteGameDao completeGameDao;
    private FirebaseLogs firebaseLogs;

    @Inject
    public GameRepository(RetrofitService retrofitService, ScreenDao screenDao, GameDao gameDao,
                          GameCountDao gameCountDao, CompleteGameDao completeGameDao,
                          CustomerDao customerDao,CustomerVisitDao customerVisitDao,ExecutorService executorService) {
        this.retrofitService = retrofitService;
        disposable = new CompositeDisposable();
        this.executorService = executorService;
        this.screenDao = screenDao;
        this.gameDao = gameDao;
        this.gameCountDao = gameCountDao;
        this.completeGameDao = completeGameDao;
        this.customerDao = customerDao;
        this.customerVisitDao = customerVisitDao;
        firebaseLogs = new FirebaseLogs();

    }

    public LiveData<List<Customer>> getCustomerList(){
        return customerDao.getAllCustomer();
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


    public void updateSelectedGame(GameType gameType) {
        executorService.submit(() -> {
            gameDao.deselectAllGame();
            gameDao.updateSelected(true,gameType.getId());


        });
    }

    public void resetSelected(){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                gameDao.resetSelected();
            }
        });
    }

    public void saveGameSession(GameCount gameCount, GamerModel gamerModel) {
        if (gameCount.getGamesCount() == 0){
            gameCount.setGamesCount(1);
        }
        executorService.submit(() -> {
           Customer gamer1 = new Customer();
           gamer1.setCustomerPhone(gamerModel.player1Phone);
           gamer1.setCustomerName(gamerModel.player1Name);

            Customer gamer2 = new Customer();
            gamer2.setCustomerPhone(gamerModel.player2Phone);
            gamer2.setCustomerName(gamerModel.player2Name);

            long insertGamer1 = customerDao.insertCustomer(gamer1);
            long insertGamer2 = customerDao.insertCustomer(gamer2);
            updateGamersVisit(gamer1,gamer2);

            long insert = gameCountDao.insert(gameCount);
            screenDao.updateActiveScreen(gameCount.getScreenId());
            gameDao.deselectGames();
            Log.e("updateGameCount:_ ",insert +" inserted" );
        });

    }

    private void updateGamersVisit(Customer gamer1, Customer gamer2) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
        List<CustomerVisit> customerVisits =new ArrayList<>();
        CustomerVisit customerVisit = new CustomerVisit();
        customerVisit.setCustomer_phone(gamer1.getCustomerPhone());
        customerVisit.setDate(todayDate);
        customerVisit.setMonth(monthString+"_"+year);
        customerVisit.setWeek(Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT)));
        customerVisits.add(customerVisit);

        CustomerVisit customerVisit2 = new CustomerVisit();
        customerVisit2.setCustomer_phone(gamer2.getCustomerPhone());
        customerVisit2.setDate(todayDate);
        customerVisit2.setMonth(monthString+"_"+year);
        customerVisit2.setWeek(Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT)));
        customerVisits.add(customerVisit2);
        customerVisitDao.insertGamerVisit(customerVisits);

    }

    public void updateGameCountValue(long gameId,int count,int bonus){
        executorService.submit(() -> {
            int update = gameCountDao.updateGameCount(gameId, count,bonus);
            int x = 1;
            Log.e("updateGameCountValue: ", update+"");
        });

    }

    public void resetActiveScreen(long id) {
        executorService.submit(() -> screenDao.resetActiveScreen(id));
    }

    public void detachGameFromScreen(GameView gameView) {
        CompletedGame completedGame = new CompletedGame();
        completedGame.setDuration(gameView.gameCount.getStartTime()+" - "+gameView.gameCount.getStopTime());
        completedGame.setGamesCount(gameView.gameCount.getGamesCount());
        completedGame.setScreenLable(gameView.screen.getScreenLable() + " - "+ gameView.gameCount.getPlayerNames());
        completedGame.setEndTimeSeconds(Utils.getSeconds(gameView.gameCount.getStopTime()));
        completedGame.setPayableAmount(gameView.payableAmount);
        completedGame.setBonusAmount(gameView.bonusAmount);
        executorService.submit(() -> {
           customerVisitDao.updateCustomerVisit(gameView.gameCount.getPlayer1Id(),
                    gameView.gameCount.getPlayer2Id(),completedGame.getGamesCount(),completedGame.getPayableAmount());
            int x = gameCountDao.updateCompletedGames(gameView.gameCount.getGameId());
            Log.e("detachGameFromScreen: ",x +"  deleted" );
            long y = completeGameDao.insert(completedGame);
            Log.e("ended_game_: ",y +" " +completedGame.getScreenLable() + "Games Count__ "+ completedGame.getGamesCount());
            firebaseLogs.setAllGameList(Utils.getTodayDate(DATE_FORMAT),"all-completed-Games",completeGameDao.getAllCompletedGameList());
        });

    }

    public LiveData<List<CompletedGame>> getCompletedGames(){
        return completeGameDao.getAllCompletedGames();
    }

    public LiveData<GameView> getScreenById(long screenId){
        return screenDao.getScreenById(screenId);
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

    public GameView getGameViewById(long gameId) {
        GameView gameView = null;
        Callable<GameView> gameViewCallable = () -> screenDao.getGameViewById(gameId);
        Future<GameView> future = executorService.submit(gameViewCallable);
        try {
            gameView = future.get();
        } catch (ExecutionException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gameView;
    }
}



