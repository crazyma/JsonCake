package com.crazyma.jsoncake;

import java.net.URL;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.crazyma.jsoncake.ClientHelper.okHttpClient;

/**
 * Created by david on 2017/3/7.
 */

public class FileCake{

    public static final class Builder{
        private String urlStr;
        private URL url;

        public Builder urlStr(String urlStr) {
            this.urlStr = urlStr;
            return this;
        }

        public Builder url(URL url){
            this.url = url;
            return this;
        }

        public FileCake build(){
            return new FileCake(this);
        }

    }

    private String urlStr;
    private URL url;

    public FileCake(Builder builder){
        this.urlStr = builder.urlStr;
        this.url = builder.url;
    }

    public Flowable<Response> start(){
        return Flowable.create(new FlowableOnSubscribe<Response>() {
            @Override
            public void subscribe(FlowableEmitter<Response> emitter) throws Exception {

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

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                if(response != null && response.isSuccessful()) {
                    emitter.onNext(response);
                    emitter.onComplete();
                }else{
                    emitter.onError(new RuntimeException(
                            response == null ? "response is null" : response.message()));
                    if(response != null)
                        response.close();
                }

            }
        }, BackpressureStrategy.BUFFER);
    }

}
