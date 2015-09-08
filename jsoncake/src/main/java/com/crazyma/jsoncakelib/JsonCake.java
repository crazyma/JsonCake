package com.crazyma.jsoncakelib;

import com.squareup.okhttp.RequestBody;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;

/**
 * Created by david on 2015/9/4.
 */
public class JsonCake {

    public static final String tag = "JsonCake";
    private JsonCake(){}

    public static RequestBuilder setUrl(String url){
        return new RequestBuilder(url);
    }

    public static class RequestBuilder {

        private String url;
        private int connectionTimeout,readTimeout,writeTimeout;
        private int delay;
        private OnFinishListener onFinishListener;
        private OnTaskFailListener onTaskFailListener;
        private OnWrapFormBody onWrapFormBody;
        private RequestBody formBody;
        private Type objectType;
        private ExecutorService pool;
        private boolean showingJson;

        private RequestBuilder(String url){
            this.url = url;

            CakeConfig cakeConfig = CakeConfig.getInstance();
            connectionTimeout = cakeConfig.getConnectionTimeout();
            readTimeout = cakeConfig.getReadTimeout();
            writeTimeout = cakeConfig.getWriteTimeout();
            delay = cakeConfig.getDelay();
            pool = cakeConfig.getPool();
            showingJson = cakeConfig.isShowingJson();
        }

        public GetTask get(){
            GetTask getTask = new GetTask(this);
            getTask.executeOnExecutor(pool);

            return getTask;
        }

        public PostTask post(){
            PostTask postTask = new PostTask(this);;
            postTask.executeOnExecutor(pool);

            return postTask;
        }

        public RequestBuilder setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public RequestBuilder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public RequestBuilder setWriteTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public RequestBuilder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public RequestBuilder setOnFinishListener(OnFinishListener onFinishListener) {
            this.onFinishListener = onFinishListener;
            return this;
        }

        public RequestBuilder setOnTaskFailListener(OnTaskFailListener onTaskFailListener) {
            this.onTaskFailListener = onTaskFailListener;
            return this;
        }

        public RequestBuilder setOnWrapFormBody(OnWrapFormBody onWrapFormBody) {
            this.onWrapFormBody = onWrapFormBody;
            return this;
        }

        public RequestBuilder setFormBody(RequestBody formBody) {
            this.formBody = formBody;
            return this;
        }

        public RequestBuilder setObjectType(Type objectType) {
            this.objectType = objectType;
            return this;
        }

        public RequestBuilder setPool(ExecutorService pool) {
            this.pool = pool;
            return this;
        }

        public RequestBuilder setShowingJson(boolean showingJson) {
            this.showingJson = showingJson;
            return this;
        }

        public boolean isShowingJson() {
            return showingJson;
        }

        public String getUrl() {
            return url;
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

        public OnFinishListener getOnFinishListener() {
            return onFinishListener;
        }

        public OnTaskFailListener getOnTaskFailListener() {
            return onTaskFailListener;
        }

        public OnWrapFormBody getOnWrapFormBody() {
            return onWrapFormBody;
        }

        public RequestBody getFormBody() {
            return formBody;
        }

        public Type getObjectType() {
            return objectType;
        }

        public ExecutorService getPool() {
            return pool;
        }
    }
}
