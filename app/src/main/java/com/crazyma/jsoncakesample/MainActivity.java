package com.crazyma.jsoncakesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crazyma.jsoncake.CakeConfig;
import com.crazyma.jsoncake.JsonCake;
import com.crazyma.jsoncake.OnFinishLoadStringListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        CakeConfig.getInstance().setConnectionTimeout(10);
        JsonCake.setUrl("http://25lol.com/veeda/api/bank_channel.php")
                .setOnFinishListener(new OnFinishLoadStringListener() {
                    @Override
                    public void onFinish(String responseStr) {
                        Log.d("JsonCake", responseStr);
                    }
                })
                .get();
    }

}
