package com.crazyma.jsoncake;

import okhttp3.OkHttpClient;

/**
 * Created by david on 2017/3/22.
 */

public class ClientHelper {
    protected static OkHttpClient okHttpClient;

    public static void setupOkHttpClient(OkHttpClient _okHttpClient){
        okHttpClient = _okHttpClient;
    }
}
