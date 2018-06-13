/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import gpts.java.DailyPlan;
import gpts.java.Employee;
import gpts.java.FormValidation;
import gpts.java.ValidationInput;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.PlanDataRow;
import gpts.java.ui.PlansPage;
import gpts.java.ui.PopupLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Collections;
import java.util.Comparator;
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
    private ObservableList<Node> dataRowsTemp;

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
                controller.setAddFormListener(new FormActionListener() {
                    @Override
                    public void onFinish(Object object) {
                        DailyPlan addedObject = (DailyPlan) object;
                        PlanDataRow row = new PlanDataRow( addedObject );
                        // when we added like this, we inform PlanPage to sort datarows
                        mPage.addItem( row.getData().getID(), row, true );
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

        // search employees
        uiSearchBtn.setOnMouseClicked( ev -> {
            if( mEnableSearch ) return;
            String searchKeyword = uiSearchInput.getText().trim();
            if( searchKeyword.equals("") ){
                // cancel search, return previous state
                clearItems();
                mPage.cancelSearch();
            } else {
                // get last state
                if( dataRowsTemp == null ) dataRowsTemp = FXCollections.observableArrayList( uiBoxContainer.getChildren() );
                mPage.search( searchKeyword );
            }
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

    // get parent PlansPage object for triggering
    public void setPageObject( PlansPage page ){
        mPage = page;
    }

    public void addRow( Parent row, boolean sort ){
        uiBoxContainer.getChildren().add(row);
        if( sort ) sortItems();
    }

    private void sortItems(){
        try {
            ObservableList<Node> dataRows = FXCollections.observableArrayList( uiBoxContainer.getChildren() );
            Collections.sort(dataRows, new Comparator<Node>(){
                @Override
                public int compare( Node vb1, Node vb2 ){
                    return vb1.getId().compareTo(vb2.getId());
                }
            });
            uiBoxContainer.getChildren().setAll(dataRows);
        } catch (IndexOutOfBoundsException e ){
            e.printStackTrace();
        }
    }

    // remove all datarows
    public void clearItems(){
        uiBoxContainer.getChildren().clear();
    }

    // after search return the first state
    public void restoreFirstState(){
        uiBoxContainer.getChildren().setAll(dataRowsTemp);
    }

    public void disableMoreBtn(){
        uiMoreBtn.setDisable(true);
    }
    public void enableMoreBtn(){
        uiMoreBtn.setDisable(false);
    }

}
