package com.example.arcgbot.retrofit.responseStructures;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elish on 9/29/2017.
 */

public class APIResponse<T> extends BaseModel {
    @SerializedName("data")
    public T data = (T) new Object();

    @SerializedName("message")
    public Object strMessage = "";

}