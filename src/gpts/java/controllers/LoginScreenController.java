/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import gpts.java.ApiUser;
import gpts.java.Main;
import gpts.java.interfaces.ActionCallback;
import gpts.java.ui.LoginScreen;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML private Label uiVersionLbl;
    @FXML private JFXTextField uiEmailInput;
    @FXML private JFXPasswordField uiPassInput;
    @FXML private JFXButton uiLoginBtn;
    @FXML private JFXButton uiCancelBtn;
    @FXML private Label uiNotfLabel;

    private boolean mLoginActionFlag = false;
    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiCancelBtn.setOnMouseClicked( ev -> {
            LoginScreen.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            LoginScreen.stage.close();
        });

        uiLoginBtn.setOnMouseClicked( ev -> {
            mLoginActionFlag = true;
            ApiUser.login(uiEmailInput.getText(), uiPassInput.getText(), new ActionCallback() {
                @Override
                public void onSuccess(String... params) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Main mainStage = new Main();
                                mainStage.start(new Stage());
                                LoginScreen.stage.close();
                            } catch( Exception e ){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                @Override
                public void onError(int type) {
                    updateNotf( ApiUser.STATUS_TEXT );
                }
            });
        });

    }

    private void updateNotf( String notf ){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                uiNotfLabel.setText(notf);
            }
        });
    }




}
