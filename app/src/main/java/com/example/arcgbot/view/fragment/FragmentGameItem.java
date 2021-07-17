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
import com.example.arcgbot.databinding.FragmentEodBinding;
import com.example.arcgbot.databinding.FragmentGameItemBinding;
import com.example.arcgbot.models.EndDayModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.example.arcgbot.viewmodels.GameItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentGameItem extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private GameItemViewModel gameItemViewModel;
    private FragmentGameItemBinding fragmentGameItemBinding;

    public static FragmentGameItem newInstance() {
        return new FragmentGameItem();
    }

    private FirebaseLogs firebaseLogs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentGameItemBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_game_item, container, false);
        gameItemViewModel = new ViewModelProvider(this, viewModelFactory).get(GameItemViewModel.class);
        fragmentGameItemBinding.setModel(gameItemViewModel);
        fragmentGameItemBinding.executePendingBindings();
        gameItemViewModel.getGameRepository().getGamesLiveData()
                .observe(getViewLifecycleOwner(), gameTypes ->
                        gameItemViewModel.setGamesList(gameTypes));

        return fragmentGameItemBinding.getRoot();
    }

}