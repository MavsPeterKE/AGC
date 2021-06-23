package com.example.arcgbot.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class FirebaseLogs {
    private DatabaseReference firebaseDatabaseReference;

    @Inject
    public FirebaseLogs() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

   /* public void setLog(String date, String screenId, T value) {
        firebaseDatabaseReference.child("logs")
                .child(date)
                .child(screenId)
                  *//*  .child(logTitle)
                    .setValue(logStructure);*//*
    }*/

}
