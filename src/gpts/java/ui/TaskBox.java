/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import com.jfoenix.controls.JFXButton;
import gpts.java.GTask;
import gpts.java.controllers.TaskBoxController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

@Deprecated
public class TaskBox {

    private GTask mTask;
    private JFXButton mUI;
    private TaskBoxController mController;

    public TaskBox( GTask data ){
        mTask = data;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/task_box.fxml"));
            mUI  = loader.load();
            mUI.setId( mTask.getName() );
            mController = loader.getController();
            mController.setData( mTask );
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public JFXButton getUI(){
        return mUI;
    }

    public void updateUI(){
        mController.setData( mTask );
    }

    // after edit, update data and ui
    public void updateData( GTask task ){
        mTask = task;
        updateUI();
    }

    public GTask getData(){
        return mTask;
    }

}
