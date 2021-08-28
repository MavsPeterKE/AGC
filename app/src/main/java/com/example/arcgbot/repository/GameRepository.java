package com.example.arcgbot.repository;

import static com.example.arcgbot.utils.Constants.CustomerTypes.*;
import static com.example.arcgbot.utils.Constants.DATE_FORMAT;
import static com.example.arcgbot.utils.Constants.PrefsKeys.IS_LOYALTY_BONUS_ENABLED;
import static com.example.arcgbot.utils.Constants.PrefsKeys.IS_LOYAL_FULL_TIME_DISCOUNT_ENABLED;
import static com.example.arcgbot.utils.Constants.PrefsKeys.IS_SPEND_AMOUNT_BONUS_ENABLED;
import static com.example.arcgbot.utils.Constants.PrefsKeys.LOYALTY_VISIT_COUNT;

import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.arcgbot.database.dao.CompleteGameDao;
import com.example.arcgbot.database.dao.CustomerDao;
import com.example.arcgbot.database.dao.CustomerVisitDao;
import com.example.arcgbot.database.dao.GameCountDao;
import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.PromotionDao;
import com.example.arcgbot.database.dao.ScreenDao;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Promotion;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.CustomerView;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.GamerModel;
import com.example.arcgbot.models.LoginModel;
import com.example.arcgbot.retrofit.RetrofitService;
import com.example.arcgbot.retrofit.responseStructures.APIListResponse;
import com.example.arcgbot.retrofit.responseStructures.GameStructure;
import com.example.arcgbot.retrofit.responseStructures.ScreenStructure;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Prefs;
import com.example.arcgbot.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    private PromotionDao promotionDao;
    private FirebaseLogs firebaseLogs;

    @Inject
    public GameRepository(RetrofitService retrofitService, ScreenDao screenDao, GameDao gameDao,
                          GameCountDao gameCountDao, CompleteGameDao completeGameDao,
                          CustomerDao customerDao, CustomerVisitDao customerVisitDao, PromotionDao promotionDao, ExecutorService executorService) {
        this.retrofitService = retrofitService;
        disposable = new CompositeDisposable();
        this.executorService = executorService;
        this.screenDao = screenDao;
        this.gameDao = gameDao;
        this.gameCountDao = gameCountDao;
        this.completeGameDao = completeGameDao;
        this.customerDao = customerDao;
        this.customerVisitDao = customerVisitDao;
        this.promotionDao = promotionDao;
        firebaseLogs = new FirebaseLogs();

    }

    public LiveData<List<CustomerView>> getCustomerList() {
        return customerDao.getAllCustomer();
    }

    public void clearGameData() {
        executorService.submit(() -> {
            screenDao.resetAllScreens();
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
                        List<Screen> screenList = new ArrayList<>();
                        for (ScreenStructure structure : screenStructures.data) {
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
                        List<Promotion> promotionList = new ArrayList<>();
                        List<GameType> gameTypeList = new ArrayList<>();
                        for (GameStructure structure : gameStructureAPIListResponse.data) {
                            GameType gameType = new GameType();
                            gameType.setId(structure.id);
                            gameType.setCharges(structure.unitPrice);
                            gameType.setGameName(structure.name);
                            gameTypeList.add(gameType);

                            Promotion promotion = new Promotion();
                            promotion.setId(gameType.getId());
                            promotion.setHappyHourDiscount(0);
                            promotion.setSpendingDiscount(0);
                            promotion.setLoyaltyDiscount(00);
                            promotionList.add(promotion);
                        }
                        setGameToDB(gameTypeList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        int x = 6;
                    }
                }));
    }

    public void observePromotionsData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER).child("gamelogs").child("configs").child("promotions");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Promotion> firebasePromotionList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    firebasePromotionList.add(snapshot.getValue(Promotion.class));
                }

                List<GameType> gameTypeList = gameDao.getAllGames();
                if (firebasePromotionList.size() > 0) {
                    insertPromotions(firebasePromotionList);
                } else {
                    List<Promotion> promotionList = new ArrayList<>();
                    for (GameType gameType : gameTypeList) {
                        Promotion promotion = new Promotion();
                        promotion.setId(gameType.getId());
                        promotion.setHappyHourDiscount(0);
                        promotion.setSpendingDiscount(0);
                        promotion.setLoyaltyDiscount(00);
                        promotionList.add(promotion);
                    }

                    new FirebaseLogs().createFirebasePromotion(promotionList);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void setGameToDB(List<GameType> gameTypeList) {
        executorService.submit(() -> {
            long[] x = gameDao.insert(gameTypeList);
            int j = 12;
        });
    }

    private void insertPromotions(List<Promotion> promotionList) {
        executorService.submit(() -> {
            long[] x = promotionDao.insert(promotionList);
        });
    }

    private void setScreensToDB(List<Screen> screenList) {
        executorService.submit(() -> {
            screenDao.insert(screenList);
        });

        startGamesSync();
    }

    public LiveData<List<GameView>> getScreensLiveData() {
        return screenDao.getAllScreens();
    }

    public LiveData<List<GameType>> getGamesLiveData() {
        return gameDao.getGameTypes();
    }


    public void updateSelectedGame(GameType gameType) {
        executorService.submit(() -> {
            gameDao.deselectAllGame();
            gameDao.updateSelected(true, gameType.getId());


        });
    }

    public void saveGameSession(GameCount gameCount, GamerModel gamerModel) {
        executorService.submit(() -> {
            saveGamersDetails(gamerModel);
            long insert = gameCountDao.insert(gameCount);
            screenDao.updateActiveScreen(gameCount.getScreenId());
            gameDao.deselectGames();
            Log.e("updateGameCount:_ ", insert + " inserted");
        });
        setCustomersData();
    }

    private void saveGamersDetails(GamerModel gamerModel) {
        Customer gamer1 = new Customer();
        gamer1.setCustomerPhone(gamerModel.player1Phone);
        gamer1.setCustomerName(gamerModel.player1Name);

        Customer gamer2 = new Customer();
        gamer2.setCustomerPhone(gamerModel.player2Phone);
        gamer2.setCustomerName(gamerModel.player2Name);

        //Save Gamers
        long insertGamer1 = customerDao.insertCustomer(gamer1);
        long insertGamer2 = customerDao.insertCustomer(gamer2);
        updateGamersVisit(gamer1, gamer2);
    }

    public void setCustomersData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER).child("gamelogs").child("all-game-customers");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CustomerView> customerViewList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    customerViewList.add(snapshot.getValue(CustomerView.class));
                }

                List<CustomerView> customerViewsInDb = customerDao.getSavedCustomers();

                if (customerViewList.size() == 0) {
                    new FirebaseLogs().setCustomerList(customerViewsInDb);
                } else {
                    if (customerViewList.size() > customerViewsInDb.size()) {
                        for (CustomerView customerView : customerViewList) {
                            customerDao.insert(customerView.gamer);
                            customerVisitDao.insert(customerView.customerVisitList);
                        }
                    } else {
                        if (customerViewList.size() != customerViewsInDb.size()) {
                            new FirebaseLogs().setCustomerList(customerViewsInDb);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateGamersVisit(Customer gamer1, Customer gamer2) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        List<CustomerVisit> customerVisits = new ArrayList<>();
        CustomerVisit customerVisit = new CustomerVisit();
        customerVisit.setCustomer_phone(gamer1.getCustomerPhone());
        customerVisit.setDate(todayDate);
        customerVisit.setMonth(monthString + "_" + year);
        customerVisit.setWeek(Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT)));
        customerVisits.add(customerVisit);

        CustomerVisit customerVisit2 = new CustomerVisit();
        customerVisit2.setCustomer_phone(gamer2.getCustomerPhone());
        customerVisit2.setDate(todayDate);
        customerVisit2.setMonth(monthString + "_" + year);
        customerVisit2.setWeek(Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT)));
        customerVisits.add(customerVisit2);
        customerVisitDao.insertGamerVisit(customerVisits);

    }

    public void updateGameCountValue(GameView gameView, int count, int bonus) {
        executorService.submit(() -> {
            GameCount gameCount = gameCountDao.getGameCountById(gameView.gameCount.getGameId());
            if (Utils.getCurrentSeconds() > Prefs.getLong(Constants.PrefsKeys.HAPPY_HOUR_TIME_MAX)) {
                double normalCharges = gameView.gameType.getCharges();
                gameCount.setGamesCount(gameCount.getGamesCount() + (count - gameView.totalGameCount));
                gameCount.setGamesBonus(bonus - gameCount.getHappyHourBonusCount());
                gameCount.setNormalGamingRateBonusAmount(gameCount.getGamesBonus() * normalCharges);
                gameCount.setNormalGamingRateAmount(normalCharges * gameCount.getGamesCount() - gameCount.getNormalGamingRateBonusAmount());

            } else {
                double happyHourCharges = gameView.gameType.getCharges() - gameView.promotion.getHappyHourDiscount();
                gameCount.setHappyHourGameCount(gameCount.getHappyHourGameCount() + (count - gameView.totalGameCount));
                gameCount.setHappyHourBonusCount(bonus - gameCount.getGamesBonus());
                gameCount.setHappyHourBonusAmount(gameCount.getHappyHourBonusCount() * happyHourCharges);
                gameCount.setHappyHourAmount(gameCount.getHappyHourGameCount() * happyHourCharges - gameCount.getHappyHourBonusAmount());
            }

            int update = gameCountDao.update(gameCount);
            int x = 1;
            Log.e("updateGameCountValue: ", update + "");
        });

    }

    public void resetActiveScreen(long id) {
        executorService.submit(() -> screenDao.resetActiveScreen(id));
    }

    public void detachGameFromScreen(GameView gameView) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        int currentWeek = Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT));
        CompletedGame completedGame = createCompletedGame(gameView);
        executorService.submit(() -> {
            updatedCustomerVisit(gameView, completedGame);
            List<String> customerPhoneList = getCustomerPhoneList(gameView);

            List<CustomerVisit> weekCustomerVisit = customerVisitDao.getWeeklyCustomerVisit(customerPhoneList,
                    currentWeek,monthString + "_" + year);

            if (Prefs.getBoolean(IS_LOYALTY_BONUS_ENABLED)){
                if (weekCustomerVisit.size()>Prefs.getInt(LOYALTY_VISIT_COUNT)){
                    if (customerDao.getLoyaltyBonusCount(customerPhoneList,currentWeek)==0){
                        completedGame.setPayableAmount(completedGame.getPayableAmount()-gameView.promotion.getLoyaltyDiscount());
                        completedGame.setBonusAmount(completedGame.getBonusAmount()+gameView.promotion.getLoyaltyDiscount());
                        customerDao.updateLoyaltyBonusAwarded(customerPhoneList, LOYAL_CUSTOMER.name(),currentWeek);
                        updateCustomerOnFireBase();
                    }
                }
            }

            if (Prefs.getBoolean(IS_SPEND_AMOUNT_BONUS_ENABLED)){
                double totalAmount = getCustomerWeekSpendAmount(weekCustomerVisit);
                if (totalAmount>=gameView.promotion.getSpendingDiscount()){
                    completedGame.setPayableAmount(completedGame.getPayableAmount()-gameView.promotion.getSpendingDiscount());
                    completedGame.setBonusAmount(completedGame.getBonusAmount()+gameView.promotion.getSpendingDiscount());
                    updateCustomerOnFireBase();
                }
            }

            int x = gameCountDao.deleteCompletedGameById(gameView.gameCount.getGameId());
            Log.e("detachGameFromScreen: ", x + "  deleted");
            long y = completeGameDao.insert(completedGame);
            Log.e("ended_game_: ", y + " " + completedGame.getScreenLable() + "Games Count__ " + completedGame.getGamesCount());
            firebaseLogs.setAllGameList(Utils.getTodayDate(DATE_FORMAT), "all-completed-Games", completeGameDao.getAllCompletedGameList());
        });

    }

    private void updateCustomerOnFireBase() {
        firebaseLogs.setCustomerList(customerDao.getSavedCustomers());
    }

    private void updatedCustomerVisit(GameView gameView, CompletedGame completedGame) {
        customerVisitDao.updateCustomerVisit(gameView.gameCount.getPlayer1Id(),
                gameView.gameCount.getPlayer2Id(), completedGame.getGamesCount(), completedGame.getPayableAmount());
    }

    @NonNull
    private List<String> getCustomerPhoneList(GameView gameView) {
        List<String> customerPhoneList = new ArrayList<>();
        customerPhoneList.add(gameView.gameCount.getPlayer1Id());
        customerPhoneList.add(gameView.gameCount.getPlayer2Id());
        return customerPhoneList;
    }

    @NonNull
    private CompletedGame createCompletedGame(GameView gameView) {
        CompletedGame completedGame = new CompletedGame();
        completedGame.setPlayer1Id(gameView.gameCount.getPlayer1Id());
        completedGame.setPlayer2Id(gameView.gameCount.getPlayer1Id());
        completedGame.setScreenLable(gameView.screen.getScreenLable());
        completedGame.setDuration(gameView.gameCount.getStartTime() + " - " + gameView.gameCount.getStopTime());
        completedGame.setGamesCount(gameView.totalGameCount);
        completedGame.setNormalGamesCount(gameView.gameCount.getGamesCount());
        completedGame.setNormalGamingRateAmount(gameView.gameCount.getNormalGamingRateAmount());
        completedGame.setNormalGamingBonus(gameView.gameCount.getGamesBonus());
        completedGame.setNormalGamesBonusAmount(gameView.gameCount.getNormalGamingRateBonusAmount());
        completedGame.setHappyHourGamesCount(gameView.gameCount.getHappyHourGameCount());
        completedGame.setHappyHourGamesBonus(gameView.gameCount.getHappyHourBonusCount());
        completedGame.setHappyHourAmount(gameView.gameCount.getHappyHourAmount());
        completedGame.setHappyHourBonusAmount(gameView.gameCount.getHappyHourBonusAmount());
        completedGame.setLoyaltyBonusAmount(gameView.promotion.getLoyaltyDiscount());
        completedGame.setStartTime(gameView.gameCount.getStartTime());
        completedGame.setGameType(gameView.gameType.getGameName());
        completedGame.setStopTime(gameView.gameCount.getStopTime());
        completedGame.setPlayerNames(gameView.gameCount.getPlayerNames());
        completedGame.setEndTimeSeconds(Utils.getSeconds(gameView.gameCount.getStopTime()));
        completedGame.setPayableAmount(gameView.payableAmount-gameView.promotion.getLoyaltyDiscount());
        completedGame.setBonusAmount((gameView.bonusAmount)+gameView.promotion.getLoyaltyDiscount());
        return completedGame;
    }

    private double getCustomerWeekSpendAmount(List<CustomerVisit> weekCustomerVisit) {
        double totalAmount=0;
        for (CustomerVisit customerVisit : weekCustomerVisit){
            totalAmount+=customerVisit.getAmountPaidToShop();
        }
        return totalAmount;
    }

    public LiveData<List<CompletedGame>> getCompletedGames() {
        return completeGameDao.getAllCompletedGames();
    }

    public LiveData<GameView> getScreenById(long screenId) {
        return screenDao.getScreenById(screenId);
    }

    public LiveData<Integer> getGameTotal() {
        return completeGameDao.getTotalGamesPlayed();
    }

    public LiveData<Double> getTotalRevenue() {
        return completeGameDao.getTotalAmountPlayed();
    }

    public LiveData<CompletedGame> getCompletedGameById(long gameId) {
        return completeGameDao.getCompletedGameById(gameId);
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

    public double getHappyHourPromotion(long id) {
        double amount = 0;
        Callable<Double> callable = () -> promotionDao.getHappyHourAmount(id);
        Future<Double> future = executorService.submit(callable);
        try {
            amount = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public LiveData<List<CustomerVisit>> getCustomerVisitThisWeek(String customerPhone, int currentWeek, String month) {
        return customerVisitDao.getCustomerVisitCurrentWeek(customerPhone,currentWeek,month);
    }

    public int getActiveScreenCount() {
        int amount = 0;
        Callable<Integer> callable = () -> screenDao.getActiveScreenCount();
        Future<Integer> future = executorService.submit(callable);
        try {
            amount = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public Customer getCustomerById(String customerPhone) {
        Customer customer = null;
        Callable<Customer> callable = () -> customerDao.getCustomerLiveDataById(customerPhone);
        Future<Customer> future = executorService.submit(callable);
        try {
            customer = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return customer;
    }
}



