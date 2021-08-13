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
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.databinding.FragmentSearchBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.viewmodels.SearchItemViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentSearch extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    FragmentSearchBinding fragmentSearchBinding;
    SearchItemViewModel searchItemViewModel;
    GameView selectedGamingScreen;

    public static FragmentSearch newInstance() {
        return new FragmentSearch();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentSearchBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_search, container, false);
        searchItemViewModel = new ViewModelProvider(this, viewModelFactory).get(SearchItemViewModel.class);
        fragmentSearchBinding.setModel(searchItemViewModel);
        fragmentSearchBinding.executePendingBindings();
        fragmentSearchBinding.edSearchText.requestFocus();
        init();
        return fragmentSearchBinding.getRoot();
    }

    private void init() {
        observeClickEvents();
        fragmentSearchBinding.edSearchText.addTextChangedListener(new TextWatcher() {
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
        searchItemViewModel.getSelectedGamingScreen().observe(getViewLifecycleOwner(), gameModel -> {
            selectedGamingScreen = gameModel;
        });
    }

    private void observeScreenData() {
        searchItemViewModel.getGameRepository().getScreensLiveData().observe(getViewLifecycleOwner(), screens -> {
            searchItemViewModel.setScreenList(screens);

        });
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
        Intent gameActivityIntent = new Intent(getActivity(), GameActivity.class);
        gameActivityIntent.putExtra(Constants.IntentKeys.GAME_COUNT_FRAGMENT, FragmentGameItem.class.getSimpleName());
        gameActivityIntent.putExtra(Constants.IntentKeys.SCREEN_ID, selectedGamingScreen.screen.getId());
        startActivity(gameActivityIntent);
    }

}