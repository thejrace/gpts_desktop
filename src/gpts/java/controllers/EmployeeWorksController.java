/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import gpts.java.ui.EmployeeWorksPage;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeWorksController extends BaseContentController implements Initializable {

    @FXML private JFXComboBox uiFilterInput;
    @FXML private JFXButton uiFilterActionBtn;

    private String[] mFilterStatusList = { "Aktif", "Tamamlandı", "Süre Aşımı", "İptal" };
    private EmployeeWorksPage mPage;

    @Override
    public void initialize(URL url, ResourceBundle res ){

        PopupLoader.show("Veri alınıyor..");

        for( int k = 0; k < mFilterStatusList.length; k++ ) uiFilterInput.getItems().add( mFilterStatusList[k] );
        uiFilterInput.getSelectionModel().select(0);

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
