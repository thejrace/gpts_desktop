/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.ui.TestWindowPage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private JFXButton uiWindowToggleBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb ) {

        uiWindowToggleBtn.setOnMouseClicked( ev -> {
            try {
                TestWindowPage test = new TestWindowPage();
                test.start( new Stage() );
            } catch( Exception e ){
                e.printStackTrace();
            }

        });


    }

}
