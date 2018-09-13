/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import gpts.java.Common;
import gpts.java.GWork;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GWorkBoxController implements Initializable {

    @FXML private VBox uiContainer;
    @FXML private Label uiNameLbl;
    @FXML private Label uiStepsLbl;
    @FXML private Label uiStartLbl;
    @FXML private Label uiFinishLbl;

    private GWork mData;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiContainer.setOnMouseClicked( ev -> {

        });

    }

    public void setData( GWork data ){
        mData = data;
        uiNameLbl.setText( data.getName());
        uiStepsLbl.setText( data.getStepSummary() );
        uiStartLbl.setText(Common.revDatetime(data.getDateAdded()) );
        uiFinishLbl.setText(Common.revDatetime(data.getDueDate()));
    }

}
