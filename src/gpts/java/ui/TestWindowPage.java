package gpts.java.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TestWindowPage extends Application {

    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gpts/res/fxml/window_test_ui.fxml"));

        Parent content = loader.load();
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Test");
        primaryStage.setScene(new Scene(content, 500, 440));
        stage = primaryStage;
        stage.getIcons().add(new Image(getClass().getResource("/gpts/res/img/gpts_ico.png").toExternalForm()));
        primaryStage.showAndWait();
    }

}