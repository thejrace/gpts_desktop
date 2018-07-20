package gpts.java;

import gpts.java.interfaces.WebRequestCallback;
import org.json.JSONException;
import org.json.JSONObject;

public class GTask {

    private String mID, mGroup, mName, mType, mDefinition;

    public GTask( JSONObject data ){
        try {
            mID = data.getString("id");
            mGroup = data.getString("group");
            mName = data.getString("name");
            mType = data.getString("type");
            mDefinition = data.getString("definition");
        } catch( JSONException e ){
            e.printStackTrace();
        }
    }

    public GTask(){

    }

    public void downloadData( WebRequestCallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(500);
                } catch( InterruptedException e ){
                    e.printStackTrace();
                }
                cb.onFinish( new JSONObject() );
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public String getName(){
        return mName;
    }
    public String getID(){
        return mID;
    }
    public String getGroup(){
        return mGroup;
    }
    public String getDefinition(){
        return mDefinition;
    }
    public String getType(){
        return mDefinition;
    }


}
