package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    @FXML private JFXButton btnNewWork;
    @FXML private FlowPane uiContainerNotfs;
    @FXML private FlowPane uiContainerTasks;
    @FXML private FlowPane uiContainerWorks;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        // todo download action



        btnNewWork.setOnMouseClicked( ev -> {

            try {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/forms/work_form.fxml"));
                ScrollPane ui  = loader.load();
                GWorkFormController controller = loader.getController();

                dialog.setContent( ui );
                dialog.setOverlayClose(false);
                dialog.show( (StackPane) ((Node) ev.getSource()).getScene().getRoot() );
                // pass the dialog to controller to trigger close form cancel button
                controller.setDialog( dialog );
            } catch( IOException e ){
                e.printStackTrace();
            }

        });

    }


}
