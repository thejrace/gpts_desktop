/* Gitaş - Obarey Inc 2018 */
package gpts.java;

public class ValidationInput {

    private String mValue, mKey;
    private int mValidator;
    private String mValidationMessage;

    private int[] mValidators;
    private boolean mMultipleValidatorFlag = false;

    public ValidationInput( String key, String value, int validator ){
        mKey = key;
        mValue = value;
        mValidator = validator;
    }

    // multiple validation constructor
    public ValidationInput( String key, String value, int[] validators ){
        mKey = key;
        mValue = value;
        mValidators = validators;
        mMultipleValidatorFlag = true;
    }

    public boolean check(){
        FormValidation validation = new FormValidation();
        if( validation.check( this ) ){
            return true;
        } else {
            mValidationMessage = validation.getMessage();
            return false;
        }
    }

    public int[] getValidators(){
        return mValidators;
    }

    public boolean getMultipleValidatorFlag(){
        return mMultipleValidatorFlag;
    }
    public String getValidationMessage(){
        return mValidationMessage;
    }
    public String getValue(){
        return mValue;
    }
    public String getKey(){
        return mKey;
    }
    public int getValidator(){
        return mValidator;
    }
}
