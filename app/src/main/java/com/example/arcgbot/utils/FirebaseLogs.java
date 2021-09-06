package com.example.arcgbot.utils;

import android.text.format.DateFormat;

import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.Promotion;
import com.example.arcgbot.database.views.CustomerView;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.EndDayModel;
import com.example.arcgbot.models.GameModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class FirebaseLogs {
    private DatabaseReference firebaseDatabaseReference;

    @Inject
    public FirebaseLogs() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    public void setGameLogList(String tablename, List<GameView> gameList) {
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child(tablename)
                .setValue(gameList);
    }

    public void setAllGameList(String date, String tableName,List<CompletedGame> gameList) {
        Date dataTest = Utils.convertToDate(date,Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  dataTest); // Jun
        String year         = (String) DateFormat.format("yyyy", dataTest); // 2013
        String monthNumber  = (String) DateFormat.format("MM",   dataTest); // 06
        String dayOfTheWeek = (String) DateFormat.format("EEEE", dataTest); // Thursday
        String day          = (String) DateFormat.format("dd",   dataTest); // 20
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child(tableName)
                .child(monthString+"_"+year)
                .child(date)
                .setValue(gameList);
    }

    public void setEndDayLog(String date, String tableName, EndDayModel endDayModel) {
        Date dataTest = Utils.convertToDate(date,Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  dataTest); // Jun
        String year         = (String) DateFormat.format("yyyy", dataTest); // 2013
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child(tableName)
                .child(monthString+"_"+year)
                .child(date)
                .setValue(endDayModel);
    }

    public void setCustomerList(List<CustomerView> customerVisits){
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("all-game-customers")
                .setValue(customerVisits);
    }

    public void removeCustomerList(){
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("all-game-customers")
                .removeValue();
    }

    public void createFirebasePromotion(List<Promotion> promotionList) {
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("configs")
                .child("promotions")
                .setValue(promotionList);
    }
}
