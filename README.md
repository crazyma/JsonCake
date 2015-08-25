
Welcome to JsonCake
=======

這是一個Android Library，方便developer開發Android App時，快速執行下載或上傳**Json file**，讓你使用Json進行資料傳輸有如**piece of cake**。

本Library使用了 [OKHttp][2] 以及 [Gson][1] 協助處理相關的操作。


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
|onTaskFailListener|OnTaskFailListener|Task 失敗時呼叫的Listener。|
|formBody|RequestBody|Http post所使用。是[OkHttp][2]所定義的類別。|
|objectType|Type|類別型態。配合[Gson][1]使用。|


Sample Code
----
#### Load JSONObject Object
```java
new JsonCake.Builder()
	.setUrl("your_json_file_url")
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
new JsonCake.Builder()
	.setUrl("your_json_file_url")
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
new JsonCake.Builder()
	.setUrl("your_json_file_url")
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
new JsonCake.Builder()
	.setUrl("your_json_file_url")
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
        
new JsonCake.Builder()
	.setUrl("your_url")
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
    compile 'com.crazyma.jsoncakelib:jsoncake:1.0.1'
}
```
Used dependencies
---
此Library所使用的dependencies：
```xml
compile 'com.android.support:appcompat-v7:22.2.1'
compile 'com.squareup.okhttp:okhttp:2.4.0'
compile 'com.google.code.gson:gson:2.3.1'
```

Required Android Permission
--------
	<uses-permission android:name="android.permission.INTERNET">

License
-------
	Copyright 2015 David Ma

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