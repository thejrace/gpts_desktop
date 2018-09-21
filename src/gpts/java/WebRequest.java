/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.WebRequestCallback;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
public class WebRequest {

    private String mURL, mOutput;
    private Map<String, String> mParams;

    //public static String SERVICE_URL = "http://localhost/gpts/service.php";
    public static String SERVICE_URL = "http://178.18.206.163/gpts_web_service/service.php";

    public static String STATUS_FLAG = "ok",
                         RETURN_TEXT = "text";

    // @todo internet baglantisi yoksa hata ver
    public WebRequest( String url, Map<String, String> params ) {
        mURL = url;
        mParams = params;
    }

    public void action( WebRequestCallback callback ){
        download( callback );
    }

    public void actionAsync( WebRequestCallback callback ){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                download( callback );
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void download( WebRequestCallback callback ){
        HttpURLConnection connection = null;
        try {
            ArrayList<String> paramsSerializedArray = new ArrayList<>();
            for (Map.Entry<String, String> param : mParams.entrySet()) {
                paramsSerializedArray.add( param.getKey() + "=" + param.getValue() );
            }
            // test user params
            paramsSerializedArray.add("api_email=ahmet@obarey.com");
            paramsSerializedArray.add("api_password=wazzabii308");
            paramsSerializedArray.add("api_device_hash=test hash 3");
            paramsSerializedArray.add("api_device_name=test device name 2");
            paramsSerializedArray.add("api_device_type=1");
            paramsSerializedArray.add("api_device_os=Windows");
            String serializedParams = Common.stringArrayListJoin(paramsSerializedArray, "&");

            URL url = new URL(mURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=ISO-8859-1");
            //connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.setRequestProperty("Content-Length",

                    Integer.toString(serializedParams.getBytes().length));
            connection.setRequestProperty("Content-Language", "tr-TR");
            connection.setRequestProperty( "charset", "ISO-8859-1");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            //wr.writeUTF(this.params);
            byte[] utf8JsonString = serializedParams.getBytes("UTF8");
            wr.write(utf8JsonString, 0, utf8JsonString.length);
            wr.close();

            // donen
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // StringBuffer Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            mOutput = response.toString();
            System.out.println(mOutput);
            callback.onFinish( new JSONObject( mOutput ) );
        } catch (Exception e) {
            System.out.println("İstek yapılırken bir hata oluştu. Tekrar deneniyor.");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
