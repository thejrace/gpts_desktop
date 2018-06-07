/***
 *  Gitaş - Obarey Inc. 2018
 *
 * */
package gpts.java;

import gpts.java.controllers.EmpBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class EmpBox{

    private VBox mUI;
    private EmpBoxController mController;
    private Employee mEmployee;

    public EmpBox( Employee employee ){
        mEmployee = employee;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/emp_box.fxml"));
            mUI  = loader.load();
            mController = loader.getController();
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public void updateData( Employee employee ){
        mEmployee = employee;
    }

    public void updateUI(){
        mController.setData( mEmployee );
    }

    public VBox getUI(){
        return mUI;
    }


}
