package gpts.java.ui;

import gpts.java.controllers.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

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
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public Parent getUI(){
        return mUI;
    }

}
