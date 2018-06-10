/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.controllers.EmployeeGroupsController;

public class EmployeeGroupsPage extends BasePage {

    private EmployeeGroupsController mController;

    public EmployeeGroupsPage(){

    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (EmployeeGroupsController)mBaseController;

    }

}
