/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import gpts.java.Employee;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpBoxController implements Initializable {

    @FXML private Label uiEmpName;
    @FXML private Label uiEmpGroup;
    @FXML private Label uiEmpTaskCount;
    @FXML private HBox uiLed;

    @FXML private JFXButton uiEmpPlanBtn;
    @FXML private JFXButton uiEmpTasksBtn;
    @FXML private JFXButton uiEmpDetailsBtn;
    @FXML private JFXButton uiEmpMessageBtn;

    private String[] mTaskStatusClasses = {
        "emp-box-circle-default",
        "emp-box-circle-green",
        "emp-box-circle-red"
    };

    private EmpPlanPopup mEmpPlanPopup;
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
    }

    // set data for ui components
    public void setData( Employee employee ){
        mEmployee = employee;
        uiEmpName.setText( employee.getName() );
        uiEmpGroup.setText( employee.getGroup() );
        uiEmpTaskCount.setText( "5" );
        uiLed.getStyleClass().remove(1);
        uiLed.getStyleClass().add(mTaskStatusClasses[employee.getTaskStatus()]);
    }


}

/* EmpBox popup layout classes  */
class EmpPopupBase {
    protected ScrollPane mUI;
    protected Employee mEmployee;
    protected JFXDialog mDialog;

    /* loades the fxml loader for child classes
    *  returns the loader object for child class to get it's own controller */
    public FXMLLoader initFXMLLoader( String fxml ){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/"+fxml+".fxml"));
            mUI  = loader.load();
            return loader;
        } catch( IOException e ){
            e.printStackTrace();
        }
        return null;
    }
    /* show the dialog */
    public void show( MouseEvent ev){
        mDialog = new JFXDialog();
        mDialog.setContent( mUI );
        mDialog.setOverlayClose(false);
        mDialog.show( (StackPane) ((Node) ev.getSource()).getScene().getRoot() );
    }

    public ScrollPane getUI(){
        return mUI;
    }
}

class EmpMessagePopup extends EmpPopupBase {

}

class EmpDetailsPopup extends EmpPopupBase{

}

class EmpTasksPopup extends EmpPopupBase {

}

class EmpPlanPopup extends EmpPopupBase {

    private EmpPlanPopupController mController;
    public EmpPlanPopup( Employee employee ){
        mEmployee = employee;
    }
    public void initUI(){
        // get the loade from base class
        FXMLLoader loader = super.initFXMLLoader( "emp_plan_popup" );
        try {
            // get controller as our own object type
            mController = loader.getController();
            updateUI();
        } catch( NullPointerException e ){
            e.printStackTrace();
        }
    }
    public void show( MouseEvent ev ){
        super.show( ev );
        mController.setDialog( mDialog );
    }
    public void updateUI(){
        mController.setData( mEmployee );
    }


}