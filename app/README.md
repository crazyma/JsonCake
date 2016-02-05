
Welcome to JsonCake
=======

這是一個Android Library，方便developer開發Android App時，快速執行下載或上傳**Json file**，讓你使用Json進行資料傳輸有如**piece of cake**。



Update[2016/02/05]
---
有鑒於RxJava/RxAndroid流行，發現很多東西直接用RxJava更方便且更有彈性，所以本Library就直接閹割大部份的功能，包含`Gson Package`, `Custom Listener`...等等。
新的`JsonCake`直接定義一個 `Observable.OnSubscribe`讓你可以直接下載 **Json File** 

Sample
---
``` java
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
    compile 'com.crazyma.jsoncake:jsoncake:2.0.2'
}
```
Used dependencies
---
此Library所使用的dependencies：
```xml
	compile 'com.squareup.okhttp3:okhttp:3.0.1'
    compile 'io.reactivex:rxjava:1.1.0'
```

Required Android Permission
--------
	<uses-permission android:name="android.permission.INTERNET">


-------


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