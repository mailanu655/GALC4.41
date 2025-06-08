package com.honda.galc.client.teamleader.qi.defectResult;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.stage.Screen;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * 
 * <h3>DefectResultMaintPanel Class description</h3>
 * <p>
 * DefectResultMaintPanel is used to load data in TableViews and perform the actions.
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author L&TInfotech<br>
 * 
 */
public class DefectResultMaintPanel extends QiAbstractTabbedView<DefectResultMaintModel, DefectResultMaintController>{
	
	private  SearchMaintenancePanel searchMaintenancePanel;
	private ListView<String> searchedDefectListView; 
	private LabeledUpperCaseTextField defectFilterTextField;	
	private LoggedButton advancedSearchBtn;
	private LoggedButton searchBtn;
	private LoggedButton submitBtn;
	private LoggedButton resetButton;
	
	public DefectResultMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	
	public String getScreenName() {
		return "Data Correction";
	}
	
	@Override
	public void initView() {
		searchMaintenancePanel = new SearchMaintenancePanel(getModel(),QiConstant.DATA_CORRECTION, getController().isFullAccess());
		searchMaintenancePanel.setDefectResultMaintPanel(this);
		getController().setSearchMaintenancePanel(searchMaintenancePanel);
		MigPane mainPane = new MigPane("insets 5", "[left,grow]", "");
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double screenResolutionWidth = screenBounds.getWidth();
		double screenResolutionHeight = screenBounds.getHeight();
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		searchBtn = createBtn("Search ", getController());
		searchBtn.setId("searchBtn");
		searchBtn.setDisable(true);
		advancedSearchBtn = createBtn("Advanced Search ", getController());
		advancedSearchBtn.setId("advancedSearchBtn");
		advancedSearchBtn.setDisable(true);
		resetButton = createBtn(QiConstant.RESET, getController());
		resetButton.setId("resetButton");
		resetButton.setDisable(true);
		submitBtn = createBtn("Submit ", getController());
		submitBtn.setId("submitBtn");
		submitBtn.setDisable(true);
		
		mainPane.add(getSearchAndAdvanceAndResetButton(screenResolutionWidth),"wrap");
		mainPane.add(createTitledPane("Current Defect Selection",createSearchedDefectContainer(screenResolutionWidth),screenResolutionWidth*0.95,screenResolutionHeight*0.40),"span");
		this.setTop(searchMaintenancePanel.getSearchPanel());
		this.setCenter(mainPane);
		
	
		searchedDefectListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
	    {
	        public void changed(ObservableValue<? extends String> ov,
	                final String oldvalue, final String newvalue) 
	        {
				submitBtn.setDisable(searchedDefectListView.getSelectionModel().getSelectedItems().isEmpty());
	    }});	
		
		
	}
	
	private MigPane getSearchAndAdvanceAndResetButton(double screenResolutionWidth) {
		MigPane searchAndAdvanceButtonPane = new MigPane("insets 5", "", "");
		searchAndAdvanceButtonPane.add(searchBtn,"split 2");
		searchAndAdvanceButtonPane.add(advancedSearchBtn);
		searchAndAdvanceButtonPane.add(resetButton);
		searchAndAdvanceButtonPane.add(submitBtn,"gapleft "+screenResolutionWidth/2+",wrap");
		Label maxLimitLabel=new Label("Max Search Limit : "+ PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionSearchListLimit());
		maxLimitLabel.setStyle("-fx-text-background-color: red;-fx-font-size: 12px;");
		searchAndAdvanceButtonPane.add(maxLimitLabel);
		return searchAndAdvanceButtonPane;
	}
	/**
	 * This method is used create searched defect list view.
	 */
	private MigPane createSearchedDefectContainer(double screenResolutionWidth) {
		MigPane searchedDefectPane = new MigPane("insets 5", "", "");
		defectFilterTextField =new LabeledUpperCaseTextField("Filter", "filterId",25, Fonts.SS_DIALOG_PLAIN(14), TextFieldState.EDIT, Pos.CENTER_LEFT, false,new Insets(0));
		defectFilterTextField.setPrefWidth(screenResolutionWidth/3);
		defectFilterTextField.setHeight(25);
		defectFilterTextField.getControl().setOnAction(getController());
		searchedDefectListView=new ListView<String>();
		searchedDefectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		searchedDefectPane.add(defectFilterTextField,"split 2,gapleft "+screenResolutionWidth/1.5+",wrap");
		searchedDefectListView.setPrefWidth(screenResolutionWidth);
		searchedDefectListView.setPlaceholder(new Label("No content in table"));
		searchedDefectPane.add(searchedDefectListView);
		return searchedDefectPane;
				 
	}
	/**
	 * This method is used to create the TitlePane 
	 * @param title  
	 * @param content 
	 * @param width
	 * @param height
	 * @return
	 */
	private static TitledPane createTitledPane(String title,Node content,double width,double height) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(title);
        titledPane.setContent(content);
        titledPane.setPrefSize(width, height);
        return titledPane;
	}
	public void onTabSelected() {
		getController().activate();
		getController().clearDisplayMessage();
	}
	
	public void changeButtonToHyperlink() {
		Hyperlink link = new Hyperlink();
		link.setText("Advanced Search ");
		getAdvancedSearchBtn().setText("");
		getAdvancedSearchBtn().setGraphic(link); 
		getAdvancedSearchBtn().setPrefHeight(35);
		getAdvancedSearchBtn().setFocusTraversable(true);
	}
	
	@Override
	public void reload() {
	}
	
	@Override
	public void start() {
	}
	
	public ListView<String> getSearchedDefectListView() {
		return searchedDefectListView;
	}
	public void setSearchedDefectListView(ListView<String> searchedDefectListView) {
		this.searchedDefectListView = searchedDefectListView;
	}
	public LabeledUpperCaseTextField getDefectFilterTextField() {
		return defectFilterTextField;
	}
	public void setDefectFilterTextField(LabeledUpperCaseTextField defectFilterTextField) {
		this.defectFilterTextField = defectFilterTextField;
	}
	public  SearchMaintenancePanel getSearchMaintenancePanel() {
		return searchMaintenancePanel;
	}
	public void setSearchMaintenancePanel(
			SearchMaintenancePanel searchMaintenancePanel) {
		this.searchMaintenancePanel = searchMaintenancePanel;
	}
	public LoggedButton getAdvancedSearchBtn() {
		return advancedSearchBtn;
	}
	public void setAdvancedSearchBtn(LoggedButton advancedSearchBtn) {
		this.advancedSearchBtn = advancedSearchBtn;
	}
	public LoggedButton getSearchBtn() {
		return searchBtn;
	}
	public void setSearchBtn(LoggedButton searchBtn) {
		this.searchBtn = searchBtn;
	}
	public LoggedButton getResetButton() {
		return resetButton;
	}
	public void setResetButton(LoggedButton resetButton) {
		this.resetButton = resetButton;
	}
	public LoggedButton getSubmitBtn() {
		return submitBtn;
	}
	public void setSubmitBtn(LoggedButton submitBtn) {
		this.submitBtn = submitBtn;
	}
	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
	}

	public void changeHyperlinkToButton() {
		getAdvancedSearchBtn().setGraphic(null); 
		getAdvancedSearchBtn().setPrefHeight(35);
		getAdvancedSearchBtn().setText("Advanced Search");
		getAdvancedSearchBtn().setFocusTraversable(true);
		
	}
	
}