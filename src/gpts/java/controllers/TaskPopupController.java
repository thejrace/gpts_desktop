/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.*;
import gpts.java.GTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
@Deprecated
public class TaskPopupController extends PopupDataBaseController implements Initializable {


    @FXML private JFXTextField uiTaskNameInput;
    @FXML private JFXTextArea uiTaskDefInput;
    @FXML private JFXComboBox uiTaskGroupInput;
    @FXML private JFXButton uiTaskDetailsFormSubmitBtn;
    @FXML private JFXButton uiTaskDeleteBtn;
    @FXML private Label uiTaskNameLbl;
    @FXML private VBox uiSubTasksContainer;
    @FXML private JFXTextField uiEmpSearchInput;
    @FXML private JFXButton uiEmpSearchBtn;
    @FXML private VBox uiEmpSearchResultsContainer;
    @FXML private JFXComboBox uiEmpGroupInput;
    @FXML private JFXButton uiEmpGroupSelectBtn;
    @FXML private Label uiRightFormHeaderLbl;
    @FXML private JFXCheckBox uiPeriodicCheckbox;
    @FXML private JFXTextField uiTimeLengthValInput;
    @FXML private JFXComboBox uiTimeLengthValComboBox;
    @FXML private HBox uiRightFormPeriodicContainer;
    @FXML private JFXTextField uiPeriodicValInput;
    @FXML private JFXComboBox uiPeriodicValComboBox;
    @FXML private JFXButton uiDefineTaskFinalBtn;


    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        uiPeriodicCheckbox.setOnMouseClicked( ev -> {
            uiRightFormPeriodicContainer.setVisible(uiPeriodicCheckbox.isSelected());
        });



    }

    public void setData( GTask data ){
        //uiEmployeeNameLbl.setText(employee.getName());
    }

}
