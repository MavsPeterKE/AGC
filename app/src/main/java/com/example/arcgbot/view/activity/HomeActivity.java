package com.example.arcgbot.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;
import com.example.arcgbot.R;
import com.example.arcgbot.view.fragment.FragmentEOD;
import com.example.arcgbot.view.fragment.FragmentGameCount;
import com.example.arcgbot.view.fragment.FragmentScreens;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.bottom_bar)
    BottomNavigationBar bottomNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setBottomNavigation();
        // TODO: 1/23/21 Check if data is synced then call sync
        AppCenter.start(getApplication(), "1ba0e29d-98b2-4701-a1f3-6ecca5232bb3",
                Analytics.class, Crashes.class);
    }

    private void setBottomNavigation() {
        BottomBarItem gameCountItem = new BottomBarItem(R.drawable.home_icon, R.string.title_home);
        bottomNavigationBar.addTab(gameCountItem);
        BottomBarItem screenItem = new BottomBarItem(R.drawable.screen_icon, R.string.title_screen);
        bottomNavigationBar.addTab(screenItem);
        /*BottomBarItem gamersData = new BottomBarItem(R.drawable.games, R.string.title_gamers);
        bottomNavigationBar.addTab(gamersData);*/
        BottomBarItem eodItem = new BottomBarItem(R.drawable.report, R.string.title_eod);
        bottomNavigationBar.addTab(eodItem);

        //Create Default Home View
       startGameCountView();

        //Bottom Navigation Action
        bottomNavigationBar.setOnSelectListener(position -> {
            switch (position) {
                case 0:
                   startGameCountView();
                    break;
                case 1:
                    startScreens();
                    break;
                case 2:
                    startEODFragment();
                    break;
            /*    case 3:
                    startEODFragment();
                    break;*/
            }
        });
    }

    private void startGamersScreen() {
    }

    private void startEODFragment() {
        changeFragment(new FragmentEOD(),FragmentEOD.class.getSimpleName());
    }


    private void startGameCountView() {
        changeFragment(new FragmentGameCount(),FragmentGameCount.class.getSimpleName());
    }

    private void startScreens() {
        changeFragment(new FragmentScreens(),FragmentScreens.class.getSimpleName());
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.frame_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


}