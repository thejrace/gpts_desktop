package gpts.java.controllers;

import gpts.java.GTask;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskPopupController extends PopupDataBaseController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();
    }

    public void setData( GTask data ){
        //uiEmployeeNameLbl.setText(employee.getName());
    }

}
