package com.example.zacharymuller.smartparking.Remote;

import retrofit2.Retrofit;

/**
 * Created by zacharymuller on 1/27/18.
 */

public class Common {
    public static final String baseURL = "https://googleapis.com";

    public static IGoogleApi getGoogleApi() {
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
