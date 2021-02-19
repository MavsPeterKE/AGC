package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;

import java.util.List;

@Dao
public abstract class GameDao extends BaseDao<GameType> {

    @Query("SELECT * FROM game_types_table")
    public abstract LiveData<List<GameType>> getGameTypes();
}
