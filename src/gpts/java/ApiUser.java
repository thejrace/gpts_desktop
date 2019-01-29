/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.ReadJOCallback;
import gpts.java.interfaces.WebRequestCallback;
import gpts.java.ui.LoginScreen;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiUser {
    public static String PERMISSONS = "1111111111111111111111"; // test
    public static String EMAIL, NICK;

    public static int NO_FILE_ERROR = 0, NO_DATA_ERROR = 1, FORM_VALIDATION_ERROR = 2, LOGIN_ERROR = 3, UNKNOWN_DEVICE;
    public static String STATUS_TEXT;

    public static boolean checkPermission( int permIndex ){
        return ApiUser.PERMISSONS.charAt(permIndex) == '1';
    }

    public static void login( String email, String password, ActionCallback cb ){
        FormValidation validation = new FormValidation();
        boolean inputCheck = validation.checkInputs( new ValidationInput[]{
                new ValidationInput("Email", email, new int[]{ FormValidation.CHECK_REQ, FormValidation.CHECK_EMAIL } ),
                new ValidationInput("Şifre", password, FormValidation.CHECK_REQ )
        });
        if( !inputCheck ){
            STATUS_TEXT = validation.getMessage();
            cb.onError( FORM_VALIDATION_ERROR );
            return;
        }
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("req", "desktop_login");
                params.put("api_email", email);
                params.put("api_password", password );
                WebRequest req = new WebRequest(WebRequest.SERVICE_URL, params);
                req.appendApiUserData( false );
                req.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        int statusFlag = output.getInt(WebRequest.STATUS_FLAG);
                        if( statusFlag == 1 ){
                            loginSuccessCallback( email );
                            cb.onSuccess();
                        } else {
                            STATUS_TEXT = output.getString(WebRequest.RETURN_TEXT);
                            cb.onError( LOGIN_ERROR );
                        }
                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public static void checkDevice( ActionCallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("req", "device_check");
                WebRequest req = new WebRequest(WebRequest.SERVICE_URL, params);
                req.action(new WebRequestCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        int statusFlag = output.getInt(WebRequest.STATUS_FLAG);
                        if( statusFlag == 1 ){
                            cb.onSuccess();
                        } else {
                            cb.onError( UNKNOWN_DEVICE );
                        }
                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public static void logout(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ApiUser.EMAIL = null;
                Common.writeStaticData("api_user", "{ \"init\": true }" );
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Main.stage.close();
                            LoginScreen loginScreen = new LoginScreen();
                            loginScreen.start( new Stage() );
                        } catch( Exception e ){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public static void loginSuccessCallback( String email ){
        ApiUser.EMAIL = email;
        Common.writeStaticData("api_user", "{ \"api_email\":\""+email+"\" }");
    }

    public static void checkLocalLoginData( ActionCallback cb ){
        try {
            if( Common.checkStaticData("api_user", Common.FJSONObject) ){
                Common.readStaticDataJO("api_user", new ReadJOCallback() {
                    @Override
                    public void onFinish(JSONObject output) {
                        try {
                            if( output.has("init") ){
                                cb.onError(NO_DATA_ERROR);
                            } else {
                                ApiUser.EMAIL = output.getString("api_email");
                                cb.onSuccess();
                            }
                        } catch ( JSONException e ){
                            cb.onError(NO_FILE_ERROR);
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                cb.onError(NO_FILE_ERROR);
            }
        } catch( Exception e ){
            e.printStackTrace();
        }
    }
}