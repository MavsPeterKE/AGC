package com.example.arcgbot.viewmodels;

import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.view.adapter.GameCountAdapter;
import com.example.arcgbot.view.adapter.GameTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.example.arcgbot.utils.Constants.DATE_FORMAT;
import static com.example.arcgbot.utils.Utils.getTodayDate;

public class GameCountViewModel extends ViewModel {
    private GameCountAdapter gameCountAdapter;
    private GameTypeAdapter gameTypeAdapter;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private ObservableField<String> gameCountObservable = new ObservableField();
    private ObservableField<String> gameCountBonusObservable = new ObservableField();
    private ObservableField<Boolean> isGameStarted = new ObservableField();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    public ObservableField<String> observableButtonText = new ObservableField("Start Game");
    private GameModel selectedGameScreen ;
    private int gameCount;
    private GameRepository gameRepository;
    List<GameType> gameTypeList = new ArrayList<>();
    public MutableLiveData<String> text = new MutableLiveData<>();
    public GameType selectedGameType;
    FirebaseLogs firebaseLogs;
    private String todayDate;
    String player_phone;
    String players;

    @Inject
    public GameCountViewModel(GameRepository gameRepository) {
        gameCountAdapter = new GameCountAdapter(R.layout.game_item, this);
        gameTypeAdapter = new GameTypeAdapter(R.layout.game_type_item, this);
        this.gameRepository = gameRepository;
        firebaseLogs = new FirebaseLogs();
        todayDate = getTodayDate(DATE_FORMAT);

    }

    public GameCountAdapter getGameCountAdapter() {
        return gameCountAdapter;
    }

    public GameTypeAdapter getGameTypeAdapter() {
        return gameTypeAdapter;
    }

    public void setGameCountAdapter(List<GameView> screens) {
        List<GameModel> gameModels = new ArrayList<>();
        for (GameView gameView : screens) {
            GameCount gameCount = gameView.gameCount;
            GameModel gameModel = new GameModel();
            gameModel.screenId = (gameView.screen != null ? gameView.screen.getId() : 0);
            gameModel.screenLable = gameView.screen.getScreenLable();
            gameModel.GameCount = String.valueOf(gameCount != null ? gameCount.getGamesCount() : 0);
            gameModel.payableAmount = String.valueOf(gameView.payableAmount);
            gameModel.bonusAmount = String.valueOf(gameView.bonusAmount);
            gameModel.GameName = gameView.gameType != null ? gameView.gameType.getGameName() : "No Active Game";
            gameModel.players = gameCount != null ? gameCount.getPlayerNames() : "Idle";
            gameModel.playerPhone = gameCount != null ? gameCount.getPlayerPhone() : "";
            gameModel.startTime = gameCount != null ? gameCount.getStartTime() : "";
            gameModel.isScreenActive = gameView.screen.isActive();
            gameModel.currentTime = getCurrentTime();
            gameModel.gameId = gameCount != null ? gameCount.getGameId() : 0;
            gameModel.hashKey = ("#" + getCurrentTime() + "#" + gameModel.GameName + gameModel);
            gameModels.add(gameModel);

        }
        isGamesAvailable.set(!screens.isEmpty());
        this.gameCountAdapter.setGameCountList(gameModels);
        this.gameCountAdapter.notifyDataSetChanged();
        if (screens!=null){
            if (!screens.isEmpty()){
                firebaseLogs.setGameLogList("all-active-games",screens);
            }
        }

    }

    public void setGamesList(List<GameType> gamesList) {
        gameTypeList = gamesList;
        this.gameTypeAdapter.setGameTypeList(gamesList);
        this.gameCountAdapter.notifyDataSetChanged();
    }

    public GameRepository gameRepository() {
        return gameRepository;
    }

    public void onGameItemClick(GameModel gameModel,int pos) {
        text.setValue(gameModel.screenLable+"");
        gameCount = Integer.parseInt(gameModel.GameCount);
        gameCountObservable.set(gameModel.GameCount);
        gameCountBonusObservable.set(String.valueOf(getBonusGames(gameModel.GameCount)));
        clickEventsLiveData.setValue(Constants.Events.GAME_ITEM_CLICK);
        selectedGameScreen = gameModel;
        setHideGameInputs();
    }

    public void onGameTypeClick(GameType gameType) {
        selectedGameType = gameType;
        gameRepository.updateSelectedGame(gameType);

    }

    public GameModel getSelectedScreen() {
        return selectedGameScreen;
    }

    public void closeSheet() {
        clickEventsLiveData.setValue(Constants.Events.BOTTOM_SHEET);
    }

