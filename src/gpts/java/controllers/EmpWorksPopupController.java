/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import gpts.java.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpWorksPopupController extends PopupDataBaseController implements Initializable {

    @FXML private Label uiPopupHeaderLbl;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();
    }

    public void setData( Employee employee ){
        uiPopupHeaderLbl.setText(employee.getName());
    }
}
