package com.example.arcgbot.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.view.adapter.ScreenAdapter;

import java.util.List;

import javax.inject.Inject;

public class ScreensViewModel extends ViewModel {
    private ScreenAdapter screenAdapter;
    private MutableLiveData<CompletedGame> selectedScreenItem = new MutableLiveData();
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

    public void setGameCountAdapter(List<CompletedGame> completedGameList) {
        int totalGameCount = 0;
        for (CompletedGame game : completedGameList) {
            totalGameCount += game.getGamesCount();

        }
        gameCount.set(totalGameCount + " Games");
        isGamesAvailable.set(completedGameList != null ? completedGameList.size() > 0 : false);
        this.screenAdapter.setGameCountList(completedGameList);
        this.screenAdapter.notifyDataSetChanged();
    }

    public void onScreenItemClick(CompletedGame screenItem) {
        selectedScreenItem.setValue(screenItem);
        clickEventsLiveData.setValue(Constants.Events.COMPLETED_GAME_CLICK);
    }

    public MutableLiveData<CompletedGame> getSelectedScreenItem() {
        return selectedScreenItem;
    }
}
