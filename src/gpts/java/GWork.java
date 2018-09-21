/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.GWorkSubItemBox;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
*  GWork class is used for both "actual works" and "work templates"
*  Essentially both types are the same but on work templates onlt 'name', 'details' and 'subItems'
*  props are used.
*
* */

public class GWork {

    public static int STATUS_ACTIVE = 0,
                      STATUS_COMPLETED = 1,
                      STATUS_EXPIRED = 2,
                      STATUS_CANCELED = 3;

    private boolean mTemplateFlag = false;
    private int mID, mStatus;
    private double mPercentageCompleted;
    private String mName, mDetails, mReturnText, mDateAdded = "", mDueDate = "Yok", mDateLastModified = "";

    private String mSubItemsEncoded = ""; // used to send subItem data to server as an encodedSTring
    private ArrayList<GWorkSubItem> mSubItems = new ArrayList<>();

    public GWork(){

    }

    public GWork( JSONObject data ){
        // test only for status to set templateFlag
        try {
            mStatus = Integer.valueOf(data.getString("status"));
        } catch( JSONException e ){
            mTemplateFlag = true;
            //e.printStackTrace();
        }
        if( mTemplateFlag ){
            try{
                mID = Integer.valueOf(data.getString("id"));
                mName = data.getString("name");
                mDetails = data.getString("details");
                JSONArray subItemsDecoded = data.getJSONArray("sub_items");
                GWorkSubItem subItemTemp;
                for( int k = 0; k < subItemsDecoded.length(); k++ ){
                    subItemTemp = new GWorkSubItem( subItemsDecoded.getJSONObject(k));
                    subItemTemp.setTemplateFlag(true);
                    mSubItems.add( subItemTemp );
                }
            } catch( JSONException e ){
                e.printStackTrace();
            }
        } else {
            try{
                mID = Integer.valueOf(data.getString("id"));
                mName = data.getString("name");
                mDetails = data.getString("details");
                JSONArray subItemsDecoded = data.getJSONArray("sub_items");
                for( int k = 0; k < subItemsDecoded.length(); k++ ) mSubItems.add( new GWorkSubItem( subItemsDecoded.getJSONObject(k)));
                mDateAdded = data.getString("date_added");
                mStatus = Integer.valueOf(data.getString("status"));
                mDateAdded = data.getString("date_added");
                mDateLastModified = data.getString("date_last_modified");
            } catch( JSONException e ){
                e.printStackTrace();
            }
        }
    }

