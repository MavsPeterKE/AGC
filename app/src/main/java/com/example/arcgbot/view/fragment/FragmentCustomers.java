package com.example.arcgbot.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentCustomersBinding;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.viewmodels.CustomerViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.example.arcgbot.utils.Constants.Events.CALL_GAMER;
import static com.example.arcgbot.utils.Constants.Events.SEND_MESSAGE;


public class FragmentCustomers extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private CustomerViewModel mViewModel;

    private String customerPhone;

    private FragmentCustomersBinding fragmentCustomersBinding;

    public static FragmentCustomers newInstance() {
        return new FragmentCustomers();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCustomersBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_customers, container, false);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(CustomerViewModel.class);
        fragmentCustomersBinding.setModel(mViewModel);
        fragmentCustomersBinding.executePendingBindings();
        observeCustomerList();
        observeClickEvents();


        return fragmentCustomersBinding.getRoot();
    }

    private void observeClickEvents() {
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case CALL_GAMER:
                    callUserAction();
                    break;
                case SEND_MESSAGE:
                    customerPhone = mViewModel.getPhoneNo();
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", customerPhone);
                    smsIntent.putExtra("sms_body", "");
                    startActivity(smsIntent);
                    break;
            }
        });
    }

    private void callUserAction() {
        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startCallIntent();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                    }
                }).check();
    }

    private void observeCustomerList() {
        mViewModel.gameRepository.getCustomerList().observe(getViewLifecycleOwner(), customers ->
                mViewModel.setCustomerList(customers));
    }

    private void startCallIntent() {
        customerPhone = mViewModel.getPhoneNo();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + customerPhone));
        startActivity(callIntent);
    }

}