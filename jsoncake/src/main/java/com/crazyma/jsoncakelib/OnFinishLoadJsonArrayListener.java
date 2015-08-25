package com.crazyma.jsoncakelib;

import org.json.JSONArray;

public interface OnFinishLoadJsonArrayListener extends OnFinishListener{
	public void onFinish(JSONArray jsonArray);
}
