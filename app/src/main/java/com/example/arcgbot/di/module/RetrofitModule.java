package com.example.arcgbot.di.module;

import android.text.TextUtils;
import android.util.Log;

import com.example.arcgbot.retrofit.AuthenticationInterceptor;
import com.example.arcgbot.retrofit.RetrofitService;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.Prefs;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    @Singleton
    @Provides
    static Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    static RetrofitService provideRetrofitApi(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        /*if (uildConfig.DEBUGB) {

        }*/

        //Add Access Token if not empty
        String accessToken = Prefs.getString(Constants.PrefsKeys.ACCESS_TOKEN);
        if (!TextUtils.isEmpty(accessToken) || !accessToken.equals("")) {
            AuthenticationInterceptor authenticationInterceptor =
                    new AuthenticationInterceptor(accessToken);
            okHttpClientBuilder.addInterceptor(authenticationInterceptor);
        }


        ConnectionPool connectionPool = new ConnectionPool();

        okHttpClientBuilder.addInterceptor(interceptor);

        okHttpClientBuilder.addInterceptor(chain -> {
            Log.e("addInterceptor", "addInterceptor");
            Request original = chain.request();
            Request request = original.newBuilder()
                   /* .header("Connection", "close")
                    .header("X-APP-ID", BuildVariant.getErpAppID())
                    .header("X-APP-TOKEN", appToken)*/
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        //Okhttp Client
        okHttpClientBuilder.connectionPool(connectionPool)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor);

        return okHttpClientBuilder.build();
    }
}
