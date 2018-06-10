/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import gpts.java.ui.EmpBox;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;


import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;



public class EmployeesController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    @FXML private JFXButton uiSearchBtn;
    @FXML private TextField uiSearchInput;
    @FXML private FlowPane uiEmpBoxContainer;

    @FXML private JFXToggleButton uiFilterLateToggle;
    @FXML private JFXComboBox uiFilterGroupComboBox;

    private boolean mEnableSearch = false;

    private Map<String, EmpBox> mEmpBoxes = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle res ){

        PopupLoader.show("Veri alınıyor..");
        // open employee add form
        uiAddBtn.setOnMouseClicked( ev -> {
            try {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/forms/employee_form.fxml"));
                ScrollPane ui  = loader.load();
                EmployeeFormController controller = loader.getController();
                dialog.setContent( ui );
                dialog.setOverlayClose(false);
                dialog.show( (StackPane) ((Node) ev.getSource()).getScene().getRoot() );
                // pass the dialog to controller to trigger close form cancel button
                controller.setDialog( dialog );
            } catch( IOException e ){
                e.printStackTrace();
            }
        });

        // search employees
        uiSearchBtn.setOnMouseClicked( ev -> {
            if( mEnableSearch ) return;
        });

    }


    public void addEmpBox( Parent boxUI){
        uiEmpBoxContainer.getChildren().add( boxUI );
    }

    public void testMethod(){
        System.out.println("Employeiiii teeeesssttt");
    }




}
