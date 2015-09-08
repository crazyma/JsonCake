package com.crazyma.jsoncake;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by david on 2015/9/4.
 */
public class GetTask extends NetworkingTask {

    public GetTask(JsonCake.RequestBuilder requestBuilder) {
        super(requestBuilder);
    }

    @Override
    protected String onNetworking() throws IOException {
        OkHttpClient client = new OkHttpClient();
        if(requestBuilder.getConnectionTimeout() > 0)
            client.setConnectTimeout(requestBuilder.getConnectionTimeout(), TimeUnit.SECONDS);
        if(requestBuilder.getReadTimeout() > 0)
            client.setReadTimeout(requestBuilder.getReadTimeout(), TimeUnit.SECONDS);
        if(requestBuilder.getWriteTimeout() > 0)
            client.setWriteTimeout(requestBuilder.getWriteTimeout(), TimeUnit.SECONDS);


        Request request = new Request.Builder()
                .url(requestBuilder.getUrl())
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            return response.body().string();
        }else{
            errorMessage = "Http Connection Error. Status Code : " + response.code();
            exception = new Exception(errorMessage);
            return null;
        }
    }
}
