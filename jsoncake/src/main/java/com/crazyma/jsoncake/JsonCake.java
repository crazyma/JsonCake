package com.crazyma.jsoncake;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import io.reactivex.Observable;

/**
 * Created by david on 2016/12/8.
 */

public class JsonCake {

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

    public JsonCake(String urlStr, boolean showingJson){
        this.urlStr = urlStr;
        this.showingJson = showingJson;
    }

    public JsonCake(Builder builder) {
        this.urlStr = builder.urlStr;   //  can not be null
        this.timeout = builder.timeout;
        this.showingJson = builder.showingJson;
        this.formBody = builder.formBody;   //  could be null. if exist -> Http Post; null -> Http Get
    }

    public Observable<String> start() {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                if(urlStr == null)
                    emitter.onError(new NullPointerException("urlStr is Null"));


                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new StethoInterceptor())
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        .build();

                Request request;
                if (formBody == null) {
                    request = new Request.Builder()
                            .url(urlStr)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(urlStr)
                            .post(formBody)
                            .build();
                }

                final Call call = client.newCall(request);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        call.cancel();
                    }
                });

                String result;
                try {
                    Response response = call.execute();
                    if (!response.isSuccessful())
                        emitter.onError( new IOException("Unexpected code " + response));

                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                    return;
                }

                if (result == null) {
                    emitter.onError(new NullPointerException("Response in JsonCake is null"));
                } else {
                    if (showingJson)
                        Log.d(tag, result);

                    emitter.onNext(result);
                    emitter.onComplete();
                }
            }
        });
    }
    
}
