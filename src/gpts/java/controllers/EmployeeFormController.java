/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.*;
import gpts.java.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML private JFXTextField uiNameInput;
    @FXML private JFXTextField uiEmailInput;
    @FXML private JFXTextField uiNickInput;
    @FXML private JFXTextField uiPhone1Input;
    @FXML private JFXTextField uiPhone2Input;
    @FXML private JFXPasswordField uiPasswordInput;
    @FXML private JFXComboBox uiGroupComboBox;
    @FXML private JFXButton uiCancelBtn;
    @FXML private JFXButton uiSaveBtn;

    private JFXDialog mParentDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiCancelBtn.setOnMouseClicked(ev->{
            mParentDialog.close();
        });

    }

    public void setDialog(JFXDialog dialog ){
        mParentDialog = dialog;
    }

    public void setEditData( Employee employee ){

    }

}
