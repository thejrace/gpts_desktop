package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.ui.GWorksPage;
import gpts.java.ui.PopupLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GWorksController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    private GWorksPage mPage;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        PopupLoader.show("Veri alınıyor..");

        uiAddBtn.setOnMouseClicked( ev -> {

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

        // load more rows
        uiMoreBtn.setOnMouseClicked( ev -> {
            mPage.downloadData();
        });


    }

    public void setPageObject( GWorksPage page ){
        mPage = page;
    }

}
