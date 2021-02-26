package com.example.arcgbot.view.fragment;

import android.content.Intent;
import android.net.Uri;
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
import com.example.arcgbot.databinding.FragmentScreensBinding;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.ScreensViewModel;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentScreens extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private ScreensViewModel mViewModel;
    private FragmentScreensBinding fragmentScreensBinding;
    boolean isWhatsappException;

    public static FragmentScreens newInstance() {
        return new FragmentScreens();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentScreensBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_screens, container, false);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(ScreensViewModel.class);
        fragmentScreensBinding.setScreenviewmodel(mViewModel);
        fragmentScreensBinding.executePendingBindings();

        mViewModel.gameRepository.getCompletedGames().observe(getViewLifecycleOwner(), completedGames -> mViewModel.setGameCountdapter(completedGames));

        mViewModel.getSelectedScreenItem().observe(getViewLifecycleOwner(), new Observer<ScreenItem>() {
            @Override
            public void onChanged(ScreenItem screenItem) {
                if (screenItem!=null){
                    StringBuilder stringBuilder = getStringBuilder(screenItem);
                    try {
                        isWhatsappException = false;
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+254"+screenItem.phoneNumber.substring(0) + "&text=" + URLEncoder.encode(stringBuilder.toString(), "UTF-8");
                        sendMsg.setPackage("com.whatsapp");
                        sendMsg.setData(Uri.parse(url));
                        if (sendMsg.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(sendMsg);
                        }
                    } catch (Exception e) {
                        isWhatsappException = true;
                        e.printStackTrace();
                        Uri uri = Uri.parse("smsto:"+screenItem.phoneNumber);
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", stringBuilder.toString());
                        startActivity(it);
                    }

                }
            }
        });

        return fragmentScreensBinding.getRoot();
    }

    @NotNull
    private StringBuilder getStringBuilder(ScreenItem screenItem) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isWhatsappException?"Arcade Gaming Lounge":"*Arcade Gaming Lounge*");
        stringBuilder.append("\n");
        stringBuilder.append(isWhatsappException? "Please Pay: ":"*Please Pay: *"+screenItem.payableAmount);
        stringBuilder.append("\n");
        stringBuilder.append(isWhatsappException?"Games Played: ":"*Games Played: *"+screenItem.GameCount);
        stringBuilder.append("\n");
        stringBuilder.append("Welcome Again Thanks.");
        return stringBuilder;
    }

}