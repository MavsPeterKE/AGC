package com.example.arcgbot.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentCompletedGameDetailBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.ScreenActivity;
import com.example.arcgbot.viewmodels.CompletedGameViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentCompletedGameDetail extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private CompletedGameViewModel completedGameViewModel;
    private FragmentCompletedGameDetailBinding fragmentCompletedGameDetailBinding;

    public static FragmentCompletedGameDetail newInstance() {
        return new FragmentCompletedGameDetail();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCompletedGameDetailBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_completed_game_detail, container, false);
        completedGameViewModel = new ViewModelProvider(this, viewModelFactory).get(CompletedGameViewModel.class);
        fragmentCompletedGameDetailBinding.setModel(completedGameViewModel);
        fragmentCompletedGameDetailBinding.executePendingBindings();
        init();
        return fragmentCompletedGameDetailBinding.getRoot();
    }

    private void init() {
        observeClickableEvents();
        observeSelectedGame();
    }

    private void observeClickableEvents(){
        completedGameViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action){
                case Constants.Events.BACK_TO_SCREENS:
                    Objects.requireNonNull(getActivity()).onBackPressed();
                    break;
            }

        });
    }

    private void observeSelectedGame(){
        long screenId = ((ScreenActivity) getActivity()).screenId;
        completedGameViewModel.getGameRepository().getCompletedGameById(screenId).observe(getViewLifecycleOwner(), completedGame -> {
          completedGameViewModel.setSelectedGame(completedGame);
        });
    }
}