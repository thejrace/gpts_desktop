/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.print.attribute.standard.NumberUp;

public class PopupFormBaseController {

    @FXML protected Label uiFormOutputLbl;
    @FXML protected JFXButton uiCancelBtn;
    @FXML protected JFXButton uiSaveBtn;
    protected JFXDialog mParentDialog;

    protected void initCommonEvents(){
        uiCancelBtn.setOnMouseClicked(ev->{
            mParentDialog.close();
        });
    }

    protected void outputError( String msg ){
        outputStatus( msg, "error" );
    }

    protected void outputSuccess( String msg ){
        outputStatus( msg, "success" );
    }
    private void outputStatus( String msg, String cssClass ){
        try {
            uiFormOutputLbl.getStyleClass().remove(2);
        } catch( IndexOutOfBoundsException | NullPointerException e ){
            //e.printStackTrace();
        }
        uiFormOutputLbl.getStyleClass().add("popup-form-output-" + cssClass );
        uiFormOutputLbl.setText( msg );
    }

    public void setDialog(JFXDialog dialog ){
        mParentDialog = dialog;
    }

}
