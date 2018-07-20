package gpts.java.ui;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/* popup base layout classes  */
public class PopupDataBase {
    protected ScrollPane mUI;
    protected JFXDialog mDialog;

    /* loades the fxml loader for child classes
     *  returns the loader object for child class to get it's own controller */
    public FXMLLoader initFXMLLoader(String fxml ){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/"+fxml+".fxml"));
            mUI  = loader.load();
            return loader;
        } catch( IOException e ){
            e.printStackTrace();
        }
        return null;
    }
    /* show the dialog */
    public void show( MouseEvent ev){
        mDialog = new JFXDialog();
        mDialog.setContent( mUI );
        mDialog.setOverlayClose(false);
        mDialog.show( (StackPane) ((Node) ev.getSource()).getScene().getRoot() );
    }

    public ScrollPane getUI(){
        return mUI;
    }
}