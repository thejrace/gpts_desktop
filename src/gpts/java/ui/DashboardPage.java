/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.WebRequest;
import gpts.java.controllers.DashboardController;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DashboardPage {

    private Parent mUI;
    private DashboardController mBaseController;

    public DashboardPage(){

    }

    public void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/dashboard.fxml"));
            // get controller  and ui
            mUI  = loader.load();
            mBaseController = loader.getController();
            downloadWorks();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public void downloadNotf(){

    }

    public void downloadWorks(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> params = new HashMap<>();
                params.put("req", "download_employee_active_works");
                WebRequest request = new WebRequest(WebRequest.SERVICE_URL, params);
                request.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        try {
                            Platform.runLater( () -> { mBaseController.updateWorks( output.getJSONArray("data") ); });
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

    public Parent getUI(){
        return mUI;
    }

}
