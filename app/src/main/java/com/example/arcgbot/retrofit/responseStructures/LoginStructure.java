package com.example.arcgbot.retrofit.responseStructures;

import com.google.gson.annotations.SerializedName;

public class LoginStructure {

    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("expires_in")
    public Integer expiresIn;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("scope")
    public String scope;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("error")
    public String error;
    @SerializedName("error_description")
    public String errorDesc;


}
