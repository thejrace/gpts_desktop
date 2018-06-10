/***
 *  Gitaş - Obarey Inc. 2018
 *
 * */

package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContentController implements Initializable {

    @FXML
    private JFXButton uiSideBarToggleBtn;
    @FXML private JFXButton uiAppTerminateBtn;
    @FXML private JFXButton uiAppLogoutBtn;
    @FXML private JFXButton uiAppMinimizeBtn;
    @FXML private Label uiUserInfoLbl;

    private boolean mSideBarVisibleFlag = true;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiUserInfoLbl.setText("Hoşgeldin, @obarey");

        // right top close btn
        uiAppTerminateBtn.setOnMouseClicked(ev -> {
            Main.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            Main.stage.close();
        });

        // minimize windows
        uiAppMinimizeBtn.setOnMouseClicked( ev -> {
            Main.stage.setIconified(true);
        });

        uiAppLogoutBtn.setOnMouseClicked( ev -> {
        });

        // toggle side bar event
        uiSideBarToggleBtn.setOnMouseClicked(ev -> {
            BorderPane borderBane = (BorderPane) ((Node) ev.getSource()).getScene().lookup(".mBorderPane");
            if ( !mSideBarVisibleFlag ) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/gpts/res/fxml/sidebar.fxml"));
                    Parent sidebar  = loader.load();
                    borderBane.setLeft(sidebar);
                    mSideBarVisibleFlag = true;
                } catch( IOException e ){
                    e.printStackTrace();
                }
            } else {
                borderBane.setLeft(null);
                mSideBarVisibleFlag = false;
            }
        });


    }

}
