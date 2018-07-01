/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;


import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.util.Collections;
import java.util.Comparator;

/**
 *  Parent class for standartizing main controllers
 *
 * */
public class BaseContentController {

    @FXML protected FlowPane uiBoxContainer;
    @FXML protected JFXButton uiMoreBtn;
    @FXML protected TextField uiSearchInput;
    @FXML protected JFXButton uiSearchBtn;
    protected boolean mEnableSearch = false;
    private boolean mMoreBtnState = false;
    protected ObservableList<Node> dataRowsTemp;

    public static int SEARCHNOTENABLED = 0,
                      SEARCHCANCEL = 1,
                      SEARCH = 3;


    public void addRow(Parent row, boolean sort, boolean searchFlag ){
        uiBoxContainer.getChildren().add(row);
        if( sort ) sortItems();
        // we update last state after every row addition
        // todo we'll perform this after delete
        if( !searchFlag ) saveLastState();
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
        uiMoreBtn.setDisable( mMoreBtnState );
    }

    protected int searchAction(){
        if( mEnableSearch ) return SEARCHNOTENABLED;
        String searchKeyword = uiSearchInput.getText().trim();
        if( searchKeyword.equals("") ){
            // todo direk bişi yazmadan butona basınca dataRowsTemp null oldugu icin, cancelSearch te Nullpointer atıyor, düzelt
            // cancel search, return previous state
            clearItems();
            return SEARCHCANCEL;
        } else {
            return SEARCH;
        }
    }

    public void saveLastState(){
        dataRowsTemp = FXCollections.observableArrayList( uiBoxContainer.getChildren() );
    }

    public void saveMoreBtnState(){
        mMoreBtnState = uiMoreBtn.isDisable();
    }

    public void disableMoreBtn( boolean flag ){
        uiMoreBtn.setDisable( flag );
    }

}
