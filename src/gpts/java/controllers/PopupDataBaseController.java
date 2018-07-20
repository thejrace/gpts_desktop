/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;


/* super class for;
 *      - EmpDetailsPopupController
  *     - EmpMessagePopupController
  *     - EmpTasksPopupController
  *     - EmpPlanPopupController
  *
  *     */
public class PopupDataBaseController {

    @FXML protected JFXButton uiCloseBtn;
    protected JFXDialog mDialog;

    /* common events */
    public void initCommonEvents(){
        uiCloseBtn.setOnMouseClicked( ev -> {
            mDialog.close();
        });
    }
    /* get controller from fxml loader to handle close event */
    public void setDialog( JFXDialog dialog ){
        mDialog = dialog;
    }
}
