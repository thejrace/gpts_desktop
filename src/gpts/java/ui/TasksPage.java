/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.controllers.TasksController;

public class TasksPage extends BasePage {

    private TasksController mController;

    public TasksPage(  ){

    }

    @Override
    public void initUI( String fxml ){
        super.initUI(fxml);
        mController = (TasksController)mBaseController;

    }

}
