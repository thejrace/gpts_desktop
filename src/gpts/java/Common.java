/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.ReadJOCallback;
import gpts.java.interfaces.WebRequestCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Common {

    public static boolean writeStaticData( String file, String content ){
        try{
            PrintWriter writer = new PrintWriter("src/gpts/config/"+file+".json", "UTF-8");
            writer.println(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public static boolean checkStaticData( String file ){
        try {
            FileReader fr = new FileReader("src/gpts/config/"+file+".json");
            return true;
        } catch( IOException e ){
            //e.printStackTrace();
            return false;
        }
    }

    public static String readStaticData( String file ){
        try {
            FileReader fr = new FileReader("src/gpts/config/"+file+".json");
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

    public static void readStaticDataJO( String filename, ReadJOCallback cb ){
        Thread read = new Thread(new Runnable() {
            @Override
            public void run() {
                cb.onFinish( new JSONObject( readStaticData(filename) ) );
            }
        });
        read.setDaemon(true);
        read.start();
    }

    public static void readStaticDataJA( String filename, ReadJACallback cb ){
        Thread read = new Thread(new Runnable() {
            @Override
            public void run() {
                cb.onFinish( new JSONArray( readStaticData(filename) ) );
            }
        });
        read.setDaemon(true);
        read.start();
    }

    public static String regexTrim( String str ){
        return str.replaceAll("\u00A0", "");
    }

    // outputs 1..9   -> 01..
    // outputs 10..23 -> 10..
    public static String convertTimeFormat( int time ){
        if( time < 0 ) time *= -1;
        if( time < 10 ){
            return "0"+String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }

    public static String getComputerName(){
        String hostname = "Bilinmiyor";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex)        {
            System.out.println("Bilgisayar adi alinamadi.");
        }
        return hostname;
    }

    public static String revDatetime( String dt ){
        String date = dt.substring(0, 10);
        String[] exp = date.split("-");
        return exp[2]+"-"+exp[1]+"-"+exp[0]+ " " + dt.substring(11);
    }

    public static String revDate( String dt ){
        String[] exp = dt.split("-");
        return  exp[2]+"-"+exp[1]+"-"+exp[0];
    }

    public static Map<String, Integer> getScreenRes(){
        Map<String, Integer> out = new HashMap<>();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        out.put("W", width );
        out.put("H", height );
        return out;
    }

    public static long getUnix() {
        return (System.currentTimeMillis() / 1000L) - 3600;
    }

    public static String getCurrentDatetime(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentHmin(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return dateFormat.format(date);

    }

    public static String getYesterdayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

}

