package com.example.arcgbot.database.entity;

import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "customer_visit_table",foreignKeys = {@ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "customer_id")})
public class CustomerVisit extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "customer_id")
    private String customer_phone;

    @ColumnInfo(name = "week_number")
    private int week;

    @ColumnInfo(name = "games_played")
    private int totalGamesPlayed;

    @ColumnInfo(name = "spent_amount")
    private double amountPaidToShop;

    @ColumnInfo(name = "happy_hour_amount")
    private double happyHourAmount;

    @ColumnInfo(name = "normal_gaming_rate_Amount")
    private double normalGamingRateAmount;

    @ColumnInfo(name = "month")
    private String month;

    @ColumnInfo(name = "date")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public double getAmountPaidToShop() {
        return amountPaidToShop;
    }

    public void setAmountPaidToShop(double amountPaidToShop) {
        this.amountPaidToShop = amountPaidToShop;
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
}
