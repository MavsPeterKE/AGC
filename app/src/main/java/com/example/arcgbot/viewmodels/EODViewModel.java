package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.repository.GameRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.arcgbot.utils.Constants.Events.GAME_ITEM_CLICK;

public class EODViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();
    public ObservableField<String> gameCount = new ObservableField("O Games");
    public ObservableField<String> gameRevenue = new ObservableField("Ksh. O.00");
    public GameRepository repository;

    @Inject
    public EODViewModel(GameRepository repository) {
        this.repository = repository;
    }

    public void onGameItemClick(GameModel gameModel){
        gameItemClickLiveData.setValue(GAME_ITEM_CLICK);
    }

    public GameRepository getRepository() {
        return repository;
    }

    public void setRepository(GameRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<String> getGameItemClickLiveData() {
        return gameItemClickLiveData;
    }
}
