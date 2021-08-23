package com.example.arcgbot.viewmodels;

import static com.example.arcgbot.utils.Constants.Events.BACK_TO_GAME_COUNT;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.ScreenSearchAdapter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class SearchItemViewModel extends ViewModel {
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData<>();
    private GameRepository gameRepository;
    private ScreenSearchAdapter screenSearchAdapter;
    private List<GameView> screenList;
    private MutableLiveData<GameView> selectedGamingScreen = new MutableLiveData();


    @Inject
    public SearchItemViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        screenSearchAdapter = new ScreenSearchAdapter(R.layout.screen_search_item,this);

    }

    public ScreenSearchAdapter getScreenSearchAdapter() {
        return screenSearchAdapter;
    }

    public MutableLiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }

    public void onBackClick(){
        clickEventsLiveData.setValue(BACK_TO_GAME_COUNT);
    }

    public GameRepository getGameRepository() {
        return gameRepository;
    }

    public void setScreenList(List<GameView> screens) {
        screenList = screens;
        screenSearchAdapter.setGameCountList(screens);
        screenSearchAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void searchScreen(String searchText) {
        List<GameView> gameViewList = screenList.stream().filter(gameView -> gameView.screen.getScreenLable().contains(searchText)).collect(Collectors.toList());
        screenSearchAdapter.setGameCountList(gameViewList);
        screenSearchAdapter.notifyDataSetChanged();
    }

    public void onGameItemClick(GameView gameModel, int pos) {
        selectedGamingScreen.setValue(gameModel);
        clickEventsLiveData.setValue(Constants.Events.GAME_ITEM_CLICK);
    }

    public MutableLiveData<GameView> getSelectedGamingScreen() {
        return selectedGamingScreen;
    }
}
