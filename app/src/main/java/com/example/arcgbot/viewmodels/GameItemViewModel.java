package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.GameTypeAdapter;
import com.example.arcgbot.view.adapter.GameTypeAdapterNew;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameItemViewModel extends ViewModel {
    private ObservableField<Boolean> isGameStarted = new ObservableField();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    private GameTypeAdapterNew gameTypeAdapter;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private GameRepository gameRepository;
    List<GameType> gameTypeList = new ArrayList<>();



    @Inject
    public GameItemViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        gameTypeAdapter = new GameTypeAdapterNew(R.layout.game_type_item, this);

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

    public void setGamesList(List<GameType> gamesList) {
        gameTypeList = gamesList;
        this.gameTypeAdapter.setGameTypeList(gamesList);
        this.gameTypeAdapter.notifyDataSetChanged();
    }

}
