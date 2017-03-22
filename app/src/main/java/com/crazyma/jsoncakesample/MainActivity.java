package com.crazyma.jsoncakesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crazyma.jsoncake.ClientHelper;
import com.crazyma.jsoncake.JsonCake;
import com.crazyma.jsoncake.JsonCakeWithPresent;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

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
    }

    private void doNetworking(){

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        ClientHelper.setupOkHttpClient(client);

        JsonCake jsonCake = new JsonCake.Builder()
                .urlStr("http://beibeilab.com/test/sample.json")
                .build();

        jsonCake.start()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String value) {
                        Log.d("crazyma",value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("crazyma",e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void doNetworking2(){
        ArrayMap<String, Object> present = new ArrayMap<>();
        present.put("other","XDDDDDDDDDDDDD");

        JsonCakeWithPresent jsonCake2 = new JsonCakeWithPresent.Builder()
                .urlStr("http://li867-162.members.linode.com/json.php")
                .present(present)
                .build();

        jsonCake2.start()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArrayMap<String, Object>>() {
                    @Override
                    public void onNext(ArrayMap<String, Object> value) {
                        Log.d("crazyma","json : "  + value.get("json").toString());
                        Log.d("crazyma","other info : "  + value.get("other").toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void buttonClick(View view) {
        switch (view.getId()){
            case R.id.button_start:
                doNetworking();
                break;
            case R.id.button_cancel:
                break;
        }
    }
}
