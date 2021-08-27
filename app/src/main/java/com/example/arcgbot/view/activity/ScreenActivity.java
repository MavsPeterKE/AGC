package com.example.arcgbot.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.arcgbot.R;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.fragment.FragmentCompletedGameDetail;
import com.example.arcgbot.view.fragment.FragmentCompletedGameSearch;
import com.example.arcgbot.view.fragment.FragmentGameCount;
import com.example.arcgbot.view.fragment.FragmentGamerDetails;
import com.example.arcgbot.view.fragment.FragmentScreens;
import com.example.arcgbot.view.fragment.FragmentSearch;

import butterknife.ButterKnife;

public class ScreenActivity extends AppCompatActivity {
    public long screenId;
    public String customerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        screenId = getIntent().getLongExtra(Constants.IntentKeys.SCREEN_ID,0);
        customerPhone = getIntent().getStringExtra(Constants.IntentKeys.CUSTOMER_ID);
        String destination = getIntent().getStringExtra(Constants.IntentKeys.DESTINATION_FRAGMENT);

        if (destination.equals(FragmentCompletedGameDetail.class.getSimpleName())){
            createFragments(new FragmentCompletedGameDetail());
        }else if (destination.equals(FragmentGamerDetails.class.getSimpleName())){
            createFragments(new FragmentGamerDetails());
        }

    }

    public void createFragments(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null) {
            transaction.add(R.id.frame_container, fragment);
            transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}