    public void addGame() {
        gameCount++;
        gameCountObservable.set(String.valueOf(gameCount));
        gameCountBonusObservable.set(String.valueOf(getBonusGames(String.valueOf(gameCount))));
        clickEventsLiveData.setValue(Constants.Events.ADD_GAME_COUNT);
        if (observableButtonText.get().equals(Constants.Events.END_GAME)) {
            gameRepository.updateGameCountValue(selectedGameScreen.gameId,
                    Integer.parseInt(gameCountObservable.get()),Integer.parseInt(gameCountBonusObservable.get()));
        }
    }


    public void minusGame() {
        if (gameCount != 0) {
            gameCount--;
            gameCountObservable.set(String.valueOf(gameCount));
            gameCountBonusObservable.set(String.valueOf(getBonusGames(String.valueOf(gameCount))));
            clickEventsLiveData.setValue(Constants.Events.MINUS_GAME_COUNT);
        }
    }

    public void startGame() {
        clickEventsLiveData.setValue(Constants.Events.START_GAME);
    }

    public LiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }

    public ObservableField<String> getGameCountObservable() {
        return gameCountObservable;
    }

    public ObservableField<String> getBonusCountObservable() {
        return gameCountBonusObservable;
    }

    public ObservableField<Boolean> getIsGameStarted() {
        return isGameStarted;
    }

    public void setHideGameInputs() {
        boolean isGameActive =selectedGameScreen != null ?selectedGameScreen.isScreenActive : false;
        isGameStarted.set(isGameActive);
        observableButtonText.set(isGameActive ? Constants.Events.END_GAME : Constants.Events.START_GAME);
    }

    public void startDataSync() {
        clickEventsLiveData.setValue(Constants.Events.SYNC_GAME_DATA);
    }

    public void syncGamesData() {
        gameRepository.startScreensApiRequest();
    }

    public void updateGameData(String player_phone, String players) {
        GameCount game = createGameCount(player_phone, players);
        gameRepository.updateGameCount(game);
       // firebaseLogs.setGameLog(todayDate,selectedGameScreen.screenLable,game);
    }

    @NotNull
    private GameCount createGameCount(String player_phone, String players) {
        GameCount game = new GameCount();
        game.setScreenId(selectedGameScreen.screenId);
        game.setGamesCount(Integer.parseInt(gameCountObservable.get()));
        game.setPlayerPhone(player_phone);
        game.setPlayerNames(players);
        game.setGameTypeId(selectedGameType.getId());
        game.setStartTime(getCurrentTime());
        game.setHashKey(selectedGameScreen.hashKey);
        game.setGamesBonus(Integer.parseInt(gameCountBonusObservable.get()));
        return game;
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    public void EndGameCount() {
        updateSelectedGame(gameRepository.getGameViewById(selectedGameScreen.gameId));
        gameRepository.resetActiveScreen(selectedGameScreen.screenId);
       selectedGameScreen.endTime = getCurrentTime();
        gameRepository.detachGameFromScreen(selectedGameScreen);
        //firebaseLogs.setGameLog(todayDate,selectedGameScreen.screenLable,createGameCount(selectedGameScreen.playerPhone,selectedGameScreen.players));
    }

    public long getSelectedGameId() {
        return selectedGameScreen != null ?selectedGameScreen.gameId : 0;
    }

    public void updateSelectedGame(GameView gameView) {
        GameCount gameCount = gameView.gameCount;
       selectedGameScreen.screenId = (gameView.screen != null ? gameView.screen.getId() : 0);
       selectedGameScreen.screenLable = gameView.screen.getScreenLable();
       selectedGameScreen.GameCount = String.valueOf(gameCount != null ? gameCount.getGamesCount() : 0);
       selectedGameScreen.payableAmount = String.valueOf(gameView.payableAmount);
       selectedGameScreen.GameName = gameView.gameType != null ? gameView.gameType.getGameName() : "No Active Game";
       selectedGameScreen.players = gameCount != null ? gameCount.getPlayerNames() : "Idle";
       selectedGameScreen.phoneNumber = gameCount != null ? gameCount.getPlayerPhone() : "";
       selectedGameScreen.startTime = gameCount != null ? gameCount.getStartTime() : "";
       selectedGameScreen.isScreenActive = gameView.screen.isActive();
       selectedGameScreen.currentTime = getCurrentTime();
       selectedGameScreen.bonusAmount = String.valueOf(gameView.bonusAmount);
       selectedGameScreen.gameId = gameCount != null ? gameCount.getGameId() : 0;
       selectedGameScreen.hashKey = ("#" + getCurrentTime() + "#" +selectedGameScreen.GameName + selectedGameScreen);

    }

    public int getBonusGames(String gameCount) {
        int gamesPlayed = Integer.parseInt(gameCount);
        int bonus = 0;
        for (int i=1; i<=gamesPlayed;i++){
            if (i%5==0){
                bonus+=i==gamesPlayed?0:1;
            }
        }
        return bonus;
    }
}
