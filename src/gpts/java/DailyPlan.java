/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import java.util.ArrayList;

public class DailyPlan {

    private String mStart, mFinish, mName, mID, mInterval;
    private ArrayList<String> mPlanTable;


    private String mReturnText;

    public DailyPlan( String id ){
        mID = id;
    }

    public DailyPlan(){

    }

    public boolean update( String name, String start, String finish, String interval ){

        return false;
    }

    public boolean add( String name, String start, String finish, String interval ){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim", name, FormValidation.CHECK_REQ ),
                new ValidationInput("Başlangıç", start, FormValidation.CHECK_REQ ),
                new ValidationInput("Bitiş", finish, FormValidation.CHECK_REQ ),
                new ValidationInput("Aralık", interval, new int[]{ FormValidation.CHECK_REQ, FormValidation.CHECK_NUMERIC } )
        });
        if( !inputCheck ){
            mReturnText = validation.getMessage();
            return false;
        }
        mReturnText = "İşlem başarılı.";
        return true;
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
    public String getFinish(){
        return mFinish;
    }
    public String getID(){
        return mID;
    }
    public String getInterval(){
        return mInterval;
    }

}
