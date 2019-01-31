package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.*;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.UpdateCheckScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateCheckScreenController implements Initializable {

    @FXML private Label uiNotfLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        try {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    updateNotfLabel("Versiyon kontrolü yapılıyor..");
                    Map<String, String> params = new HashMap<>();
                    params.put("req", "update_check");
                    params.put("version_info", Main.APP_VERSION );
                    WebRequest req = new WebRequest(WebRequest.VERSION_SERVICE_URL, params);
                    req.appendApiUserData( false );
                    req.action(new WebRequestCallback() {
                        @Override
                        public void onFinish(JSONObject output) {
                            try {
                                int statusFlag = output.getInt(WebRequest.STATUS_FLAG);
                                if( statusFlag == 1 ){
                                    closeUpdateScreen();
                                    MainLoginScreenSwticher uiSwitcher = new MainLoginScreenSwticher();
                                    uiSwitcher.init();
                                } else {
                                    updateNotfLabel("Hazırlanıyor..");
                                    // first check gpts dir
                                    if( !Common.checkDirectory( Common.STATIC_LOCATION ) ) Common.createStaticDirectory();
                                    // check if already an exe exists
                                    if( Common.checkFile( Common.STATIC_LOCATION  + "GPTS.exe" ) ){
                                        updateNotfLabel("Yedek alınıyor..");
                                        Common.copyFile( new File( Common.STATIC_LOCATION + "GPTS.exe"), new File( Common.STATIC_LOCATION + "GTPS_old.exe" ) );
                                    }
                                    updateNotfLabel("Yeni versiyon indirilirken lütfen bekleyin..");
                                    FileDownload.downloadFileFromUrlAsync("http://178.18.206.163/gpts_web_service/desktop_app_updates/GPTS.json", "C:/gpts/GPTS.exe", new ActionCallback() {
                                        @Override
                                        public void onSuccess(String... params) {
                                            updateNotfLabel("Yeni versiyon indirildi! Yedekler temizleniyor..");
                                            if( !Common.deleteFile( Common.STATIC_LOCATION + "GTPS_old.exe") ) updateNotfLabel("Yedekler temizlenemedi..");
                                            closeUpdateScreen();
                                            MainLoginScreenSwticher uiSwitcher = new MainLoginScreenSwticher();
                                            uiSwitcher.init();
                                        }
                                        @Override
                                        public void onError(int type) {
                                            updateNotfLabel("Yeni versiyon indirilirken bir HATA oluştu!!");
                                        }
                                    });
                                }
                            } catch( JSONException e ){
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
            th.setDaemon(true);
            th.start();
        } catch( Exception e ){
            e.printStackTrace();
        }

    }

    private void updateNotfLabel( String text ){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                uiNotfLabel.setText(text);
            }
        });
    }

    private void closeUpdateScreen(){
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                UpdateCheckScreen.stage.close();
            }
        });
    }

}
