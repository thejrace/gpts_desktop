/* Gitaş - Obarey Inc 2018 */
package gpts.java;

import javafx.util.Pair;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {

    private String mMessage;

    public static int CHECK_REQ = 1,
                      CHECK_EMAIL = 2,
                      CHECK_NUMERIC = 3,
                      CHECK_HOUR_FORMAT = 4;

    public boolean check( ValidationInput input ){
        Pair params = new Pair<String, String>( input.getKey(), input.getValue().trim() );
        int validator;
        // multiple validators
        if( input.getMultipleValidatorFlag() ){
            for( int k = 0; k < input.getValidators().length; k++ ){
                validator = input.getValidators()[k];
                if( !getValidator( validator, params ) ) return false;
            }
            // no error
            return true;
        } else {
            validator = input.getValidator();
            return getValidator( validator, params );
        }
    }

    // call_user_func_array java version obarey
    private boolean getValidator( int validator,  Pair<String, String> params ){
        if( validator == CHECK_REQ ){
            return checkReq( params );
        } else if( validator == CHECK_EMAIL ){
            return checkEmail( params );
        } else if ( validator == CHECK_NUMERIC ){
            return checkNumeric( params );
        } else if ( validator == CHECK_HOUR_FORMAT ){
            return checkHourFormat( params );
        } else {
            mMessage = "Bilinmeyen validator.";
            return false;
        }
    }

    public boolean checkEmail( Pair<String, String> input ){
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(input.getValue());
        if( matcher.find() ){
            return true;
        } else {
            mMessage = input.getKey() + " geçersiz.";
            return false;
        }
    }

    public boolean checkReq( Pair<String, String> input ){
        if( input.getValue().trim().equals("") ){
            mMessage = input.getKey() + " boş bırakılamaz.";
            return false;
        } else {
            return true;
        }
    }

    public boolean checkNumeric( Pair<String, String> input ){
        final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
        Matcher matcher = NUMERIC_PATTERN.matcher(input.getValue());
        if( matcher.find()){
            return true;
        } else {
            mMessage = input.getKey() + " yalnızca rakam içermelidir.";
            return false;
        }
    }

    // XX:XX format check
    public boolean checkHourFormat( Pair<String, String> input ){
        // length control
        String val = Common.regexTrim( input.getValue() );
        if( val.length() != 5 ){
            mMessage = input.getKey() + " gerçersiz formatta.";
            return false;
        }
        final Pattern TIME24HOURS_PATTERN = Pattern.compile("^([0-1]\\d|2[0-3]):([0-5]\\d)$");
        Matcher matcher = TIME24HOURS_PATTERN.matcher(input.getValue());
        if( matcher.find() ){
            return true;
        } else {
            mMessage = input.getKey() + " gerçersiz formatta.";
            return false;
        }
    }

    public boolean checkInputs( ValidationInput[] inputs  ){
        for( int k = 0; k < inputs.length; k++ ){
            if( !inputs[k].check() ){
                mMessage = inputs[k].getValidationMessage();
                return false;
            }
        }
        return true;
    }



    public String getMessage(){
        return mMessage;
    }

}
