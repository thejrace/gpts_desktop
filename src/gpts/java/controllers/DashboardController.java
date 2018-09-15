/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import gpts.java.GWork;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.GWorkBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    @FXML private JFXButton btnNewWork;
    @FXML private VBox uiContainerNotfs;
    @FXML private FlowPane uiContainerWorks;
    @FXML private JFXSpinner uiNotfSpinner;
    @FXML private JFXSpinner uiWorksSpinner;
    @FXML private JFXButton uiFilterActionBtn;
    @FXML private JFXComboBox uiFilterInput;

    private String[] mFilterStatusList = { "Aktif", "Tamamlandı", "Süre Aşımı", "İptal" };

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        for( int k = 0; k < mFilterStatusList.length; k++ ) uiFilterInput.getItems().add( mFilterStatusList[k] );
        uiFilterInput.getSelectionModel().select(0);

        btnNewWork.setOnMouseClicked( ev -> {
            try {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/forms/work_form.fxml"));
                ScrollPane ui  = loader.load();
                GWorkFormController controller = loader.getController();
                controller.setAddFormListener(new FormActionListener() {
                    @Override
                    public void onFinish(Object object) {
                        GWork workData = (GWork)object;
                        if( workData.getStatus() == GWork.STATUS_ACTIVE ) uiContainerWorks.getChildren().add( 0, new GWorkBox( workData ).getUI() );
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

    public void updateWorks( JSONArray data ){
        uiWorksSpinner.setVisible(false);
        for( int k = 0; k < data.length(); k++ ){
            GWork work = new GWork( data.getJSONObject(k) );
            GWorkBox box = new GWorkBox( work );
            uiContainerWorks.getChildren().add( 0, box.getUI() );
        }
    }

    public void updateNotfs( JSONArray data ){

    }

}
