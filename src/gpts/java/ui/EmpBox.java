/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.Employee;
import gpts.java.controllers.EmpBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class EmpBox{

    private AnchorPane mUI;
    private EmpBoxController mController;
    private Employee mEmployee;

    public EmpBox( Employee employee ){
        mEmployee = employee;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/emp_box_v2.fxml"));
            mUI  = loader.load();
            mUI.setId( mEmployee.getName() );
            mController = loader.getController();
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public Employee getData(){
        return mEmployee;
    }

    public void updateData( Employee employee ){
        mEmployee = employee;
    }

    public void updateUI(){
        mController.setData( mEmployee );
    }

    public AnchorPane getUI(){
        return mUI;
    }


}
