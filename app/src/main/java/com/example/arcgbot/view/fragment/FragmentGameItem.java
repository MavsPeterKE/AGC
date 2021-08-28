package com.example.arcgbot.view.fragment;

import static com.example.arcgbot.utils.Constants.Events.GAME_STARTED;
import static com.example.arcgbot.utils.Constants.Events.MINUS_GAME_EVENT_ERROR;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentGameItemBinding;
import com.example.arcgbot.models.GamerModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.viewmodels.GameItemViewModel;

import java.util.Objects;

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
    private GamerModel gamerModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentGameItemBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_game_item, container, false);
        gameItemViewModel = new ViewModelProvider(this, viewModelFactory).get(GameItemViewModel.class);
        fragmentGameItemBinding.setModel(gameItemViewModel);
        fragmentGameItemBinding.executePendingBindings();
        init();
        return fragmentGameItemBinding.getRoot();
    }

    private void observePlayerSuggestListPick() {
        gameItemViewModel.getSelectedGamerLiveData().observe(getViewLifecycleOwner(), customer -> {
            if (customer!=null){
                if (gameItemViewModel.getIsPlayerOneEnabled().get()){
                    fragmentGameItemBinding.edPlayer1Name.setText(customer.gamer.getCustomerName());
                    fragmentGameItemBinding.edPlayer1Phone.setText(customer.gamer.getCustomerPhone());
                    gameItemViewModel.getIsPlayerOneEnabled().set(false);
                    fragmentGameItemBinding.player2layout.setVisibility(View.VISIBLE);
                    fragmentGameItemBinding.spinner.setVisibility(View.VISIBLE);
                }

                if (gameItemViewModel.getIsPlayerTwoEnabled().get()){
                    fragmentGameItemBinding.edPlayer2Name.setText(customer.gamer.getCustomerName());
                    fragmentGameItemBinding.edPlayer2Phone.setText(customer.gamer.getCustomerPhone());
                    gameItemViewModel.getIsPlayerTwoEnabled().set(false);
                    fragmentGameItemBinding.spinner.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setPlayTwoTextChangeListener() {
        fragmentGameItemBinding.edPlayer2Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPlayerTwoInputAction(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fragmentGameItemBinding.edPlayer2Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPlayerTwoInputAction(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setPlayerOneTextChangeListener() {
        fragmentGameItemBinding.edPlayer1Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPlayerOneInputAction(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        fragmentGameItemBinding.edPlayer1Phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPlayerOneInputAction(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onPlayerOneInputAction(CharSequence charSequence) {
        gameItemViewModel.updatePlayerSearchList(charSequence.toString().toLowerCase());
        if (charSequence.length()>0){
            boolean isListEmpty = gameItemViewModel.getIsSearchListEmpty().get();
            gameItemViewModel.getIsPlayerOneEnabled().set(!isListEmpty);
            fragmentGameItemBinding.player2layout.setVisibility(isListEmpty?View.VISIBLE:View.INVISIBLE);
            fragmentGameItemBinding.spinner.setVisibility(isListEmpty?View.VISIBLE:View.INVISIBLE);
        }else {
            gameItemViewModel.getIsPlayerOneEnabled().set(false);
            fragmentGameItemBinding.player2layout.setVisibility(View.VISIBLE);
            fragmentGameItemBinding.spinner.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onPlayerTwoInputAction(CharSequence charSequence) {
        gameItemViewModel.updatePlayerSearchList(charSequence.toString().toLowerCase());
        if (charSequence.length()>0){
            boolean isListEmpty = gameItemViewModel.getIsSearchListEmpty().get();
            gameItemViewModel.getIsPlayerTwoEnabled().set(!isListEmpty);
            fragmentGameItemBinding.spinner.setVisibility(isListEmpty?View.VISIBLE:View.INVISIBLE);
        }else {
            gameItemViewModel.getIsPlayerTwoEnabled().set(false);
            fragmentGameItemBinding.spinner.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        setGameTypeList();
        observeScreenData();
        observeButtonActionClicks();
        observeCustomerList();
        setPlayerOneTextChangeListener();
        setPlayTwoTextChangeListener();
        observePlayerSuggestListPick();
    }

    private void observeCustomerList() {
        gameItemViewModel.getGameRepository().getCustomerList().observe(getViewLifecycleOwner(), customers ->
                gameItemViewModel.setCustomerList(customers));
    }

    private void observeScreenData() {
        long screenId = ((GameActivity) getActivity()).screenId;
        gameItemViewModel.getGameRepository().getScreenById(screenId).observe(getViewLifecycleOwner(), gameView -> {
            gameItemViewModel.setGameTitleObservable(gameView);
        });
    }

    private void setGameTypeList() {
        gameItemViewModel.getGameRepository().getGamesLiveData()
                .observe(getViewLifecycleOwner(), gameTypes ->
                        gameItemViewModel.setGamesList(gameTypes));
    }

    private void observeButtonActionClicks() {
        gameItemViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case Constants.Events.START_GAME:
                    validateInputs();
                    break;
                case Constants.Events.END_GAME:
                    gameItemViewModel.endGameSession();
                    Toast.makeText(getActivity(), "added to Completed Games Successfully", Toast.LENGTH_SHORT).show();
                    goBack();
                    break;
                case Constants.Events.BACK_TO_GAME_COUNT:
                case GAME_STARTED:
                    goBack();
                    break;
                case MINUS_GAME_EVENT_ERROR:
                    Toast.makeText(getActivity(), "Cannot reduce active Game Count", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void goBack() {
        ((GameActivity) getActivity()).onBackPressed();
    }

    private void validateInputs() {
        String player1Name = Objects.requireNonNull(fragmentGameItemBinding.edPlayer1Name.getText()).toString().trim();
        String player1phone = Objects.requireNonNull(fragmentGameItemBinding.edPlayer1Phone.getText()).toString().trim();
        String player2Name = Objects.requireNonNull(fragmentGameItemBinding.edPlayer2Name.getText()).toString().trim();
        String player2Phone = Objects.requireNonNull(fragmentGameItemBinding.edPlayer2Phone.getText()).toString().trim();

        if (player1Name.isEmpty()) {
            fragmentGameItemBinding.edPlayer1Name.setError("Required");
        } else if (player2Name.isEmpty()) {
            fragmentGameItemBinding.edPlayer1Phone.setError("Required");
        } else if (player2Name.isEmpty()) {
            fragmentGameItemBinding.edPlayer2Name.setError("Required");
        } else if (player2Name.isEmpty()) {
            fragmentGameItemBinding.edPlayer2Phone.setError("Required");
        } else if (gameItemViewModel.getSelectedGameType() == null) {
            Toast.makeText(getActivity(), "Please Select Game to Proceed", Toast.LENGTH_SHORT).show();
        } else {
            if (player1phone.length() < 10) {
                fragmentGameItemBinding.edPlayer1Phone.setError("Invalid Phone");
            } else if (player1phone.length() < 10) {
                fragmentGameItemBinding.edPlayer2Phone.setError("Invalid Phone");
            } else {
                gamerModel = new GamerModel();
                gamerModel.player1Name = player1Name;
                gamerModel.player1Phone = player1phone;
                gamerModel.player2Name = player2Name;
                gamerModel.player2Phone = player2Phone;
                gameItemViewModel.updateGameData(gamerModel);
            }


        }
    }
}