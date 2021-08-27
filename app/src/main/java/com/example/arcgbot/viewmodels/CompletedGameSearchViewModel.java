package com.example.arcgbot.viewmodels;

import static com.example.arcgbot.utils.Constants.Events.BACK_TO_GAME_COUNT;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.CompletedGameSearchAdapter;
import com.example.arcgbot.view.adapter.ScreenSearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class CompletedGameSearchViewModel extends ViewModel {
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData<>();
    private GameRepository gameRepository;
    private CompletedGameSearchAdapter completedGameSearchAdapter;
    private List<ScreenItem> screenList;
    private MutableLiveData<CompletedGame> selectedGameLiveData = new MutableLiveData();


    @Inject
    public CompletedGameSearchViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        completedGameSearchAdapter = new CompletedGameSearchAdapter(R.layout.completed_game_search,this);

    }

    public CompletedGameSearchAdapter getScreenSearchAdapter() {
        return completedGameSearchAdapter;
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

    public void setScreenList(List<CompletedGame> screens) {
        screenList = getScreenList(screens);
        completedGameSearchAdapter.setScreenList(screenList);
        completedGameSearchAdapter.notifyDataSetChanged();
    }

    private List<ScreenItem> getScreenList(List<CompletedGame> completedGameList){
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
        return screenItemList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void searchScreen(String searchText) {
        List<ScreenItem> gameViewList = screenList.stream().filter(screenItem ->
                screenItem.screenLable.toLowerCase().contains(searchText)).collect(Collectors.toList());
        completedGameSearchAdapter.setScreenList(gameViewList);
        completedGameSearchAdapter.notifyDataSetChanged();
    }

    public void onCompletedGameClick(CompletedGame completedGame) {
        selectedGameLiveData.setValue(completedGame);
        clickEventsLiveData.setValue(Constants.Events.COMPLETED_GAME_CLICK);
    }

    public MutableLiveData<CompletedGame> getSelectedGame() {
        return selectedGameLiveData;
    }
}
