package com.example.arcgbot.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.arcgbot.repository.GameRepository;

import javax.inject.Inject;

public class SearchItemViewModel extends ViewModel {


    @Inject
    public SearchItemViewModel(GameRepository gameRepository) {

    }

}
