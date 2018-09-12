package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import gpts.java.GWorkSubItem;
import gpts.java.interfaces.NoParamCallback;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GWorkSubItemBoxController implements Initializable {

    // edit mod, new mod, viewer mod

    @FXML private VBox uiMainContainer;
    @FXML private JFXTextField uiNameInput;
    @FXML private JFXButton uiValidationBtn;
    @FXML private JFXTextArea uiDetailsInput;
    @FXML private JFXComboBox uiStatusInput;
    @FXML private JFXButton uiSaveBtn;
    @FXML private JFXButton uiDeleteBtn;

    private String[] mStatusList = { "Beklemede", "Aktif", "Onay Bekliyor", "İptal Edildi", "Tamamlandı" };
    private NoParamCallback mDeleteListener;
    private GWorkSubItem mData;

    private boolean mEditFlag = false;

    @Override
    public void initialize(URL url, ResourceBundle rb ){

        // add status data to comboBox
        for( int k = 0; k < mStatusList.length; k++ ) uiStatusInput.getItems().add( mStatusList[k] );

        uiDeleteBtn.setOnMouseClicked( ev -> {
           mDeleteListener.action();
        });

    }

    public void getFormData(){
        mData.setName( uiNameInput.getText() );
        mData.setDetails(uiDetailsInput.getText());
        mData.setStatus( uiStatusInput.getSelectionModel().getSelectedIndex() );
    }

    public void setData( GWorkSubItem data ){
        mData = data;
        uiNameInput.setText(data.getName());
        uiDetailsInput.setText( data.getDetails());
        uiStatusInput.getSelectionModel().select( data.getStatus() );
    }

    public void addDeleteListener( NoParamCallback cb ){
        mDeleteListener = cb;
    }

    public void setStyleClassName( String className ){
        uiMainContainer.getStyleClass().add(className);
    }

}
