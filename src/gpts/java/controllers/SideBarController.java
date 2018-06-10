/***
 *  Gitaş - Obarey Inc. 2018
 *
 * */

package gpts.java.controllers;

import gpts.java.ui.BasePage;
import gpts.java.ui.EmployeesPage;
import gpts.java.ui.SettingsPage;
import gpts.java.ui.TasksPage;
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

    private int mPageIndex = 0;
    private Map<Integer, BasePage> mPages = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        // TODO - generalize it, research: reflection in java
        uiSideBarBtn1.setOnMouseClicked(ev -> {
            // check if we already in this page
            if( mPageIndex == 1 ) return;
            // initialize the page if it didnt
            if( !mPages.containsKey(1) ){
                EmployeesPage page = new EmployeesPage();
                page.initUI("employees");
                mPages.put(1, page );
            }
            // set content
            MainController.UICONTENTMAIN.setContent( mPages.get(1).getUI());
            mPageIndex = 1;
        });
        uiSideBarBtn2.setOnMouseClicked(ev -> {
            if( mPageIndex == 2 ) return;
            if( !mPages.containsKey(2) ){
                TasksPage page = new TasksPage();
                page.initUI("tasks");
                mPages.put(2, page );
            }
            // set content
            MainController.UICONTENTMAIN.setContent( mPages.get(2).getUI());
            mPageIndex = 2;
        });
        uiSideBarBtn3.setOnMouseClicked(ev -> {
            if( mPageIndex == 3 ) return;
            if( !mPages.containsKey(3) ){
                SettingsPage page = new SettingsPage();
                page.initUI("settings");
                mPages.put(3, page );
            }
            // set content
            MainController.UICONTENTMAIN.setContent( mPages.get(3).getUI());
            mPageIndex = 3;
        });
    }

    /*
    *  - method that loads the fxml to the content.fxml -> ScrollPane's content
    *  - @fxml  : fxml file's name to be loaded
    *  - @index : fxml state for avoiding the loading same content again
    * */
    @Deprecated
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
