package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GWorkDefinitionData {

    private boolean mPeriodicFlag = false;
    private String mDueDate;
    private String mStartDate;
    private String mReturnText;
    // below data used in periodic def
    private int mPDueDate;
    private int mPPeriod;

    // dk - saat - gün - ay - yıl
    private int[] mIntervalCoeffs = { 60, 24, 30, 12 };

    public GWorkDefinitionData(){

    }
    /*
    *  @dataKey : employee_group_name or employee_id
    *  @dataVal : emp id or emp group name
    * */
    public void defineToEmpOrGroup( String dataKey, String dataVal, GWork workTemplate, ActionCallback cb ){

        Map<String, String> params = new HashMap<>();
        params.put("req", "define_work_to");
        params.put(dataKey, dataVal);
        if( mPeriodicFlag ) {
            params.put("periodic_flag", "1");
            params.put("due_date", String.valueOf(mPDueDate) );
            params.put("name", "");
            params.put("details", "");
            params.put("sub_items_encoded", "");
        } else {
            params.put("periodic_flag", "0");
            params.put("due_date", mDueDate );
            params.put("name", workTemplate.getName());
            params.put("details", workTemplate.getDetails());
            ArrayList<String> subTemp = new ArrayList<>();
            for( GWorkSubItem subItem : workTemplate.getSubItems() ) subTemp.add( subItem.serialize() );
            params.put("sub_items_encoded", Common.stringArrayListJoin(subTemp, "|"));
        }
        params.put("start_date", mStartDate);
        params.put("define_interval", String.valueOf(mPPeriod));
        params.put("work_template_id", String.valueOf(workTemplate.getID()));
        WebRequest req = new WebRequest(WebRequest.SERVICE_URL, params );
        req.actionAsync(new WebRequestCallback() {
            @Override
            public void onFinish(JSONObject output) {
                mReturnText = output.getString(WebRequest.RETURN_TEXT);
                if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                    Platform.runLater(() -> {
                        cb.onSuccess("");
                    });
                } else {
                    Platform.runLater(() -> {
                        // clear sub items data if there is an error
                        cb.onError( ActionStatusCode.ERROR );
                    });
                }
            }
        });
    }
    public void setPeriodicFlag( boolean d ){
        mPeriodicFlag = d;
    }
    public void setStartDate( String datePickerVal, String hVal, String mVal ){
        mStartDate = setDate( datePickerVal, hVal, mVal );
    }
    public void setDueDate( String datePickerVal, String hVal, String mVal ){
        mDueDate = setDate( datePickerVal, hVal, mVal );
    }
    private String setDate( String datePickerVal, String hVal, String mVal ){
        if( hVal.equals("") || Integer.valueOf(hVal) > 23 || Integer.valueOf(hVal) < 0 ) hVal = "00";
        if( mVal.equals("") || Integer.valueOf(mVal) > 59 || Integer.valueOf(mVal) < 0 ) mVal = "00";
        return datePickerVal + " " + hVal + ":" + mVal + ":00";
    }
    public void setPDueDate( int val, int intervalListItemIndex ){
        mPDueDate = calculateValAsMins( val, intervalListItemIndex );
    }
    public void setPPeriod( int val, int intervalListItemIndex ){
        mPPeriod = calculateValAsMins( val, intervalListItemIndex );
    }
    private int calculateValAsMins( int val, int intervalListItemIndex ){
        for( int k = intervalListItemIndex - 1; k >= 0; k-- ) val *= mIntervalCoeffs[k];
        return val;
    }
    public String getDueDate(){
        return mDueDate;
    }
    public int getPDueDate(){
        return mPDueDate;
    }
    public int getPPeriod(){
        return mPPeriod;
    }
    public String getStartDate(){
        return mStartDate;
    }
    public boolean getPeriodicFlag(){
        return mPeriodicFlag;
    }
    public String getReturnText(){
        return mReturnText;
    }
    @Override
    public String toString(){
        return "mPeriodicFlag: " + mPeriodicFlag + "\n" +
                "mStartDate: " + mStartDate + "\n" +
                "mDueDate: " + mDueDate + "\n" +
                "mPDueDate: " + mPDueDate + "\n" +
                "mPPeriod: " + mPPeriod;
    }
}
