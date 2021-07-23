package com.example.arcgbot.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.arcgbot.R;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.fragment.FragmentGameItem;

import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {
    /*@BindView(R.id.btLogin)
    Button btLogin;*/
    public long screenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        ButterKnife.bind(this);
        screenId = getIntent().getLongExtra(Constants.IntentKeys.SCREEN_ID,0);
        createFragments(new FragmentGameItem());
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