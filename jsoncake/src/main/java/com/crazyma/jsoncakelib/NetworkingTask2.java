package com.crazyma.jsoncakelib;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by david on 2015/9/4.
 */
public abstract class NetworkingTask2 extends AsyncTask<Void, Void, Object> {

    protected JsonCake2.RequestBuilder requestBuilder;
    protected RequestBody formBody;
    protected Exception exception = null;
    protected String errorMessage = null;

    public NetworkingTask2(JsonCake2.RequestBuilder requestBuilder){
        this.requestBuilder = requestBuilder;
    }

    protected abstract String onNetworking() throws IOException;

    @Override
    protected Object doInBackground(Void... params) {
        // TODO Auto-generated method stub
        Object result = null;
        String responseStr = null;

        if(requestBuilder.getUrl() == null){
            errorMessage = "You have no url address";
            throw new NullPointerException(errorMessage);

        }else if(requestBuilder.getOnFinishListener() == null){
            errorMessage = "You have no OnFinishListener";
            throw new NullPointerException(errorMessage);

        }else{	//	url != null && onFinishListener != null

            try {
                responseStr = onNetworking();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                errorMessage = "There is a Error through the http connection, please check the exception in second parameter of OnTaskFailListener";
                result = null;
                exception = e;
            }

            if(responseStr != null){
                try {
                    Thread.sleep(requestBuilder.getDelay() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(requestBuilder.isShowingJson()){
                    Log.d(JsonCake2.tag, "[url : " + requestBuilder.getUrl() + "]\n" + responseStr);
                }
                if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadObjectListener){
                    if(requestBuilder.getObjectType() == null){
                        errorMessage = "You have no object type for json parsing in Gson";
                        throw new NullPointerException(errorMessage);
                    }else{
                        try{
                            Gson gson = new Gson();
                            result = gson.fromJson(responseStr, requestBuilder.getObjectType());
                        } catch (Exception e){
                            // TODO Auto-generated catch block
                            errorMessage = "There is an Error, please check the exception in second parameter of OnTaskFailListener";
                            exception = e;
                            result = null;
                            e.printStackTrace();
                        }
                    }

                }else if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadJsonObjectListener){

                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        result = jsonObject;
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        errorMessage = "There is a JsonObject Error, please check the exception in second parameter of OnTaskFailListener";
                        exception = e;
                        result = null;
                        e.printStackTrace();
                    }

                }else if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadJsonArrayListener){

                    try {
                        JSONArray jsonArray = new JSONArray(responseStr);
                        result = jsonArray;
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        errorMessage = "There is a JsonArray Error, please check the exception in second parameter of OnTaskFailListener";
                        exception = e;
                        result = null;
                        e.printStackTrace();
                    }

                }else if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadStringListener){
                    result = responseStr;
                }
            }
        }

        return result;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.w(JsonCake2.tag,"JsonCake is cancelled");
    }

    @Override
    protected void onPostExecute(Object result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if(result != null){
            if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadObjectListener){
                ((OnFinishLoadObjectListener)requestBuilder.getOnFinishListener()).onFinish(result);
            }else if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadJsonObjectListener){
                ((OnFinishLoadJsonObjectListener)requestBuilder.getOnFinishListener()).onFinish((JSONObject)result);
            }else if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadJsonArrayListener){
                ((OnFinishLoadJsonArrayListener)requestBuilder.getOnFinishListener()).onFinish((JSONArray)result);
            }else if(requestBuilder.getOnFinishListener() instanceof OnFinishLoadStringListener){
                ((OnFinishLoadStringListener)requestBuilder.getOnFinishListener()).onFinish((String)result);
            }
            return;
        }

        if(errorMessage != null)
            Log.e(JsonCake2.tag,errorMessage);

        if(exception != null){
            Log.e(JsonCake2.tag,exception.toString());
        }

        if(requestBuilder.getOnTaskFailListener() != null){
            requestBuilder.getOnTaskFailListener().onFail(errorMessage, exception);
        }
    }

    public void cancel(){
        this.cancel(true);
    }

}
