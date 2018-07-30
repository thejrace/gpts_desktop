package gpts.java;



public class ApiUser {
    public static String PERMISSONS = "1000100000000000000000"; // test
    public static String EMAIL, NICK;

    public static boolean checkPermission( int permIndex ){
        return ApiUser.PERMISSONS.charAt(permIndex) == '1';
    }
}
