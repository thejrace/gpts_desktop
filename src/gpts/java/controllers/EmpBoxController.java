package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpBoxController implements Initializable {

    @FXML private Label uiEmpName;
    @FXML private Label uiEmpGroup;
    @FXML private Label uiEmpTaskCount;
    @FXML private JFXButton uiEmpPlanBtn;
    @FXML private JFXButton uiEmpTasksBtn;
    @FXML private JFXButton uiEmpDetailsBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

    }

    // set data for ui components
    public void setData( Employee employee ){
        uiEmpName.setText( employee.getName() );
        uiEmpGroup.setText( employee.getGroupID() );
    }


}
