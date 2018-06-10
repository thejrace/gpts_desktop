package gpts.java;

import org.json.JSONObject;

public interface WebRequestCallback {

    void onFinish(JSONObject output);

}
