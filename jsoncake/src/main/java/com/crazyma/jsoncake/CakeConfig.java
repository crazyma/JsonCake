package com.crazyma.jsoncake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by david on 2015/9/7.
 */
public class CakeConfig {
    private int connectionTimeout,readTimeout,writeTimeout;
    private int delay;
    private ExecutorService pool;
    private boolean showingJson;

    private static CakeConfig instance;

    private CakeConfig() {
        connectionTimeout = 20;
        readTimeout = 20;
        writeTimeout = 20;
        delay = 0;
        showingJson = false;
        pool = Executors.newFixedThreadPool(5);
    }

    public static CakeConfig getInstance(){
        if(instance == null) {
            synchronized (CakeConfig.class) {
                if (instance == null) {
                    instance = new CakeConfig();
                }
            }
        }
        return instance;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setPool(ExecutorService pool) {
        this.pool = pool;
    }

    public void setShowingJson(boolean showingJson) {
        this.showingJson = showingJson;
    }

    public static void setInstance(CakeConfig instance) {
        CakeConfig.instance = instance;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getDelay() {
        return delay;
    }

    public ExecutorService getPool() {
        return pool;
    }

    public boolean isShowingJson() {
        return showingJson;
    }
}
