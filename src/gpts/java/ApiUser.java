/* Gitaş - Obarey Inc 2018 */
package gpts.java;

public class ApiUser {
    public static String PERMISSONS = "1111111111111111111111"; // test
    public static String EMAIL, NICK;

    public static boolean checkPermission( int permIndex ){
        return ApiUser.PERMISSONS.charAt(permIndex) == '1';
    }
}
