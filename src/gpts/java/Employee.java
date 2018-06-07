package gpts.java;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee {

    private String mName, mNick, mEmail, mID, mGroupID;

    // empty constructor
    public Employee(){
    }

    // set constructor with data
    public Employee( JSONObject data ){
        try {
            mName = data.getString("name");
            mNick = data.getString("nick");
            mEmail = data.getString("email");
            mID = data.getString("id");
            mGroupID = data.getString("group_id");
        } catch( JSONException e ){
            e.printStackTrace();
        }
    }
    public String getName(){
        return mName;
    }
    public String getNick(){
        return mNick;
    }
    public String getEmail(){
        return mEmail;
    }
    public String getID(){
        return mID;
    }
    public String getGroupID(){
        return mGroupID;
    }

}
