package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.GameView;

import java.util.List;

@Dao
public abstract class ScreenDao extends BaseDao<Screen> {

    @Transaction
    @Query("SELECT * FROM gameview")
    public abstract LiveData<List<GameView>> getAllScreens();

    @Transaction
    @Query("UPDATE screen_table SET active=1 WHERE id=:screenId")
    public abstract void updateActiveScreen(long screenId);

    @Transaction
    @Query("UPDATE screen_table SET active=0 WHERE id=:id")
    public abstract void resetActiveScreen(long id);
}
