package com.crazyma.jsoncake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crazyma.jsoncakelib.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//        new JsonCake.Builder()
//                    .setUrl("http://25lol.com/veeda/api/bank_channel.php")
//                    .setOnFinishListener(new OnFinishLoadStringListener() {
//                        @Override
//                        public void onFinish(String responseStr) {
//                        }
//                    })
//                    .get();
//        new JsonCake.Builder()
//                .setUrl("http://25lol.com/veeda/api/bank_channel.php")
//                .setOnFinishListener(new OnFinishLoadStringListener() {
//                    @Override
//                    public void onFinish(String responseStr) {
//                        Log.d("TAG", "response : " + responseStr);
//                    }
//                })
//                .get();

        JsonCake2.setUrl("http://25lol.com/veeda/api/bank_channel.php")
                    .setOnFinishListener(new OnFinishLoadStringListener() {
                        @Override
                        public void onFinish(String responseStr) {
                            Log.d("crazyma","111111111 : " + responseStr);
                        }
                    })
                    .get();
        JsonCake2.setUrl("http://25lol.com/veeda/api/bank_channel.php")
                .setOnFinishListener(new OnFinishLoadStringListener() {
                    @Override
                    public void onFinish(String responseStr) {
                        Log.d("crazyma","222222222222 : " + responseStr);
                    }
                })
                .get();


//        CustomGsonObject customGsonObject = new CustomGsonObject();
//        CustomData customData = customGsonObject.getParsedObject(CustomData.class);
//        Log.i("crazyma", "name : " + customGsonObject.getParsedObject(CustomData.class).getData().get(2).getName());
//
//        List<CustomData2.City> customData2 = customGsonObject.getParsedObject2(CustomData2.City.class);
//        customData2.get(0);
//        Log.i("crazyma", "cities : " + customData2.get(0).getCityName() + ", " + customData2.get(1).getCityName());

    }

    public static class CustomGsonObject{

        String str;
        public <T>T getParsedObject(Class<T> cls){
            T t = null;
            Gson gson = new Gson();
            try {
                t = gson.fromJson("{ \"status\":\"true\", \"data\":[ {\"name\":\"David Ma\"}, {\"name\":\"WT Wu\"}, {\"name\":\"Cathy Wu\"}] }", cls);
            }catch (Exception e){
                Log.e("crazyma",e.toString());
            }
            return t;
        }

        public <T> List<T> getParsedObject2(Class<T> cls){

            List<T> list = new ArrayList<T>();
            String str = "[\n" +
                    "{\"cityName\":\"Taipei\" , \"country\":\"Taiwan\"},\n" +
                    "{\"cityName\":\"New York\" , \"country\":\"USA\"}\n" +
                    "]";

                JsonArray array = new JsonParser().parse(str).getAsJsonArray();
                for(final JsonElement elem : array){
                    list.add(new Gson().fromJson(elem, cls));
                }

                return list;

        }
    }

    public static class CustomData2{
        private List<City> cities;

        public List<City> getCities() {
            return cities;
        }

        public class City{
            String cityName,country;

            public String getCityName() {
                return cityName;
            }

            public String getCountry() {
                return country;
            }
        }
    }

    public static class CustomData{
        private String status;
        private List<Person> data;

        public String getStatus() {
            return status;
        }

        public List<Person> getData() {
            return data;
        }

        public class Person{
            private String name;

            public String getName() {
                return name;
            }
        }
    }
}
