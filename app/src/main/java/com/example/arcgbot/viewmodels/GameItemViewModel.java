package com.example.arcgbot.viewmodels;

import static com.example.arcgbot.utils.Constants.Events.BACK_TO_GAME_COUNT;
import static com.example.arcgbot.utils.Constants.Events.GAME_STARTED;
import static com.example.arcgbot.utils.Utils.getCurrentTime;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.views.CustomerView;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.GamerModel;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Prefs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.view.adapter.GameTypeAdapterNew;
import com.example.arcgbot.view.adapter.GamerSuggestAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class GameItemViewModel extends ViewModel {
    private ObservableField<Boolean> isGameStarted = new ObservableField();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    private GameTypeAdapterNew gameTypeAdapter;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private MutableLiveData<CustomerView> selectedGamerLiveData = new MutableLiveData();
    private GameRepository gameRepository;
    List<GameType> gameTypeList = new ArrayList<>();
    private ObservableField<Integer> gameCountObservable = new ObservableField(1);
    private ObservableField<Integer> gameBonusCountObservable = new ObservableField(0);
    private ObservableField<String> gameTitleObservable = new ObservableField("Game Details");
    private ObservableField<Boolean> isCustomerListEmpty = new ObservableField(false);
    private ObservableField<Boolean> isPlayerOneEnabled = new ObservableField(false);
    private ObservableField<Boolean> isPlayerTwoEnabled = new ObservableField(false);
    private ObservableField<Boolean> isSearchListEmpty = new ObservableField(false);
    public ObservableField<Boolean> isViewHideSet = new ObservableField(false);
    private int gameCount = 0;
    public GameType selectedGameType;
    private ObservableField<GameView> selectedGamingScreen = new ObservableField();
    FirebaseLogs firebaseLogs;
    private GamerSuggestAdapter gamerSuggestAdapter;
    private List<CustomerView> customerList;


    @Inject
    public GameItemViewModel(GameRepository gameRepository) {
        firebaseLogs = new FirebaseLogs();
        this.gameRepository = gameRepository;
        gameTypeAdapter = new GameTypeAdapterNew(R.layout.game_type_item, this);
        gamerSuggestAdapter = new GamerSuggestAdapter(R.layout.gamer_suggest_item, this);

    }

    public GamerSuggestAdapter getGamerSuggestAdapter() {
        return gamerSuggestAdapter;
    }

    public int getBonusGames(int gameCount) {
        int bonus = 0;
        for (int count = 1; count <= gameCount; count++) {
            if (count % 5 == 0) {
                bonus += count == gameCount ? 0 : 1;
            }
        }
        return bonus;
    }

    public ObservableField<GameView> getSelectedGamingScreen() {
        return selectedGamingScreen;
    }

    public ObservableField<Integer> getGameCountObservable() {
        return gameCountObservable;
    }

    public ObservableField<Integer> getGameBonusCountObservable() {
        return gameBonusCountObservable;
    }

    public ObservableField<String> getGameTitleObservable() {
        return gameTitleObservable;
    }

    public ObservableField<Boolean> getIsPlayerOneEnabled() {
        return isPlayerOneEnabled;
    }

    public ObservableField<Boolean> getIsPlayerTwoEnabled() {
        return isPlayerTwoEnabled;
    }

    public void setGameTitleObservable(GameView gameView) {
        selectedGamingScreen.set(gameView);
        gameTitleObservable.set(gameView.screen.getScreenLable());
        setGameCount(gameView.totalGameCount == 0 ? 1 : gameView.totalGameCount);

    }

    private int getCurrentGameCount(GameCount gameCount) {
        int count = 1;
        if (Utils.getCurrentSeconds() > Prefs.getLong(Constants.PrefsKeys.HAPPY_HOUR_TIME_MAX)) {
            count = gameCount != null ? gameCount.getGamesCount() : 1;
        } else {
            count = gameCount != null ? gameCount.getHappyHourGameCount() : 1;
        }
        return count;
    }

    public MutableLiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }

    public GameRepository getGameRepository() {
        return gameRepository;
    }

    public ObservableField<Boolean> getIsGameStarted() {
        return isGameStarted;
    }

    public GameTypeAdapterNew getGameTypeAdapter() {
        return gameTypeAdapter;
    }

    public void startGame() {
        clickEventsLiveData.setValue(Constants.Events.START_GAME);
    }

    public void stopGame() {
        clickEventsLiveData.setValue(Constants.Events.END_GAME);
    }

    public void onBack() {
        clickEventsLiveData.setValue(BACK_TO_GAME_COUNT);
    }

    public void setGamesList(List<GameType> gamesList) {
        gameTypeList = gamesList;
        this.gameTypeAdapter.setGameTypeList(gamesList);
        this.gameTypeAdapter.notifyDataSetChanged();
    }

    public void updateGameData(GamerModel gamerModel) {
        GameCount game = createGameCount(gamerModel);
        gameRepository.saveGameSession(game, gamerModel);
        clickEventsLiveData.setValue(GAME_STARTED);

    }

    public void addGame() {
        gameCount = gameCountObservable.get();
        gameCount += 1;
        setGameCount(gameCount);
        if (selectedGamingScreen.get().screen.isActive()) {
            updateActiveGameCount();
        }
        clickEventsLiveData.setValue(Constants.Events.ADD_GAME_COUNT);

    }

    public void minusGame() {
        gameCount = gameCountObservable.get();
        if (gameCount != 1) {
            if (!selectedGamingScreen.get().screen.isActive()) {
                gameCount -= 1;
                setGameCount(gameCount);
                clickEventsLiveData.setValue(Constants.Events.MINUS_GAME_COUNT);
            } else {
                clickEventsLiveData.setValue(Constants.Events.MINUS_GAME_EVENT_ERROR);
            }

        }
    }

    private void setGameCount(int gameCount) {
        this.gameCountObservable.set(gameCount);
        this.gameBonusCountObservable.set(getBonusGames(gameCountObservable.get()));
    }

    public void onGameTypeClick(GameType gameType) {
        selectedGameType = gameType;
        gameRepository.updateSelectedGame(gameType);

    }

    public void updateActiveGameCount() {
        gameRepository.updateGameCountValue(selectedGamingScreen.get(), gameCountObservable.get(),
                gameBonusCountObservable.get());
    }

    public void endGameSession() {
        gameRepository.resetActiveScreen(selectedGamingScreen.get().screen.getId());
        selectedGamingScreen.get().gameCount.setStopTime(getCurrentTime());
        gameRepository.detachGameFromScreen(selectedGamingScreen.get());
    }

    public void setCustomerList(List<CustomerView> customerList) {
        if (customerList != null) {
            isCustomerListEmpty.set(!customerList.isEmpty());
        }
        this.customerList = customerList;
        gamerSuggestAdapter.setCustomerList(customerList);
        gamerSuggestAdapter.notifyDataSetChanged();

    }

    public GameType getSelectedGameType() {
        return selectedGameType;
    }

    @NotNull
    private GameCount createGameCount(GamerModel gamerModel) {
        double happyHourDiscount = gameRepository.getHappyHourPromotion(selectedGameType.getId());
        GameView selectedScreen = (selectedGamingScreen.get());
        GameCount game = new GameCount();
        game.setPlayer1Id(gamerModel.player1Phone);
        game.setPlayer2Id(gamerModel.player2Phone);
        game.setPlayerNames(gamerModel.player1Name + " Vs " + gamerModel.player2Name);
        game.setScreenId(selectedScreen.screen.getId());
        game.setPlayerNames(gamerModel.player1Name + " vs " + gamerModel.player2Name);
        game.setGameTypeId(selectedGameType.getId());
        game.setStartTime(getCurrentTime());

        if (Utils.getCurrentSeconds() > Prefs.getLong(Constants.PrefsKeys.HAPPY_HOUR_TIME_MAX)) {
            game.setGamesCount(gameCountObservable.get());
            game.setGamesBonus(gameBonusCountObservable.get());
            game.setNormalGamingRateBonusAmount(game.getGamesBonus() * selectedGameType.getCharges());
            game.setNormalGamingRateAmount(selectedGameType.getCharges() * game.getGamesCount() - game.getNormalGamingRateBonusAmount());

        } else {
            double happyHourCharges = selectedGameType.getCharges() - happyHourDiscount;
            game.setHappyHourGameCount(gameCountObservable.get());
            game.setHappyHourBonusCount(gameBonusCountObservable.get());
            game.setHappyHourBonusAmount(game.getHappyHourBonusCount() * happyHourCharges);
            game.setHappyHourAmount(game.getHappyHourGameCount() * happyHourCharges - game.getHappyHourBonusAmount());
        }
        return game;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updatePlayerSearchList(String searchText) {
        List<CustomerView> newList = customerList.stream().filter(customer -> customer.gamer.getCustomerName()
                .toLowerCase().contains(searchText) || customer.gamer.getCustomerPhone().toLowerCase()
                .contains(searchText))
                .collect(Collectors.toList());
        isSearchListEmpty.set(newList.isEmpty());
        gamerSuggestAdapter.setCustomerList(newList);
        gamerSuggestAdapter.notifyDataSetChanged();
    }

    public ObservableField<Boolean> getIsSearchListEmpty() {
        return isSearchListEmpty;
    }

    public void onGamerItemClick(CustomerView customer) {
        selectedGamerLiveData.setValue(customer);
    }

    public MutableLiveData<CustomerView> getSelectedGamerLiveData() {
        return selectedGamerLiveData;
    }
}
