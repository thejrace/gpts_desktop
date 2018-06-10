/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.WebRequestCallback;
import org.json.JSONException;
import org.json.JSONObject;

public class Employee {

    private String mName, mNick, mEmail, mID, mGroup;
    private int mTaskCount, mTaskStatus;

    // empty constructor
    public Employee(){
    }

    public Employee( String id ){
        mID = id;
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

    public void downloadPlan( WebRequestCallback cb ){
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
