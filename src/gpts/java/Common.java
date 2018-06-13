/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.ReadJOCallback;
import gpts.java.interfaces.WebRequestCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Common {

    // join an arraylist with connector character
    public static String stringArrayListJoin(ArrayList<String> array, String con ){
        StringBuilder sb = new StringBuilder();
        int arraySize = array.size();
        for( int k = 0; k < arraySize; k++ ){
            sb.append(array.get(k));
            if( k < arraySize - 1 ) sb.append(con);
        }
        return sb.toString();
    }

    private static String readStaticData(){
        try {
            FileReader fr = new FileReader("src/gpts/config/static_data.json");
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch( IOException | JSONException e ){
            e.printStackTrace();
        }
        return null;
    }

    public static void readStaticDataJO( String key, ReadJOCallback cb ){
        Thread read = new Thread(new Runnable() {
            @Override
            public void run() {
                cb.onFinish( new JSONObject( readStaticData() ).getJSONObject(key) );
            }
        });
        read.setDaemon(true);
        read.start();
    }

    public static void readStaticDataJA( String key, ReadJACallback cb ){
        Thread read = new Thread(new Runnable() {
            @Override
            public void run() {
                cb.onFinish( new JSONObject( readStaticData() ).getJSONArray(key) );
            }
        });
        read.setDaemon(true);
        read.start();
    }

    public static String regexTrim( String str ){
        return str.replaceAll("\u00A0", "");
    }

}

