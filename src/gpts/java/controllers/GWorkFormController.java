/* Gitaş - Obarey Inc 2018 */
package gpts.java.controllers;

import com.jfoenix.controls.*;
import gpts.java.*;
import gpts.java.interfaces.ActionCallback;
import gpts.java.interfaces.FormActionListener;
import gpts.java.interfaces.NoParamCallback;
import gpts.java.interfaces.ReadJACallback;
import gpts.java.ui.GWorkSubItemBox;
import gpts.java.ui.PopupLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
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

    @FXML private Label uiPopupHeaderLbl;
    @FXML private JFXTextField uiTaskNameInput;
    @FXML private JFXTextArea uiTaskDefInput;
    @FXML private VBox uiSubTasksContainer;
    @FXML private JFXButton uiNewSubItemBtn;
    @FXML private Tab tabBundle;
    @FXML private Tab tabDetails;
    @FXML private Tab tabDownloadProfile;
    @FXML private Tab tabDefinitions;
    @FXML private TabPane uiTabPane;
    @FXML private JFXTextField uiSearchInput;
    @FXML private JFXButton uiSearchBtn;
    @FXML private JFXButton uiSelectBtn;
    @FXML private JFXComboBox uiWorkStatusInput;
    @FXML private VBox uiSearchContainer;
    @FXML private Label uiSummaryNameLbl;
    @FXML private Label uiSummaryDetailsLbl;
    @FXML private VBox uiSummarySubItemsContainer;
    @FXML private JFXButton uiDeleteBtn;

    @FXML private JFXTextField uiEmpSearchInput;
    @FXML private JFXButton uiEmpSearchBtn;
    @FXML private VBox uiEmpSearchResultsContainer;
    @FXML private JFXComboBox uiEmpGroupInput;
    @FXML private JFXButton uiEmpGroupSelectBtn;
    @FXML private Label uiRightFormHeaderLbl;
    @FXML private JFXCheckBox uiPeriodicCheckbox;
    @FXML private DatePicker uiDueDateInput;
    @FXML private JFXTextField uiDueDateHoursInput;
    @FXML private JFXTextField uiDueDateMinsInput;

    @FXML private VBox uiRightFormPeriodicContainer;
    @FXML private JFXTextField uiPeriodicDueDateInput;
    @FXML private JFXComboBox uiPeriodicDueDateComboBox;
    @FXML private JFXTextField uiPeriodicIntervalInput;
    @FXML private JFXComboBox uiPeriodicIntervalComboBox;
    @FXML private JFXButton uiDefineWorkBtn;
    @FXML private DatePicker uiStartDateInput;
    @FXML private JFXTextField uiStartDateHoursInput;
    @FXML private JFXTextField uiStartDateMinsInput;

    // GWork status list, has to same with the server-side one
    private String[] mStatusList = { "Aktif", "Tamamlandı", "Süre Aşımı", "İptal" };
    // flags
    private boolean mEditFlag = false;
    private boolean mTemplateFlag = false;
    // counter for subItems.
    // used to keep index of added subItem in mSubItems. Therefore when user deleted a
    // subItem we can determine which one is it.
    private int mSubItemStepCounter = 0;
    // subItems holder array
    private Map<Integer, GWorkSubItemBox> mSubItems = new HashMap<>();
    // active GWork data if is set.
    // also used for editData not just for template
    private GWork mSelectedTemplate;
    // action listener
    private FormActionListener mFormActionListener;
    // selected Employee for definitions
    private Employee mSelectedEmployee;
    private EmployeeGroup mSelectedEmployeeGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb ){
        super.initCommonEvents();

        Map<String, Double> resData = Common.calculateAppWindowSize();
        uiTabPane.setPrefHeight(resData.get("H")*0.8);

        // hide def tab on initialize, we only show it when mTemplateFlag is set
        uiTabPane.getTabs().remove( tabDefinitions );

        // default header label
        uiPopupHeaderLbl.setText("Yeni İş");

        // fill status combobox
        Common.fillComboBox( mStatusList, uiWorkStatusInput, 0 );

        /* add new subItem actions  */
        uiNewSubItemBtn.setOnMouseClicked( ev -> {
            GWorkSubItemBox newBox = new GWorkSubItemBox( new GWorkSubItem() );
            // handle extra UI actions for template mode
            if( mTemplateFlag ) newBox.getController().switchToTemplateMode();
            addSubItem( newBox );
        });

        /* saving actions for every mode */
        uiSaveBtn.setOnMouseClicked( ev -> {
            uiSaveBtn.setDisable(true);
            // set order of subitems before upload action
            int stepCounter = 1;
            for (Map.Entry<Integer, GWorkSubItemBox> entry : mSubItems.entrySet()) {
                entry.getValue().getData().setStepOrder( stepCounter );
                stepCounter++;
            }
            if( !mEditFlag && !mTemplateFlag ){
                // add new work
                mSelectedTemplate = new GWork();
                // pass mSubItems directly and get GWorkSubItem from GWorkSubItemBox
                mSelectedTemplate.add(uiTaskNameInput.getText(), uiTaskDefInput.getText(), uiWorkStatusInput.getSelectionModel().getSelectedIndex(), mSubItems, mFormActionListenerCallback );
            } else if( !mEditFlag ){ // mTemplateFlag = true
                // add template
                mSelectedTemplate = new GWork();
                mSelectedTemplate.addTemplate(uiTaskNameInput.getText(), uiTaskDefInput.getText(), mSubItems, mFormActionListenerCallback );
            } else if( !mTemplateFlag ){ // mEditFlag = true
                // edit work
                mSelectedTemplate.edit(uiTaskNameInput.getText(), uiTaskDefInput.getText(), uiWorkStatusInput.getSelectionModel().getSelectedIndex(), mSubItems, mFormActionListenerCallback);
            } else {
                // edit template
                mSelectedTemplate.editTemplate(uiTaskNameInput.getText(), uiTaskDefInput.getText(), mSubItems, mFormActionListenerCallback );
            }
        });

        /* template search */
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
                        SearchResultBox cont = new SearchResultBox(template.getString("name"), "Özet");
                        Platform.runLater(()->{
                            uiSearchContainer.getChildren().add(cont);
                            // display summary of work template action
                            cont.getBtn().setOnMouseClicked( ev -> {
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

        /* template selection */
        uiSelectBtn.setOnMouseClicked( ev -> {
            fillForm();
            uiTabPane.getSelectionModel().select(tabDetails);
        });

        /* definiton periodic def checkbox */
        uiPeriodicCheckbox.setOnMouseClicked( ev -> {
            uiRightFormPeriodicContainer.setVisible(uiPeriodicCheckbox.isSelected());
        });

        /* search employee */
        uiEmpSearchBtn.setOnMouseClicked( ev -> {
            FormValidation validation = new FormValidation();
            if( !validation.checkInputs( new ValidationInput[]{
                    new ValidationInput("Arama", uiEmpSearchInput.getText(), FormValidation.CHECK_REQ )
            }) ) return;
            uiEmpSearchResultsContainer.getChildren().clear();
            Employee.search(uiEmpSearchInput.getText(), new ReadJACallback() {
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
                        JSONObject employee = output.getJSONObject(k);
                        // result UI action
                        SearchResultBox cont = new SearchResultBox(employee.getString("name"), "Seç");
                        Platform.runLater(()->{
                            uiEmpSearchResultsContainer.getChildren().add(cont);
                            // display summary of work template action
                            cont.getBtn().setOnMouseClicked( ev -> {
                                mSelectedEmployeeGroup = null;
                                mSelectedEmployee = new Employee( employee.getString("id") );
                                uiRightFormHeaderLbl.setText("Seçili Personel/Grup: " + employee.getString("name") );
                            });
                        });
                    }
                }
            });
        });

        /* fill def groups combobox */
        Common.readStaticDataJA("user_groups", new ReadJACallback() {
            @Override
            public void onFinish(JSONArray output) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Common.fillComboBox( output, uiEmpGroupInput, "name", 0 );
                    }
                });
            }
        });

        /* select emp group  */
        uiEmpGroupSelectBtn.setOnMouseClicked( ev -> {
            mSelectedEmployee = null;
            mSelectedEmployeeGroup = new EmployeeGroup( uiEmpGroupInput.getSelectionModel().getSelectedItem().toString() );
            uiRightFormHeaderLbl.setText("Seçili Personel/Grup: " + mSelectedEmployeeGroup.getName());
        });

        /* fill interval inputs */
        Common.fillComboBox( Common.TIMEINTERVAL_LIST, uiPeriodicIntervalComboBox, 0 );
        Common.fillComboBox( Common.TIMEINTERVAL_LIST, uiPeriodicDueDateComboBox, 0 );

        /* define work */
        uiDefineWorkBtn.setOnMouseClicked( ev -> {
            outputError("");
            GWorkDefinitionData defData = new GWorkDefinitionData();
            defData.setPeriodicFlag( uiPeriodicCheckbox.isSelected() );
            try {
                defData.setStartDate( uiStartDateInput.getValue().toString(), uiStartDateHoursInput.getText(), uiStartDateMinsInput.getText() );
            } catch( NullPointerException e ){

            }
            if( defData.getPeriodicFlag() ){
                // required
                try {
                    defData.setPPeriod( Integer.valueOf(uiPeriodicIntervalInput.getText()), uiPeriodicIntervalComboBox.getSelectionModel().getSelectedIndex() );
                } catch( NullPointerException | NumberFormatException e ){
                    outputError("Tanımlanma sıklığı boş bırakılamaz.");
                    return;
                }
                // optional
                try {
                    defData.setPDueDate( Integer.valueOf(uiPeriodicDueDateInput.getText()), uiPeriodicDueDateComboBox.getSelectionModel().getSelectedIndex() );
                } catch( NullPointerException e ){

                }
            } else {
                // optional
                try {
                    defData.setDueDate( uiDueDateInput.getValue().toString(), uiDueDateHoursInput.getText(), uiDueDateMinsInput.getText() );
                } catch( NullPointerException e ){
                    //e.printStackTrace();
                }
            }
            if( mSelectedEmployee != null ){
                defData.defineToEmpOrGroup("employee_id", mSelectedEmployee.getID(), mSelectedTemplate, new ActionCallback() {
                    @Override
                    public void onSuccess(String... params) {
                        PopupLoader.showMessage(defData.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                    }
                    @Override
                    public void onError(int type) {
                        outputError(defData.getReturnText());
                    }
                });
            } else if( mSelectedEmployeeGroup != null ){
                defData.defineToEmpOrGroup("employee_group_name", mSelectedEmployeeGroup.getName(), mSelectedTemplate, new ActionCallback() {
                    @Override
                    public void onSuccess(String... params) {
                        PopupLoader.showMessage(defData.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
                    }
                    @Override
                    public void onError(int type) {
                        outputError(defData.getReturnText());
                    }
                });
            } else {
                outputError("Tanımlanacak Personel veya Personel Grubu seçilmedi.");
            }
        });

    }

    /*
    *  adds sub item to UI
    * */
    private void addSubItem( GWorkSubItemBox newBox ){
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
    *  FormActionListener setter for actions after server-request actions.
    *  used for both work and template after adding and editing actions.
    * */
    public void setFormActionListener( FormActionListener listener ){
        mFormActionListener = listener;
    }

    /*
    *  flag setter for editing actions
    * */
    public void setEditFlag( boolean val ){
        mEditFlag = val;
    }

    /*
    *   flag setter for templates
    **/
    public void setTemplateFlag( boolean val ){
        mTemplateFlag = val;
        if( mTemplateFlag ) switchToTemplateAddMode();
    }

    /*
    *  main UI fill method for both work and template.
    *  called from other controllers.
    * */
    public void setData( GWork data ){
        mSelectedTemplate = data;
        if( mTemplateFlag && mEditFlag ){
            // work template edit
            uiTabPane.getTabs().remove( tabDownloadProfile );
            uiTabPane.getTabs().add( tabDefinitions );
            uiDeleteBtn.setVisible(true);
            uiWorkStatusInput.setVisible(false);
            fillForm();
        } else if( mEditFlag ){
            // work edit
            uiPopupHeaderLbl.setText("'" + data.getName() + "'");
            // fill form before to determine if we switch to preview mode
            fillForm();
            if( mSelectedTemplate.getStatus() != GWork.STATUS_ACTIVE ) switchToPreviewMod();
            // hide download tab
            uiTabPane.getTabs().remove( tabDownloadProfile );
            uiTabPane.getTabs().remove( tabDefinitions );
        }
    }

    /*
    *  fill all inputs and append subItems to screen using active data.
    *  used for both work and template.
    * */
    private void fillForm(){
        // clear everything and fill form with selected template
        uiTaskNameInput.setText(mSelectedTemplate.getName());
        uiTaskDefInput.setText(mSelectedTemplate.getDetails());
        uiSubTasksContainer.getChildren().clear();
        mSubItemStepCounter = 0;
        mSubItems = new HashMap<>();
        uiWorkStatusInput.getSelectionModel().select( mSelectedTemplate.getStatus());
        for( int k = 0; k < mSelectedTemplate.getSubItems().size(); k++ ){
            addSubItem( new GWorkSubItemBox( mSelectedTemplate.getSubItems().get(k) ) );
        }
    }

    /*
    *  switch UI to template add form.
    * */
    private void switchToTemplateAddMode(){
        uiTabPane.getTabs().remove( tabDownloadProfile );
        uiTabPane.getTabs().remove( tabDefinitions );
        uiWorkStatusInput.setVisible(false);
    }

    /*
    *  switch UI to inspect GWork ( completed, expired etc.. )
    * */
    private void switchToPreviewMod(){
        uiTaskNameInput.setDisable(true);
        uiTaskDefInput.setDisable(true);
        uiWorkStatusInput.setDisable(true);
        uiSaveBtn.setDisable(true);
        uiNewSubItemBtn.setDisable(true);
        for (Map.Entry<Integer, GWorkSubItemBox> entry : mSubItems.entrySet()) {
            entry.getValue().getController().switchToPreviewMode();
        }
    }

    /*
    *  common actioncallback for all form-submit actions
    * */
    private ActionCallback mFormActionListenerCallback = new ActionCallback() {
        @Override
        public void onSuccess(String... params) {
            mParentDialog.close();
            PopupLoader.showMessage(mSelectedTemplate.getReturnText(), PopupLoader.MESSAGE_SUCCESS );
            mFormActionListener.onFinish( mSelectedTemplate );
        }

        @Override
        public void onError(int type) {
            outputError(mSelectedTemplate.getReturnText());
            uiSaveBtn.setDisable(false);
            PopupLoader.hide();
        }
    };

    class SearchResultBox extends HBox {
        private JFXButton mBtn;
        public SearchResultBox( String label, String btnlbl ){
            super();
            Label lbl = new Label( label );
            lbl.getStyleClass().add("ctext-white");
            mBtn = new JFXButton( btnlbl );
            mBtn.getStyleClass().addAll("content-btn", "content-btn-warning");
            getChildren().addAll( lbl, mBtn );
            setSpacing(10);
            setAlignment(Pos.CENTER);
        }
        public JFXButton getBtn(){
            return mBtn;
        }
    }

}
