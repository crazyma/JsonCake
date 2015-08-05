package com.crazyma.jsoncakelib;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class JsonCake {
	
	public static class Builder{
		private String url;
		private int connectionTimeout,readTimeout,writeTimeout;	
		private OnFinishListener onFinishListener;
		private OnTaskFailListener onTaskFailListener;
		private OnWrapFormBody onWrapFormBody;
		private RequestBody formBody;
		private Type objectType;
		
		public Builder(){
			connectionTimeout = 10;
			readTimeout = 10;
			writeTimeout = 10;
		}
		
		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}		
		public Builder setConnectionTimeout(int connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
			return this;
		}		
		public Builder setReadTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
			return this;
		}		
		public Builder setWriteTimeout(int writeTimeout) {
			this.writeTimeout = writeTimeout;
			return this;
		}		
		public Builder setOnFinishListener(OnFinishListener onFinishListener) {
			this.onFinishListener = onFinishListener;
			return this;
		}		
		public Builder setOnTaskFailListener(OnTaskFailListener onTaskFailListener) {
			this.onTaskFailListener = onTaskFailListener;
			return this;
		}		
		public Builder setFormBody(RequestBody formBody) {
			this.formBody = formBody;
			return this;
		}				
		public Builder setObjectType(Type objectType){
			this.objectType = objectType;
			return this;
		}
		public Builder setOnWrapFormBody(OnWrapFormBody onWrapFormBody){
			this.onWrapFormBody = onWrapFormBody;
			return this;
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
		public OnFinishListener getOnFinishListener() {
			return onFinishListener;
		}
		public OnTaskFailListener getOnTaskFailListener() {
			return onTaskFailListener;
		}
		public RequestBody getFormBody() {
			return formBody;
		}
		public Type getObjectType(){
			return objectType;
		}
		public OnWrapFormBody getOnWrapFormBody(){
			return onWrapFormBody;
		}
		public void post(){
			new JsonCake(this).post();
		}
		public void get(){
			new JsonCake(this).get();
		}
	}
	
	private Builder builder;
	
	public JsonCake(Builder builder){
		this.builder = builder;
	}
	
	public void post(){
		new PostTask(builder).execute();
	}
	
	public void get(){
		new GetTask(builder).execute();
	}
		
	private class GetTask extends NetworkingTask {

		public GetTask(Builder builder) {
			super(builder);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String onNetworking() throws IOException{
			// TODO Auto-generated method stub
			OkHttpClient client = new OkHttpClient();
			if(connectionTimeout > 0)
				client.setConnectTimeout(connectionTimeout, TimeUnit.SECONDS);
			if(readTimeout > 0)
				client.setReadTimeout(readTimeout, TimeUnit.SECONDS);
			if(writeTimeout > 0)
				client.setWriteTimeout(writeTimeout, TimeUnit.SECONDS);
			
			
			Request request = new Request.Builder()
											.url(url)
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
	
	private class PostTask extends NetworkingTask{

		public PostTask(Builder builder) {
			super(builder);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String onNetworking() throws IOException {
			// TODO Auto-generated method stub
			OkHttpClient client = new OkHttpClient();
			if(connectionTimeout > 0)
				client.setConnectTimeout(connectionTimeout, TimeUnit.SECONDS);
			if(readTimeout > 0)
				client.setReadTimeout(readTimeout, TimeUnit.SECONDS);
			if(writeTimeout > 0)
				client.setWriteTimeout(writeTimeout, TimeUnit.SECONDS);
			
			if(onWrapFormBody != null){
				formBody = onWrapFormBody.wrapForm();
			}
			
			Request request = new Request.Builder()
									        .url(url)
									        .post(formBody)
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
	
}
