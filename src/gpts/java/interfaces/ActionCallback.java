/* Gitaş - Obarey Inc 2018 */
package gpts.java.interfaces;

public interface ActionCallback {

    // https://stackoverflow.com/questions/3158730/java-3-dots-in-parameters
    void onSuccess( String... params  );

    void onError( int type );

}
