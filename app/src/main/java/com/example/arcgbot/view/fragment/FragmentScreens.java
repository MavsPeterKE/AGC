package com.example.arcgbot.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.databinding.FragmentScreensBinding;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.ScreenActivity;
import com.example.arcgbot.view.activity.SearchActivity;
import com.example.arcgbot.viewmodels.ScreensViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentScreens extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private ScreensViewModel mViewModel;
    private FragmentScreensBinding fragmentScreensBinding;
    private CompletedGame clickedScreen;

    public static FragmentScreens newInstance() {
        return new FragmentScreens();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentScreensBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_screens, container, false);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(ScreensViewModel.class);
        fragmentScreensBinding.setScreenviewmodel(mViewModel);
        fragmentScreensBinding.executePendingBindings();
        init();
        return fragmentScreensBinding.getRoot();
    }

    private void init() {
        observeCompletedGames();
        observeClickEvents();
        mViewModel.getSelectedScreenItem().observe(getViewLifecycleOwner(),
                screenItem -> {
                    clickedScreen = screenItem;
                });
    }

    private void observeCompletedGames() {
        mViewModel.gameRepository.getCompletedGames().observe(getViewLifecycleOwner(), completedGames ->
                mViewModel.setGameCountAdapter(completedGames));
    }


    public void observeClickEvents() {
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), value -> {
            switch (value) {
                case Constants.Events.SEARCH_GAME:
                    startSearchActivity();
                    break;
                case Constants.Events.GAME_ITEM_CLICK:
                    startScreenDetail();
                    break;


            }
        });
    }

    private void startSearchActivity() {
        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
        searchIntent.putExtra(Constants.IntentKeys.ORIGIN_FRAGMENT, FragmentScreens.class.getSimpleName());
        startActivity(searchIntent);
    }

    private void startScreenDetail() {
        CompletedGame screen = clickedScreen;
        Intent screenIntent = new Intent(getActivity(), ScreenActivity.class);
        screenIntent.putExtra(Constants.IntentKeys.DESTINATION_FRAGMENT, FragmentCompletedGameDetail.class.getSimpleName());
        screenIntent.putExtra(Constants.IntentKeys.SCREEN_ID, screen.getId());
        startActivity(screenIntent);
    }

}