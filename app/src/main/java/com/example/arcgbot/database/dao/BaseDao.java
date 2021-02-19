package com.example.arcgbot.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(List<T> t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(T t);

    @Delete
    public abstract int delete(T t);

    @Update
    public abstract int update(T t);
}
