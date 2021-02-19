package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "screen_table")
public class Screen extends BaseObservable {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "screen_lable")
    private String screenLable;

    @ColumnInfo(name = "active")
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenLable() {
        return screenLable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setScreenLable(String screenLable) {
        this.screenLable = screenLable;
    }
}
