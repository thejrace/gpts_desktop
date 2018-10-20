/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Employee {

    private String mName, mNick, mEmail, mID, mGroup, mPhone1, mPhone2;
    private int mWorkStatus, mWorkCount;
    private String mReturnText;

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
            mGroup = data.getString("employee_group");
            mWorkStatus = data.getInt("work_status");
            mWorkCount = data.getInt("work_count");
        } catch( JSONException e ){
            e.printStackTrace();
        }
    }
    public void add( String name, String nick, String email, String group, String phone1, String phone2, ActionCallback cb ){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim", name, FormValidation.CHECK_REQ ),
                new ValidationInput("Rumuz", nick, FormValidation.CHECK_REQ ),
                new ValidationInput("Email", email, new int[]{ FormValidation.CHECK_REQ, FormValidation.CHECK_EMAIL } ),
                new ValidationInput("Üye Grubu", group, FormValidation.CHECK_REQ )
        });
        if( !inputCheck ){
            mReturnText = validation.getMessage();
            cb.onError(ActionStatusCode.VALIDATION_ERROR);
            return;
        }
        PopupLoader.show(PopupLoader.PLEASE_WAIT);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name );
                params.put("nick", nick );
                params.put("email", email );
                params.put("employee_group", group );
                params.put("phone_1", phone1 );
                params.put("phone_2", phone2 );
                params.put("req", "add_employee" );
                WebRequest req = new WebRequest( WebRequest.SERVICE_URL, params );
                req.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        mReturnText = output.getString(WebRequest.RETURN_TEXT);
                        if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                            Platform.runLater(() -> {
                                // data is lastInsertedId
                                mID = output.getString("data");
                                mName = name;
                                mNick = nick;
                                mEmail = email;
                                mGroup = group;
                                mPhone1 = phone1;
                                mPhone2 = phone2;
                                cb.onSuccess( output.getString("data") );
                            });
                        } else {
                            Platform.runLater(() -> {
                                cb.onError( ActionStatusCode.ERROR );
                            });
                        }
                    }
                });

            }
        });
        th.setDaemon(true);
        th.start();
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

    public void downloadWorks( ReadJACallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("req", "employee_works_download");
                params.put("employee_id", mID );
                WebRequest request = new WebRequest( WebRequest.SERVICE_URL, params );
                request.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        try {
                            cb.onFinish(output.getJSONArray("data"));
                        } catch( JSONException e ){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();
    }



    public static void search( String keyword, ReadJACallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("keyword", keyword );
                params.put("req", "employees_search");
                WebRequest request = new WebRequest( WebRequest.SERVICE_URL, params );
                request.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        try {
                            cb.onFinish(output.getJSONArray("data"));
                        } catch( JSONException e ){
                            e.printStackTrace();
                        }
                    }
                });
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
    public String getPhone1(){
        return mPhone1;
    }
    public String getPhone2(){
        return mPhone2;
    }
    public int getWorkStatus(){
        return mWorkStatus;
    }
    public int getWorkCount(){
        return mWorkCount;
    }
    public String getReturnText(){
        return mReturnText;
    }
    public void setID( String id ){
        mID = id;
    }
}
