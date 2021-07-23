package com.example.arcgbot.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.databinding.FragmentGameCountBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.view.activity.HomeActivity;
import com.example.arcgbot.viewmodels.GameCountViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentGameCount extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private GameCountViewModel mViewModel;
    private FragmentGameCountBinding fragmentGameCountBinding;
    private GameView selectedGamingScreen;

    public static FragmentGameCount newInstance() {
        return new FragmentGameCount();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentGameCountBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_game_count, container, false);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(GameCountViewModel.class);
        fragmentGameCountBinding.setModel(mViewModel);
        fragmentGameCountBinding.executePendingBindings();
        init();
        return fragmentGameCountBinding.getRoot();
    }

    private void init() {
        observeClickEvents();
        observeScreenData();
        observeGamingScreens();
        observeButtonClicks();
    }

    private void observeGamingScreens() {
        mViewModel.getSelectedGamingScreen().observe(getViewLifecycleOwner(), gameModel -> {
            selectedGamingScreen = gameModel;
        });
    }

    private void observeScreenData() {
        mViewModel.gameRepository().getScreensLiveData().observe(getViewLifecycleOwner(), screen -> {
            if (!screen.isEmpty()) {
                fragmentGameCountBinding.noGameData.progressBarSync.setVisibility(View.VISIBLE);
            }
            mViewModel.setGameCountAdapter(screen);

        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void observeButtonClicks() {
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case Constants.Events.GAME_ITEM_CLICK:
                    startGameActivity();
                    break;
                case Constants.Events.SYNC_GAME_DATA:
                    fragmentGameCountBinding.noGameData.progressBarSync.setVisibility(View.VISIBLE);
                    fragmentGameCountBinding.noGameData.tvSyncTitleHint.setText(getString(R.string.syncing_data));
                    mViewModel.syncGamesData();
                    break;
            }
        });
    }

    private void startGameActivity() {
        Intent gameActivityIntent = new Intent(getActivity(), GameActivity.class);
        gameActivityIntent.putExtra(Constants.IntentKeys.GAME_COUNT_FRAGMENT, FragmentGameItem.class.getSimpleName());
        gameActivityIntent.putExtra(Constants.IntentKeys.SCREEN_ID, selectedGamingScreen.screen.getId());
        startActivity(gameActivityIntent);
    }


    public void observeClickEvents() {
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), value -> {
            switch (value) {
                case Constants.Events.SEARCH_GAME:
                    startSearchFragment();
                    break;


            }
        });
    }

    private void startSearchFragment() {
        ((HomeActivity) Objects.requireNonNull(getActivity()))
                .changeFragment(new FragmentSearch(), FragmentSearch.class.getSimpleName());
    }
}