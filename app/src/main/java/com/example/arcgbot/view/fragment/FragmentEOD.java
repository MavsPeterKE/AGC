package com.example.arcgbot.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentEodBinding;
import com.example.arcgbot.databinding.FragmentScreensBinding;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.example.arcgbot.viewmodels.ScreensViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentEOD extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private EODViewModel mViewModel;
    private FragmentEodBinding fragmentEodBinding;

    public static FragmentEOD newInstance() {
        return new FragmentEOD();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentEodBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_eod, container, false);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(EODViewModel.class);
        fragmentEodBinding.setModel(mViewModel);
        fragmentEodBinding.executePendingBindings();
        return fragmentEodBinding.getRoot();
    }

}