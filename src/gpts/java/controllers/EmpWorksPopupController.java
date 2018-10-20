/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import gpts.java.Employee;
import gpts.java.ui.EmployeeWorksPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpWorksPopupController extends PopupDataBaseController implements Initializable {

    @FXML private Label uiPopupHeaderLbl;
    @FXML private ScrollPane uiWorksPageContainer;
    private EmployeeWorksPage employeesWorksPage;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();
    }

    public void setData( Employee employee ){
        uiPopupHeaderLbl.setText(employee.getName());
        // create works page ui
        // i do this here because i need employee's ID
        employeesWorksPage = new EmployeeWorksPage( true, Integer.valueOf(employee.getID()) );
        employeesWorksPage.initUI("employee_works");
        uiWorksPageContainer.setContent(employeesWorksPage.getUI());
    }
}
