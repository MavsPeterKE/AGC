package com.example.arcgbot.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentGameCountBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.GameCountViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentGameCount extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private GameCountViewModel mViewModel;
    private FragmentGameCountBinding fragmentGameCountBinding;
    private BottomSheetBehavior sheetBehavior;
    private String player_phone;
    private String player1;
    private String player2;
    private EditText edPlayer1;
    private EditText edPlayer2;
    private EditText edPlayerPhone;

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
        createBottomSheet();
        observeClickEvents();
        observeScreenData();
    }

    private void observeScreenData() {
        mViewModel.gameRepository().getScreensLiveData().observe(getViewLifecycleOwner(), screen -> {
            if (!screen.isEmpty()) {
                fragmentGameCountBinding.noGameData.progressBarSync.setVisibility(View.VISIBLE);
            }
            mViewModel.setGameCountAdapter(screen);
        });

        mViewModel.gameRepository().getGamesLiveData().observe(getViewLifecycleOwner(), gameTypes -> mViewModel.setGamesList(gameTypes));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String action) {
                switch (action) {
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
            clearInputs();
        } else {
            mViewModel.gameRepository().resetSelected();
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void createBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(fragmentGameCountBinding.bottomSheet.getRoot());
        sheetBehavior.setHideable(false);
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
        edPlayerPhone = fragmentGameCountBinding.bottomSheet.editTextPhone;
        edPlayer1 = fragmentGameCountBinding.bottomSheet.edPlayer1;
        edPlayer2 = fragmentGameCountBinding.bottomSheet.edPlayer2;


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
                    if (btnText.contains(Constants.Events.END_GAME)) {
                        mViewModel.EndGameCount();
                        showGameBottomSheetAction();
                    } else {
                        if (isInputsValid()) {
                            mViewModel.updateGameData(player_phone, player1 +" Vs " +player2);
                            showGameBottomSheetAction();
                        }
                    }


            }
        });
    }

    private boolean isInputsValid() {
        player_phone = edPlayerPhone.getText().toString();
        player1 = edPlayer1.getText().toString();
        player2 = edPlayer2.getText().toString();
        if (player1.isEmpty() || player1.equals("")) {
            edPlayer1.setError("Required");
            return false;
        } else if (player2.isEmpty() || player2.equals("")) {
            edPlayer1.setError("Required");
            return false;
        } else if (player_phone.isEmpty() || player_phone.equals("")) {
            edPlayerPhone.setError("Required");
            return false;
        } else if (mViewModel.selectedGameType == null) {
            Toast.makeText(getActivity(), "Select Game Type", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            edPlayerPhone.setError(null);
            edPlayer1.setError(null);
            return true;
        }

    }

    private void clearInputs(){
        edPlayerPhone.setText("");
        edPlayer1.setText("");
        edPlayer2.setText("");
    }
}