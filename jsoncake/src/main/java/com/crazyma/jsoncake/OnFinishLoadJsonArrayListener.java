package com.crazyma.jsoncake;

import org.json.JSONArray;

public interface OnFinishLoadJsonArrayListener extends OnFinishListener{
	public void onFinish(JSONArray jsonArray);
}
