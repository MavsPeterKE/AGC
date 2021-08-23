package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.view.adapter.GameCountAdapter;

import java.util.List;

import javax.inject.Inject;

public class GameCountViewModel extends ViewModel {
    private GameCountAdapter gameCountAdapter;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private MutableLiveData<GameView> selectedGamingScreen = new MutableLiveData();
    private ObservableField<String> gameCountObservable = new ObservableField();
    private ObservableField<String> gameCountBonusObservable = new ObservableField();
    private ObservableField<Boolean> isGameStarted = new ObservableField();
    public ObservableField<Boolean> isGamesAvailable = new ObservableField(false);
    public ObservableField<String> observableButtonText = new ObservableField("Start Game");
    private GameRepository gameRepository;
    public MutableLiveData<String> text = new MutableLiveData<>();
    public GameType selectedGameType;
    FirebaseLogs firebaseLogs;

    @Inject
    public GameCountViewModel(GameRepository gameRepository) {
        gameCountAdapter = new GameCountAdapter(R.layout.game_item_revamp, this);
        this.gameRepository = gameRepository;
        firebaseLogs = new FirebaseLogs();

    }

    public void onSearchClicked() {
        clickEventsLiveData.setValue(Constants.Events.SEARCH_GAME);
    }

    public GameCountAdapter getGameCountAdapter() {
        return gameCountAdapter;
    }

    public void setGameCountAdapter(List<GameView> screens) {
        isGamesAvailable.set(!screens.isEmpty());
        this.gameCountAdapter.setGameCountList(screens);
        this.gameCountAdapter.notifyDataSetChanged();
        if (screens != null) {
            if (!screens.isEmpty()) {
                firebaseLogs.setGameLogList("all-active-games", screens);
            }
        }

    }

    public GameRepository gameRepository() {
        return gameRepository;
    }

    public void onGameItemClick(GameView gameModel, int pos) {
        selectedGamingScreen.setValue(gameModel);
        clickEventsLiveData.setValue(Constants.Events.GAME_ITEM_CLICK);
    }

    public MutableLiveData<GameView> getSelectedGamingScreen() {
        return selectedGamingScreen;
    }

    public LiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }


    public void startDataSync() {
        clickEventsLiveData.setValue(Constants.Events.SYNC_GAME_DATA);
    }

    public void syncGamesData() {
        gameRepository.startScreensApiRequest();
    }
}
