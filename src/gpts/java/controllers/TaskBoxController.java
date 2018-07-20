package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.GTask;
import gpts.java.ui.PopupDataBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskBoxController implements Initializable {
    @FXML private JFXButton uiMain;

    private TaskPopup mTaskPopup;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiMain.setOnMouseClicked( ev -> {

            if( mTaskPopup == null ){
                mTaskPopup = new TaskPopup( new GTask());
                mTaskPopup.initUI();
            }
            mTaskPopup.show( ev );

        });

    }

    public void setData( GTask data ){
        uiMain.setText( data.getName() );
    }

}

class TaskPopup extends PopupDataBase {

    private GTask mTask;
    private TaskPopupController mController;
    public TaskPopup( GTask task ){

    }
    public void initUI(){
        // get the loade from base class
        FXMLLoader loader = super.initFXMLLoader( "task_content" );
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
        //mController.setData(  );
    }

}