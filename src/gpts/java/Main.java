/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.ui.LoginScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    // we use this access it from MainController
    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ApiUser.checkLocalLoginData(new ActionCallback() {
            @Override
            public void onSuccess(String... params) {
                try {
                    ApiUser.checkDevice(new ActionCallback() {
                        @Override
                        public void onSuccess(String... params) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    initMainUI( new Stage() );
                                }
                            });
                        }
                        @Override
                        public void onError(int type) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Gitaş Filo Personel Takip Sistemi");
                                    alert.setHeaderText("Hata oluştu. Kod: KYB_1");
                                    alert.setContentText("Sistem yöneticisine hatayı bildirin.");
                                    ButtonType cancelBtn = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
                                    alert.getButtonTypes().setAll(cancelBtn );
                                    alert.showAndWait();
                                }
                            });
                        }
                    });
                } catch( Exception e ){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(int type) {
                initLoginUI();
            }
        });
    }

    private void initMainUI( Stage primaryStage ){
        System.out.println("geldim");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/main.fxml"));
            Parent content = loader.load();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("GPTS");
            primaryStage.setScene(new Scene(content, 1224, 768));
            stage = primaryStage;
            ServerSync.start();
            primaryStage.show();
        } catch( IOException e ){
            e.printStackTrace();
        }

    }
    private void initLoginUI(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginScreen loginScreen = new LoginScreen();
                    loginScreen.start( new Stage() );
                } catch( Exception e ){
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("helo");
        launch(args);
    }


}
