package gpts.java.ui;

import com.jfoenix.controls.JFXDialog;
import gpts.java.controllers.PopupLoaderController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PopupLoader {

    public static JFXDialog DIALOG;
    public static PopupLoaderController CONTROLLER;
    public static StackPane ROOT;

    public PopupLoader( StackPane root ){
        try {
            ROOT = root;
            DIALOG = new JFXDialog();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/popup_loader.fxml"));
            VBox ui  = loader.load();
            CONTROLLER = loader.getController();
            DIALOG.setContent(ui );

            DIALOG.setOverlayClose(false);
        } catch( IOException e ){
            e.printStackTrace();
        }
    }
    public static void show( String text ){
        CONTROLLER.setStatusText(text);
        DIALOG.show(ROOT);
    }
    public static void hide(){
        DIALOG.close();
    }
    public static void setText( String text ){
        CONTROLLER.setStatusText( text );
    }
}
