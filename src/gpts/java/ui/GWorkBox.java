/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.GWork;
import gpts.java.controllers.GWorkBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class GWorkBox {

    private VBox mUI;
    private GWorkBoxController mController;
    private GWork mData;

    public GWorkBox( GWork data ){
        mData = data;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/work_box.fxml"));
            mUI  = loader.load();
            mController = loader.getController();
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public GWork getData(){
        return mData;
    }

    public void updateData( GWork data ){
        mData = data;
    }

    public void updateUI(){
        mController.setData( mData );
    }

    public VBox getUI(){
        return mUI;
    }


}
