package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.annimon.stream.Stream;
import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.GameCountAdapter;
import com.example.arcgbot.view.adapter.GameTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameCountViewModel extends ViewModel {
    private GameCountAdapter gameCountAdapter;
    private GameTypeAdapter gameTypeAdapter;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private ObservableField<String> gameCountObservable = new ObservableField();
    private ObservableField<Boolean> isGameStarted = new ObservableField();
    public ObservableField<Boolean> isGameSelected = new ObservableField();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    public ObservableField<String> obervableButtonText = new ObservableField("Start Game");
    private GameModel selectedGame;
    private int gameCount;
    private GameRepository gameRepository;
    List<GameType> gameTypeList = new ArrayList<>();

    @Inject
    public GameCountViewModel(GameRepository gameRepository) {
        gameCountAdapter = new GameCountAdapter(R.layout.game_item, this);
        gameTypeAdapter = new GameTypeAdapter(R.layout.game_type_item, this);
        this.gameRepository = gameRepository;
    }

    public GameCountAdapter getGameCountAdapter() {
        return gameCountAdapter;
    }

    public GameTypeAdapter getGameTypeAdapter() {
        return gameTypeAdapter;
    }

    public void setGameCountdapter(List<Screen> screens) {
        List<GameModel> gameModels = new ArrayList<>();
        for (Screen screen: screens){
            GameModel gameModel = new GameModel();
            gameModel.screenLable = screen.getScreenLable();
            gameModel.GameCount = "0";
            gameModel.payableAmount = "0.00";
            gameModel.GameName = "Idle";
            gameModel.players = "No Players";
            //gameCountModel.player1+` Vs ` +gameCountModel.player2 + `  : `+gameCountModel.playTime
            gameModels.add(gameModel);

        }
        isGamesAvailable.set(!screens.isEmpty());
        this.gameCountAdapter.setGameCountList(gameModels);
        this.gameCountAdapter.notifyDataSetChanged();
    }

    public void setGamesList(List<GameType> gamesList) {
        gameTypeList = gamesList;
        this.gameTypeAdapter.setGameTypeList(gamesList);
        this.gameCountAdapter.notifyDataSetChanged();
    }

    public GameRepository gameRepository(){return gameRepository;}

    public void onGameItemClick(GameModel gameModel) {
        selectedGame = gameModel;
        gameCount = Integer.parseInt(selectedGame.GameCount);
        gameCountObservable.set(selectedGame.GameCount);
        clickEventsLiveData.setValue(Constants.Events.GAME_ITEM_CLICK);
    }

    public void onGameTypeClick(GameType gameType){
       gameRepository.updateSelectedGame(gameType);
    }

    public void closeSheet() {
        clickEventsLiveData.setValue(Constants.Events.BOTTOM_SHEET);
    }

    public void addGame() {
        gameCount++;
        gameCountObservable.set(String.valueOf(gameCount));
        clickEventsLiveData.setValue(Constants.Events.ADD_GAME_COUNT);
    }

    public void closeError() {
        gameCount++;
        gameCountObservable.set(String.valueOf(gameCount));
        clickEventsLiveData.setValue(Constants.Events.ADD_GAME_COUNT);
    }

    public void minusGame() {
        if (gameCount!=0){
            gameCount--;
            gameCountObservable.set(String.valueOf(gameCount));
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

    public ObservableField<Boolean> getIsGameStarted() {
        return isGameStarted;
    }

    public void setHideGameInputs(boolean b) {
        isGameStarted.set(b);
        obervableButtonText.set(b?Constants.Events.END_GAME:Constants.Events.START_GAME);
    }

    public void startDataSync(){
        clickEventsLiveData.setValue(Constants.Events.SYNC_GAME_DATA);
    }

    public void syncGamesData(){
        gameRepository.startScreensApiRequest();
    }
}
