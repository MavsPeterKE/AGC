package com.example.arcgbot.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.models.ScreenItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.arcgbot.utils.Constants.Events.GAME_ITEM_CLICK;

public class EODViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();

    @Inject
    public EODViewModel() {
    }

    public void onGameItemClick(GameModel gameModel){
        gameItemClickLiveData.setValue(GAME_ITEM_CLICK);
    }

    public MutableLiveData<String> getGameItemClickLiveData() {
        return gameItemClickLiveData;
    }
}
