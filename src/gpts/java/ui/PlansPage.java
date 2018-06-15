/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.DailyPlan;
import gpts.java.WebRequest;
import gpts.java.controllers.PlansController;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class PlansPage extends BasePage {

    private PlansController mController;
    // Rows holder lists
    protected Map<String, PlanDataRow> mRows = new HashMap<>();
    protected Map<String, PlanDataRow> mSearchRows = new HashMap<>();
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
        mController.disableMoreBtn( false );
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
                        addItems(new BasePage.AddItemCallback() {
                            @Override
                            public void action( int length ) {
                                if( length < mRRP ) mController.disableMoreBtn( true );
                                JSONObject temp;
                                for( int k = 0; k < length; k++ ){
                                    temp = mData.getJSONObject(k);
                                    PlanDataRow row = new PlanDataRow( new DailyPlan( temp.getString("id"), temp.getString("name"), temp.getString("start"), temp.getString("end"), temp.getString("plan_interval") ));
                                    addItem( temp.getString("id"), row, false );
                                }
                            }
                        });
                    }
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
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
        Platform.runLater( () -> { mController.addRow( row.getUI(), sort); } );
    }

    @Override
    public void search( String keyword ){
        // if user makes search repeateadly, we avoid overwriting
        // first state's moreBtton state, that's why we only save it
        // if no search flag is set
        if( !mSearchFlag ) mController.saveMoreBtnState();
        super.search( keyword );
        clearItems();
        downloadData();
    }

    // remove all datarows
    private void clearItems(){
        Platform.runLater(()->{
            mController.clearItems();
        });
    }

    // cancel search return first state
    public void cancelSearch(){
        super.cancelSearch();
        mController.restoreFirstState();
    }


}
