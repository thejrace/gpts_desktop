package gpts.java;

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

}
