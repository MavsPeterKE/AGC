package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.ScreenAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ScreensViewModel extends ViewModel {
    private ScreenAdapter screenAdapter;
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    public ObservableField<String> gameCount = new ObservableField("O Games");
    public GameRepository gameRepository;

    @Inject
    public ScreensViewModel(GameRepository gameRepository) {
        screenAdapter = new ScreenAdapter(R.layout.screen_item, this);
        this.gameRepository = gameRepository;
        gameCount.set(gameRepository.getGameTotal() +" Games");
    }

    public ScreenAdapter getScreenAdapterAdapter() {
        return screenAdapter;
    }

    public void setGameCountdapter(List<CompletedGame> completedGameList) {
        List<ScreenItem> screenItemList = new ArrayList<>();
        for (CompletedGame game : completedGameList){
            ScreenItem screenItem = new ScreenItem();
            screenItem.GameCount = String.valueOf(game.getGamesCount());
            screenItem.duration = game.getDuration();
            screenItem.payableAmount = game.getPayableAmount()+"0";
            screenItem.screenLable = game.getScreenLable();
            screenItemList.add(screenItem);

        }
        isGamesAvailable.set(screenItemList!=null?screenItemList.size()>0:false);
        this.screenAdapter.setGameCountList(screenItemList);
        this.screenAdapter.notifyDataSetChanged();
    }

    public void onGameItemClick(GameModel gameModel){
        gameItemClickLiveData.setValue(Constants.Events.GAME_ITEM_CLICK);
    }

    public MutableLiveData<String> getGameItemClickLiveData() {
        return gameItemClickLiveData;
    }
}
