package com.crazyma.jsoncake;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;


import java.io.IOException;
import java.net.URL;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.crazyma.jsoncake.ClientHelper.okHttpClient;

/**
 * Created by david on 2016/2/19.
 */
@TargetApi(19)
public class JsonCakeWithPresent {

    private final String tag = "JsonCake";

    public static final class Builder{
        private String urlStr;
        private URL url;
        private boolean showingJson;
        private RequestBody formBody;
        private ArrayMap<String,Object> present;

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

        public Builder present(ArrayMap present) {
            this.present = present;
            return this;
        }

        public JsonCakeWithPresent build(){
            return new JsonCakeWithPresent(this);
        }
    }

    private String urlStr;
    private URL url;
    private boolean showingJson;
    private RequestBody formBody;
    private ArrayMap<String,Object> present;

    public JsonCakeWithPresent(String urlStr){
        this.urlStr = urlStr;
    }

    public JsonCakeWithPresent(String urlStr,boolean showingJson){
        this.urlStr = urlStr;
        this.showingJson = showingJson;
    }

    public JsonCakeWithPresent(URL url){
        this.url = url;
    }

    public JsonCakeWithPresent(URL url,boolean showingJson){
        this.url = url;
        this.showingJson = showingJson;
    }

    public JsonCakeWithPresent(JsonCakeWithPresent.Builder builder) {
        this.urlStr = builder.urlStr;   //  can not be null
        this.url = builder.url;
        this.showingJson = builder.showingJson;
        this.formBody = builder.formBody;   //  could be null. if exist -> Http Post; null -> Http Get
        this.present = builder.present; //  could be null.
    }

    public Flowable<ArrayMap<String,Object>> startWithFlowable(){
        if(Build.VERSION.SDK_INT < 19)
            return Flowable.empty();

        return Flowable.create(new FlowableOnSubscribe<ArrayMap<String, Object>>() {
            @Override
            public void subscribe(FlowableEmitter<ArrayMap<String, Object>> emitter) throws Exception {
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
                if(formBody == null){
                    request = new Request.Builder()
                            .url(url)
                            .build();
                }else{
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
                try {
                    Response response = call.execute();
                    if(!response.isSuccessful()) {
                        emitter.onError(new IOException("Unexpected code " + response));
                        response.close();
                        return;
                    }

                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                    return;
                }

                if(result == null) {
                    emitter.onError(new NullPointerException("Response in JsonCake is null"));
                }else {
                    if(showingJson)
                        Log.d(tag, result);

                    if(present == null)
                        present = new ArrayMap<>();
                    present.put("json",result);

                    emitter.onNext(present);
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Observable<ArrayMap<String,Object>> start() {

        if(Build.VERSION.SDK_INT < 19)
            return Observable.empty();


        return Observable.create(new ObservableOnSubscribe<ArrayMap<String,Object>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayMap<String,Object>> emitter) throws Exception {

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
                if(formBody == null){
                    request = new Request.Builder()
                            .url(url)
                            .build();
                }else{
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
                    if(!response.isSuccessful()) {
                        emitter.onError(new IOException("Unexpected code " + response));
                        response.close();
                        return;
                    }

                    result = response.body().string();
                    response.close();
                } catch (IOException e) {
                    if(response != null) response.close();

                    e.printStackTrace();
                    emitter.onError(e);
                    return;
                }

                if(result == null) {
                    emitter.onError(new NullPointerException("Response in JsonCake is null"));
                }else {
                    if(showingJson)
                        Log.d(tag, result);

                    if(present == null)
                        present = new ArrayMap<>();
                    present.put("json",result);

                    emitter.onNext(present);
                    emitter.onComplete();
                }
            }
        });

    }

}
