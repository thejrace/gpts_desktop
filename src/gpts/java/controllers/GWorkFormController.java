package gpts.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import gpts.java.FormValidation;
import gpts.java.GWork;
import gpts.java.GWorkSubItem;
import gpts.java.ValidationInput;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.NoParamCallback;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.ui.GWorkSubItemBox;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GWorkFormController extends PopupFormBaseController implements Initializable {


    @FXML private JFXTextField uiTaskNameInput;
    @FXML private JFXTextArea uiTaskDefInput;
    @FXML private VBox uiSubTasksContainer;
    @FXML private JFXButton uiNewSubItemBtn;
    @FXML private Tab tabBundle;
    @FXML private Tab tabDetails;
    @FXML private Tab tabDownloadProfile;   // todo will be hidden when editFlag is set
    @FXML private TabPane uiTabPane;
    @FXML private JFXTextField uiSearchInput;
    @FXML private JFXButton uiSearchBtn;
    @FXML private JFXButton uiSelectBtn;
    @FXML private JFXButton uiFinishWorkBtn;
    @FXML private VBox uiSearchContainer;
    @FXML private Label uiSummaryNameLbl;
    @FXML private Label uiSummaryDetailsLbl;
    @FXML private VBox uiSummarySubItemsContainer;

    private boolean mEditFlag = false;

    private int mSubItemStepCounter = 0;
    private Map<Integer, GWorkSubItemBox> mSubItems = new HashMap<>();
    private String mBoxClassName = "task-sub-item-even";
    private GWork mSelectedTemplate;


    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        uiNewSubItemBtn.setOnMouseClicked( ev -> {
            if( mEditFlag ){

            } else {
                // empty
                addSubItem( new GWorkSubItemBox( new GWorkSubItem() ) );
            }
        });

        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            if( mEditFlag ){

            } else {
                // set order of subitems before upload action
                int stepCounter = 1;
                for (Map.Entry<Integer, GWorkSubItemBox> entry : mSubItems.entrySet()) {
                    entry.getValue().getData().setStepOrder( stepCounter );
                    stepCounter++;
                }
                GWork newWork = new GWork();
                // pass mSubItems directly and get GWorkSubItem from GWorkSubItemBox
                newWork.add(uiTaskNameInput.getText(), uiTaskDefInput.getText(), mSubItems, new ActionCallback() {
                    @Override
                    public void onSuccess(String... params) {
                        mParentDialog.close();
                        PopupLoader.showMessage(newWork.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                        //mAddListener.onFinish( employee );
                    }
                    @Override
                    public void onError(int type) {
                        outputError(newWork.getReturnText());
                        uiSaveBtn.setDisable(false);
                        PopupLoader.hide();
                    }
                });
            }
        });

        uiSearchBtn.setOnMouseClicked( ev -> {
            FormValidation validation = new FormValidation();
            if( !validation.checkInputs( new ValidationInput[]{
                    new ValidationInput("Arama", uiSearchInput.getText(), FormValidation.CHECK_REQ )
            }) ) return;
            uiSearchContainer.getChildren().clear();
            GWork.searchTemplate(uiSearchInput.getText(), new ReadJACallback() {
                @Override
                public void onFinish(JSONArray output) {
                    int outputLength = output.length();
                    if( outputLength > 0 ){
                        Platform.runLater( () -> { uiSelectBtn.setDisable(false); });
                    } else {
                        Platform.runLater( () -> { uiSelectBtn.setDisable(true); });
                    }
                    // list found results
                    for( int k = 0; k < outputLength; k++ ){
                        JSONObject template = output.getJSONObject(k);
                        // result UI action
                        Label lbl = new Label( template.getString("name"));
                        lbl.getStyleClass().add("ctext-white");
                        JFXButton btn = new JFXButton( "Özet" );
                        btn.getStyleClass().addAll("content-btn", "content-btn-warning");
                        HBox cont = new HBox( lbl, btn );
                        cont.setSpacing(10);
                        cont.setAlignment(Pos.CENTER);
                        Platform.runLater(()->{
                            uiSearchContainer.getChildren().add(cont);
                            // display summary of work template action
                            btn.setOnMouseClicked( ev -> {
                                // we save selected template to use it to fill form when
                                // user is decided to use it
                                mSelectedTemplate = new GWork();
                                // set props
                                mSelectedTemplate.setName( template.getString("name") );
                                mSelectedTemplate.setDetails( template.getString("details"));
                                uiSummarySubItemsContainer.getChildren().clear();
                                uiSummaryNameLbl.setText(template.getString("name"));
                                uiSummaryDetailsLbl.setText(template.getString("details"));
                                // sub items come as jsonarray so decode them and print them on screen
                                JSONArray subItemsDecoded = new JSONArray(template.getString("sub_items"));
                                for( int j = 0; j < subItemsDecoded.length(); j++ ){
                                    JSONObject tempSubItemData = subItemsDecoded.getJSONObject(j);
                                    mSelectedTemplate.addSubItem( new GWorkSubItem( tempSubItemData ) );
                                    Label subItemLbl = new Label( ( j + 1 ) + " - " + tempSubItemData.getString("name") );
                                    subItemLbl.getStyleClass().add("ctext-white");
                                    uiSummarySubItemsContainer.getChildren().add( subItemLbl );
                                }
                            });
                        });
                    }
                }
            });
        });

        uiSelectBtn.setOnMouseClicked( ev -> {
            // clear everything and fill form with selected template
            uiTaskNameInput.setText(mSelectedTemplate.getName());
            uiTaskDefInput.setText(mSelectedTemplate.getDetails());
            uiSubTasksContainer.getChildren().clear();
            mSubItemStepCounter = 0;
            mSubItems = new HashMap<>();
            // add sub items in reverse order
            for( int k = mSelectedTemplate.getSubItems().size() - 1; k >= 0; k-- ){
                addSubItem( new GWorkSubItemBox( mSelectedTemplate.getSubItems().get(k) ) );
            }
            uiTabPane.getSelectionModel().select(tabDetails);
        });
    }

    private void addSubItem( GWorkSubItemBox newBox ){
        newBox.setStyleClassName( mBoxClassName );
        switchBoxClassName();
        uiSubTasksContainer.getChildren().add( 0, newBox.getUI() );
        newBox.setUIID(mSubItemStepCounter);
        mSubItems.put( mSubItemStepCounter, newBox );
        mSubItemStepCounter++;
        newBox.getController().addDeleteListener(new NoParamCallback() {
            @Override
            public void action() {
                for( int k = 0; k < uiSubTasksContainer.getChildren().size(); k++ ){
                    if( uiSubTasksContainer.getChildren().get(k).getId().equals( newBox.getUI().getId() ) ){
                        mSubItems.remove( Integer.valueOf(newBox.getUI().getId()) );
                        uiSubTasksContainer.getChildren().remove(k);
                        break;
                    }
                }
            }
        });
    }

    /*
    *  triggered from GWorkSubItemBoxController
    * */
    public void deleteSubItem(){

    }

    public void setEditData( GWork data ){
        mEditFlag = true;

        // update ui from here
    }

    private void switchBoxClassName(){
        if( mBoxClassName.equals("task-sub-item-even") ){
            mBoxClassName = "task-sub-item-odd";
        } else {
            mBoxClassName = "task-sub-item-even";
        }
    }

}
