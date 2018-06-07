package gpts.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupLoaderController implements Initializable {

    @FXML private Label uiStatusText;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

    }

    public void setStatusText( String text ){
        uiStatusText.setText(text);
    }

}
