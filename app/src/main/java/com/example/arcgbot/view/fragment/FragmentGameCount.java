package com.example.arcgbot.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.databinding.FragmentGameCountBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.GameCountViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentGameCount extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private GameCountViewModel mViewModel;
    private FragmentGameCountBinding fragmentGameCountBinding;
    private BottomSheetBehavior sheetBehavior;
    private Spinner gamesSpinner;

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
        gamesSpinner = fragmentGameCountBinding.bottomSheet.spinner;
        init();
        return fragmentGameCountBinding.getRoot();
    }

    private void init() {
        createBottomSheet();
        observeClickEvents();
        observeScreenData();
    }

    private void observeScreenData() {
        mViewModel.gameRepository().getScreensLiveData().observe(getViewLifecycleOwner(), screen -> {
            if (!screen.isEmpty()){
                fragmentGameCountBinding.noGameData.progressBarSync.setVisibility(View.VISIBLE);
            }
            mViewModel.setGameCountdapter(screen);
        });

        mViewModel.gameRepository().getGamesLiveData().observe(getViewLifecycleOwner(), new Observer<List<GameType>>() {
            @Override
            public void onChanged(List<GameType> gameTypes) {
                if (!gameTypes.isEmpty()){
                    
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String action) {
                switch (action){
                    case Constants.Events.GAME_ITEM_CLICK:
                        showGameBottomSheetAction();
                        break;
                    case Constants.Events.SYNC_GAME_DATA:
                        fragmentGameCountBinding.noGameData.progressBarSync.setVisibility(View.VISIBLE);
                        fragmentGameCountBinding.noGameData.tvSyncTitleHint.setText(getString(R.string.syncing_data));
                        mViewModel.syncGamesData();
                        break;
                }
            }
        });
    }

    private void showGameBottomSheetAction() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void createBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(fragmentGameCountBinding.bottomSheet.getRoot());
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    int clickCount = 0;

    public void observeClickEvents() {
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), value -> {
            switch (value) {
                case Constants.Events.BOTTOM_SHEET:
                    showGameBottomSheetAction();
                    break;
                case Constants.Events.START_GAME:
                    clickCount += 1;
                    String btnText = fragmentGameCountBinding.bottomSheet.button.getText().toString();
                    if (btnText.contains(Constants.Events.END_GAME)){
                        //reset data
                        mViewModel.setHideGameInputs(false);
                    }else {
                        showGameBottomSheetAction();
                        setViews();
                    }
            }
        });
    }

    private void setViews() {
        mViewModel.setHideGameInputs(clickCount >= 1);

    }
}