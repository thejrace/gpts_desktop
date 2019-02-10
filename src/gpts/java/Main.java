/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.ui.UpdateCheckScreen;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends Application {

    public static String APP_VERSION = "1.0.12";
    //private String mDebugFilePrefix = ""; // release
    private String mDebugFilePrefix = "C://gpts/"; // debug

    @Override
    public void start(Stage primaryStage) throws Exception{
        // check and read configuration file to determine install folder
        if( Common.checkFile( mDebugFilePrefix + "gpts_config.json" ) ){
            try {
                JSONObject config = new JSONObject( Common.readJSONFile(mDebugFilePrefix + "gpts_config.json") );
                Common.STATIC_LOCATION = config.getString("static_dir");
                WebRequest.SERVICE_URL = config.getString("web_service_url") + config.getString("service_api_suffix");
                WebRequest.VERSION_SERVICE_URL = config.getString("web_service_url") + config.getString("version_service_api_suffix");
            } catch (JSONException e ){
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gitaş PTS");
            alert.setHeaderText("Hata oluştu. Kod: CONFIG_EKSIK");
            alert.setContentText("Sistem yöneticisine hatayı bildirin.");
            ButtonType cancelBtn = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(cancelBtn );
            alert.showAndWait();
            return;
        }
        UpdateCheckScreen updateCheckScreen = new UpdateCheckScreen();
        updateCheckScreen.start( new Stage() );
    }

    public static void main(String[] args) {
        launch(args);
    }

}
