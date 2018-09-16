/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import org.json.JSONException;
import org.json.JSONObject;

public class GWorkSubItem {

    public static int STATUS_WAITING = 0,
                      STATUS_ACTIVE = 1,
                      STATUS_WAITS_VALIDATION = 2,
                      STATUS_CANCELED = 3,
                      STATUS_COMPLETED = 4,
                      STATUS_EXPIRED = 5;
    private String mName, mDetails, mReturnText, mDateAdded, mDateLastModified;
    private int mID, mStatus, mStepOrder, mNeedsValidation;

    public GWorkSubItem(){

    }

    public GWorkSubItem( JSONObject data ){
        try {
            mName = data.getString("name");
            mID = Integer.valueOf(data.getString("id"));
            mDetails = data.getString("details");
            mStepOrder = Integer.valueOf(data.getString("step_order"));
            mNeedsValidation = Integer.valueOf(data.getString("needs_validation"));
            mStatus = Integer.valueOf(data.getString("status"));
            mDateAdded = data.getString("date_added");
            mDateLastModified = data.getString("date_last_modified");
        } catch( JSONException e ){
            //e.printStackTrace();
        }

    }

    public boolean validate(){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim [ Adım No: "+mStepOrder+"]", mName, FormValidation.CHECK_REQ )
        });
        if( !inputCheck ) mReturnText = validation.getMessage();
        return inputCheck;
    }

    /*
    *  newWorkFlag = true, when adding sub item to not-existing work
    * */
    public void add( boolean newWorkFlag, String parentGWorkID, String name, String details, int status, int stepOrder ){
        if( newWorkFlag ){
            // this case we only save data to properties, later we'll serialize it to send
            // them together with parent GWork data
            mName = name;
            mDetails = details;
            mStepOrder = stepOrder;
            mStatus = status;
        } else {
            // using parentGWorkID send form to server

        }

    }

    public String serialize(){
        return "id="+mID+"#name="+mName+"#details="+mDetails+"#step_order="+mStepOrder+"#status="+mStatus;
    }

    public void setID( int d ){
        mID = d;
    }
    public void setName( String d ){
        mName = d;
    }
    public void setStatus( int d ){ mStatus = d; }
    public void setDetails( String d ){
        mDetails = d;
    }
    public void setStepOrder( int d ){
        mStepOrder = d;
    }
    public String getReturnText(){
        return mReturnText;
    }
    public int getID(){
        return mID;
    }
    public String getName(){
        return mName;
    }
    public String getDetails(){
        return mDetails;
    }
    public int getStatus(){
        return mStatus;
    }
    public int getStepOrder(){
        return mStepOrder;
    }
    public int getNeedsValidation(){
        return mNeedsValidation;
    }


}
