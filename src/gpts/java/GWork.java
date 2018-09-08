package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.ui.GWorkSubItemBox;

import java.util.Map;

public class GWork {

    private int mID, mStatus;
    private String mName, mDetails, mReturnText;

    public GWork(){

    }

    /*
    *  add
    * */
    public void add(String name, String details, Map<Integer, GWorkSubItemBox> subItems, ActionCallback cb ){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim", name, FormValidation.CHECK_REQ )
        });
        if( !inputCheck ){
            mReturnText = validation.getMessage();
            cb.onError(ActionStatusCode.VALIDATION_ERROR);
            return;
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void removeSubItem( int subItemID ){

    }

    public String getReturnText(){
        return mReturnText;
    }

}
