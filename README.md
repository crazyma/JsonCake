
Welcome to JsonCake
=======

這是一個Android Library，方便developer開發Android App時，快速執行下載或上傳**Json file**，讓你使用Json進行資料傳輸有如**piece of cake**。



Update - v2.2.0 [2016/12/21]
---
導入 RxJava2。重新簡介目前功能
- 使用 [RxJava2 (v2.0.2)](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
- 使用 [Stetho (v1.3.1)](http://facebook.github.io/stetho/) 套件，可利用 Network Inspection 瀏覽 api call 的資訊


HowTo
---
``` java
JsonCake jsonCake = new JsonCake.Builder()
                .urlStr("YOUR_URL")
                .build();

jsonCake.start()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String value) {
                        // Get your result here
                        Log.d(TAG,value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
```

你可以利用 `JsonCake` 設定一些參數
``` java
// This is okhttp object for http post
MultipartBody.Builder bodybuilder =
                new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("data",data);

// This is okhttp object for http post
RequestBody requestBody = bodybuilder.build();

JsonCake jsonCake = new JsonCake.Builder()
                .urlStr("YOUR_URL")
                .showingJson(true)    // true if you want to log the json string
                .timeout(5) // set timeout (second)
                .formBody(requestBody) // set form data if you want to do http-post
                .build();
```
啟用 Stetho
---
導入了 [Stetho](http://facebook.github.io/stetho/) 套件 (`com.facebook.stetho:stetho-okhttp3:1.3.1`) <p>
可以直接利用瀏覽器查看 ***Network Request/Response*** <p>
只需要在 `Application` 啟用 Stetho 即可，如下範例：
``` java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
```


Download
---
```xml
repositories {
    maven {
        url  "http://dl.bintray.com/badu/maven"
    }
}

...

dependencies {
	...
    compile 'com.crazyma.jsoncake:jsoncake:2.2.0'
}
```
Used dependencies
---
此Library所使用的dependencies：
```xml
compile 'com.squareup.okhttp3:okhttp:3.3.1'
compile 'com.facebook.stetho:stetho-okhttp3:1.3.1'
compile 'io.reactivex.rxjava2:rxjava:2.0.2'
```

Required Android Permission
--------
	<uses-permission android:name="android.permission.INTERNET">


-------
以下為舊版 JsonCake(v.1.0.3)
---

本Library以AsyncTask為基礎建立，使用了 [OKHttp][2] 以及 [Gson][1] 協助處理相關的操作。


Variables
---
**Required**

|Name    |Data type|Description|
|:-------|:--------|:----------|      
|urlStr |String|json file 的網址。|
|onFinishListener|OnFinishListener|Task 結束時呼叫的Listener。|


Optional

|Name    |Data type|Description|
|:-------|:--------|:----------|      
|connectionTimeout|int|連線至server的timeout時間，單位是秒。|
|readTimeout|int|Http get 的 timeout時間，單位是秒。|
|writeTimeout|int|Http post 的timeout時間，單位是秒。|
|delay|int|延遲送出http request，單位是秒|
|onTaskFailListener|OnTaskFailListener|Task 失敗時呼叫的Listener。|
|formBody|RequestBody|Http post所使用。是[OkHttp][2]所定義的類別。|
|objectType|Type|類別型態。配合[Gson][1]使用。|
|pool|ExecutorService|設定AsyncTask的Executor。詳情請參閱[官網][3]|
|showingJson|boolean|指示是否要Log response。|


**CakeConfig**


你可以於app初始設定一些參數，讓所有的Task共用此設定
```java
CakeConfig.getInstance()
		  .setConnectionTimeout(10);
		  .setDelay(1)
		  .setPool(Executors.newFixedThreadPool(5));

/*	此JsonCake Task會自動帶入上述設定	*/		  
JsonCake.setUrl("you_url")
        .setOnFinishListener(new OnFinishLoadStringListener() {
			@Override
            public void onFinish(String responseStr) {

            }
	     })
         .get();
```
>**Note:**
>如果設定CakeConfig的同時，也在JsonCake設定相關參數，會以最後的設定為參考依據。


Sample Code
----
#### Load JSONObject Object
```java
JsonCake.setUrl("your_json_file_url")
	    .setOnFinishListener(new OnFinishLoadJsonObjectListener(){

			@Override
			public void onFinish(JSONObject arg0) {
				// TODO Auto-generated method stub
				/* write your code here */
			}

	    })
	    .get();
```

#### Load JSONArray Object with Read Timeout
```java
JsonCake.setUrl("your_json_file_url")
	    .setReadTimeout(5)
	    .setOnFinishListener(new OnFinishLoadJsonArrayListener(){

			@Override
			public void onFinish(JSONArray arg0) {
				// TODO Auto-generated method stub
				/* write your code here */
			}

	    })
	    .get();
```

#### Load String Object with OnTaskFailListener
```java
JsonCake.setUrl("your_json_file_url")
	    .setOnFinishListener(new OnFinishLoadStringListener(){

			@Override
			public void onFinish(String arg0) {
				// TODO Auto-generated method stub
				/* write your code here */
			}

	    })
	    .setOnTaskFailListener(new OnTaskFailListener(){

			@Override
			public void onFail(String errorMessage, Exception exception) {
				// TODO Auto-generated method stub
				/* write your code here */
			}

	    })
	    .get();
```

####Load Custom Object parsed by Gson
```java
JsonCake.setUrl("your_json_file_url")
	    .setObjectType(DataSet.class)
	    .setOnFinishListener(new OnFinishLoadObjectListener(){

			@Override
			public void onFinish(Object object) {
				// TODO Auto-generated method stub
				DataSet dataSet = (DataSet)object;
				/* write your code here */
			}

	    })
	    .get();
```
>**Note:**
>DataSet是自己定義的class。Gson可以把下載的Json解析成DataSet Object。詳情請參考[官網](https://code.google.com/p/google-gson/)。


####Send Http Post and Get String Response
```java
RequestBody formBody = new FormEncodingBuilder()
	.add("Name", "Tom") // key-value pair in POST
    .add("Age", "25")
    .build();

JsonCake.setUrl("your_url")
	    .setOnFinishListener(new OnFinishLoadStringListener(){

			@Override
			public void onFinish(String responseStr) {
				// TODO Auto-generated method stub
				/* write your code here */
			}

	    })        	
	    .setFormBody(formBody)
	    .post();
```

>**Note:**
>RequestBody 是 OkHttp所定義的Class，可以依序將Form的資料依序填入。詳情請參考[官網][2]。


####Cancel Task
```java
GetTask *getTask = JsonCake.setUrl("your_url")
						   .setOnFinishListener(...)
						   .get();

/* your cord... */

getTask.cancel();
```

Download
---
```xml
repositories {
    maven {
        url  "http://dl.bintray.com/badu/maven"
    }
}

...

dependencies {
	...
    compile 'com.crazyma.jsoncake:jsoncake:1.0.3'
}
```
Used dependencies
---
此Library所使用的dependencies：
```xml
compile 'com.squareup.okhttp:okhttp:2.4.0'
compile 'com.google.code.gson:gson:2.3.1'
```

Required Android Permission
--------
	<uses-permission android:name="android.permission.INTERNET">

License
-------
	Copyright 2016 David Ma

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.


[1]: https://code.google.com/p/google-gson/
[2]: http://square.github.io/okhttp/
[3]: http://goo.gl/2xXqjA
