/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    public void setDialog(JFXDialog dialog ){
        mParentDialog = dialog;
    }

}
