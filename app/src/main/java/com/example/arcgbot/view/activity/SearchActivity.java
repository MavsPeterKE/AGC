package com.example.arcgbot.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.arcgbot.R;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.fragment.FragmentCompletedGameSearch;
import com.example.arcgbot.view.fragment.FragmentGameCount;
import com.example.arcgbot.view.fragment.FragmentGameItem;
import com.example.arcgbot.view.fragment.FragmentScreens;
import com.example.arcgbot.view.fragment.FragmentSearch;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    public long screenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        String origin = getIntent().getStringExtra(Constants.IntentKeys.ORIGIN_FRAGMENT);
        if (origin.equals(FragmentGameCount.class.getSimpleName())){
            createFragments(new FragmentSearch());
        }else if (origin.equals(FragmentScreens.class.getSimpleName())){
            createFragments(new FragmentCompletedGameSearch());
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