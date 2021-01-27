package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "game_count_table")
public class GameCount extends BaseObservable {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private String screenId;

    @ColumnInfo(name = "start_time")
    private String startTime;

    @ColumnInfo(name = "games_count")
    private int gamesCount;

    @ColumnInfo(name = "players_name")
    private String playerNames;

    @ColumnInfo(name = "player_phone")
    private String playerPhone;


    @ColumnInfo(name = "game_id")
    private String gameId;

    @NonNull
    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(@NonNull String screenId) {
        this.screenId = screenId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(int gamesCount) {
        this.gamesCount = gamesCount;
    }

    public String getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String playerNames) {
        this.playerNames = playerNames;
    }

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameType) {
        this.gameId = gameType;
    }
}
