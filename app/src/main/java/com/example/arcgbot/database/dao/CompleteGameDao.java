package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.GameView;

import java.util.List;

@Dao
public abstract class CompleteGameDao extends BaseDao<CompletedGame> {
    @Query("SELECT * FROM complete_games_table")
    public abstract LiveData<List<CompletedGame>> getAllCompletedGames();

    @Query("SELECT sum(games_count) FROM  complete_games_table ")
    public abstract LiveData<Integer> getTotalGamesPlayed();

    @Query("SELECT sum(payable_amount) FROM  complete_games_table ")
    public abstract LiveData<Double> getTotalAmountPlayed();
}
