/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.Employee;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.EmpWorksPopup;
import gpts.java.ui.PopupDataBase;
import gpts.java.ui.PopupLoader;
import gpts.java.ui.EmpPlanPopup;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpBoxController implements Initializable {

    @FXML private Label uiEmpName;
    @FXML private Label uiEmpGroup;
    @FXML private Label uiEmpTaskCount;
    @FXML private HBox uiLed;

    @FXML private JFXButton uiEmpPlanBtn;
    @FXML private JFXButton uiEmpWorksBtn;
    @FXML private JFXButton uiEmpDetailsBtn;
    @FXML private JFXButton uiEmpMessageBtn;
    @FXML private AnchorPane uiContainer;
    private String[] mTaskStatusClasses = {
        "emp-box-circle-default",
        "emp-box-circle-green",
        "emp-box-circle-red"
    };
    private String[] mBoxClasses = {
        "emp-box-default",
        "emp-box-green",
        "emp-box-red"
    };

    private EmpPlanPopup mEmpPlanPopup;
    private EmpWorksPopup mEmpWorksPopup;
    private Employee mEmployee;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiEmpPlanBtn.setOnMouseClicked( ev -> {
            PopupLoader.show("Veri alınıyor...");
            if( mEmployee == null ){
                return;
            } else {
                if( mEmpPlanPopup == null ){
                    mEmpPlanPopup = new EmpPlanPopup( mEmployee );
                    mEmpPlanPopup.initUI();
                }
                mEmployee.downloadPlan(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        PopupLoader.hide();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                mEmpPlanPopup.updateUI();
                                mEmpPlanPopup.show( ev );
                            }
                        });
                    }
                });
            }
        });

        uiEmpWorksBtn.setOnMouseClicked(ev->{
            PopupLoader.show("Veri alınıyor...");
            if( mEmployee == null ){
                return;
            } else {
                if( mEmpWorksPopup == null ){
                    mEmpWorksPopup = new EmpWorksPopup( mEmployee );
                    mEmpWorksPopup.initUI();
                }
                mEmpWorksPopup.updateUI();
                mEmpWorksPopup.show( ev );
            }
        });

    }

    // set data for ui components
    public void setData( Employee employee ){
        mEmployee = employee;
        uiEmpName.setText( employee.getName() );
        uiEmpGroup.setText( employee.getGroup() );
        uiEmpTaskCount.setText( String.valueOf(employee.getWorkCount()) );
        uiLed.getStyleClass().remove(1);
        uiLed.getStyleClass().add(mTaskStatusClasses[employee.getWorkStatus()]);
        try {uiContainer.getStyleClass().remove(1);

        } catch( IndexOutOfBoundsException e ){}
        uiContainer.getStyleClass().add(mBoxClasses[employee.getWorkStatus()]);
    }
}

