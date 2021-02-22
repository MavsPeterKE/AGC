package com.example.arcgbot.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.databinding.FragmentScreensBinding;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.ScreensViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentScreens extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private ScreensViewModel mViewModel;
    private FragmentScreensBinding fragmentScreensBinding;

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

        mViewModel.gameRepository.getCompletedGames().observe(getViewLifecycleOwner(), new Observer<List<CompletedGame>>() {
            @Override
            public void onChanged(List<CompletedGame> completedGames) {
                mViewModel.setGameCountdapter(completedGames);
            }
        });

        return fragmentScreensBinding.getRoot();
    }

}