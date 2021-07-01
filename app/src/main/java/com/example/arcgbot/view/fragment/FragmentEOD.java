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
import com.example.arcgbot.models.EndDayModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
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

    private FirebaseLogs firebaseLogs;

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

        firebaseLogs = new FirebaseLogs();

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
        String time = Utils.getTodayDate(" "+Constants.GENERIC_DATE_TIME_FORMAT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*Arcade Gaming EOD*");
        stringBuilder.append("\n");
        stringBuilder.append(time);
        stringBuilder.append("\n");
        stringBuilder.append("*Games Played:* " + fragmentEodBinding.tvGameCount.getText().toString().trim());
        stringBuilder.append("\n");
        stringBuilder.append("*Total Revenue:* " + fragmentEodBinding.tvGameRevenue.getText().toString().trim());
        stringBuilder.append("\n\n");
        stringBuilder.append("*Business Issue:* ");
        stringBuilder.append("\n");
        stringBuilder.append(issues);


        //Added
        String happyHour = fragmentEodBinding.edHappyHour.getText().toString().trim();
        String moviesAmount = fragmentEodBinding.edMovies.getText().toString().trim();
        String printingAmount = fragmentEodBinding.edPrintingAmount.getText().toString().trim();
        String otherAmount = fragmentEodBinding.edOtherAmount.getText().toString().trim();
        String normalGamingSalesRate = fragmentEodBinding.tvGameRevenue.getText().toString().trim().substring(5);
        double happyHourSale = !happyHour.isEmpty()?Double.parseDouble(happyHour):0.00;
        double moviesSaleAmount = !moviesAmount.isEmpty()?Double.parseDouble(moviesAmount):0.00;
        double printingSaleAmount = !printingAmount.isEmpty()?Double.parseDouble(printingAmount):0.00;
        double otherSaleAmount =  !otherAmount.isEmpty()?Double.parseDouble(otherAmount):0.00;
        double normalGamingSalesAmount = !normalGamingSalesRate.isEmpty()?Double.parseDouble(normalGamingSalesRate):0.00;
        double totals = happyHourSale+moviesSaleAmount+printingSaleAmount+otherSaleAmount+normalGamingSalesAmount;

        //Format Issues;
        StringBuilder issuesString = new StringBuilder();
        issuesString.append("Happy Hour Gaming Sales: "+happyHour);
        issuesString.append("\n");
        issuesString.append("Normal Rate Gaming Sales: "+normalGamingSalesAmount);
        issuesString.append("\n");
        issuesString.append("Movies  Sales: "+moviesAmount);
        issuesString.append("\n");
        issuesString.append("Printing  Sales: "+printingAmount);
        issuesString.append("\n");
        issuesString.append("Others  Sales: "+otherAmount);
        issuesString.append("\n");
        issuesString.append("\n");
        issuesString.append("Business Issue");
        issuesString.append("\n");
        issuesString.append(issues);

        EndDayModel endDayModel = new EndDayModel();
        endDayModel.endOfDayTime = time;
        endDayModel.happyHourAmount = happyHourSale;
        endDayModel.moviesAmount = moviesSaleAmount;
        endDayModel.otherSales = otherSaleAmount;
        endDayModel.printingSales = normalGamingSalesAmount;
        endDayModel.normalGamingRateSales = printingSaleAmount;
        endDayModel.issues = issuesString.toString();
        endDayModel.date = new Date().getTime();
        endDayModel.totalGamesPlayed = fragmentEodBinding.tvGameCount.getText().toString().trim();
        endDayModel.totalSales = totals +"";

        firebaseLogs.setEndDayLog(Utils.getTodayDate(Constants.DATE_FORMAT),"-all-end-days",endDayModel);
        return stringBuilder;
    }

}