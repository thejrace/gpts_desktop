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

// daily plan schema data class
public class DailyPlanSchema {

    private String mStart, mEnd, mName, mID, mPlanInterval;
    private ArrayList<DailyPlan> mPlanTable;


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

    /*
    *  - creates DailyPlan rows for schema
    * */
    public void generatePlan(){
        // explode timeformats
        String[] startExploded = mStart.split(":");
        String[] endExploded = mEnd.split(":");
        // get them as integers
        int startHours = Integer.valueOf(startExploded[0]);
        int endHours = Integer.valueOf( endExploded[0] );
        int startMins = Integer.valueOf( startExploded[1] );
        int endMins = Integer.valueOf( endExploded[1] );
        // vars for loop
        int nextHour = startHours,
            nextMin = startMins,
            minCounter = startMins + Integer.valueOf(mPlanInterval);
        String prevHourStr = mStart;
        boolean breakFlag = false;
        while( !breakFlag ){
            if( minCounter >= 60 ){
                // if previous min + interval bigger than 60
                // next min will be mod of it
                nextMin = minCounter % 60;
                // we increment hour by how many 60 mins in total min
                nextHour += minCounter / 60;
                // after 24:00 go back to 01..
                if( nextHour >= 24 ) nextHour = 24 - nextHour;
                // if we pass the end and adding interval passes the end, break the loop
                if( nextHour == endHours && nextMin > endMins ){
                    nextMin = endMins;
                    breakFlag = true;
                }
                // if check above fails for nextMin > endMin means that
                // there is a time left which is smaller than interval
                // we add a last orer item for it
                if( nextHour > endHours ){
                    nextMin = endMins;
                    // next hour is incremented in last iteration if we're in here
                    // we make it equal to endHour
                    nextHour = endHours;
                    breakFlag = true;
                }
            }
            System.out.println( prevHourStr + " - " + Common.convertTimeFormat(nextHour) + ":" + Common.convertTimeFormat(nextMin) );
            // min counter is always = previous_min + interval
            // we set here for next iteration
            minCounter = nextMin + Integer.valueOf(mPlanInterval);
            prevHourStr = Common.convertTimeFormat(nextHour) + ":" + Common.convertTimeFormat(nextMin);
        }
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
