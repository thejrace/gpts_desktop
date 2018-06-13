/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.DailyPlan;
import gpts.java.WebRequest;
import gpts.java.controllers.PlansController;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class PlansPage extends BasePage {

    private PlansController mController;


    private int mRRP = 5;
    private int mLastItemIndex = 0;
    private JSONArray mData;
    private Map<String, PlanDataRow> mRows = new HashMap<>();
    private boolean mDownloadInited = false;

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

    // we show limited data on intialize, if user wants to see more
    // clicks to download more button
    public void downloadData(){
        // if download more data, show loader
        // first download loader inited from planscontroller
        if( mDownloadInited ) PopupLoader.show("Veri alınıyor..");
        if( !mDownloadInited ) mDownloadInited = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> downloadParams = new HashMap<>();
                downloadParams.put("req", "daily_plan_schemas_download");
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
        for( int k = 0; k < length; k++ ){
            try {
                temp = mData.getJSONObject(k);
                PlanDataRow row = new PlanDataRow( new DailyPlan( temp.getString("id"), temp.getString("name"), temp.getString("start"), temp.getString("end"), temp.getString("plan_interval") ));
                mRows.put( temp.getString("id"), row );
                Platform.runLater(()->{
                    mController.addRow( row.getUI() );
                });
            } catch ( JSONException e ){
                // if length of data less than rrp, we will get exception, so we handle it
                break;
            }
        }
        Platform.runLater(()->{ PopupLoader.hide(); } );

    }

}
