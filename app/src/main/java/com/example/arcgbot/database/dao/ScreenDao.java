package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.Screen;

import java.util.List;

@Dao
public abstract class ScreenDao extends BaseDao<Screen> {

    @Query("SELECT * FROM screen_table")
    public abstract LiveData<List<Screen>> getAllScreens();
}
