package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "game_types_table")
public class GameType extends BaseObservable {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "game_name")
    private String gameName;

    @ColumnInfo(name = "charges")
    private double charges;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String screenId) {
        this.gameName = screenId;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }
}
