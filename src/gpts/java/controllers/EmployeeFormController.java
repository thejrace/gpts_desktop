/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.*;
import gpts.java.Common;
import gpts.java.Employee;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.FormActionListener;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.json.JSONArray;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeFormController extends PopupFormBaseController implements Initializable {

    @FXML private JFXTextField uiNameInput;
    @FXML private JFXTextField uiEmailInput;
    @FXML private JFXTextField uiNickInput;
    @FXML private JFXTextField uiPhone1Input;
    @FXML private JFXTextField uiPhone2Input;
    @FXML private JFXComboBox uiGroupComboBox;

    private FormActionListener mAddListener;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        Common.readStaticDataJA("employee_groups", new ReadJACallback() {
            @Override
            public void onFinish(JSONArray output) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for( int k = 0; k < output.length(); k++ ) uiGroupComboBox.getItems().add( output.getJSONObject(k).getString("name") );
                        // set first item otherwise we get Nullpointer
                        uiGroupComboBox.getSelectionModel().select(0);
                    }
                });
            }
        });

        uiNameInput.setOnKeyReleased(ev->{
            uiNickInput.setText(Common.regexTrim(uiNameInput.getText()).toLowerCase().replaceAll(" ", ""));
        });

        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            Employee employee = new Employee();
            employee.add(uiNameInput.getText(), uiNickInput.getText(), uiEmailInput.getText(), uiGroupComboBox.getValue().toString(), uiPhone1Input.getText(), uiPhone2Input.getText(), new ActionCallback() {
                @Override
                public void onSuccess(String... params) {
                    mParentDialog.close();
                    PopupLoader.showMessage(employee.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                    mAddListener.onFinish( employee );
                }
                @Override
                public void onError(int type) {
                    outputError(employee.getReturnText());
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
