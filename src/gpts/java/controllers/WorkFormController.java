package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import gpts.java.GWork;
import gpts.java.GWorkSubItem;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.NoParamCallback;
import gpts.java.ui.GWorkSubItemBox;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class WorkFormController extends PopupFormBaseController implements Initializable {


    @FXML private JFXTextField uiTaskNameInput;
    @FXML private JFXTextArea uiTaskDefInput;
    @FXML private VBox uiSubTasksContainer;
    @FXML private JFXButton uiNewSubItemBtn;
    @FXML private Tab tabBundle;
    @FXML private Tab tabDetails;

    private boolean mEditFlag = false;

    private int mSubItemStepCounter = 0;
    private Map<Integer, GWorkSubItemBox> mSubItems = new HashMap<>();
    private String mBoxClassName = "task-sub-item-even";


    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        uiNewSubItemBtn.setOnMouseClicked( ev -> {
            if( mEditFlag ){

            } else {
                GWorkSubItemBox newBox = new GWorkSubItemBox( new GWorkSubItem() );
                newBox.setStyleClassName( mBoxClassName );
                switchBoxClassName();
                uiSubTasksContainer.getChildren().add( 0, newBox.getUI() );
                newBox.setUIID(mSubItemStepCounter);
                mSubItems.put( mSubItemStepCounter, newBox );
                mSubItemStepCounter++;
                newBox.getController().addDeleteListener(new NoParamCallback() {
                    @Override
                    public void action() {
                        for( int k = 0; k < uiSubTasksContainer.getChildren().size(); k++ ){
                            if( uiSubTasksContainer.getChildren().get(k).getId().equals( newBox.getUI().getId() ) ){
                                mSubItems.remove( Integer.valueOf(newBox.getUI().getId()) );
                                uiSubTasksContainer.getChildren().remove(k);
                                break;
                            }
                        }
                    }
                });
            }
        });

        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            if( mEditFlag ){

            } else {
                GWork newWork = new GWork();
                // pass mSubItems directly and get GWorkSubItem from GWorkSubItemBox
                newWork.add(uiTaskNameInput.getText(), uiTaskDefInput.getText(), mSubItems, new ActionCallback() {
                    @Override
                    public void onSuccess(String... params) {
                        mParentDialog.close();
                        //PopupLoader.showMessage(employee.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                        //mAddListener.onFinish( employee );
                    }
                    @Override
                    public void onError(int type) {
                        outputError(newWork.getReturnText());
                        uiSaveBtn.setDisable(false);
                        PopupLoader.hide();
                    }
                });
            }
        });

    }

    /*
    *  triggered from GWorkSubItemBoxController
    * */
    public void deleteSubItem(){

    }

    public void setEditData( GWork data ){
        mEditFlag = true;

        // update ui from here
    }

    private void switchBoxClassName(){
        if( mBoxClassName.equals("task-sub-item-even") ){
            mBoxClassName = "task-sub-item-odd";
        } else {
            mBoxClassName = "task-sub-item-even";
        }
    }

}
