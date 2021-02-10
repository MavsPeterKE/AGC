package com.example.arcgbot.utils;

import com.example.arcgbot.retrofit.responseStructures.APIListResponse;
import com.example.arcgbot.retrofit.responseStructures.APIResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class Utils {
    /**
     * Convert http response string to APIResponse Object using Gson
     *
     * @param e Throwable Thrown by Retrofit
     * @return
     */
    public static APIResponse getApiResponse(Throwable e) {
        APIResponse result = null;

        if (e instanceof com.jakewharton.retrofit2.adapter.rxjava2.HttpException) {
            ResponseBody body = ((com.jakewharton.retrofit2.adapter.rxjava2.HttpException) e).response().errorBody();
            try {
                Gson gson = new Gson();
                Type apiResponse = new TypeToken<APIResponse>() {
                }.getType();
                result = gson.fromJson(body.string(), apiResponse);
            } catch (IOException ex) {
                ex.printStackTrace();
                APIResponse apiResponse2 = new APIResponse();
                apiResponse2.strMessage = e.getMessage();
                if (e.getMessage() != null) {
                    apiResponse2.statusCode = e.getMessage().contains("401") ? 401 : 0;
                }
                result = apiResponse2;
            }

        } else {
            APIResponse apiResponse = new APIResponse();
            apiResponse.strMessage = e.getMessage();
            apiResponse.statusCode = 0;
            result = apiResponse;

        }

        return result;
    }

    /**
     * Convert http response string to APIListResponse Object using Gson
     *
     * @param e Throwable Thrown by Retrofit
     * @return
     */
    public static APIListResponse getApiListResponse(Throwable e) {
        APIListResponse result = null;

        if (e instanceof com.jakewharton.retrofit2.adapter.rxjava2.HttpException) {
            ResponseBody body = ((com.jakewharton.retrofit2.adapter.rxjava2.HttpException) e).response().errorBody();
            Gson gson = new Gson();
            Type apiResponse = new TypeToken<APIListResponse>() {
            }.getType();

            try {
                result = gson.fromJson(body.string(), apiResponse);
            } catch (IOException ex) {
                ex.printStackTrace();
                APIListResponse apiResponse2 = new APIListResponse();
                apiResponse2.strMessage = ex.getMessage();
                if (e.getMessage() != null) {
                    apiResponse2.statusCode = e.getMessage().contains("401") ? 401 : 0;
                }
                result = apiResponse2;
            }

        } else {
            APIListResponse apiResponse = new APIListResponse();
            apiResponse.strMessage = e.getMessage();
            apiResponse.statusCode = 0;
            result = apiResponse;

        }

        return result;
    }



}