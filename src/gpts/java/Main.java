/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Time;

public class Main extends Application {

    // we use this access it from MainController
    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gpts/res/fxml/main.fxml"));

        Parent content = loader.load();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("GPTS");
        primaryStage.setScene(new Scene(content, 1224, 768));
        stage = primaryStage;
        primaryStage.show();

        String start = "08:00";
        String end   = "17:30";
        int interval = 25;
        DailyPlanSchema test = new DailyPlanSchema("xx", "xx", start, end, String.valueOf(interval));
        test.generatePlanStandart();






    }


    public static void main(String[] args) {
        launch(args);
    }
}
