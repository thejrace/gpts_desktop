package gpts.java;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee {

    private String mName, mNick, mEmail, mID, mGroup;
    private int mTaskCount, mTaskStatus;

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
            mGroup = data.getString("group");
            mTaskCount = data.getInt("task_count");
            mTaskStatus = data.getInt("task_status");
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
    public String getGroup(){
        return mGroup;
    }
    public int getTaskCount(){
        return mTaskCount;
    }
    public int getTaskStatus(){
        return mTaskStatus;
    }

}
