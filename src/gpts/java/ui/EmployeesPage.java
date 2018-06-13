/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.Employee;
import gpts.java.WebRequest;
import gpts.java.controllers.EmployeesController;
import gpts.java.interfaces.WebRequestCallback;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployeesPage extends BasePage{

    private boolean mDownloadThreadActive = true, mDataDownloadInited;
    private Map<String, EmpBox> mEmpBoxes = new HashMap<>();
    private EmployeesController mController;

    public EmployeesPage(  ){

    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (EmployeesController)mBaseController;
        startDownloadThread();
    }

    public void startDownloadThread(){
        Map<String, String> downloadParams = new HashMap<>();
        downloadParams.put("req", "employees_download");
        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while( mDownloadThreadActive ){
                    // download data
                    WebRequest request = new WebRequest(WebRequest.SERVICE_URL, downloadParams );
                    request.action(new WebRequestCallback() {
                        @Override
                        public void onFinish(JSONObject output) {
                            JSONArray list = output.getJSONArray("data");
                            JSONObject tempData;
                            // first download and initialize boxes
                            if( !mDataDownloadInited ){
                                for( int k = 0; k < list.length(); k++ ){
                                    tempData = list.getJSONObject(k);
                                    final EmpBox empBox = new EmpBox( new Employee( tempData ) );
                                    mEmpBoxes.put( tempData.getString("id"), empBox );
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            mController.addEmpBox( empBox.getUI() );
                                        }
                                    });
                                }
                                mDataDownloadInited = true;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        PopupLoader.hide();
                                    }
                                });
                            } else {
                                // boxes initalized, update them
                                for( int k = 0; k < list.length(); k++ ){
                                    tempData = list.getJSONObject(k);
                                    try {
                                        EmpBox boxTemp = mEmpBoxes.get( tempData.getString("id"));
                                        boxTemp.updateData( new Employee( tempData ) );
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                boxTemp.updateUI();
                                            }
                                        });
                                    } catch( NullPointerException e ){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    try {
                        // todo configden alinacak
                        Thread.sleep( 60000 );
                    } catch( InterruptedException e ){
                        e.printStackTrace();
                    }
                }
            }
        });
        downloadThread.setDaemon(true);
        downloadThread.start();
    }

}
