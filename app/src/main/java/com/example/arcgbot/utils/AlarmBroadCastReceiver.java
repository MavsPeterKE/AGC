package com.example.arcgbot.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show();
        new NotificationUtils(context).showNotification("Game Count","Check Screen 2");

    }
}
