package gpts.java;

import gpts.java.interfaces.ActionCallback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownload {

    public static int ERROR_ON_CLOSE = 0, ERROR_ON_DOWNLOAD = 1, SUCCESS = 2;

    public static void downloadFileFromUrlAsync( String url, String destinationFile, ActionCallback cb ){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFileFromUrl( url, destinationFile, cb );
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public static void downloadFileFromUrl( String url, String destinationFile, ActionCallback cb ){
        URL urlObj = null;
        ReadableByteChannel rbcObj = null;
        FileOutputStream fOutStream  = null;
        try {
            urlObj = new URL(url);
            rbcObj = Channels.newChannel(urlObj.openStream());
            fOutStream = new FileOutputStream(destinationFile);
            fOutStream.getChannel().transferFrom(rbcObj, 0, Long.MAX_VALUE);
            System.out.println("Download completed.");
            cb.onSuccess();
        } catch (IOException e) {
            cb.onError(ERROR_ON_DOWNLOAD);
            System.out.println("Download error!");
            e.printStackTrace();
        } finally {
            try {
                if(fOutStream != null){
                    fOutStream.close();
                }
                if(rbcObj != null) {
                    rbcObj.close();
                }
            } catch (IOException e) {
                cb.onError(ERROR_ON_CLOSE);
                System.out.println("Close object failed( File Download )");
                e.printStackTrace();
            }
        }
    }


}
