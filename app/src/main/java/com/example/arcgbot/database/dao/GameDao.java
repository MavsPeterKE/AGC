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

    @Query("UPDATE game_types_table SET isSelected=0")
    public abstract void deselectAllGame();

    @Query("UPDATE game_types_table SET isSelected=:isSelected WHERE type_id=:id")
    public abstract void updateSelected(boolean isSelected,long id);


    @Query("UPDATE game_types_table SET isSelected=0")
    public abstract void deselectGames();
}
