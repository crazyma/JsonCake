package com.crazyma.jsoncake;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by david on 2017/3/7.
 */

public class FileCake {

    public static final class Builder{
        private String urlStr;
        private URL url;
        private int timeout = 15;

        public Builder urlStr(String urlStr) {
            this.urlStr = urlStr;
            return this;
        }

        public Builder url(URL url){
            this.url = url;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public FileCake build(){
            return new FileCake(this);
        }

    }

    private String urlStr;
    private URL url;
    private int timeout;

    public FileCake(Builder builder){
        this.urlStr = builder.urlStr;
        this.url = builder.url;
        this.timeout = builder.timeout;
    }

    public Flowable<Response> start(){
        return Flowable.create(new FlowableOnSubscribe<Response>() {
            @Override
            public void subscribe(FlowableEmitter<Response> emitter) throws Exception {

                if(url == null){
                    if(urlStr == null) {
                        emitter.onError(new NullPointerException("url is Null"));
                    }else{
                        url = new URL(urlStr);
                    }
                }

                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new StethoInterceptor())
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                if(response != null && response.isSuccessful()) {
                    emitter.onNext(response);
                }else{
                    emitter.onError(new RuntimeException(
                            response == null ? "response is null" : response.message()));
                }

            }
        }, BackpressureStrategy.BUFFER);
    }

}
