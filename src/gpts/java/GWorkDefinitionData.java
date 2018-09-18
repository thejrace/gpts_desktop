package gpts.java;

import gpts.java.interfaces.ActionCallback;

public class GWorkDefinitionData {

    private boolean mPeriodicFlag = false;
    private String mDueDate;
    private String mStartDate;
    // below data used in periodic def
    private int mPDueDate;
    private int mPPeriod;

    // dk - saat - gün - ay - yıl
    private int[] mIntervalCoeffs = { 60, 24, 30, 12 };

    public GWorkDefinitionData(){

    }
    public void defineToEmpOrGroup( boolean empGroupFlag, int defToID, GWork workTemplate, ActionCallback cb ){
        if( empGroupFlag ){

        } else {

        }
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
        return Common.revDateServer(datePickerVal, "-") + " " + hVal + ":" + mVal + ":00";
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
    @Override
    public String toString(){
        return "mPeriodicFlag: " + mPeriodicFlag + "\n" +
                "mStartDate: " + mStartDate + "\n" +
                "mDueDate: " + mDueDate + "\n" +
                "mPDueDate: " + mPDueDate + "\n" +
                "mPPeriod: " + mPPeriod;
    }
}
