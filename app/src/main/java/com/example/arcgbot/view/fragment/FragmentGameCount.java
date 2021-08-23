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
import com.example.arcgbot.models.Configs;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.Prefs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.view.activity.SearchActivity;
import com.example.arcgbot.viewmodels.GameCountViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private void observeAppConfigs(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER).child("gamelogs").child("configs");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configs configs = dataSnapshot.getValue(Configs.class);
                Prefs.putLong(Constants.PrefsKeys.HAPPY_HOUR_TIME_MAX,configs.happy_hour_max_seconds);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void init() {
        mViewModel.gameRepository().setCustomersData();
        mViewModel.gameRepository().observePromotionsData();
        observeClickEvents();
        observeScreenData();
        observeClickedGamingScreen();
        observeButtonClicks();
        observeAppConfigs();
    }

    private void observeClickedGamingScreen() {
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
                    startSearchActivity();
                    break;


            }
        });
    }

    private void startSearchActivity() {
        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
        searchIntent.putExtra(Constants.IntentKeys.ORIGIN_FRAGMENT,FragmentGameCount.class.getSimpleName());
        startActivity(searchIntent);
    }
}