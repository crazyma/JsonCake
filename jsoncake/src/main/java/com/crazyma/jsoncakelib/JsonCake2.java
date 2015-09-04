package com.crazyma.jsoncakelib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by david on 2015/9/4.
 */
public class JsonCake2 {

    public static final String tag = "JsonCake";
    private static JsonCake2 uniqueInstance;
    private static ExecutorService pool;
    private JsonCake2(){}

    public static synchronized JsonCake2 getInstance() { // 使用 synchronized 關鍵字避免同時兩支Thread 進入函數
        if(uniqueInstance == null ) {
            uniqueInstance = new JsonCake2();
        }
        return uniqueInstance;
    }

    public static synchronized ExecutorService getDefaultPool(){
        if(pool == null ) {
            pool = Executors.newFixedThreadPool(5);
        }
        return pool;
    }


    public RequestBuilder setUrl(String url){
        return new RequestBuilder(url);
    }
}
