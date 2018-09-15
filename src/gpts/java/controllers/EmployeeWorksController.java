/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import gpts.java.GWork;
import gpts.java.interfaces.FormActionListener;
import gpts.java.ui.EmployeeWorksPage;
import gpts.java.ui.GWorkBox;
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

public class EmployeeWorksController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    @FXML private JFXComboBox uiFilterInput;
    @FXML private JFXButton uiFilterActionBtn;

    private String[] mFilterStatusList = { "Aktif", "Tamamlandı", "Süre Aşımı", "İptal" };
    private EmployeeWorksPage mPage;

    @Override
    public void initialize(URL url, ResourceBundle res ){

        PopupLoader.show("Veri alınıyor..");

        for( int k = 0; k < mFilterStatusList.length; k++ ) uiFilterInput.getItems().add( mFilterStatusList[k] );
        uiFilterInput.getSelectionModel().select(0);

        uiAddBtn.setOnMouseClicked( ev -> {
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
                        if( workData.getStatus() == uiFilterInput.getSelectionModel().getSelectedIndex() ){
                            mPage.addItem( String.valueOf(workData.getID()), new GWorkBox( workData ), true, true );
                        }
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

        // load more rows
        uiMoreBtn.setOnMouseClicked( ev -> {
            mPage.downloadData();
        });

        uiFilterActionBtn.setOnMouseClicked( ev -> {
            mPage.applyFilter();
            clearItems();
            mPage.downloadData();
        });

        // todo search ( filtre olayini daha iyilişetirmem lazım, bide jfxspinner cancelSearch sonrasi animated oluyor.
        /*uiSearchBtn.setOnMouseClicked( ev -> {
            int searchActionType = super.searchAction();
            if( searchActionType == BaseContentController.SEARCH ){
                String searchKeyword = uiSearchInput.getText().trim();
                mPage.search( searchKeyword );
            } else if( searchActionType == BaseContentController.SEARCHCANCEL ){
                mPage.cancelSearch();
            }
        });*/


    }



    public void setPageObject( EmployeeWorksPage page ){
        mPage = page;
    }
    public int getStatusFilterInput(){
        return uiFilterInput.getSelectionModel().getSelectedIndex();
    }

}
