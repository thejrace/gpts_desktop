package gpts.java.ui;

import gpts.java.GWorkSubItem;
import gpts.java.controllers.GWorkSubItemBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class GWorkSubItemBox {

    private VBox mUI;
    private GWorkSubItemBoxController mController;
    private GWorkSubItem mGWorkSubItem;

    public GWorkSubItemBox( GWorkSubItem data ){
        // new form constructor
        mGWorkSubItem = data;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/work_sub_item.fxml"));
            mUI  = loader.load();
            mController = loader.getController();
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public GWorkSubItemBoxController getController(){
        return mController;
    }

    public void setUIID( int id ){
        mUI.setId( String.valueOf(id) );
    }

    public void fetchDataFromForm(){
        mController.getFormData();
    }

    public GWorkSubItem getData(){
        return mGWorkSubItem;
    }

    public void setStyleClassName( String className ){
        mController.setStyleClassName( className );
    }

    public void updateUI(){
        mController.setData( mGWorkSubItem );
    }

    public VBox getUI(){
        return mUI;
    }

}
