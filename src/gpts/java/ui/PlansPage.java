/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.DailyPlan;
import gpts.java.WebRequest;
import gpts.java.controllers.PlansController;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class PlansPage extends BasePage {

    private PlansController mController;

    // record count to download
    private int mRRP = 24;
    // data download index
    private int mLastItemIndex = 0;
    // data download index temp holder for restoring it after search
    private int mLastItemIndexTemp = 0;
    // last downloaded data
    private JSONArray mData;
    // Rows holder lists
    private Map<String, PlanDataRow> mRows = new HashMap<>();
    private Map<String, PlanDataRow> mSearchRows = new HashMap<>();
    // first download flag for PopupLoader
    private boolean mDownloadInited = false;
    // search state flag
    private boolean mSearchFlag = false;
    private String mSearchKeyword;

    public PlansPage(){

    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (PlansController)mBaseController;
        downloadData();
        // for showMore button we have to pass the page to the controller
        // in order to recieve download triggers
        mController.setPageObject( this );
    }

    /* we show limited data on intialize, if user wants to see more
    *  clicks to download more button
    */
    public void downloadData(){
        // if download more data, show loader
        // first download loader inited from planscontroller
        if( mDownloadInited ) PopupLoader.show("Veri alınıyor..");
        if( !mDownloadInited ) mDownloadInited = true;
        mController.enableMoreBtn();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> downloadParams = new HashMap<>();
                if( mSearchFlag ){
                    downloadParams.put("req", "daily_plan_schemas_search");
                    downloadParams.put("keyword", mSearchKeyword );
                } else {
                    downloadParams.put("req", "daily_plan_schemas_download");
                }
                downloadParams.put("start_index", String.valueOf(mLastItemIndex));
                downloadParams.put("rrp", String.valueOf(mRRP));
                WebRequest request = new WebRequest(WebRequest.SERVICE_URL, downloadParams );
                request.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        mData = output.getJSONArray("data");
                        addItems();
                    }
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    // adds data rows to layout
    private void addItems( ){
        JSONObject temp;
        int length = mData.length();
        // mLastItemIndex 0 indexed add length everytime user shows more
        mLastItemIndex += length;
        // if last downloaded data is less than RRP means that
        // there aren't any data left to download
        // therefore we disable the button to avoid user making useless requests
        if( length < mRRP ) mController.disableMoreBtn();
        for( int k = 0; k < length; k++ ){
            temp = mData.getJSONObject(k);
            PlanDataRow row = new PlanDataRow( new DailyPlan( temp.getString("id"), temp.getString("name"), temp.getString("start"), temp.getString("end"), temp.getString("plan_interval") ));
            addItem( temp.getString("id"), row, false );
        }
        Platform.runLater(()->{ PopupLoader.hide(); } );
    }

    /* add single item
    *   @key  : mRows key
    *   @row  : PlanDataRows object
    *   @sort : if true, datarows sorted according to their node ID's ( names for this case )
    */
    public void addItem( String key, PlanDataRow row, boolean sort ){
        if( mSearchFlag ){
            mSearchRows.put( key, row );
        } else {
            mRows.put( key, row );
        }
        Platform.runLater(()->{
            mController.addRow( row.getUI(), sort );
        });
    }

    // search plan
    public void search( String keyword ){
        mSearchFlag = true;
        mSearchKeyword = keyword;
        // do it once, after first search
        // if we do it every search call, when user searched twice in a row
        // we would lost the first state index
        if( mLastItemIndexTemp == 0 ) mLastItemIndexTemp = mLastItemIndex;
        // reset item index
        mLastItemIndex = 0;
        clearItems();
        downloadData();
    }

    // remove all datarows
    public void clearItems(){
        Platform.runLater(()->{
            mController.clearItems();
        });
    }

    // cancel search return first state
    public void cancelSearch(){
        mSearchFlag = false;
        // restore first state index for data download
        mLastItemIndex = mLastItemIndexTemp;
        // reset temp index
        mLastItemIndexTemp = 0;
        mController.restoreFirstState();
    }


}
