/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import gpts.java.Common;
import gpts.java.Employee;
import gpts.java.EmployeeGroup;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.FormActionListener;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.ReadJOCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class EmployeeGroupFormController extends PopupFormBaseController implements Initializable {

    @FXML private JFXTextField uiNameInput;
    @FXML private VBox uiFormContainer;

    private FormActionListener mAddListener;
    private ArrayList<JFXCheckBox> mCheckBoxes = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            EmployeeGroup employeeGroup = new EmployeeGroup();

            StringBuilder permString = new StringBuilder();
            for( int k = 0; k < mCheckBoxes.size(); k++ ){
                if(mCheckBoxes.get(k).isSelected()) {
                    permString.append("1");
                } else {
                    permString.append("0");
                }
            }
            employeeGroup.add(uiNameInput.getText(), permString.toString(), new ActionCallback() {
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

        // add permissions as checbox controls
        Common.readStaticDataJA("permissions_template", new ReadJACallback() {
            @Override
            public void onFinish(JSONArray output) {
                JSONObject permItem;
                for( int k  = 0; k < output.length(); k++ ){
                    permItem = output.getJSONObject(k);
                    JFXCheckBox cb = new JFXCheckBox();
                    cb.getStyleClass().add("text-field-popup-style");
                    cb.setText(permItem.getString("title"));
                    mCheckBoxes.add(cb);
                    cb.setSelected(permItem.getBoolean("value"));
                    Platform.runLater( () -> { uiFormContainer.getChildren().add(cb); });
                }
            }
        });

    }

    public void setAddFormListener( FormActionListener listener ){
        mAddListener = listener;
    }


    public void setEditData( Employee employee ){

    }

}
