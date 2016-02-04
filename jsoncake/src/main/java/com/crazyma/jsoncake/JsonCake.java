package com.crazyma.jsoncake;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 2015/9/4.
 */
public class JsonCake implements Observable.OnSubscribe<String>{

    private final String tag = "JsonCake";

    public static final class Builder{
        private String urlStr;
        private int timeout = 15;
        private boolean showingJson;
        private RequestBody formBody;

        public Builder urlStr(String urlStr) {
            this.urlStr = urlStr;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder showingJson(boolean showingJson) {
            this.showingJson = showingJson;
            return this;
        }

        public Builder formBody(RequestBody formBody) {
            this.formBody = formBody;
            return this;
        }

        public JsonCake build(){
            return new JsonCake(this);
        }
    }

    private String urlStr;
    private int timeout = 15;
    private boolean showingJson;
    private RequestBody formBody;

    public JsonCake(String urlStr){
        this.urlStr = urlStr;
    }

    public JsonCake(String urlStr,boolean showingJson){
        this.urlStr = urlStr;
        this.showingJson = showingJson;
    }

    public JsonCake(JsonCake.Builder builder) {
        this.urlStr = builder.urlStr;   //  can not be null
        this.timeout = builder.timeout;
        this.showingJson = builder.showingJson;
        this.formBody = builder.formBody;   //  could be null. if exist -> Http Post; null -> Http Get
    }

    @Override
    public void call(Subscriber<? super String> subscriber) {

        OkHttpClient client = new OkHttpClient.Builder()
                                                .connectTimeout(timeout,TimeUnit.SECONDS)
                                                .readTimeout(timeout,TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();

        Request request;

        if(formBody == null){
            request = new Request.Builder()
                    .url(urlStr)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(urlStr)
                    .post(formBody)
                    .build();
        }


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
