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
        String end   = "17:00";
        int interval = 70;
        String[] from_exp = start.split(":");
        String[] to_exp = end.split(":");
        int from_saat = Integer.valueOf(from_exp[0]);
        int to_saat = Integer.valueOf( to_exp[0] );
        int from_dk = Integer.valueOf( from_exp[1] );
        int to_dk = Integer.valueOf( to_exp[1] );

        int next_hour = from_saat,
                next_min = from_dk;
        String prev_hour_str = start;
        String next_hour_str;
        for( int k = from_saat; k < to_saat; k++ ){
            next_min += interval;
            if( next_min >= 60 ){
                next_hour++;
                if( next_hour == to_saat && next_min > from_dk ){
                    next_min = from_dk;
                }
                next_hour_str =  String.valueOf(next_hour) + ":" + ( next_min % 60 );

            } else {
                next_hour_str =  String.valueOf(next_hour) + ":" + next_min;
            }
            System.out.println( prev_hour_str + "  -  " + next_hour_str );
            prev_hour_str = next_hour_str;
        }






    }


    public static void main(String[] args) {
        launch(args);
    }
}
