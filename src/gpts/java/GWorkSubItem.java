package gpts.java;

import org.json.JSONException;
import org.json.JSONObject;

public class GWorkSubItem {

    private String mName, mDetails;
    private int mID, mStatus, mStepOrder, mNeedsValidation;

    public GWorkSubItem(){

    }

    public GWorkSubItem( JSONObject data ){
        try {

        } catch( JSONException e ){
            e.printStackTrace();
        }
    }

    /*
    *  newWorkFlag = true, when adding sub item to not-existing work
    * */
    public void add( boolean newWorkFlag, String parentGWorkID, String name, String details, int stepOrder ){
        if( newWorkFlag ){
            // this case we only save data to properties, later we'll serialize it to send
            // them together with parent GWork data
            mName = name;
            mDetails = details;
            mStepOrder = stepOrder;
        } else {
            // using parentGWorkID send form to server


        }

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
