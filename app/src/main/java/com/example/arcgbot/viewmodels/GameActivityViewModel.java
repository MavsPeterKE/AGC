package com.example.arcgbot.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.arcgbot.repository.GameRepository;

import javax.inject.Inject;

public class GameActivityViewModel extends ViewModel {


    @Inject
    public GameActivityViewModel(GameRepository gameRepository) {

    }
}
