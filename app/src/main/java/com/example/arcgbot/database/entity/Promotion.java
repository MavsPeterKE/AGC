package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "game_promotion")
public class Promotion extends BaseObservable {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "promo_game_id")
    private long id;

    @ColumnInfo(name = "happy_hour_discount")
    private double happyHourDiscount;

    @ColumnInfo(name = "loyalty_discount")
    private double loyaltyDiscount;

    @ColumnInfo(name = "spending_discount")
    private double spendingDiscount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getHappyHourDiscount() {
        return happyHourDiscount;
    }

    public void setHappyHourDiscount(double happyHourDiscount) {
        this.happyHourDiscount = happyHourDiscount;
    }

    public double getLoyaltyDiscount() {
        return loyaltyDiscount;
    }

    public void setLoyaltyDiscount(double loyaltyDiscount) {
        this.loyaltyDiscount = loyaltyDiscount;
    }

    public double getSpendingDiscount() {
        return spendingDiscount;
    }

    public void setSpendingDiscount(double spendingDiscount) {
        this.spendingDiscount = spendingDiscount;
    }
}
