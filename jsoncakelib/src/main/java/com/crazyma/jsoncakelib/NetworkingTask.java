package com.crazyma.jsoncakelib;

import java.io.IOException;
import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;

public abstract class NetworkingTask extends AsyncTask<Void, Void, Object> {

	protected final String tag = "JsonCake";
	protected Exception exception = null;
	protected String errorMessage = null;
	protected int connectionTimeout,readTimeout,writeTimeout;
	protected OnFinishListener onFinishListener;
	protected OnTaskFailListener onTaskFailListener;
	protected RequestBody formBody;
	protected OnWrapFormBody onWrapFormBody;
	protected String url;
	protected Type objectType;
	
	public NetworkingTask(JsonCake.Builder builder){
		url = builder.getUrl();
		connectionTimeout = builder.getConnectionTimeout();
		readTimeout = builder.getReadTimeout();
		writeTimeout = builder.getWriteTimeout();
		onFinishListener = builder.getOnFinishListener();
		onTaskFailListener = builder.getOnTaskFailListener();
		formBody = builder.getFormBody();
		objectType = builder.getObjectType();
		onWrapFormBody = builder.getOnWrapFormBody();
	}
	
	protected abstract String onNetworking() throws IOException;
	
	@Override
	protected Object doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Object result = null;			
		String responseStr = null;
		
		if(url == null){
			errorMessage = "You have no url address";
			throw new NullPointerException(errorMessage);
			
		}else if(onFinishListener == null){
			errorMessage = "You have no OnFinishListener";
			throw new NullPointerException(errorMessage);
			
		}else{	//	url != null && onFinishListener != null
		
			try {
				responseStr = onNetworking();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorMessage = "There is a Error through the http connection, please check the exception in second parament of OnTaskFailListener";
				result = null;
				exception = e;
			}			
			
			if(responseStr != null){
				if(onFinishListener instanceof OnFinishLoadObjectListener){
					if(objectType == null){
						errorMessage = "You have no object type for json parsing in Gson";
						throw new NullPointerException(errorMessage);
					}else{
						try{
							Gson gson = new Gson();
							result = gson.fromJson(responseStr, objectType);
						} catch (Exception e){
							// TODO Auto-generated catch block
							errorMessage = "There is an Error, please check the exception in second parament of OnTaskFailListener";
							exception = e;
							result = null;
							e.printStackTrace();
						}
					}
					
				}else if(onFinishListener instanceof OnFinishLoadJsonObjectListener){
					
					try {
						JSONObject jsonObject = new JSONObject(responseStr);
						result = jsonObject;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						errorMessage = "There is a JsonObject Error, please check the exception in second parament of OnTaskFailListener";
						exception = e;
						result = null;
						e.printStackTrace();
					}
					
				}else if(onFinishListener instanceof OnFinishLoadJsonArrayListener){
					
					try {
						JSONArray jsonArray = new JSONArray(responseStr);
						result = jsonArray; 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						errorMessage = "There is a JsonArray Error, please check the exception in second parament of OnTaskFailListener";
						exception = e;
						result = null;
						e.printStackTrace();
					}
					
				}else if(onFinishListener instanceof OnFinishLoadStringListener){
					result = responseStr;
				}
			}			
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(result != null){			
			if(onFinishListener instanceof OnFinishLoadObjectListener){
				((OnFinishLoadObjectListener)onFinishListener).onFinish(result);
			}else if(onFinishListener instanceof OnFinishLoadJsonObjectListener){
				((OnFinishLoadJsonObjectListener)onFinishListener).onFinish((JSONObject)result);
			}else if(onFinishListener instanceof OnFinishLoadJsonArrayListener){
				((OnFinishLoadJsonArrayListener)onFinishListener).onFinish((JSONArray)result);
			}else if(onFinishListener instanceof OnFinishLoadStringListener){
				((OnFinishLoadStringListener)onFinishListener).onFinish((String)result);
			}
			return;				
		}
			
		if(errorMessage != null)
			Log.e(tag,errorMessage);
		
		if(exception != null){
			Log.e(tag,exception.toString());
		}
		
		if(onTaskFailListener != null){	
			onTaskFailListener.onFail(errorMessage, exception);
		}
	}
	
}
