/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import gpts.java.Employee;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.EmpBox;
import gpts.java.ui.EmployeesPage;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeesController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    @FXML private JFXToggleButton uiFilterLateToggle;
    @FXML private JFXComboBox uiFilterGroupComboBox;
    private EmployeesPage mPage;

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
                controller.setAddFormListener(new FormActionListener() {
                    @Override
                    public void onFinish(Object object) {
                        Employee addedObject = (Employee) object;
                        EmpBox row = new EmpBox( addedObject );
                        mPage.addItem( addedObject.getID(), row, true, true );
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



    public void setPageObject( EmployeesPage page ){
        mPage = page;
    }





}
