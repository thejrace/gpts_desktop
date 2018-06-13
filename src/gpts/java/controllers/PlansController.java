/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import gpts.java.ui.PlansPage;
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
import java.util.ResourceBundle;

public class PlansController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    @FXML private JFXButton uiSearchBtn;
    @FXML private JFXButton uiDownloadBtn;
    @FXML private JFXButton uiMoreBtn;
    @FXML private TextField uiSearchInput;
    @FXML private FlowPane uiBoxContainer;


    private boolean mEnableSearch = false;
    private PlansPage mPage;

    @Override
    public void initialize(URL url, ResourceBundle res ){

        PopupLoader.show("Veri alınıyor..");
        // open add form
        uiAddBtn.setOnMouseClicked( ev -> {
            try {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/forms/plan_form.fxml"));
                ScrollPane ui  = loader.load();
                PlanFormController controller = loader.getController();
                // todo eklenme listener a yapilacak
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

        // download data
        // if another user added a new plan we should get it with this button
        // rather than restart the application
        uiDownloadBtn.setOnMouseClicked(ev -> {

        });

        // load more rows
        uiMoreBtn.setOnMouseClicked( ev -> {
            mPage.downloadData();
        });

    }

    public void setPageObject( PlansPage page ){
        mPage = page;
    }

    public void addRow( Parent row ){
        uiBoxContainer.getChildren().add(row);
    }

}
