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
            loader.setLocation(getClass().getResource("/gpts/res/fxml/dashboard_v2.fxml"));
            // get controller  and ui
            mUI  = loader.load();
            mBaseController = loader.getController();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public Parent getUI(){
        return mUI;
    }

}
