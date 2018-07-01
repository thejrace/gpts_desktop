/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONException;
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

    // create object with downloaded data
    public DailyPlanSchema( JSONObject data ){
        try {
            mName = data.getString("name");
            mStart = data.getString("start");
            mEnd = data.getString("end");
            mPlanInterval = data.getString("plan_interval");
            mID = data.getString("id");
        } catch (JSONException e ){
            e.printStackTrace();
        }
    }

    public boolean update( String name, String start, String end, String planInterval ){

        return false;
    }

    public void add( String name, String start, String end, String planInterval, ActionCallback cb ){
        PopupLoader.show(PopupLoader.PLEASE_WAIT);
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
                                mID = output.getString("data");
                                mName = name;
                                mStart = start;
                                mEnd = end;
                                mPlanInterval = planInterval;
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
    *  - generates daily plan rows for given start, end and interval values of schema
    * */
    public void generatePlan(){
        // first explode time formatted parameters
        String[] startExploded = mStart.split(":");
        String[] endExploded = mEnd.split(":");
        // convert them to int
        int startHour = Integer.valueOf( startExploded[0] );
        int startMin = Integer.valueOf( startExploded[1] );
        int endHour = Integer.valueOf( endExploded[0] );
        int endMin = Integer.valueOf( endExploded[1] );
        ArrayList<String> testPrint = new ArrayList<>();
        // if there's a condition that we stuck in infinit loop
        // implemented a guard ( not ideal but until all tests are done it's good to have it )
        int loopCounter = 0;
        // calculated hour and min data for loop
        int nextHour, nextMin;
        // we keep the previous nextHour:nextMin to use it
        // as a start for the next calculation
        String prevHourStr;
        // main loop guard
        boolean breakFlag = false,
                // if startHour > endHour we raise this flag.
                // to use same algorithm for both normal and reverse cases
                // we get help from these two flags to figure out what to do in the loop
                reverseFlag = false,
                // if we have reverse flag, means that we multiplied startHour with -1.
                // but then we cannot use it for calculations, so if we did that
                // we make startHour positive again and after calculations we reverse it
                // back to negative
                positived = false;
        // if we have same hours in start and end
        // we shorten the algorithm by not calculating nextHour
        if( startHour == endHour ){
            // initialization of loop varibles
            nextHour = startHour;
            nextMin = startMin;
            prevHourStr = mStart;
            while( !breakFlag ){
                nextMin += Integer.valueOf(mPlanInterval);
                // if we pass the endMin, break the loop
                if( nextMin >= endMin ){
                    breakFlag = true;
                    // set last rows end to endMin ( limit )
                    nextMin = endMin;
                }
                testPrint.add( prevHourStr + " - " + nextHour + ":" + nextMin );
                prevHourStr = nextHour + ":" + nextMin;
            }
        // this else block is main algorithm
        } else {
            // if start is 00:00 we make it 24
            // because every other hour is bigger than 0 so algorithm breaks
            // after first break check
            if( startHour == 0 ) startHour = 24;
            // reverse flag control
            if( startHour > endHour ){
                startHour *= -1;
                reverseFlag = true;
            }
            // regular counting - init vars
            nextHour = startHour;
            nextMin  = startMin;
            prevHourStr = mStart;
            while( !breakFlag ){
                // each iteration add interval to nextMin
                nextMin += Integer.valueOf(mPlanInterval);
                // if we pass the 60 mins, calculate nextHour
                if( nextMin >= 60 ){
                    // if reverseFlag is set, nextHour will be negative
                    // make it positiove and do calculations
                    if( nextHour < 0 ){
                        nextHour *= -1;
                        positived = true;
                    }
                    nextHour += nextMin / 60;
                    nextMin = nextMin % 60;
                    // after 23 turn back to 00
                    if( nextHour >= 24 ) nextHour = 24 - nextHour;
                    // if we positived the nextHour make it negative again
                    if( positived ){
                        nextHour *= -1;
                        positived = false;
                    }
                } else {
                    // for both normal and reversed cases, if nextMin + interval
                    // does not passes the 60, we again make sure we dont pass 23
                    if( nextHour == 24 ) nextHour = 24 - nextHour;
                    if( nextHour == -24 ) nextHour = 24 + nextHour;
                }
                // first break check
                // if nextHour:nextMin = mEnd
                if( (Common.convertTimeFormat(nextHour) + ":" + Common.convertTimeFormat(nextMin)).equals(mEnd) ){
                    breakFlag = true;
                } else {
                    if( !reverseFlag ){
                        // second break check for normal cases
                        // if we pass the mEnd
                        if(TimeDifference.isPast( (nextHour + ":" + nextMin), mEnd )) breakFlag = true;
                    } else {
                        // second break check for reversed cases
                        // if nextHour > endHour
                        if( nextHour > endHour ){
                            breakFlag = true;
                        // second break check part 2 for reversed cases
                        } else if( nextHour == endHour ){
                            // if nextHour == endHour doesn't always mean we should break from loop.
                            // interval + nextMin might not pass the endMin. so we have to continue to loop until we pass mEnd
                            if(TimeDifference.isPast( (nextHour + ":" + nextMin), mEnd )) breakFlag = true;
                        }
                    }
                }
                // common break action
                if( breakFlag ){
                    nextMin = endMin;
                    nextHour = endHour;
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
