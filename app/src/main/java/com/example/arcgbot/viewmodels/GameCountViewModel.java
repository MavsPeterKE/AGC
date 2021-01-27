package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.GameCountAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameCountViewModel extends ViewModel {
    private GameCountAdapter gameCountAdapter;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private ObservableField<String> gameCountObservable = new ObservableField();
    private ObservableField<Boolean> isGameStarted = new ObservableField();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    public ObservableField<String> obervableButtonText = new ObservableField("Start Game");
    private GameModel selectedGame;
    private int gameCount;

    @Inject
    public GameCountViewModel() {
        gameCountAdapter = new GameCountAdapter(R.layout.game_item, this);
        addTestData();
    }

    private void addTestData() {
        List<GameModel> gameCounts = new ArrayList<>();
        GameModel model = new GameModel();
        model.screenLable = "Screen 1 TCL";
        model.GameCount = "4";
        model.GameName = "FiFa 21";
        model.startTime = "2.30 pm";
        model.playTime = "1h";
        model.payableAmount = String.valueOf(Integer.parseInt(model.GameCount) * 30);
        model.player1 = "Fred";
        model.player2 = "Peter";
        gameCounts.add(model);

        GameModel model2 = new GameModel();
        model2.screenLable = "Screen 2 Hsc";
        model2.GameCount = "5";
        model2.GameName = "NDA";
        model2.startTime = "11.30 pm";
        model2.payableAmount = String.valueOf(Integer.parseInt(model.GameCount) * 50);
        model2.player1 = "PMC";
        model2.player2 = "Derick";
        model2.playTime = "1h 30min";
        gameCounts.add(model2);

        GameModel model3 = new GameModel();
        model3.screenLable = "Screen 3 TCL";
        model3.GameCount = "10";
        model3.GameName = "FIFA 20";
        model3.startTime = "09.30 pm";
        model3.payableAmount = String.valueOf(Integer.parseInt(model.GameCount) * 50);
        model3.player1 = "PMC";
        model3.player2 = "Derick";
        model3.playTime = "2h";
        gameCounts.add(model3);


        GameModel model4 = new GameModel();
        model4.screenLable = "Screen 4 TCL";
        model4.GameCount = "8";
        model4.GameName = "FIFA 21";
        model4.startTime = "08.30 pm";
        model4.payableAmount = String.valueOf(Integer.parseInt(model.GameCount) * 30);
        model4.player1 = "Owen";
        model4.player2 = "Maina";
        model4.playTime = "1h 45min";
        gameCounts.add(model4);

        setGameCountdapter(gameCounts);
    }

    public GameCountAdapter getGameCountAdapter() {
        return gameCountAdapter;
    }

    public void setGameCountdapter(List<GameModel> gameCountList) {
        isGamesAvailable.set(!gameCountList.isEmpty());
        this.gameCountAdapter.setGameCountList(gameCountList);
        this.gameCountAdapter.notifyDataSetChanged();
    }

    public void onGameItemClick(GameModel gameModel) {
        selectedGame = gameModel;
        gameCount = Integer.parseInt(selectedGame.GameCount);
        gameCountObservable.set(selectedGame.GameCount);
        clickEventsLiveData.setValue(Constants.Events.GAME_ITEM_CLICK);
    }

    public void closeSheet() {
        clickEventsLiveData.setValue(Constants.Events.BOTTOM_SHEET);
    }

    public void addGame() {
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
}
