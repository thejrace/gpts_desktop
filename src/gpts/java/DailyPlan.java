package gpts.java;

public class DailyPlan {

    private int mPlanOrder;
    private String mStart, mEnd, mStatus, mID;
    private String mReturnText;

    public DailyPlan(){

    }
    public DailyPlan( int planOrder, String start, String end, String status ){
        mPlanOrder = planOrder;
        mStart = start;
        mEnd = end;
        mStatus = status;
    }
    public DailyPlan( int id ){

    }

    public String getReturnText(){
        return mReturnText;
    }

    public boolean delete(){
        return false;
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
    public int getTimeLength(){
        return TimeDifference.calculateRound(mStart, mEnd);
    }

}
