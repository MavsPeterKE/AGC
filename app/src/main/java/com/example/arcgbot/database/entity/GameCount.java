package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "game_count_table")
public class GameCount extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    private long gameId;

    @ColumnInfo(name = "start_time")
    private String startTime;

    @ColumnInfo(name = "stop_time")
    private String stopTime;

    @ColumnInfo(name = "start_time_minutes")
    private int startTimeMinutes;

    @ColumnInfo(name = "games_count")
    private int gamesCount;

    @ColumnInfo(name = "players_name")
    private String playerNames;

    @ColumnInfo(name = "player_phone")
    private String playerPhone;

    @ColumnInfo(name = "screen_id")
    private long screenId;

    @ColumnInfo(name = "game_type_id")
    private long gameTypeId;

    @ColumnInfo(name = "payable_amount")
    private double payableAmount;

    @ColumnInfo(name = "is_game_completed")
    boolean isGameCompleted;

    @NonNull
    public long getScreenId() {
        return screenId;
    }

    public void setScreenId(long screenId) {
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

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(long gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getStartTimeMinutes() {
        return startTimeMinutes;
    }

    public void setStartTimeMinutes(int startTimeMinutes) {
        this.startTimeMinutes = startTimeMinutes;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public boolean isGameCompleted() {
        return isGameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        isGameCompleted = gameCompleted;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }
}
