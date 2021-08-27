package com.example.arcgbot.database.entity;

import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "complete_games_table")
public class CompletedGame extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "screen_lable")
    private String screenLable;

    @ColumnInfo(name = "game_type")
    private String gameType;

    @ColumnInfo(name = "start_time")
    private String startTime;

    @ColumnInfo(name = "stop_time")
    private String stopTime;

    @ColumnInfo(name = "duration")
    private String duration;

    @ColumnInfo(name = "games_count")
    private int gamesCount;

    @ColumnInfo(name = "end_time_seconds")
    private int endTimeSeconds;

    @ColumnInfo(name = "payable_amount")
    private double payableAmount;

    @ColumnInfo(name = "player_phone")
    private String playerPhone;

    @ColumnInfo(name = "player_one_id")
    private String player1Id;

    @ColumnInfo(name = "player_two_id")
    private String player2Id;

    @ColumnInfo(name = "bonus_amount")
    private double bonusAmount;

    @ColumnInfo(name = "normal_games_count")
    private int normalGamesCount;

    @ColumnInfo(name = "normal_gaming_bonus")
    private int normalGamingBonus;

    @ColumnInfo(name = "normal_games_bonus_amount")
    private double normalGamesBonusAmount;

    @ColumnInfo(name = "normal_game_rate_amount")
    private double normalGamingRateAmount;

    @ColumnInfo(name = "happy_hour_games_count")
    private int happyHourGamesCount;

    @ColumnInfo(name = "happy_hour_games_bonus")
    private int happyHourGamesBonus;

    @ColumnInfo(name = "happy_hour_bonus_amount")
    private double happyHourBonusAmount;

    @ColumnInfo(name = "happy_hour_amount")
    private double happyHourAmount;

    @ColumnInfo(name = "loyalty_bonus_amount")
    private double loyaltyBonusAmount;

    @ColumnInfo(name = "players_name")
    private String playerNames;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenLable() {
        return screenLable;
    }

    public void setScreenLable(String screenLable) {
        this.screenLable = screenLable;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(int gamesCount) {
        this.gamesCount = gamesCount;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getEndTimeSeconds() {
        return endTimeSeconds;
    }

    public void setEndTimeSeconds(int endTimeSeconds) {
        this.endTimeSeconds = endTimeSeconds;
    }

    public double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public double getLoyaltyBonusAmount() {
        return loyaltyBonusAmount;
    }

    public void setLoyaltyBonusAmount(double loyaltyBonusAmount) {
        this.loyaltyBonusAmount = loyaltyBonusAmount;
    }

    public double getHappyHourBonusAmount() {
        return happyHourBonusAmount;
    }

    public void setHappyHourBonusAmount(double happyHourBonusAmount) {
        this.happyHourBonusAmount = happyHourBonusAmount;
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

    public int getNormalGamingBonus() {
        return normalGamingBonus;
    }

    public void setNormalGamingBonus(int normalGamingBonus) {
        this.normalGamingBonus = normalGamingBonus;
    }

    public double getNormalGamingRateAmount() {
        return normalGamingRateAmount;
    }

    public void setNormalGamingRateAmount(double normalGamingRateAmount) {
        this.normalGamingRateAmount = normalGamingRateAmount;
    }

    public double getHappyHourAmount() {
        return happyHourAmount;
    }

    public void setHappyHourAmount(double happyHourAmount) {
        this.happyHourAmount = happyHourAmount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String playerNames) {
        this.playerNames = playerNames;
    }

    public int getNormalGamesCount() {
        return normalGamesCount;
    }

    public void setNormalGamesCount(int normalGamesCount) {
        this.normalGamesCount = normalGamesCount;
    }

    public int getHappyHourGamesCount() {
        return happyHourGamesCount;
    }

    public void setHappyHourGamesCount(int happyHourGamesCount) {
        this.happyHourGamesCount = happyHourGamesCount;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public double getNormalGamesBonusAmount() {
        return normalGamesBonusAmount;
    }

    public void setNormalGamesBonusAmount(double normalGamesBonusAmount) {
        this.normalGamesBonusAmount = normalGamesBonusAmount;
    }

    public int getHappyHourGamesBonus() {
        return happyHourGamesBonus;
    }

    public void setHappyHourGamesBonus(int happyHourGamesBonus) {
        this.happyHourGamesBonus = happyHourGamesBonus;
    }
}
