package com.example.arcgbot.retrofit;


import com.example.arcgbot.retrofit.responseStructures.APIListResponse;
import com.example.arcgbot.retrofit.responseStructures.APIResponse;
import com.example.arcgbot.retrofit.responseStructures.GameStructure;
import com.example.arcgbot.retrofit.responseStructures.LoginStructure;
import com.example.arcgbot.retrofit.responseStructures.ScreenStructure;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {

    @GET("api/v1/screens/")
    Single<APIListResponse<ScreenStructure>> getScreens();

    @GET("api/v1/games/")
    Single<APIListResponse<GameStructure>> getGames();

    @POST("o/token/")
    Single<LoginStructure> login(@Body HashMap<String,Object> params);

}
