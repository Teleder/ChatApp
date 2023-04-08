package com.example.chatapp.Retrofit;

import static com.example.chatapp.Utils.CONSTS.BASEURL;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance;
    private Retrofit retrofit;
    private TokenManager tokenManager;

    private RetrofitClient(Context context) {
        tokenManager = new TokenManager(context);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AccessTokenInterceptor(tokenManager))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://"+BASEURL+"/api/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }
}
