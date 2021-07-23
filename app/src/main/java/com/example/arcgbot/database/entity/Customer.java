package com.example.arcgbot.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "customer_table")
public class Customer extends BaseObservable {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "customer_name")
    private String customerName;

    @ColumnInfo(name = "customer_phone")
    private String customerPhone;

    @ColumnInfo(name = "gender")
    private String gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
