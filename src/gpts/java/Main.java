/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.ui.UpdateCheckScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static String APP_VERSION = "1.0.1";

    @Override
    public void start(Stage primaryStage) throws Exception{
        UpdateCheckScreen updateCheckScreen = new UpdateCheckScreen();
        updateCheckScreen.start( new Stage() );
    }

    public static void main(String[] args) {
        launch(args);
    }


}
