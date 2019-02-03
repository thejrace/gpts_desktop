package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.NoParamCallback;
import gpts.java.ui.LoginScreen;
import gpts.java.ui.MainScreen;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MainLoginScreenSwticher {

    public MainLoginScreenSwticher(){

    }

    public void init(){
        Common.checkStaticFileLocation(new NoParamCallback() {
            @Override
            public void action() {
                ApiUser.checkLocalLoginData(new ActionCallback() {
                    @Override
                    public void onSuccess(String... params) {
                        try {
                            ApiUser.checkDevice(new ActionCallback() {
                                @Override
                                public void onSuccess(String... params) {
                                    // prevent ending the process when last window is closed.
                                    // process is killed when terminate buttons is clicked
                                    // https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html#setImplicitExit-boolean-
                                    Platform.setImplicitExit(false);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            initMainUI();
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
                                            Platform.exit();
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
        });
    }

    private void initMainUI(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // again set the 'dont kill the process' flag
                    Platform.setImplicitExit(false);
                    MainScreen mainScreen = new MainScreen();
                    mainScreen.start( new Stage() );
                } catch( Exception e ){
                    e.printStackTrace();
                }
            }
        });

    }
    private void initLoginUI(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // again set the 'dont kill the process' flag
                    Platform.setImplicitExit(false);
                    LoginScreen loginScreen = new LoginScreen();
                    loginScreen.start( new Stage() );
                } catch( Exception e ){
                    e.printStackTrace();
                }
            }
        });
    }

}
