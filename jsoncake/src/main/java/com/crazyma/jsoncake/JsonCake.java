package com.crazyma.jsoncake;

import android.util.Log;

import java.io.IOException;
import java.net.URL;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import io.reactivex.Observable;

import static com.crazyma.jsoncake.ClientHelper.okHttpClient;

/**
 * Created by david on 2016/12/8.
 */

public class JsonCake {

    private final String tag = "JsonCake";

    public static final class Builder{
        private String urlStr;
        private URL url;
        private boolean showingJson;
        private RequestBody formBody;

        public Builder urlStr(String urlStr) {
            this.urlStr = urlStr;
            return this;
        }

        public Builder url(URL url){
            this.url = url;
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
    private URL url;
    private boolean showingJson;
    private RequestBody formBody;

    public JsonCake(String urlStr){
        this.urlStr = urlStr;
    }

    public JsonCake(String urlStr, boolean showingJson){
        this.urlStr = urlStr;
        this.showingJson = showingJson;
    }

    public JsonCake(URL url){
        this.url = url;
    }

    public JsonCake(URL url, boolean showingJson){
        this.url = url;
        this.showingJson = showingJson;
    }

    public JsonCake(Builder builder) {
        this.urlStr = builder.urlStr;   //  can not be null
        this.url = builder.url;
        this.showingJson = builder.showingJson;
        this.formBody = builder.formBody;   //  could be null. if exist -> Http Post; null -> Http Get
    }

    public Flowable<String> startWithFlowable(){

        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {

                if(url == null){
                    if(urlStr == null) {
                        emitter.onError(new NullPointerException("url is Null"));
                        return;
                    }else{
                        url = new URL(urlStr);
                    }
                }

                OkHttpClient client = okHttpClient;
                if(client == null) {
                    emitter.onError(new NullPointerException("OkHttpClient is Null"));
                    return;
                }

                Request request;
                if (formBody == null) {
                    request = new Request.Builder()
                            .url(url)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
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

                Response response = null;
                String result;
                try {
                    response = call.execute();
                    if (!response.isSuccessful()) {
                        emitter.onError(new IOException("Unexpected code " + response));
                        response.close();
                        return;
                    }

                    result = response.body().string();
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();

                    if(response != null) response.close();
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
        }, BackpressureStrategy.BUFFER);
    }

    public Observable<String> start() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                if(url == null){
                    if(urlStr == null) {
                        emitter.onError(new NullPointerException("url is Null"));
                        return;
                    }else{
                        url = new URL(urlStr);
                    }
                }

                OkHttpClient client = okHttpClient;
                if(client == null){
                    emitter.onError(new NullPointerException("OkHttpClient is Null"));
                    return;
                }

                Request request;
                if (formBody == null) {
                    request = new Request.Builder()
                            .url(url)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
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
                Response response = null;
                try {
                    response = call.execute();
                    if (!response.isSuccessful()) {
                        emitter.onError(new IOException("Unexpected code " + response));
                        response.close();
                        return;
                    }

                    result = response.body().string();
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();

                    if(response != null) response.close();
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
