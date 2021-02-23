package com.example.arcgbot.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentEodBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

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
        mViewModel.repository.getTotalRevenue().observe(getViewLifecycleOwner(), aDouble -> {
            mViewModel.gameRevenue.set(String.valueOf(aDouble != null ? aDouble : 0.00));
        });

        mViewModel.repository.getGameTotal().observe(getViewLifecycleOwner(), integer -> mViewModel.gameCount.set((integer != null ? integer : 0) + " Games"));
        fragmentEodBinding.button2.setOnClickListener(view -> {
            startEndOfDay();
        });

        return fragmentEodBinding.getRoot();
    }


    private void startEndOfDay() {
        StringBuilder stringBuilder = getMsgStringBuilder();
        // Performs action on click
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(sendIntent, ""));
            startActivity(sendIntent);
        }catch (Exception e){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "AGC End Day");
            startActivity(shareIntent);
        }

    }

    @NotNull
    private StringBuilder getMsgStringBuilder() {
        String issues = fragmentEodBinding.editTextTextMultiLine.getText().toString().trim();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*Arcade Gaming EOD*");
        stringBuilder.append("\n");
        stringBuilder.append("*Games Played:* " + fragmentEodBinding.tvGameCount.getText().toString().trim());
        stringBuilder.append("\n");
        stringBuilder.append("*Total Revenue:* " + fragmentEodBinding.tvGameRevenue.getText().toString().trim());
        stringBuilder.append("\n\n");
        stringBuilder.append("*Business Issue:* ");
        stringBuilder.append("\n");
        stringBuilder.append(issues);
       /* Matcher matcher = Utils.getRegexMatcher(Constants.MPESA_DEPOSIT_REGEX, issues);
        if (matcher.find()) {
            try {
                stringBuilder.append(matcher.group(1) + " -  " + matcher.group(5));
                stringBuilder.append("\n");
                stringBuilder.append("Deposit on :" + " - ");
                stringBuilder.append("\n");
                stringBuilder.append(matcher.group(3) + " " + matcher.group(4));
            } catch (Exception e) {
                stringBuilder.append(issues);
            }
        }*/
        return stringBuilder;
    }

}