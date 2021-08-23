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

    @Query("DELETE FROM game_count_table WHERE game_id=:gameId")
    public abstract int deleteCompletedGameById(long gameId);

    @Query("UPDATE game_count_table SET games_count=:count,games_bonus_count=:bonus WHERE game_id=:gameId")
    public abstract int updateGameCount(long gameId, int count,int bonus);

    @Query("SELECT * FROM game_count_table WHERE game_id=:id")
    public abstract  GameCount getGameCountById(long id);

    @Query("DELETE FROM game_count_table")
    public abstract void clearData();

    @Query("SELECT * FROM game_count_table WHERE game_id=:id")
    public abstract GameCount getGameById(long id);
}
