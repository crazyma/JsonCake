package com.crazyma.jsoncake;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 2016/2/19.
 */
public class JsonCakeWithPresent implements Observable.OnSubscribe<HashMap<String,Object>> {

    private final String tag = "JsonCake";

    public static final class Builder{
        private String urlStr;
        private int timeout = 15;
        private boolean showingJson;
        private RequestBody formBody;
        private HashMap<String,Object> present;

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

        public Builder present(HashMap present) {
            this.present = present;
            return this;
        }

        public JsonCakeWithPresent build(){
            return new JsonCakeWithPresent(this);
        }
    }

    private String urlStr;
    private int timeout = 15;
    private boolean showingJson;
    private RequestBody formBody;
    private HashMap<String,Object> present;

    public JsonCakeWithPresent(String urlStr){
        this.urlStr = urlStr;
    }

    public JsonCakeWithPresent(String urlStr,boolean showingJson){
        this.urlStr = urlStr;
        this.showingJson = showingJson;
    }

    public JsonCakeWithPresent(JsonCakeWithPresent.Builder builder) {
        this.urlStr = builder.urlStr;   //  can not be null
        this.timeout = builder.timeout;
        this.showingJson = builder.showingJson;
        this.formBody = builder.formBody;   //  could be null. if exist -> Http Post; null -> Http Get
        this.present = builder.present; //  could be null.
    }

    @Override
    public void call(Subscriber<? super HashMap<String,Object>> subscriber) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(timeout, TimeUnit.SECONDS)
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
            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

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

            if(present == null)
                present = new HashMap();

            present.put("json",result);
            subscriber.onNext(present);
            subscriber.onCompleted();
        }
    }

}
