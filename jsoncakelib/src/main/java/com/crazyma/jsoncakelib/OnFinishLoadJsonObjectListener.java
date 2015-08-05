package com.crazyma.jsoncakelib;

import org.json.JSONObject;


public interface OnFinishLoadJsonObjectListener extends OnFinishListener{
	public void onFinish(JSONObject jsonObject);
}
