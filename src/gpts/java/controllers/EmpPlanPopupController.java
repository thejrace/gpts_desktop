/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import gpts.java.Common;
import gpts.java.Employee;
import gpts.java.interfaces.ReadJACallback;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.json.JSONArray;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpPlanPopupController extends EmpPopupBaseController implements Initializable {

    @FXML private JFXComboBox uiPlansComboBox;
    @FXML private JFXButton uiChangePlan;
    @FXML private DatePicker uiDatePicker;
    @FXML private JFXButton uiExcelOutBtn;
    @FXML private Label uiEmployeeNameLbl;


    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        // set comboBox data from json file
        Common.readStaticDataJA("profiles", new ReadJACallback() {
            @Override
            public void onFinish(JSONArray output) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for( int k = 0; k < output.length(); k++ ){
                            uiPlansComboBox.getItems().add( output.getJSONObject(k).getString("name") );
                        }
                    }
                });
            }
        });
    }

    public void setPlanComboBox( String value ){
        uiPlansComboBox.setValue(value);
    }

    public void setData( Employee employee ){
        uiEmployeeNameLbl.setText(employee.getName());
    }

}
