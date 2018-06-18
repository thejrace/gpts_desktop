/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// daily plan data class
public class DailyPlanSchema {

    private String mStart, mEnd, mName, mID, mPlanInterval;
    private ArrayList<String> mPlanTable;


    private String mReturnText;

    public DailyPlanSchema( String id ){
        mID = id;
    }

    public DailyPlanSchema(){

    }
    public DailyPlanSchema( String id, String name, String start, String end, String planInterval ){
        mName = name;
        mStart = start;
        mEnd = end;
        mPlanInterval = planInterval;
        mID = id;
    }

    public boolean update( String name, String start, String end, String planInterval ){

        return false;
    }

    public void add( String name, String start, String end, String planInterval, ActionCallback cb ){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim", name, FormValidation.CHECK_REQ ),
                new ValidationInput("Başlangıç", start, new int[]{ FormValidation.CHECK_REQ, FormValidation.CHECK_HOUR_FORMAT } ),
                new ValidationInput("Bitiş", end, new int[]{ FormValidation.CHECK_REQ, FormValidation.CHECK_HOUR_FORMAT } ),
                new ValidationInput("Aralık", planInterval, new int[]{ FormValidation.CHECK_REQ, FormValidation.CHECK_NUMERIC } )
        });
        if( !inputCheck ){
            mReturnText = validation.getMessage();
            cb.onError(ActionStatusCode.VALIDATION_ERROR);
            return;
        }
        // show before thread
        PopupLoader.show(PopupLoader.PLEASE_WAIT);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                // send request
                Map<String, String> params = new HashMap<>();
                params.put("name", name );
                params.put("start", start );
                params.put("end", end);
                params.put("plan_interval", planInterval );
                params.put("req", "add_daily_plan_schema" );
                WebRequest req = new WebRequest( WebRequest.SERVICE_URL, params );
                req.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        mReturnText = output.getString(WebRequest.RETURN_TEXT);
                        if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                            Platform.runLater(() -> {
                                // data is lastInsertedId
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

    public String getReturnText(){
        return mReturnText;
    }

    public boolean delete(){
        return false;
    }

    public String getName(){
        return mName;
    }
    public String getStart(){
        return mStart;
    }
    public String getEnd(){
        return mEnd;
    }
    public String getID(){
        return mID;
    }
    public String getPlanInterval(){
        return mPlanInterval;
    }

}
