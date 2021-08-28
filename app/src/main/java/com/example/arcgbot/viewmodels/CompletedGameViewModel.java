package com.example.arcgbot.viewmodels;

import static com.example.arcgbot.utils.Constants.PrefsKeys.HAPPY_HOUR_TIME_MAX;
import static com.example.arcgbot.utils.Constants.PrefsKeys.IS_LOYALTY_BONUS_ENABLED;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.Prefs;
import com.example.arcgbot.utils.Utils;

import javax.inject.Inject;

public class CompletedGameViewModel extends ViewModel {
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private ObservableField<CompletedGame> completedGameField = new ObservableField<>();
    private ObservableField<Boolean> isHappyHourEnabled = new ObservableField<>();
    private ObservableField<Boolean> isLoyaltyEnabled = new ObservableField<>();
    private GameRepository gameRepository;

    @Inject
    public CompletedGameViewModel(GameRepository gameRepository) {
        this.gameRepository =gameRepository;
        isLoyaltyEnabled.set(Prefs.getBoolean(IS_LOYALTY_BONUS_ENABLED));
        isHappyHourEnabled.set(Utils.getCurrentSeconds() > Prefs.getLong(HAPPY_HOUR_TIME_MAX));

    }

    public void onBack() {
        clickEventsLiveData.setValue(Constants.Events.BACK_TO_SCREENS);
    }

    public MutableLiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }

    public void setClickEventsLiveData(MutableLiveData<String> clickEventsLiveData) {
        this.clickEventsLiveData = clickEventsLiveData;
    }

    public GameRepository getGameRepository() {
        return gameRepository;
    }

    public void setSelectedGame(CompletedGame completedGame) {
        completedGameField.set(completedGame);
    }

    public ObservableField<CompletedGame> getCompletedGameField() {
        return completedGameField;
    }

    public ObservableField<Boolean> getIsHappyHourEnabled() {
        return isHappyHourEnabled;
    }

    public ObservableField<Boolean> getIsLoyaltyEnabled() {
        return isLoyaltyEnabled;
    }
}
