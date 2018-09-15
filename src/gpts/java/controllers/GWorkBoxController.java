/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import gpts.java.Common;
import gpts.java.GWork;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.GWorkBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GWorkBoxController implements Initializable {

    @FXML private VBox uiContainer;
    @FXML private Label uiNameLbl;
    @FXML private Label uiStepsLbl;
    @FXML private Label uiStartLbl;
    @FXML private Label uiFinishLbl;
    @FXML private JFXSpinner uiProgressSpinner;

    private GWork mData;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        uiContainer.setOnMouseClicked( ev -> {
            try {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/forms/work_form.fxml"));
                ScrollPane ui  = loader.load();
                GWorkFormController controller = loader.getController();
                controller.setEditFlag( true );
                controller.setData( mData );
                controller.setEditFormListener(new FormActionListener() {
                    @Override
                    public void onFinish(Object object) {
                        GWork workData = (GWork)object;
                        setData( workData );
                    }
                });
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

    public void setData( GWork data ){
        mData = data;
        uiNameLbl.setText( data.getName());
        uiStepsLbl.setText( data.getStepSummary() );
        uiStartLbl.setText(Common.revDatetime(data.getDateAdded()) );
        uiFinishLbl.setText(Common.revDatetime(data.getDueDate()));
        double progress = data.getPercentageCompleted();
        String newClass = "red-spinner";
        uiProgressSpinner.setProgress(progress);
         if( progress > 0 && progress <= 0.75 ){
            newClass = "yellow-spinner";
        } else if( progress > 0.75 ){
            newClass = "green-spinner";
        }
        uiProgressSpinner.getStyleClass().remove(2 );
        uiProgressSpinner.getStyleClass().add(newClass );
    }


}
