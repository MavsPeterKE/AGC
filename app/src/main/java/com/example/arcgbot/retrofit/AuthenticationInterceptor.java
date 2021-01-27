package com.example.arcgbot.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by elish on 10/23/2017.
 */

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;

    public AuthenticationInterceptor(String token) {
        Log.e("token", "token: " + token.toString());
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        Request.Builder builder = chain.request().newBuilder()
//                .header("Authorization", "");
        Request.Builder builder = chain.request().newBuilder()
                .header("Authorization", authToken);
//                .header("Authorization", "1234");
        return chain.proceed(builder.build());
    }
}