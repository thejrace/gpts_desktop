/* Gitaş - Obarey Inc 2018 */
package gpts.java.ui;

import gpts.java.DailyPlanSchema;
import gpts.java.controllers.PlanDataRowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PlanDataRow {

    private DailyPlanSchema mDailyPlan;
    private AnchorPane mUI;
    private PlanDataRowController mController;

    public PlanDataRow( DailyPlanSchema plan ){
        mDailyPlan = plan;
        initUI();
    }

    private void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gpts/res/fxml/plan_data_row.fxml"));
            mUI  = loader.load();
            mUI.setId( mDailyPlan.getName() );
            mController = loader.getController();
            updateUI();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public AnchorPane getUI(){
        return mUI;
    }

    public void updateUI(){
        mController.setData( mDailyPlan );
    }

    // after edit, update data and ui
    public void updateData( DailyPlanSchema plan ){
        mDailyPlan = plan;
        updateUI();
    }

    public DailyPlanSchema getData(){
        return mDailyPlan;
    }

}
