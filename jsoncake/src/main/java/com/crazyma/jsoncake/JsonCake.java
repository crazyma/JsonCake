package com.crazyma.jsoncake;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 2015/9/4.
 */
public class JsonCake implements Observable.OnSubscribe<String>{

    private final String tag = "JsonCake";
    private String urlStr;
    private int timeout = 15;
    private boolean showingJson;

    public JsonCake(String urlStr){
        this.urlStr = urlStr;
    }

    public JsonCake(String urlStr,boolean showingJson){
        this.urlStr = urlStr;
        this.showingJson = showingJson;
    }

    public JsonCake(String urlStr,int timeout){
        this.urlStr = urlStr;
        this.timeout = timeout;
    }

    public JsonCake(String urlStr, int timeout, boolean showingJson) {
        this.urlStr = urlStr;
        this.timeout = timeout;
        this.showingJson = showingJson;
    }

    @Override
    public void call(Subscriber<? super String> subscriber) {

        OkHttpClient client = new OkHttpClient.Builder()
                                                .connectTimeout(timeout,TimeUnit.SECONDS)
                                                .readTimeout(timeout,TimeUnit.SECONDS)
                                                .writeTimeout(timeout, TimeUnit.SECONDS)
                                                .build();

        Request request = new Request.Builder()
                                        .url(urlStr)
                                        .build();

        String result;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            subscriber.onError(e);
            return;
        }

        if(result == null) {
            subscriber.onError(new NullPointerException("Response in JsonCake is null"));
        }else {
            if(showingJson)
                Log.d(tag, result);

            subscriber.onNext(result);
            subscriber.onCompleted();
        }
    }
}
