package com.example.arcgbot.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgbot.R;
import com.example.arcgbot.databinding.FragmentCustomersBinding;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.ViewModelFactory;
import com.example.arcgbot.view.activity.ScreenActivity;
import com.example.arcgbot.viewmodels.CustomerViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.example.arcgbot.utils.Constants.Events.BACK_TO_SCREENS;
import static com.example.arcgbot.utils.Constants.Events.CALL_GAMER;
import static com.example.arcgbot.utils.Constants.Events.CUSTOMER_CLICK;
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
        fragmentCustomersBinding.edSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.searchGamer(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        init();
        return fragmentCustomersBinding.getRoot();
    }

    private void init() {
        observeCustomerList();
        observeClickEvents();
    }

    private void observeClickEvents() {
        mViewModel.getClickEventsLiveData().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case CALL_GAMER:
                    callUserAction();
                    break;
                case SEND_MESSAGE:
                    customerPhone = mViewModel.getPhoneNo();
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(Uri.parse("smsto:"+customerPhone));
                    startActivity(smsIntent);
                    break;
                case CUSTOMER_CLICK:
                   startCustomerDetailFragment();
                    break;
                case BACK_TO_SCREENS:
                    getActivity().onBackPressed();
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

    private void startCustomerDetailFragment(){
        customerPhone = mViewModel.getPhoneNo();
        Intent screenIntent = new Intent(getActivity(), ScreenActivity.class);
        screenIntent.putExtra(Constants.IntentKeys.DESTINATION_FRAGMENT, FragmentGamerDetails.class.getSimpleName());
        screenIntent.putExtra(Constants.IntentKeys.CUSTOMER_ID, mViewModel.getPhoneNo());
        startActivity(screenIntent);
    }

}