package com.example.arcgbot.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.ActivityLoginBinding;
import com.example.arcgbot.models.LoginModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.Prefs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.LoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

import static com.example.arcgbot.utils.Constants.Events.CLOSE_ERROR_SHEET;
import static com.example.arcgbot.utils.Constants.Events.LOGIN;
import static com.example.arcgbot.utils.Constants.InputError.PASSWORD_ERROR;
import static com.example.arcgbot.utils.Constants.InputError.USERNAME_ERROR;
import static com.example.arcgbot.utils.Constants.InputError.VALID_LOGIN;

public class LoginActivity extends DaggerAppCompatActivity {
    @Inject
    ViewModelFactory viewModelFactory;
    LoginViewModel viewModel;
    ActivityLoginBinding activityLoginBinding;
    private BottomSheetBehavior sheetErrorBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        if (Prefs.getBoolean(Constants.PrefsKeys.LOGIN_SUCCESS)){
            if (Prefs.getString(Constants.PrefsKeys.CURRENT_DATE).equals(Utils.getTodayDate(Constants.DATE_FORMAT))){
                goToGameCountHome();
            }else {
                setUpLogin();
                viewModel.gameRepository.clearGameData();
            }

        }else {
            setUpLogin();
        }

    }

    private void setUpLogin() {
        activityLoginBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_login);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
        activityLoginBinding.setViewmodel(viewModel);
        viewModel.clickEventsLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String action) {
                switch (action){
                    case LOGIN:
                        viewModel.validateLoginInputs();
                        break;
                    case CLOSE_ERROR_SHEET:
                        showErrorBottomSheetAction();
                        break;
                }

            }
        });

        sheetErrorBehavior = BottomSheetBehavior.from(activityLoginBinding.errorBottomSheet.getRoot());
        sheetErrorBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@androidx.annotation.NonNull View bottomSheet, int newState) {
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
            public void onSlide(@androidx.annotation.NonNull View bottomSheet, float slideOffset) {

            }
        });

        observeLoginInput();
    }

    @NotNull
    private DisposableObserver<LoginModel> getLoginObserver() {
        return new DisposableObserver<LoginModel>() {
            @Override
            public void onNext(@NonNull LoginModel loginModel) {
                activityLoginBinding.progressBar.setVisibility(View.GONE);
                if (loginModel.message.equals(Constants.SUCCESS)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Prefs.putBoolean(Constants.PrefsKeys.LOGIN_SUCCESS,true);
                    Prefs.putString(Constants.PrefsKeys.CURRENT_DATE, Utils.getTodayDate(Constants.DATE_FORMAT));
                    goToGameCountHome();
                } else {
                    String title = loginModel.message.contains("resolve host") ? "Connection Error" :
                            "Login Credentials Error";
                    String msg = title.equals("Connection Error") ? "Check your internet connection" : "Wrong Username/Password";
                    setBottomSheetError(title, msg);
                    showErrorBottomSheetAction();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                viewModel.updateProgressValue(false);
                setBottomSheetError("Server Connection Error", e.getMessage());
                showErrorBottomSheetAction();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void goToGameCountHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    private void setBottomSheetError(String s, String message) {
        activityLoginBinding.errorBottomSheet.tvTitle.setText(s);
        activityLoginBinding.errorBottomSheet.tvErrorMsg.setText(message);
    }

    private void showErrorBottomSheetAction() {
        if (sheetErrorBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetErrorBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetErrorBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void observeLoginInput() {
        viewModel.loginInputObservable.observe(this, flag -> {
            if (flag != null) {
                if (flag.equals(USERNAME_ERROR)) {
                    activityLoginBinding.editTextTextPersonName.setError(USERNAME_ERROR);
                } else if (flag.equals(PASSWORD_ERROR)) {
                    activityLoginBinding.editTextTextPassword.setError(PASSWORD_ERROR);
                } else if (flag.equals(VALID_LOGIN)) {
                    viewModel.loginUser();
                    activityLoginBinding.progressBar.setVisibility(View.VISIBLE);
                    viewModel.getLoginObervable().subscribeWith(getLoginObserver());

                }
            }
        });
    }

}