package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customer_table")
public class Customer extends BaseObservable {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private String customerPhone;

    @ColumnInfo(name = "customer_name")
    private String customerName;

    @ColumnInfo(name = "is_gaming_beast")
    private String isGamingBeast;

    @ColumnInfo(name = "loyalty_bonus")
    private boolean isLoyaltyBonusAwarded;

    @ColumnInfo(name = "loyalty_bonus_week")
    private int loyaltyBonusWeek;

    @ColumnInfo(name = "review_comment")
    private double reviewComment;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "customer_type")
    private String customerType;

    @ColumnInfo(name = "is_loyalty_discount_applicable")
    private boolean isLoyalFullTimeDiscountApplied;

    @ColumnInfo(name = "is_new_customer_discount_applied")
    private boolean isNewCustomerDiscountApplied;


    @NonNull
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(@NonNull String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIsGamingBeast() {
        return isGamingBeast;
    }

    public void setIsGamingBeast(String isGamingBeast) {
        this.isGamingBeast = isGamingBeast;
    }

    public boolean getLoyaltyBonus() {
        return isLoyaltyBonusAwarded;
    }

    public void setLoyaltyBonus(boolean loyaltyBonus) {
        this.isLoyaltyBonusAwarded = loyaltyBonus;
    }

    public double getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(double reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isLoyaltyBonusAwarded() {
        return isLoyaltyBonusAwarded;
    }

    public void setLoyaltyBonusAwarded(boolean loyaltyBonusAwarded) {
        isLoyaltyBonusAwarded = loyaltyBonusAwarded;
    }

    public int getLoyaltyBonusWeek() {
        return loyaltyBonusWeek;
    }

    public void setLoyaltyBonusWeek(int loyaltyBonusWeek) {
        this.loyaltyBonusWeek = loyaltyBonusWeek;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public boolean isLoyalFullTimeDiscountApplied() {
        return isLoyalFullTimeDiscountApplied;
    }

    public void setLoyalFullTimeDiscountApplied(boolean loyalFullTimeDiscountApplied) {
        isLoyalFullTimeDiscountApplied = loyalFullTimeDiscountApplied;
    }

    public boolean isNewCustomerDiscountApplied() {
        return isNewCustomerDiscountApplied;
    }

    public void setNewCustomerDiscountApplied(boolean newCustomerDiscountApplied) {
        isNewCustomerDiscountApplied = newCustomerDiscountApplied;
    }
}
