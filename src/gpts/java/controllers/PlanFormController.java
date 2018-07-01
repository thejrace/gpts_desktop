/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXTextField;
import gpts.java.DailyPlanSchema;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class PlanFormController extends PopupFormBaseController implements Initializable {

    @FXML private JFXTextField uiNameInput;
    @FXML private JFXTextField uiStartInput;
    @FXML private JFXTextField uiEndInput;
    @FXML private JFXTextField uiIntervalInput;

    private FormActionListener mAddListener;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            DailyPlanSchema plan = new DailyPlanSchema();
            plan.add(uiNameInput.getText(), uiStartInput.getText(), uiEndInput.getText(), uiIntervalInput.getText(), new ActionCallback() {
                @Override
                public void onSuccess( String[] args ) {
                    mParentDialog.close();
                    PopupLoader.showMessage(plan.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                    mAddListener.onFinish( plan );
                }
                @Override
                public void onError( int type ) {
                    outputError(plan.getReturnText());
                    uiSaveBtn.setDisable(false);
                    PopupLoader.hide();
                }
            });
        });
    }

    public void setAddFormListener( FormActionListener listener ){
        mAddListener = listener;
    }

}
