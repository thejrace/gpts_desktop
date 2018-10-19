package gpts.java.ui;

import gpts.java.Employee;
import gpts.java.controllers.EmpWorksPopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

public class EmpWorksPopup extends PopupDataBase {

    private Employee mEmployee;
    private EmpWorksPopupController mController;
    public EmpWorksPopup( Employee employee ){
        mEmployee = employee;
    }
    public void initUI(){
        // get the loade from base class
        FXMLLoader loader = super.initFXMLLoader( "emp_work_popup" );
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
