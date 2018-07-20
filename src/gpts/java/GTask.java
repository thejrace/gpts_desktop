package gpts.java;

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
