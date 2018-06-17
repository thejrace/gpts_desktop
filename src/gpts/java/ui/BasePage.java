/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.controllers.BaseContentController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.json.JSONArray;
import java.io.IOException;

public class BasePage {

    protected BaseContentController mBaseController;
    protected Parent mUI;
    // record count to download
    protected int mRRP = 24;
    // data download index
    protected int mLastItemIndex = 0;
    // data download index temp holder for restoring it after search
    protected int mLastItemIndexTemp = 0;
    // last downloaded data
    protected JSONArray mData;
    // first download flag for PopupLoader
    protected boolean mDownloadInited = false;
    // search state flag
    protected boolean mSearchFlag = false;
    protected String mSearchKeyword;
    // enabled-disabled flag for ShowMoreBtn
    // after search, when user returns the first state
    // we make the button return to first state, otherwise
    // after search, if button is disabled, remains disabled after canceling search
    // even though there are data to download
    protected boolean mMoreBtnState = true;

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

    // adding items action, after each request
    // real action is happening in the AddItemCallback function
    // addItem
    protected void addItems( AddItemCallback cb ){
        int length = mData.length();
        // mLastItemIndex 0 indexed add length everytime user shows more
        mLastItemIndex += length;
        cb.action( length );
        Platform.runLater(()->{ PopupLoader.hide(); } );
    }

    // search common actions
    public void search( String keyword ){
        mSearchFlag = true;
        mSearchKeyword = keyword;
        // do it once, after first search
        // if we do it every search call, when user searched twice in a row
        // we would lost the first state index
        if( mLastItemIndexTemp == 0 ) mLastItemIndexTemp = mLastItemIndex;
        // reset item index, it will be used for searched data during searching action
        mLastItemIndex = 0;
    }

    // common events for canceling search
    public void cancelSearch(){
        mSearchFlag = false;
        // restore first state index for data download
        mLastItemIndex = mLastItemIndexTemp;
        // reset temp index
        mLastItemIndexTemp = 0;
    }


    public Parent getUI(){
        return mUI;
    }

    public BaseContentController getBaseController(){
        return mBaseController;
    }

    interface AddItemCallback {
        void action( int length );
    }
}
