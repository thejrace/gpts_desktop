/***
 *  Gitaş - Obarey Inc. 2018
 *
 * */

package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TasksController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    @FXML private JFXButton uiSearchBtn;
    @FXML private TextField uiSearchInput;
    @FXML private FlowPane uiEmpBoxContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

    }

    public void testMethod(){
        System.out.println("tasskkkssssss teeeesssttt");
    }

}
