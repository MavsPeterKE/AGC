package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.Screen;

import java.util.List;

@Dao
public abstract class GameCountDao extends BaseDao<GameCount> {

    @Query("SELECT * FROM game_count_table")
    public abstract LiveData<List<GameCount>> getAllGames();

    @Transaction
    @Query("DELETE FROM game_count_table WHERE game_type_id=:gameId")
    public abstract void updateCompletedGames(long gameId);
}
