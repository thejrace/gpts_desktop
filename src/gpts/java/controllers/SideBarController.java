/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import gpts.java.ApiUser;
import gpts.java.ApiUserPermissions;
import gpts.java.Common;
import gpts.java.ui.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {

    @FXML private HBox uiSideBarBtn0; // main
    @FXML private HBox uiSideBarBtn1; // employees
    @FXML private HBox uiSideBarBtn2; // tasks
    @FXML private HBox uiSideBarBtn3; // settings
    @FXML private HBox uiSideBarBtn4; // plans
    @FXML private HBox uiSideBarBtn5; // employee groups
    @FXML private HBox uiSideBarBtn6; // employee works
    @FXML private VBox uiContainer;

    private int mPageIndex = -1;
    private Map<Integer, BasePage> mPages = new HashMap<>();
    private DashboardPage mDashboardPage; // dashboard doesn't extend BasePage

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        Map<String, Double> resData = Common.calculateAppWindowSize();
        uiContainer.setPrefHeight( resData.get("H") );

        uiSideBarBtn0.setOnMouseClicked( ev -> {
            // check if we already in this page
            if( mPageIndex == 0 ) return;
            // initialize the page if it didnt
            try {
                MainController.UICONTENTMAIN.setContent( mDashboardPage.getUI());
            } catch( NullPointerException e ){
                mDashboardPage = new DashboardPage();
                mDashboardPage.initUI();
                MainController.UICONTENTMAIN.setContent( mDashboardPage.getUI() );
            }
            mPageIndex = 0;
        });

        // TODO - bunu fonksiyon haline getir, bak -> reflection in java
        if( ApiUser.checkPermission(ApiUserPermissions.AC_EMPLOYEES) ){
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
        } else {
            uiContainer.getChildren().remove(3);
        }

        if( ApiUser.checkPermission(ApiUserPermissions.AC_TASKS) ) {
            uiSideBarBtn2.setOnMouseClicked(ev -> {
                if( mPageIndex == 2 ) return;
                if( !mPages.containsKey(2) ){
                    GWorksPage page = new GWorksPage();
                    page.initUI("works");
                    mPages.put(2, page );
                }
                // set content
                MainController.UICONTENTMAIN.setContent( mPages.get(2).getUI());
                mPageIndex = 2;
            });
        } else {
            uiContainer.getChildren().remove(6,8);
        }

        if( ApiUser.checkPermission(ApiUserPermissions.AC_PLANS) ) {
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
        } else {
            uiContainer.getChildren().remove(5);
        }

        uiContainer.getChildren().remove(4);
        /*if( ApiUser.checkPermission(ApiUserPermissions.AC_EMPLOYEE_GROUPS) ) {
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
        } else {
            uiContainer.getChildren().remove(4);
        }*/

        uiSideBarBtn6.setOnMouseClicked(ev -> {
            if( mPageIndex == 6 ) return;
            if( !mPages.containsKey(6) ){
                EmployeeWorksPage page = new EmployeeWorksPage();
                page.initUI("employee_works");
                mPages.put(6, page );
            }
            // set content
            MainController.UICONTENTMAIN.setContent( mPages.get(6).getUI());
            mPageIndex = 6;
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

    // triggered when sideBar and content UI's are ready
    public void initDashboard(){
        mDashboardPage = new DashboardPage();
        mDashboardPage.initUI();
        MainController.UICONTENTMAIN.setContent( mDashboardPage.getUI() );
        mPageIndex = 0;
    }

}
