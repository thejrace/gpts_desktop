package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkFormController extends PopupFormBaseController implements Initializable {


    @FXML private JFXTextField uiTaskNameInput;
    @FXML private JFXTextField uiTaskDefInput;
    @FXML private VBox uiSubTasksContainer;
    @FXML private JFXButton btnNewSubItem;
    @FXML private Tab tabBundle;
    @FXML private Tab tabDetails;


    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();


        btnNewSubItem.setOnMouseClicked( ev -> {



        });

    }

}
