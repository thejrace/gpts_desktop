/***
 *  Gitaş - Obarey Inc. 2018
 *
 * */

package gpts.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {

    @FXML private HBox uiSideBarBtn1;
    @FXML private HBox uiSideBarBtn2;
    @FXML private HBox uiSideBarBtn3;

    private int mMainContentStateIndex = 0;

    private Map<Integer, BaseContentController> mControllers = new HashMap<>();
    private Map<Integer, Parent> mUIs = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        uiSideBarBtn1.setOnMouseClicked(ev -> {
            setContentUI("employees", 1, new ControllerCallback() {
                @Override
                public void onLoad(BaseContentController controller) {
                    EmployeesController castedController = (EmployeesController)controller;
                    castedController.testMethod();
                }
            });
        });
        uiSideBarBtn2.setOnMouseClicked(ev -> {
            setContentUI("tasks", 2, new ControllerCallback() {
                @Override
                public void onLoad(BaseContentController controller) {
                    TasksController castedController = (TasksController)controller;
                    castedController.testMethod();
                }
            });
        });
        uiSideBarBtn3.setOnMouseClicked(ev -> {
            setContentUI("settings", 3, new ControllerCallback() {
                @Override
                public void onLoad(BaseContentController controller) {
                    SettingsController castedController = (SettingsController)controller;
                    castedController.testMethod();
                }
            });
        });
    }

    /*
    *  - method that loads the fxml to the content.fxml -> ScrollPane's content
    *  - @fxml  : fxml file's name to be loaded
    *  - @index : fxml state for avoiding the loading same content again
    * */
    private void setContentUI( String fxml, int index, ControllerCallback cb ){
        try {
            // if already in this layout, don't do anything
            if( mMainContentStateIndex == index ) return;
            Parent contentUI;
            BaseContentController controller;
            // if we have the UI already initialized we use that
            if( mControllers.containsKey(index) && mUIs.containsKey(index) ){
                contentUI = mUIs.get(index);
                controller = mControllers.get(index);
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/"+fxml+".fxml"));
                contentUI  = loader.load();
                // get controllers and pass them to callback
                controller = loader.getController();
                // store the controller and layout
                mControllers.put( index, controller );
                mUIs.put(index, contentUI );
            }
            cb.onLoad( controller );
            // set content
            MainController.UICONTENTMAIN.setContent(contentUI);
            mMainContentStateIndex = index;
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

}
