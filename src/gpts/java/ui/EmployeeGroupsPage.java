/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.EmployeeGroup;
import gpts.java.WebRequest;
import gpts.java.controllers.EmployeeGroupsController;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployeeGroupsPage extends BasePage {

    protected Map<String, EmployeeGroupDataRow> mRows = new HashMap<>();
    protected Map<String, EmployeeGroupDataRow> mSearchRows = new HashMap<>();
    private EmployeeGroupsController mController;

    public EmployeeGroupsPage(){
        mItemDataKey = "name";
    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (EmployeeGroupsController)mBaseController;
        downloadData();
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
                    downloadParams.put("req", "employee_groups_search");
                    downloadParams.put("keyword", mSearchKeyword );
                } else {
                    downloadParams.put("req", "employee_groups_download");
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
                                    EmployeeGroupDataRow row = new EmployeeGroupDataRow( new EmployeeGroup( temp ));
                                    addItem( temp.getString(mItemDataKey), row, false, false );
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
     *   @row  : ui object
     *   @sort : if true, datarows sorted according to their node ID's ( names for this case )
     */
    public void addItem(String key, EmployeeGroupDataRow row, boolean sort, boolean formFlag ){
        // if item is added during  search, cancel search action
        if( formFlag && mSearchFlag ) cancelSearch();
        if( mSearchFlag ){
            mSearchRows.put( key, row );
        } else {
            mRows.put( key, row );
        }
        Platform.runLater( () -> { mController.addRow( row.getUI(), sort, mSearchFlag); } );
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
        mSearchRows = new HashMap<>();
        mController.restoreFirstState();
    }

}
