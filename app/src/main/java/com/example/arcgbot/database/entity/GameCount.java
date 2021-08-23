package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "game_count_table" , indices ={@Index(value = {"screen_id"}, unique = true)})
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

    @ColumnInfo(name = "games_bonus_count")
    private int gamesBonus;

    @ColumnInfo(name = "happy_hour_bonus_count")
    private int happyHourBonusCount;

    @ColumnInfo(name = "happy_hour_game_count")
    private int happyHourGameCount;

    @ColumnInfo(name = "players_name")
    private String playerNames;

    @ColumnInfo(name = "player_one_id")
    private String player1Id;

    @ColumnInfo(name = "player_two_id")
    private String player2Id;

    @ColumnInfo(name = "player_phone")
    private String playerPhone;

    @ColumnInfo(name = "screen_id")
    private long screenId;

    @ColumnInfo(name = "game_type_id")
    private long gameTypeId;

    @ColumnInfo(name = "payable_amount")
    private double payableAmount;

    @ColumnInfo(name = "happy_hour_amount")
    private double happyHourAmount;

    @ColumnInfo(name = "normal_game_rate_amount")
    private double normalGamingRateAmount;

    @ColumnInfo(name = "happy_hour_bonus_amount")
    private double happyHourBonusAmount;

    @ColumnInfo(name = "normal_game_rate_bonus_amount")
    private double normalGamingRateBonusAmount;

    @ColumnInfo(name = "hash_key")
    private String hashKey;

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

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public int getGamesBonus() {
        return gamesBonus;
    }

    public void setGamesBonus(int gamesBonus) {
        this.gamesBonus = gamesBonus;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(String player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }

    public double getHappyHourAmount() {
        return happyHourAmount;
    }

    public void setHappyHourAmount(double happyHourAmount) {
        this.happyHourAmount = happyHourAmount;
    }

    public double getNormalGamingRateAmount() {
        return normalGamingRateAmount;
    }

    public void setNormalGamingRateAmount(double normalGamingRateAmount) {
        this.normalGamingRateAmount = normalGamingRateAmount;
    }

    public double getHappyHourBonusAmount() {
        return happyHourBonusAmount;
    }

    public void setHappyHourBonusAmount(double happyHourBonusAmount) {
        this.happyHourBonusAmount = happyHourBonusAmount;
    }

    public double getNormalGamingRateBonusAmount() {
        return normalGamingRateBonusAmount;
    }

    public void setNormalGamingRateBonusAmount(double normalGamingRateBonusAmount) {
        this.normalGamingRateBonusAmount = normalGamingRateBonusAmount;
    }

    public int getHappyHourBonusCount() {
        return happyHourBonusCount;
    }

    public void setHappyHourBonusCount(int happyHourBonusCount) {
        this.happyHourBonusCount = happyHourBonusCount;
    }

    public int getHappyHourGameCount() {
        return happyHourGameCount;
    }

    public void setHappyHourGameCount(int happyHourGameCount) {
        this.happyHourGameCount = happyHourGameCount;
    }
}
