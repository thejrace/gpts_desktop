/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.controllers.MainController;
import gpts.java.interfaces.WebRequestCallback;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServerSync {

    public static int FREQUENCY = 60;
    public static boolean ACTIVE = true;
    public static Map<String, String> PARAMS = new HashMap<>();
    private static boolean CACHEDDATADOWNLOADFLAG = false;

    public static int TRIGGER_ACTION_ADD = 1,
                      TRIGGER_ACTION_EDIT = 2,
                      TRIGGER_ACTION_DELETE = 3;

    public static int TKEY_PLAN_SCHEMAS = 2,
                      TKEY_EMPLOYEES = 1,
                      TKEY_TASKS = 4,
                      TKEY_EMPLOYEE_GROUPS = 3;

    public static void start(){
        PARAMS.put("req", "app_server_sync");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                downloadCachedData();
                while( ACTIVE ){
                    if( CACHEDDATADOWNLOADFLAG ) continue;
                    // first check cached data
                    /*if( !checkCachedData() ){
                        downloadCachedData();
                        continue;
                    }*/
                    MainController.CONTENT_CONTROLLER.updateSyncStatus("Senkron yapılıyor..");
                    // sync
                    WebRequest req = new WebRequest(WebRequest.SERVICE_URL, PARAMS);
                    req.action(new WebRequestCallback() {
                        @Override
                        public void onFinish(JSONObject output) {
                            // check api triggers from server to figure out we need to update anything on
                            // client side. ( static jsons, download threads )



                            MainController.CONTENT_CONTROLLER.updateSyncStatus("Son senkron " + Common.getCurrentHmin() );
                        }
                    });
                    try {
                        Thread.sleep( FREQUENCY * 1000 );
                    } catch( InterruptedException e ){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static boolean checkCachedData(){
        return Common.checkStaticData("plan_schemas", Common.FJSONArray) && Common.checkStaticData("user_groups", Common.FJSONArray);
    }

    public static void downloadCachedData(){
        CACHEDDATADOWNLOADFLAG = true;
        MainController.CONTENT_CONTROLLER.updateSyncStatus("CDownload başladı.");
        Map<String, String> params = new HashMap<>();
        params.put("req", "download_cached_data");
        WebRequest req = new WebRequest(WebRequest.SERVICE_URL, params );
        req.actionAsync(new WebRequestCallback() {
            @Override
            public void onFinish(JSONObject output) {
                if( Common.writeStaticData("user_groups", output.getJSONObject("data").getString("employee_groups")) &&
                    Common.writeStaticData("permissions_template", output.getJSONObject("data").getString("permissions_template")) &&
                    Common.writeStaticData("plan_schemas", output.getJSONObject("data").getString("plan_schemas")) ){
                    MainController.CONTENT_CONTROLLER.updateSyncStatus("CDownload tamamlandı.");
                } else {
                    MainController.CONTENT_CONTROLLER.updateSyncStatus("CDownload yazım hatası. Tekrar deneniyor.");
                }
                CACHEDDATADOWNLOADFLAG = false;
            }
        });
    }
    public static void setSyncStatus( boolean flag ){
        ACTIVE = flag;
    }
}
