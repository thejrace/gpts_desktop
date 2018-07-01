/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXTextField;
import gpts.java.Employee;
import gpts.java.EmployeeGroup;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeGroupFormController extends PopupFormBaseController implements Initializable {

    @FXML private JFXTextField uiNameInput;

    private FormActionListener mAddListener;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            EmployeeGroup employeeGroup = new EmployeeGroup();
            employeeGroup.add(uiNameInput.getText(), new ActionCallback() {
                @Override
                public void onSuccess(String... params) {
                    mParentDialog.close();
                    PopupLoader.showMessage(employeeGroup.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                    mAddListener.onFinish( employeeGroup );
                }
                @Override
                public void onError(int type) {
                    outputError(employeeGroup.getReturnText());
                    uiSaveBtn.setDisable(false);
                    PopupLoader.hide();
                }
            });
        });

    }

    public void setAddFormListener( FormActionListener listener ){
        mAddListener = listener;
    }


    public void setEditData( Employee employee ){

    }

}
