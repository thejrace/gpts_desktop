/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import gpts.java.ui.MainScreen;
import gpts.java.ui.PopupLoader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane mBorderPane;

    private Parent mSideBar, mContent;

    public static ScrollPane UICONTENTMAIN;
    public static ContentController CONTENT_CONTROLLER;

    // for stage drag
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        makeStageDrageable();
        try {
            // load fxml layouts
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/sidebar.fxml"));
            mSideBar = loader.load();
            SideBarController sideBarController = loader.getController();
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("/gpts/res/fxml/content.fxml"));
            mContent = loader2.load();
            // set border pane's ui elements
            mBorderPane.setCenter( mContent );
            mBorderPane.setLeft( mSideBar );
            // make it static, we use it to dynamically change main content
            UICONTENTMAIN = (ScrollPane) mContent.lookup( ".content-main" );
            // for status and sync labels
            CONTENT_CONTROLLER = loader2.getController();
            // init loader
            PopupLoader popupLoader = new PopupLoader( (StackPane)mBorderPane.getParent() );
            // let sidebar controller know that UI's are done to init dashboard
            sideBarController.initDashboard();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    /*
    * we have undecorated scene, therefore we manually make it draggable
    * */
    private void makeStageDrageable() {
        mBorderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        mBorderPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MainScreen.stage.setX(event.getScreenX() - xOffset);
                MainScreen.stage.setY(event.getScreenY() - yOffset);
                MainScreen.stage.setOpacity(0.7f);
            }
        });
        mBorderPane.setOnDragDone((e) -> {
            MainScreen.stage.setOpacity(1.0f);
        });
        mBorderPane.setOnMouseReleased((e) -> {
            MainScreen.stage.setOpacity(1.0f);
        });
    }


}
