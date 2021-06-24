package com.example.arcgbot.utils;

import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.EndDayModel;
import com.example.arcgbot.models.GameModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;

public class FirebaseLogs {
    private DatabaseReference firebaseDatabaseReference;

    @Inject
    public FirebaseLogs() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    public void setGameLog(String date, String screenId, GameCount gameCount) {
        firebaseDatabaseReference.child("gamelogs")
                .child(date)
                .child(screenId+""+gameCount.getGameId())
                    .setValue(gameCount);
    }

    public void setGameLogList(String date, List<GameView> gameList) {
        firebaseDatabaseReference.child("gamelogs")
                .child(date)
                .setValue(gameList);
    }

    public void setAllGameList(String date, String tableName,List<CompletedGame> gameList) {
        firebaseDatabaseReference.child("gamelogs")
                .child(tableName)
                .child(date)
                //.child(screenId+""+gameCount.getGameId())
                .setValue(gameList);
    }

    public void setEndDayLog(String date, String tableName, EndDayModel endDayModel) {
        firebaseDatabaseReference.child("gamelogs")
                .child(tableName)
                .child(date)
                .setValue(endDayModel);
    }

}
