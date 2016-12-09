package com.crazyma.jsoncakesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crazyma.jsoncake.JsonCake;
import com.crazyma.jsoncake.JsonCakeWithPresent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
        doNetworking();
    }

    private void doNetworking(){
        JsonCake jsonCake = new JsonCake.Builder()
                .urlStr("http://li867-162.members.linode.com/json.php")
                .build();

        jsonCake.start()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
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
                        Log.d("crazyma","??????  "  + value.get("other").toString());
                        Log.d("crazyma","!!!!!!  "  + value.get("json").toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void cancelButtonClick(View view) {

    }
}
