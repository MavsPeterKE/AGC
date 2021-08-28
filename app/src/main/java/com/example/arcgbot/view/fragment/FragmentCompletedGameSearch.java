package com.example.arcgbot.view.fragment;

import static com.example.arcgbot.utils.Constants.Events.BACK_TO_GAME_COUNT;
import static com.example.arcgbot.utils.Constants.Events.GAME_ITEM_CLICK;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.databinding.FragmentCompletedGameSearchBinding;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.view.activity.ScreenActivity;
import com.example.arcgbot.viewmodels.CompletedGameSearchViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentCompletedGameSearch extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    FragmentCompletedGameSearchBinding fragmentCompletedGameSearchBinding;
    CompletedGameSearchViewModel searchItemViewModel;
    CompletedGame selectedGame;

    public static FragmentCompletedGameSearch newInstance() {
        return new FragmentCompletedGameSearch();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCompletedGameSearchBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_completed_game_search, container, false);
        searchItemViewModel = new ViewModelProvider(this, viewModelFactory).get(CompletedGameSearchViewModel.class);
        fragmentCompletedGameSearchBinding.setModel(searchItemViewModel);
        fragmentCompletedGameSearchBinding.executePendingBindings();
        fragmentCompletedGameSearchBinding.edSearchText.requestFocus();
        init();
        return fragmentCompletedGameSearchBinding.getRoot();
    }

    private void init() {
        observeClickEvents();
        fragmentCompletedGameSearchBinding.edSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchItemViewModel.searchScreen(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        observeScreenData();
        observeClickedGamingScreen();
    }

    private void observeClickedGamingScreen() {
        searchItemViewModel.getSelectedGame().observe(getViewLifecycleOwner(), completedGame -> {
            selectedGame = completedGame;
        });
    }

    private void observeScreenData() {
        searchItemViewModel.getGameRepository().getCompletedGames()
                .observe(getViewLifecycleOwner(), completedGames ->
                        searchItemViewModel.setScreenList(completedGames));
    }

    private void observeClickEvents() {
        searchItemViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case BACK_TO_GAME_COUNT:
                    getActivity().onBackPressed();
                    break;
                case GAME_ITEM_CLICK:
                    startGameActivity();
                    break;
            }

        });
    }

    private void startGameActivity() {
        CompletedGame screen = selectedGame;
        Intent screenIntent = new Intent(getActivity(), ScreenActivity.class);
        screenIntent.putExtra(Constants.IntentKeys.DESTINATION_FRAGMENT, FragmentCompletedGameDetail.class.getSimpleName());
        screenIntent.putExtra(Constants.IntentKeys.SCREEN_ID, screen.getId());
        startActivity(screenIntent);
    }

}