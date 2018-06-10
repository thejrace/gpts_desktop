/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class PlanFormController extends PopupFormBaseController implements Initializable {

    @FXML private JFXTextField uiNameInput;
    @FXML private JFXTextField uiStartInput;
    @FXML private JFXTextField uiEndInput;
    @FXML private JFXTextField uiIntervalInput;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();
    }

}
