/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.controllers.PlansController;


public class PlansPage extends BasePage {

    private PlansController mController;

    public PlansPage(){

    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (PlansController)mBaseController;

    }

}
