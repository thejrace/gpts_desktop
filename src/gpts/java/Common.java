/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import com.jfoenix.controls.JFXComboBox;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.NoParamCallback;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.interfaces.ReadJOCallback;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Common {
    public static String[] TIMEINTERVAL_LIST = { "Dakika", "Saat", "Gün", "Ay", "Yıl" };
    public static int FJSONObject, FJSONArray ;
    public static String STATIC_LOCATION;

    static {
        FJSONObject = 1;
        FJSONArray = 2;
    }

    public static void checkStaticFileLocation(NoParamCallback cb ){
        try {

            if( !checkDirectory( STATIC_LOCATION ) ) createStaticDirectory();
            if( !checkFile( STATIC_LOCATION + "api_user.json"  ) ) createFile(  "api_user", "{ \"init\" : true }" );
            if( !checkFile( STATIC_LOCATION + "employee_groups.json"  ) ) createFile(  "employee_groups", "[]" );
            if( !checkFile( STATIC_LOCATION + "permissions_template.json"  ) ) createFile(  "permissions_template", "[]");

            /*if( !checkDirectory( STATIC_LOCATION ) ){
                createFile(  "api_user", "{ \"init\" : true }" );
                createFile(  "employee_groups", "[]" );
                createFile(  "permissions_template", "[]" );
            }*/
            cb.action();
        } catch( Exception e ){
            e.printStackTrace();
        }
    }

    public static void createFile(String name, String content){
        try {
            // create directory
            Path path = Paths.get(STATIC_LOCATION +  name + ".json" );
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            writeStaticData( name, content );
        } catch ( Exception e ) {
            e.printStackTrace();
            //System.out.println("already exists: " + e.getMessage());
        }
    }

    public static boolean deleteFile( String path ){
        try{
            File f = new File( path );
            return (f.exists() && f.delete() );
        } catch( Exception e ){
            e.printStackTrace();
        }
        return false;
    }

    public static void copyFile(File source, File dest) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch( Exception e ) {
            e.printStackTrace();
        }
        try {
            sourceChannel.close();
            destChannel.close();
        } catch( NullPointerException | IOException e ){

        }
    }

    public static boolean checkDirectory( String path ){
        File f = new File( path );
        return f.exists() && f.isDirectory();
    }

    public static boolean checkFile( String path ){
        File f = new File( path );
        return f.exists();
    }

    public static void createStaticDirectory(){
        new File(STATIC_LOCATION ).mkdirs();
    }

    public static boolean writeStaticData( String file, String content ){
        try{
            PrintWriter writer = new PrintWriter( STATIC_LOCATION + file + ".json", "UTF-8");
            writer.print(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    public static boolean checkStaticData( String file, int type ){
        String out = readStaticData( file );
        try {
            if( type == FJSONArray ){
                new JSONArray( out );
            } else {
                new JSONObject( out );
            }
            return true;
        } catch ( JSONException e ){
            //e.printStackTrace();
            return false;
        }
    }

    public static String readJSONFile( String src ){
        try {
            FileReader fr = new FileReader( src);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while( line != null ){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            fr.close();
            return sb.toString();
        } catch( IOException e ){
            e.printStackTrace();
            return null;
        }
    }

    // todo topla bunları
    public static String readStaticData( String src ){
        try {
            FileReader fr = new FileReader( STATIC_LOCATION + src + ".json");
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while( line != null ){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            fr.close();
            return sb.toString();
        } catch( IOException e ){
            e.printStackTrace();
            return null;
        }
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

    public static String getDeviceHash(){
        return DigestUtils.sha256Hex(Common.getComputerName());
    }

    // YYYY-MM-DD HH:MM:SS to DD-MM-YYYY HH:MM:SS
    public static String revDatetime( String dt ){
        try {
            String date = dt.substring(0, 10);
            String[] exp = date.split("-");
            return exp[2]+"-"+exp[1]+"-"+exp[0]+ " " + dt.substring(11);
        } catch( StringIndexOutOfBoundsException e ){
            return dt;
        }
    }

    // DD-MM-YYYY to YYYY-MM-DD
    public static String revDateServer( String dt, String delim ){
        try {
            String[] exp = dt.split(delim);
            return exp[2]+"-"+exp[1]+"-"+exp[0];
        } catch( StringIndexOutOfBoundsException e ){
            return dt;
        }
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

    public static Map<String, Double> calculateAppWindowSize(){
        Map<String, Integer> resData = getScreenRes();
        Map<String, Double> out = new HashMap<>();
        double width = resData.get("W") * 0.5, height = resData.get("H") * 0.5;
        if( width < 1224 ) width = 1224;
        if( height < 768 ) height = 768;
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

    public static void fillComboBox(String[] vals, JFXComboBox comboBox, int selectIndex ){
        for( int k = 0; k < vals.length; k++ ) comboBox.getItems().add( vals[k] );
        if( selectIndex >= 0 ) comboBox.getSelectionModel().select(selectIndex);
    }
    public static void fillComboBox( JSONArray vals, JFXComboBox comboBox, String valKey, int selectIndex ){
        for( int k = 0; k < vals.length(); k++ ) comboBox.getItems().add( vals.getJSONObject(k).getString(valKey) );
        if( selectIndex >= 0 ) comboBox.getSelectionModel().select(selectIndex);
    }
}

