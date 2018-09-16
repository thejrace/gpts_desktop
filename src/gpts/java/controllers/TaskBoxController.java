/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.GTask;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.PopupDataBase;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;
@Deprecated
public class TaskBoxController implements Initializable {
    @FXML private JFXButton uiMain;

    private TaskPopup mTaskPopup;
    private GTask mTask;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiMain.setOnMouseClicked( ev -> {
            //if( mTask == null ) return;
            PopupLoader.show("Veri alınıyor...");
            if( mTaskPopup == null ){
                mTaskPopup = new TaskPopup( mTask );
                mTaskPopup.initUI();
            }
            mTask.downloadData(new WebRequestCallback() {
                @Override
                public void onFinish(JSONObject output) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            PopupLoader.hide();
                            mTaskPopup.show( ev );
                        }
                    });

                }
            });
        });
    }

    public void setData( GTask data ){
        mTask = data;
        uiMain.setText( data.getName() );
    }

}
@Deprecated
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