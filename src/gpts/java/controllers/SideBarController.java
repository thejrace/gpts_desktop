/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import gpts.java.ui.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {

    @FXML private HBox uiSideBarBtn1; // employees
    @FXML private HBox uiSideBarBtn2; // tasks
    @FXML private HBox uiSideBarBtn3; // settings
    @FXML private HBox uiSideBarBtn4; // plans
    @FXML private HBox uiSideBarBtn5; // employee groups

    private int mPageIndex = 0;
    private Map<Integer, BasePage> mPages = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        // TODO - bunu fonksiyon haline getir, bak -> reflection in java
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
        uiSideBarBtn4.setOnMouseClicked(ev -> {
            if( mPageIndex == 4 ) return;
            if( !mPages.containsKey(4) ){
                PlansPage page = new PlansPage();
                page.initUI("plans");
                mPages.put(4, page );
            }
            // set content
            MainController.UICONTENTMAIN.setContent( mPages.get(4).getUI());
            mPageIndex = 4;
        });
        uiSideBarBtn5.setOnMouseClicked(ev -> {
            if( mPageIndex == 5 ) return;
            if( !mPages.containsKey(5) ){
                EmployeeGroupsPage page = new EmployeeGroupsPage();
                page.initUI("employee_groups");
                mPages.put(5, page );
            }
            // set content
            MainController.UICONTENTMAIN.setContent( mPages.get(5).getUI());
            mPageIndex = 5;
        });
    }
}
