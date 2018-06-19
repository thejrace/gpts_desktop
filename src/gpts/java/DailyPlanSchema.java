/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONObject;

import java.sql.Time;
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

    public void generatePlan(){
        String[] startExploded = mStart.split(":");
        String[] endExploded = mEnd.split(":");
        int startHour = Integer.valueOf( startExploded[0] );
        int startMin = Integer.valueOf( startExploded[1] );
        int endHour = Integer.valueOf( endExploded[0] );
        int endMin = Integer.valueOf( endExploded[1] );
        ArrayList<String> testPrint = new ArrayList<>();
        int loopCounter = 0;
        int nextHour, nextMin;
        String prevHourStr;
        boolean breakFlag = false,
                reverseFlag = false,
                positived = false;
        if( startHour == endHour ){
            nextHour = startHour;
            nextMin = startMin;
            prevHourStr = mStart;
            while( !breakFlag ){
                nextMin += Integer.valueOf(mPlanInterval);
                if( nextMin >= endMin ){
                    breakFlag = true;
                    nextMin = endMin;
                }
                testPrint.add( prevHourStr + " - " + nextHour + ":" + nextMin );
                prevHourStr = nextHour + ":" + nextMin;
            }
        } else {
            if( startHour == 0 ) startHour = 24;
            if( startHour > endHour ){
                startHour *= -1;
                reverseFlag = true;
            }
            // regular counting
            nextHour = startHour;
            nextMin  = startMin;
            prevHourStr = mStart;
            while( !breakFlag ){
                nextMin += Integer.valueOf(mPlanInterval);
                if( nextMin >= 60 ){
                    if( nextHour < 0 ){
                        nextHour *= -1;
                        positived = true;
                    }
                    nextHour += nextMin / 60;
                    nextMin = nextMin % 60;
                    if( nextHour >= 24 ) nextHour = 24 - nextHour;
                    if( positived ){
                        nextHour *= -1;
                        positived = false;
                    }
                } else {
                    if( nextHour == 24 ) nextHour = 24 - nextHour;
                    if( nextHour == -24 ) nextHour = 24 + nextHour;
                }
                if( (Common.convertTimeFormat(nextHour) + ":" + Common.convertTimeFormat(nextMin)).equals(mEnd) ){
                    nextMin = endMin;
                    nextHour = endHour;
                    breakFlag = true;
                } else {
                    if( !reverseFlag ){
                        if(TimeDifference.isPast( (nextHour + ":" + nextMin), mEnd )){
                            nextMin = endMin;
                            nextHour = endHour;
                            breakFlag = true;
                        }
                    } else {
                        if( nextHour > endHour ){
                            nextMin = endMin;
                            nextHour = endHour;
                            breakFlag = true;
                        } else if( nextHour == endHour ){
                            if(TimeDifference.isPast( (nextHour + ":" + nextMin), mEnd )){
                                nextMin = endMin;
                                nextHour = endHour;
                                breakFlag = true;
                            }
                        }
                    }
                }
                testPrint.add(prevHourStr + " - " + Common.convertTimeFormat(nextHour) + ":" + Common.convertTimeFormat(nextMin));
                prevHourStr = Common.convertTimeFormat(nextHour) + ":" + Common.convertTimeFormat(nextMin);
                loopCounter++;
                if( loopCounter == 150 ) break;
            }
        }
        System.out.println("######### " + mStart + " - " + mEnd + " #########");
        for( String print : testPrint ) System.out.println(print);
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
