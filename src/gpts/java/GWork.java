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

public class GWork {

    private int mID, mStatus;
    private double mPercentageCompleted;
    private String mName, mDetails, mReturnText, mDateAdded, mDueDate = "", mDateLastModified;
    private ArrayList<GWorkSubItem> mSubItems = new ArrayList<>();

    public GWork(){

    }

    public GWork( JSONObject data ){
        try{
            mID = Integer.valueOf(data.getString("id"));
            mName = data.getString("name");
            mDetails = data.getString("details");
            mStatus = Integer.valueOf(data.getString("status"));
            mDateAdded = data.getString("date_added");
            JSONArray subItemsDecoded = data.getJSONArray("sub_items");
            for( int k = 0; k < subItemsDecoded.length(); k++ ) mSubItems.add( new GWorkSubItem( subItemsDecoded.getJSONObject(k)));
            mDateLastModified = data.getString("date_last_modified");
        } catch( JSONException e ){
            //e.printStackTrace();
        }


        try{
            mDueDate = data.getString("due_date");
        } catch( JSONException e ){
            mDueDate = "Yok";
            //e.printStackTrace();
        }

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
        PopupLoader.show(PopupLoader.PLEASE_WAIT);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GWorkSubItem tempSubItem;
                ArrayList<String> subItemsSerialized = new ArrayList<>();
                for (Map.Entry<Integer, GWorkSubItemBox> entry : subItems.entrySet()) {
                    // get form data and validate
                    entry.getValue().fetchDataFromForm();
                    tempSubItem = entry.getValue().getData();
                    mSubItems.add( tempSubItem );
                    if( !tempSubItem.validate() ){
                        mReturnText = tempSubItem.getReturnText();
                        Platform.runLater( () -> cb.onError(ActionStatusCode.VALIDATION_ERROR) );
                        return;
                    }
                    subItemsSerialized.add( tempSubItem.serialize() );
                }
                System.out.println( Common.stringArrayListJoin(subItemsSerialized, "|"));
                // send request
                Map<String, String> params = new HashMap<>();
                params.put("name", name );
                params.put("details", details );
                params.put("sub_items_encoded", Common.stringArrayListJoin(subItemsSerialized, "|"));
                params.put("req", "add_work" );
                WebRequest req = new WebRequest( WebRequest.SERVICE_URL, params );
                req.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        mReturnText = output.getString(WebRequest.RETURN_TEXT);
                        if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                            Platform.runLater(() -> {
                                mName = name;
                                mDetails = details;
                                JSONObject successData = output.getJSONObject("data");
                                mID = Integer.valueOf(successData.getString("id"));
                                mDateAdded = successData.getString("date_added");
                                mStatus = 0;
                                cb.onSuccess( successData.getString("id") );
                            });
                        } else {
                            Platform.runLater(() -> {
                                // clear sub items data if there is an error
                                mSubItems = new ArrayList<>();
                                cb.onError( ActionStatusCode.ERROR );
                            });
                        }
                    }
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void removeSubItem( int subItemID ){

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

}
