/* Gitaş - Obarey Inc 2018 */
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
    public static int MESSAGE_SUCCESS = 1,
                      MESSAGE_ERROR = 2;
    public static String LOADER_FXML = "/gpts/res/fxml/popup_loader.fxml";
    public static String PLEASE_WAIT = "Lütfen bekleyin..";

    public PopupLoader( StackPane root ){
        ROOT = root;
        DIALOG = new JFXDialog();
        initLoader();
    }

    private static void initLoader(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PopupLoader.class.getResource(LOADER_FXML));
            VBox ui  = loader.load();
            CONTROLLER = loader.getController();
            DIALOG.setContent(ui );
            DIALOG.setOverlayClose(false);
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public static void showMessage( String msg, int type ){
        try {
            FXMLLoader loader = new FXMLLoader();
            String fxml;
            if( type == MESSAGE_SUCCESS ){
                fxml = "success";
            } else {
                fxml = "error";
            }
            loader.setLocation( PopupLoader.class.getResource("/gpts/res/fxml/popup_loader_message_"+fxml+".fxml"));
            VBox ui  = loader.load();
            CONTROLLER = loader.getController();
            CONTROLLER.setStatusText( msg );
            DIALOG.setContent( ui );
            DIALOG.setOverlayClose(true);
            DIALOG.show(ROOT);
            DIALOG.setOnDialogClosed( ev->{
                // when closed, switch back to loader layout
                initLoader();
            });
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public static void show( String text ){
        CONTROLLER.setStatusText(text);
        DIALOG.show(ROOT);
    }
    public static void hide(){
        try {
            DIALOG.close();
        } catch( NullPointerException e ){

        }
    }
    public static void setText( String text ){
        CONTROLLER.setStatusText( text );
    }
}
