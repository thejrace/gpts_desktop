package gpts.java;

public class GWorkDefinitionData {

    private boolean mPeriodicFlag = false;
    private String mDueDate;
    // below data used in periodic def
    private int mPDueDate;
    private int mPPeriod;

    // dk - saat - gün - ay - yıl
    private int[] mIntervalCoeffs = { 60, 24, 30, 12 };

    public GWorkDefinitionData(){

    }

    public void setPeriodicFlag( boolean d ){
        mPeriodicFlag = d;
    }
    public void setDueDate( String datePickerVal, String hVal, String mVal ){
        if( Integer.valueOf(hVal) > 23 || Integer.valueOf(hVal) < 0 ) hVal = "00";
        if( Integer.valueOf(mVal) > 59 || Integer.valueOf(mVal) < 0 ) mVal = "00";
        mDueDate = Common.revDateServer(datePickerVal, "-") + " " + hVal + ":" + mVal + ":00";
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
    public int getmPPeriod(){
        return mPPeriod;
    }
    public boolean getPeriodicFlag(){
        return mPeriodicFlag;
    }
    @Override
    public String toString(){
        return "mPeriodicFlag: " + mPeriodicFlag + "\n" +
                "mDueDate: " + mDueDate + "\n" +
                "mPDueDate: " + mPDueDate + "\n" +
                "mPPeriod: " + mPPeriod;
    }
}
