package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.GWorkSubItemBox;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void removeSubItem( int subItemID ){

    }

    public static void search(String keyword, ReadJACallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("search_keyword", keyword );
                params.put("req", "search_work");
                WebRequest request = new WebRequest( WebRequest.SERVICE_URL, params );
                request.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {

                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public String getReturnText(){
        return mReturnText;
    }

}
