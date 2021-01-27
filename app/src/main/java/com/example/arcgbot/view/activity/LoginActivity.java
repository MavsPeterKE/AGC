package com.example.arcgbot.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;
import com.example.arcgbot.R;
import com.example.arcgbot.databinding.ActivityLoginBinding;
import com.example.arcgbot.models.LoginModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.LoginViewModel;
import com.example.arcgbot.viewmodels.ScreensViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

import static com.example.arcgbot.utils.Constants.InputError.PASSWORD_ERROR;
import static com.example.arcgbot.utils.Constants.InputError.USERNAME_ERROR;
import static com.example.arcgbot.utils.Constants.InputError.VALID_LOGIN;

public class LoginActivity extends DaggerAppCompatActivity {
    @Inject
    ViewModelFactory viewModelFactory;
    LoginViewModel viewModel;
    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding =  DataBindingUtil.setContentView(this,
                R.layout.activity_login);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
        activityLoginBinding.setViewmodel(viewModel);
        init();

    }

    private void init() {
        viewModel.clickEventsLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String action) {
                if (action.equals(Constants.Events.LOGIN)){
                    viewModel.validateLoginInputs();
                }
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
                if (loginModel.message.equals(Constants.SUCCESS)){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                viewModel.updateProgressValue(false);
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
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