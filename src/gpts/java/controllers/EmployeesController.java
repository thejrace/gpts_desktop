/***
 *  Gitaş - Obarey Inc. 2018
 *
 * */

package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import gpts.java.EmpBox;
import gpts.java.Employee;
import gpts.java.WebRequest;
import gpts.java.WebRequestCallback;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;


import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EmployeesController extends BaseContentController implements Initializable {

    @FXML private JFXButton uiAddBtn;
    @FXML private JFXButton uiSearchBtn;
    @FXML private TextField uiSearchInput;
    @FXML private FlowPane uiEmpBoxContainer;

    private boolean mDownloadThreadActive = true;
    private boolean mEnableSearch = false;
    private boolean mDataDownloadInited = false;

    private Map<String, EmpBox> mEmpBoxes = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle res ){

        startDownloadThread();

        // open employee add form
        uiAddBtn.setOnMouseClicked( ev -> {

        });

        // search employees
        uiSearchBtn.setOnMouseClicked( ev -> {
            if( mEnableSearch ) return;
        });

    }

    private void startDownloadThread(){
        Map<String, String> downloadParams = new HashMap<>();
        downloadParams.put("req", "employees_download");
        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while( mDownloadThreadActive ){
                    // download data
                    WebRequest request = new WebRequest(WebRequest.SERVICE_URL, downloadParams );
                    request.actionAsync(new WebRequestCallback() {
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
                                            uiEmpBoxContainer.getChildren().add( empBox.getUI() );
                                        }
                                    });
                                }
                                mDataDownloadInited = true;
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


    public void testMethod(){
        System.out.println("Employeiiii teeeesssttt");
    }




}
