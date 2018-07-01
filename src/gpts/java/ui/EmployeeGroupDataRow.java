/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.EmployeeGroup;
import gpts.java.controllers.EmployeeGroupDataRowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EmployeeGroupDataRow {

    private EmployeeGroup mUserGroup;
    private AnchorPane mUI;
    private EmployeeGroupDataRowController mController;

    public EmployeeGroupDataRow(EmployeeGroup data ){
        mUserGroup = data;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/employee_group_data_row.fxml"));
            mUI  = loader.load();
            mUI.setId( mUserGroup.getName() );
            mController = loader.getController();
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public AnchorPane getUI(){
        return mUI;
    }

    public void updateUI(){
        mController.setData( mUserGroup );
    }

    // after edit, update data and ui
    public void updateData( EmployeeGroup plan ){
        mUserGroup = plan;
        updateUI();
    }

    public EmployeeGroup getData(){
        return mUserGroup;
    }

}
