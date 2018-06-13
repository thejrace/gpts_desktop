/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.DailyPlan;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class PlanDataRowController extends DataRowBaseController implements Initializable {

    @FXML private JFXButton uiUsersBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        uiEditBtn.setOnMouseClicked( ev -> {

        });

        uiUsersBtn.setOnMouseClicked( ev -> {
            PopupLoader.showMessage("Bü özellik henüz eklenmedi.", PopupLoader.MESSAGE_ERROR );
        });

    }

    public void setData( DailyPlan plan ){
        // update ui
        uiNameLbl.setText( plan.getName() + " [ "+plan.getID()+" ]" );
    }

}
