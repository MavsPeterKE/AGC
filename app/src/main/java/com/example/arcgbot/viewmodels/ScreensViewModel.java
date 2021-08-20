package com.example.arcgbot.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.view.adapter.ScreenAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.arcgbot.utils.Constants.DATE_FORMAT;

public class ScreensViewModel extends ViewModel {
    private ScreenAdapter screenAdapter;
    private MutableLiveData<ScreenItem> selectedScreenItem = new MutableLiveData();
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    public ObservableField<String> gameCount = new ObservableField("O Games");
    public GameRepository gameRepository;
    FirebaseLogs firebaseLogs;

    @Inject
    public ScreensViewModel(GameRepository gameRepository) {
        screenAdapter = new ScreenAdapter(R.layout.screen_item, this);
        this.gameRepository = gameRepository;
        firebaseLogs = new FirebaseLogs();
    }

    public void onSearchClicked() {
        clickEventsLiveData.setValue(Constants.Events.SEARCH_GAME);
    }

    public MutableLiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }

    public ScreenAdapter getScreenAdapterAdapter() {
        return screenAdapter;
    }

    public void setGameCountdapter(List<CompletedGame> completedGameList) {
        List<ScreenItem> screenItemList = new ArrayList<>();
        int totalGameCount = 0;
        for (CompletedGame game : completedGameList){
            ScreenItem screenItem = new ScreenItem();
            screenItem.GameCount = String.valueOf(game.getGamesCount());
            screenItem.duration = game.getDuration();
            screenItem.payableAmount = game.getPayableAmount()+"0";
            screenItem.bonusAmount = game.getBonusAmount()+"0";
            screenItem.phoneNumber = game.getPlayerPhone();
            screenItem.screenLable = game.getScreenLable();
            screenItem.isBonusActive = game.getBonusAmount()>0;
            screenItemList.add(screenItem);
            totalGameCount+=game.getGamesCount();

        }
        gameCount.set(totalGameCount+ " Games");
        isGamesAvailable.set(screenItemList!=null?screenItemList.size()>0:false);
        this.screenAdapter.setGameCountList(screenItemList);
        this.screenAdapter.notifyDataSetChanged();
    }

    public void onScreenItemClick(ScreenItem screenItem){
        selectedScreenItem.setValue(screenItem);
        Log.e("onScreenItemClick:__",screenItem.phoneNumber + "" );
    }

    public MutableLiveData<ScreenItem> getSelectedScreenItem() {
        return selectedScreenItem;
    }
}
