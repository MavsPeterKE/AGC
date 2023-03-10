package com.example.arcgbot.view.fragment;

import static com.example.arcgbot.utils.Constants.Events.CLOSE_ERROR_SHEET;
import static com.example.arcgbot.utils.Constants.Events.CLOSE_SUCCESS_SHEET;
import static com.example.arcgbot.utils.Constants.Events.POST_END_DAY;

import android.content.Intent;
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
import com.example.arcgbot.databinding.FragmentEodBinding;
import com.example.arcgbot.models.EndDayModel;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Prefs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.EODViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentEOD extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private EODViewModel mViewModel;
    private FragmentEodBinding fragmentEodBinding;
    private BottomSheetBehavior sheetErrorBehavior;
    private BottomSheetBehavior sheetSuccessBehavior;
    private Intent sendIntent = new Intent();
    private boolean isWhatsappException = false;

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

        mViewModel.repository.getGameTotal().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                int games = count != null ? count : 0;
                mViewModel.gameCount.set(games + " Games");
            }
        });

        fragmentEodBinding.btEndDay.setOnClickListener(view -> {
            startEndOfDay();
        });

        firebaseLogs = new FirebaseLogs();
        initBottomSheet();
        observeButtonEvents();
        return fragmentEodBinding.getRoot();
    }

    private void observeButtonEvents() {
        mViewModel.getGameItemClickLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case CLOSE_ERROR_SHEET:
                    showErrorBottomSheetAction();
                    break;
                case CLOSE_SUCCESS_SHEET:
                    showSuccessBottomSheetAction();
                    break;
                case POST_END_DAY:
                    if (isWhatsappException) {
                        startActivity(Intent.createChooser(sendIntent, "AGC Summary"));
                        startActivity(sendIntent);
                    } else {
                        Intent shareIntent = Intent.createChooser(sendIntent, "AGC End Day");
                        startActivity(shareIntent);
                    }
                    break;
            }
        });
    }

    private void initBottomSheet() {
        sheetErrorBehavior = BottomSheetBehavior.from(fragmentEodBinding.errorBottomSheet.getRoot());
        sheetErrorBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

        sheetSuccessBehavior = BottomSheetBehavior.from(fragmentEodBinding.successBottomSheet.getRoot());
        sheetSuccessBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
    }

    private void setBottomSheetError(String s, String message) {
        fragmentEodBinding.errorBottomSheet.tvTitle.setText(s);
        fragmentEodBinding.errorBottomSheet.tvErrorMsg.setText(message);
    }

    private void setBottomSheetSuccess(String title, String message) {
        fragmentEodBinding.successBottomSheet.tvTitle.setText(title);
        fragmentEodBinding.successBottomSheet.tvErrorMsg.setText(message);
    }

    private void showErrorBottomSheetAction() {
        if (sheetErrorBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetErrorBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetErrorBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void showSuccessBottomSheetAction() {
        if (sheetSuccessBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetSuccessBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetSuccessBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    private void startEndOfDay() {
        if (mViewModel.isAnyScreenActive()) {
            setBottomSheetError("Active Game", "End any active game before you can end day");
            showErrorBottomSheetAction();
        } else {
            String required_token_val = fragmentEodBinding.edTokens.getText().toString().trim();
            if (required_token_val.equals("")) {
                setBottomSheetError("Missing EOD Token Reading", "End of Day Token Reading Must be entered");
                showErrorBottomSheetAction();
            } else {
                StringBuilder stringBuilder = getMsgStringBuilder();
                // Performs action on click
                try {
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    showSuccessDialog();
                } catch (Exception e) {
                    isWhatsappException = true;
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                    sendIntent.setType("text/plain");
                    showSuccessDialog();
                }
            }

        }


    }

    private void showSuccessDialog() {
        setBottomSheetSuccess("Sales End Day", "Sales Summary Posted Successfully");
        showSuccessBottomSheetAction();
    }

    @NotNull
    private StringBuilder getMsgStringBuilder() {
        String issues = fragmentEodBinding.editTextTextMultiLine.getText().toString().trim();
        String token = fragmentEodBinding.edTokens.getText().toString().trim();
        double end_day_token_value = token.equals("") ? 0 : Double.parseDouble(token);
        String start_day_token_value = Prefs.getString(Constants.PrefsKeys.END_DAY_TOKEN);
        double start_day_token = start_day_token_value.equals("") ? 0 : Double.parseDouble(start_day_token_value);
        double token_consume = Math.round((end_day_token_value - start_day_token) * 100.0) / 100.0;
        String time = Utils.getTodayDate(" " + Constants.GENERIC_DATE_TIME_FORMAT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*Arcade Gaming EOD*");
        stringBuilder.append("\n");
        stringBuilder.append(time);
        stringBuilder.append("\n\n");
        stringBuilder.append("*Games Played:* " + fragmentEodBinding.tvGameCount.getText().toString().trim());
        stringBuilder.append("\n");
        stringBuilder.append("*Total Revenue:* " + fragmentEodBinding.tvGameRevenue.getText().toString().trim());
        stringBuilder.append("\n\n");
        stringBuilder.append("*Token Consumption:* ");
        stringBuilder.append("\n");
        stringBuilder.append(" Start: " + start_day_token_value + " End: " + end_day_token_value + " --> " + token_consume);
        stringBuilder.append("\n\n");
        stringBuilder.append("*Business Issue:* ");
        stringBuilder.append("\n");
        stringBuilder.append(issues);


        //Added
        String moviesAmount = fragmentEodBinding.edMovies.getText().toString().trim();
        String printingAmount = fragmentEodBinding.edPrintingAmount.getText().toString().trim();
        String psSalesTotals = fragmentEodBinding.edPsHireAmount.getText().toString().trim();
        String otherServicesAmount = fragmentEodBinding.edOtherSalesAmount.getText().toString().trim();
        String normalGamingSalesRate = fragmentEodBinding.tvGameRevenue.getText().toString().trim().substring(5);
        double moviesSaleAmount = !moviesAmount.isEmpty() ? Double.parseDouble(moviesAmount) : 0.00;
        double printingSaleAmount = !printingAmount.isEmpty() ? Double.parseDouble(printingAmount) : 0.00;
        double psHireSalesAmount = !psSalesTotals.isEmpty() ? Double.parseDouble(psSalesTotals) : 0.00;
        double otherServicesSalesAmount = !otherServicesAmount.isEmpty() ? Double.parseDouble(otherServicesAmount) : 0.00;
        double normalGamingSalesAmount = !normalGamingSalesRate.isEmpty() ? Double.parseDouble(normalGamingSalesRate) : 0.00;
        double totals = printingSaleAmount + psHireSalesAmount + normalGamingSalesAmount;


        //Format Issues;
        StringBuilder issuesString = new StringBuilder();
        issuesString.append("Gaming Sales: " + normalGamingSalesRate);
        issuesString.append("\n");
//        issuesString.append("Movies Total Sales: " + moviesAmount);
//        issuesString.append("\n");
        issuesString.append("Printing Sales: " + printingAmount);
        issuesString.append("\n");
        issuesString.append("PS Hire Sales: " + psSalesTotals);
        issuesString.append("\n");
        issuesString.append("Software Service Sales: " + otherServicesSalesAmount);
        issuesString.append("\n");
        issuesString.append("\n");
        issuesString.append("Tokens Usage:  " + token_consume);
        issuesString.append("\n");
        issuesString.append("\n");
        issuesString.append("Business Issue");
        issuesString.append("\n");
        issuesString.append(issues);

        EndDayModel endDayModel = new EndDayModel();
        endDayModel.endOfDayTime = time;
        endDayModel.happyHourAmount = 0;
        endDayModel.moviesAmount = moviesSaleAmount;
        endDayModel.otherSales = otherServicesSalesAmount;
        endDayModel.psHireAmount = psHireSalesAmount;
        endDayModel.printingSales = printingSaleAmount;
        endDayModel.endDayTokens = token_consume;
        endDayModel.normalGamingRateSales = normalGamingSalesAmount;
        endDayModel.issues = issuesString.toString();
        endDayModel.date = new Date().getTime();
        endDayModel.totalGamesPlayed = fragmentEodBinding.tvGameCount.getText().toString().trim();
        endDayModel.totalSales = totals + "";
        Prefs.putString(Constants.PrefsKeys.END_DAY_TOKEN, end_day_token_value + "");
        firebaseLogs.setEndDayLog(Utils.getTodayDate(Constants.DATE_FORMAT), "-all-end-days", endDayModel);
        return stringBuilder;
    }

}