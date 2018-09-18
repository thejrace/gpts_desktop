/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployeeGroup {

    private String mName, mID, mReturnText, mPermissions;

    public EmployeeGroup(){

    }

    public EmployeeGroup( String name ){
        mName = name;
    }

    public EmployeeGroup(JSONObject data ){
        try {
            mName = data.getString("name");
            mID = data.getString("id");
            mPermissions = data.getString("permissions");
        } catch( JSONException e ){
            e.printStackTrace();
        }
    }

    public void add(String name, String permissions, ActionCallback cb ){
        PopupLoader.show(PopupLoader.PLEASE_WAIT);
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("İsim", name, FormValidation.CHECK_REQ )
        });
        if( !inputCheck ){
            mReturnText = validation.getMessage();
            cb.onError(ActionStatusCode.VALIDATION_ERROR);
            return;
        }
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                // send request
                Map<String, String> params = new HashMap<>();
                params.put("name", name );
                params.put("permissions", permissions );
                params.put("req", "add_employee_group" );
                WebRequest req = new WebRequest( WebRequest.SERVICE_URL, params );
                req.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        mReturnText = output.getString(WebRequest.RETURN_TEXT);
                        if( output.getInt(WebRequest.STATUS_FLAG) == 1 ){
                            Platform.runLater(() -> {
                                mID = output.getString("data");
                                mName = name;
                                // data is lastInsertedId
                                cb.onSuccess( output.getString("data") );
                            });
                        } else {
                            Platform.runLater(() -> {
                                cb.onError( ActionStatusCode.ERROR );
                            });
                        }
                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();

    }

    public String getName(){
        return mName;
    }
    public String getID(){
        return mID;
    }
    public String getReturnText(){
        return mReturnText;
    }
    public String getPermissions(){
        return mPermissions;
    }

}
