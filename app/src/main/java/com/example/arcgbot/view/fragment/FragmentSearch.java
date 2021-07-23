package com.example.arcgbot.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentSearchBinding;
import com.example.arcgbot.utils.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentSearch extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    FragmentSearchBinding fragmentGameCountBinding;

    public static FragmentSearch newInstance() {
        return new FragmentSearch();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentGameCountBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_search, container, false);
        //mViewModel = new ViewModelProvider(this, viewModelFactory).get(GameCountViewModel.class);
        //fragmentGameCountBinding.setModel(mViewModel);
        fragmentGameCountBinding.executePendingBindings();
        fragmentGameCountBinding.edSearchText.requestFocus();
       /* imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/
        return fragmentGameCountBinding.getRoot();
    }
}