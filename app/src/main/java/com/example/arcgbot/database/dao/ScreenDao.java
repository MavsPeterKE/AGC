package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.GameView;

import java.util.List;

@Dao
public abstract class ScreenDao extends BaseDao<Screen> {

    @Transaction
    @Query("SELECT * FROM gameview")
    public abstract LiveData<List<GameView>> getAllScreens();

    @Transaction
    @Query("SELECT * FROM gameview WHERE id=:screenId")
    public abstract LiveData<GameView> getScreenById(long screenId);

    @Transaction
    @Query("UPDATE screen_table SET active=1 WHERE id=:screenId")
    public abstract void updateActiveScreen(long screenId);

    @Transaction
    @Query("UPDATE screen_table SET active=0 WHERE id=:id")
    public abstract void resetActiveScreen(long id);

    @Transaction
    @Query("SELECT * FROM gameview WHERE game_id=:gameId")
    public abstract GameView getGameViewById(long gameId);

    @Query("UPDATE screen_table SET active=0")
    public abstract void resetAllScreens();

    @Query("SELECT COUNT(*) FROM screen_table WHERE active=1")
    public abstract int getActiveScreenCount();
}
