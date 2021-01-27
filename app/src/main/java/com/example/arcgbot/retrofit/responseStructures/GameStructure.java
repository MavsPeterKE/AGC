package com.example.arcgbot.retrofit.responseStructures;

import com.google.gson.annotations.SerializedName;

public class GameStructure {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("pricing_mode")
    public String pricingMode;
    @SerializedName("unit_price")
    public Double unitPrice;
    @SerializedName("active")
    public Boolean active;
}
