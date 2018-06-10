/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.controllers.SettingsController;

public class SettingsPage extends BasePage {

    private SettingsController mController;

    public SettingsPage(  ){

    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (SettingsController)mBaseController;

    }

}
