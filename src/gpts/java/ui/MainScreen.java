package gpts.java.ui;

import gpts.java.Common;
import gpts.java.ServerSync;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Map;

public class MainScreen extends Application {

    // we use this access it from MainController
    public static Stage stage = null;
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/main.fxml"));
            Parent content = loader.load();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("Gitaş PTS");
            Map<String, Double> resData = Common.calculateAppWindowSize();
            primaryStage.setScene(new Scene(content, resData.get("W"), resData.get("H") ));
            stage = primaryStage;
            stage.getIcons().add(new Image(getClass().getResource("/gpts/res/img/gpts_ico.png").toExternalForm()));
            primaryStage.show();
            ServerSync.start();
        } catch( Exception e ){
            e.printStackTrace();
        }
    }


}
