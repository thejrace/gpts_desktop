/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import gpts.java.EmployeeGroup;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeGroupsController extends BaseContentController implements Initializable {

    @FXML
    private JFXButton uiAddBtn;
    @FXML private JFXButton uiSearchBtn;
    @FXML private TextField uiSearchInput;

    private EmployeeGroupsPage mPage;

    @Override
    public void initialize(URL url, ResourceBundle res ){

        PopupLoader.show("Veri alınıyor..");

        // open add form
        uiAddBtn.setOnMouseClicked( ev -> {
            try {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gpts/res/fxml/forms/employee_group_form.fxml"));
                ScrollPane ui  = loader.load();
                EmployeeGroupFormController controller = loader.getController();
                controller.setAddFormListener(new FormActionListener() {
                    @Override
                    public void onFinish(Object object) {
                        EmployeeGroup addedObject = (EmployeeGroup) object;
                        EmployeeGroupDataRow row = new EmployeeGroupDataRow( addedObject );
                        mPage.addItem( addedObject.getID(), row, true, true );
                    }
                });
                dialog.setContent( ui );
                dialog.setOverlayClose(false);
                dialog.show( (StackPane) ((Node) ev.getSource()).getScene().getRoot() );
                controller.setDialog( dialog );
            } catch( IOException e ){
                e.printStackTrace();
            }
        });

        // search
        uiSearchBtn.setOnMouseClicked( ev -> {
            int searchActionType = super.searchAction();
            if( searchActionType == BaseContentController.SEARCH ){
                String searchKeyword = uiSearchInput.getText().trim();
                mPage.search( searchKeyword );
            } else if( searchActionType == BaseContentController.SEARCHCANCEL ){
                mPage.cancelSearch();
            }
        });

        // todo download data
        // if another user added a new plan we should get it with this button
        // rather than restart the application
        /*uiDownloadBtn.setOnMouseClicked(ev -> {

        });*/

        // load more rows
        uiMoreBtn.setOnMouseClicked( ev -> {
            mPage.downloadData();
        });

    }

    public void setPageObject( EmployeeGroupsPage page ){
        mPage = page;
    }
}