    /*
    *
    * */
    private void sendDataToServer( String req, String name, String details, int status, Map<Integer, GWorkSubItemBox> subItems, WebRequestCallback wcb ,ActionCallback cb ){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim", name, FormValidation.CHECK_REQ )
        });
        if( !inputCheck ){
            mReturnText = validation.getMessage();
            cb.onError(ActionStatusCode.VALIDATION_ERROR);
            return;
        }
        mSubItems.clear();
        PopupLoader.show(PopupLoader.PLEASE_WAIT);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if( !encodeSubItems( subItems ) ){
                    Platform.runLater( () -> cb.onError(ActionStatusCode.VALIDATION_ERROR) );
                    return;
                }
                // send request
                Map<String, String> params = new HashMap<>();
                params.put("item_id", String.valueOf(mID));
                params.put("name", name );
                params.put("details", details );
                if( !mTemplateFlag ){ // status not needed for templates
                    params.put("status", String.valueOf(status) );
                }
                params.put("sub_items_encoded", mSubItemsEncoded );
                params.put("req", req );
                WebRequest req = new WebRequest( WebRequest.SERVICE_URL, params );
                req.action(wcb);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void addTemplate( String name, String details, Map<Integer, GWorkSubItemBox> subItems, ActionCallback cb ){
        mTemplateFlag = true;
        sendDataToServer("add_work_template", name, details, 0, subItems, new WebRequestCallback() {
            @Override
            public void onFinish(JSONObject output) {
                mReturnText = output.getString(WebRequest.RETURN_TEXT);
                if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                    Platform.runLater(() -> {
                        mName = name;
                        mDetails = details;
                        mID = Integer.valueOf(output.getString("data"));
                        cb.onSuccess( output.getString("data") );
                    });
                } else {
                    Platform.runLater(() -> {
                        // clear sub items data if there is an error
                        mSubItems = new ArrayList<>();
                        cb.onError( ActionStatusCode.ERROR );
                    });
                }
            }
        }, cb );
    }

    public void editTemplate( String name, String details, Map<Integer, GWorkSubItemBox> subItems, ActionCallback cb ){
        mTemplateFlag = true;
        sendDataToServer("edit_work_template", name, details, 0, subItems, new WebRequestCallback() {
            @Override
            public void onFinish(JSONObject output) {
                mReturnText = output.getString(WebRequest.RETURN_TEXT);
                if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                    Platform.runLater(() -> {
                        mName = name;
                        mDetails = details;
                        cb.onSuccess( "" );
                    });
                } else {
                    Platform.runLater(() -> {
                        // clear sub items data if there is an error
                        mSubItems = new ArrayList<>();
                        cb.onError( ActionStatusCode.ERROR );
                    });
                }
            }
        }, cb );
    }

    /*
    *  edit work
    * */
    public void edit( String name, String details, int status, Map<Integer, GWorkSubItemBox> subItems, ActionCallback cb ){
        sendDataToServer( "edit_work", name, details, status, subItems, new WebRequestCallback() {
            @Override
            public void onFinish(JSONObject output) {
                mReturnText = output.getString(WebRequest.RETURN_TEXT);
                if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                    Platform.runLater(() -> {
                        mName = name;
                        mDetails = details;
                        mStatus = status;
                        // if work is completed or canceled, edit all STATUS_WAITING and STATUS_ACTIVE
                        // subItems' status to GWork's status
                        for( GWorkSubItem subItem : mSubItems ){
                            if( subItem.getStatus() == GWorkSubItem.STATUS_ACTIVE || subItem.getStatus() == GWorkSubItem.STATUS_WAITING ){
                                if( mStatus ==  GWork.STATUS_COMPLETED ){
                                    subItem.setStatus(GWorkSubItem.STATUS_COMPLETED);
                                } else if( mStatus == GWork.STATUS_CANCELED ){
                                    subItem.setStatus(GWorkSubItem.STATUS_CANCELED);
                                } else if( mStatus == GWork.STATUS_EXPIRED ){
                                    subItem.setStatus(GWorkSubItem.STATUS_EXPIRED);
                                }
                            }
                        }
                        cb.onSuccess( String.valueOf( mID ) );
                    });
                } else {
                    Platform.runLater(() -> {
                        cb.onError( ActionStatusCode.ERROR );
                    });
                }
            }
        }, cb );
    }

    /*
    *  add work
    * */
    public void add(String name, String details, int status, Map<Integer, GWorkSubItemBox> subItems, ActionCallback cb ){
        sendDataToServer( "add_work", name, details, status, subItems, new WebRequestCallback() {
            @Override
            public void onFinish(JSONObject output) {
                mReturnText = output.getString(WebRequest.RETURN_TEXT);
                if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                    mName = name;
                    mDetails = details;
                    JSONObject successData = output.getJSONObject("data");
                    mID = Integer.valueOf(successData.getString("id"));
                    mDateAdded = successData.getString("date_added");
                    mStatus = status;
                    // set added subItems IDs
                    JSONArray subItemIDs = successData.getJSONArray("added_sub_items_data");
                    for( int k = 0; k < subItemIDs.length(); k++ ){
                        JSONObject idObject = subItemIDs.getJSONObject(k);
                        for( GWorkSubItem subItem : mSubItems ){
                            if(String.valueOf(subItem.getStepOrder()).equals( idObject.getString("step_order") ) ){
                                subItem.setID( Integer.valueOf(idObject.getString("id")) );
                                subItem.setStatus( idObject.getInt("status"));
                                break;
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        cb.onSuccess( successData.getString("id") );
                    });
                } else {
                    // clear sub items data if there is an error
                    mSubItems = new ArrayList<>();
                    Platform.runLater(() -> {
                        cb.onError( ActionStatusCode.ERROR );
                    });
                }
            }
        } ,cb );
    }

    private boolean encodeSubItems( Map<Integer, GWorkSubItemBox> subItems ){
        GWorkSubItem tempSubItem;
        ArrayList<String> subItemsSerialized = new ArrayList<>();
        for (Map.Entry<Integer, GWorkSubItemBox> entry : subItems.entrySet()) {
            // get form data and validate
            entry.getValue().fetchDataFromForm();
            tempSubItem = entry.getValue().getData();
            mSubItems.add( tempSubItem );
            if( !tempSubItem.validate() ){
                mReturnText = tempSubItem.getReturnText();
                return false;
            }
            subItemsSerialized.add( tempSubItem.serialize() );
        }
        mSubItemsEncoded = Common.stringArrayListJoin(subItemsSerialized, "|");
        return true;
    }


    /*
    *  also calculates percentage
    * */
    public String getStepSummary(){
        int completed = 0, total = mSubItems.size();
        for( GWorkSubItem subItem : mSubItems ){
            if( subItem.getStatus() == GWorkSubItem.STATUS_COMPLETED ) completed++;
        }
        mPercentageCompleted = (double)completed / total;
        return completed + " / " + total;
    }

    public static void searchTemplate(String keyword, ReadJACallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("search_keyword", keyword );
                params.put("req", "search_work_template");
                WebRequest request = new WebRequest( WebRequest.SERVICE_URL, params );
                request.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        try {
                            cb.onFinish(output.getJSONArray("data"));
                        } catch( JSONException e ){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public void setTemplateFlag( boolean d ){
        mTemplateFlag = d;
    }
    public void setName( String d ){
        mName = d;
    }
    public void setDetails( String d ){
        mDetails = d;
    }
    public void addSubItem( GWorkSubItem d ){
        mSubItems.add( d );
    }
    public String getName(){
        return mName;
    }
    public int getStatus(){
        return mStatus;
    }
    public int getID(){
        return mID;
    }
    public String getDetails(){
        return mDetails;
    }
    public ArrayList<GWorkSubItem> getSubItems(){
        return mSubItems;
    }
    public String getDateAdded(){
        return mDateAdded;
    }
    public String getDueDate(){
        return mDueDate;
    }
    public double getPercentageCompleted(){
        return mPercentageCompleted;
    }
    public String getReturnText(){
        return mReturnText;
    }
    public boolean getTemplateFlag(){
        return mTemplateFlag;
    }
    public String getSubItemsEncoded(){
        return mSubItemsEncoded;
    }
}
