/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.controllers.BaseContentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.ScrollEvent;

import java.io.IOException;

public class BasePage {

    protected BaseContentController mBaseController;
    protected Parent mUI;
    protected int mIndex;

    public void initUI( String fxml ){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/"+fxml+".fxml"));
            // get controller  and ui
            mUI  = loader.load();
            mBaseController = loader.getController();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public Parent getUI(){
        return mUI;
    }

    public BaseContentController getBaseController(){
        return mBaseController;
    }


}
