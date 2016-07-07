package com.crazyma.jsoncakesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crazyma.jsoncake.JsonCake;
import com.crazyma.jsoncake.JsonCakeWithPresent;
import com.facebook.stetho.Stetho;

import java.util.HashMap;
import java.util.Timer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //http://25lol.com/veeda/api/bank_channel.php

        Action1<String> onNextAction = new Action1<String>() {
            // onError()
            @Override
            public void call(String s) {
                Log.d("JsonCake Sample","Result : " + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
                Log.d("JsonCake Sample", "Error : " + throwable.toString());
            }
        };


        JsonCake jsonCake = new JsonCake.Builder()
                                        .urlStr("http://25lol.com/veeda/api/bank_channel.php")
                                        .build();

        Observable.create(jsonCake)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextAction,onErrorAction);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("other","XDDDDDDDDD");

        JsonCakeWithPresent jsonCakeWithPresent = new JsonCakeWithPresent.Builder()
                                                                        .urlStr("http://25lol.com/veeda/api/bank_channel.php")
                                                                        .present(hashMap)
                                                                        .build();

        Observable.create(jsonCakeWithPresent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HashMap<String,Object>>() {
                    @Override
                    public void call(HashMap<String,Object> hashMap) {
                        Log.d("crazyma","??????  "  + hashMap.get("other").toString());
                        Log.d("crazyma","!!!!!!  "  + hashMap.get("json").toString());
                    }
                }, onErrorAction);
    }

    public void cancelButtonClick(View view) {

    }
}
