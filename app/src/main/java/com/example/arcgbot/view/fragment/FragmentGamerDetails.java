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
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.databinding.FragmentGameItemBinding;
import com.example.arcgbot.databinding.FragmentGamerDetailBinding;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.GameActivity;
import com.example.arcgbot.view.activity.ScreenActivity;
import com.example.arcgbot.viewmodels.CustomerViewModel;
import com.example.arcgbot.viewmodels.GameItemViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentGamerDetails extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private CustomerViewModel customerViewModel;
    private FragmentGamerDetailBinding fragmentGamerDetailBinding;

    public static FragmentGamerDetails newInstance() {
        return new FragmentGamerDetails();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentGamerDetailBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_gamer_detail, container, false);
        customerViewModel = new ViewModelProvider(this, viewModelFactory).get(CustomerViewModel.class);
        fragmentGamerDetailBinding.setModel(customerViewModel);
        fragmentGamerDetailBinding.executePendingBindings();
        observeCustomerVisitData();
        return fragmentGamerDetailBinding.getRoot();
    }

    private void observeCustomerVisitData(){
        String customerPhone = ((ScreenActivity) getActivity()).customerPhone;
        customerViewModel.getThisWeekCustomerVisitsByPhone(customerPhone).observe(getViewLifecycleOwner(), customerVisitList -> {

        });
    }


}