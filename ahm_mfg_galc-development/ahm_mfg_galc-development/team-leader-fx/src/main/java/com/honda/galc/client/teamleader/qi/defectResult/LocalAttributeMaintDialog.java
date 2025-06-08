package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.ElevatedLoginDialog;
import com.honda.galc.client.ui.ElevatedLoginResult;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LocalAttributeMaintDialog</code> is the Panel class for Update Attribute Dialog
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR> 
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */

public class LocalAttributeMaintDialog extends QiFxDialog<DefectResultMaintModel> {
	
	private LoggedLabel gdpDefectLabel;
	private LoggedLabel updateDefectLabel;
	private LoggedLabel trpuDefectLabel;
	private LoggedRadioButton gdpDefectButton;
	private LoggedRadioButton nonGdpDefectButton;
	private LoggedRadioButton trpuDefectButton;
	private LoggedRadioButton nonTrpuDefectButton;
	
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> departmentComboBox;
	private LabeledComboBox<KeyValue<Integer, String>> responsibleLevel1ComboBox;
	private LabeledComboBox<KeyValue<Integer, String>> responsibleLevel2ComboBox;
	private LabeledComboBox<KeyValue<Integer, String>> responsibleLevel3ComboBox;
	private LabeledComboBox<String> localThemeComboBox;
	private LabeledComboBox<String> originalyDefectStatusCombo;
	private LabeledComboBox<Double> iqsComboBox;
	private Label iqsScoreLabel;
	private Label emptyLabel;

	
	private LocalAttributeMaintController controller;
	private LoggedButton changeBtn;
	private LoggedButton cancelBtn;
	private LoggedButton updateDefectBtn;
	private LoggedButton cancelDefectBtn;
	private LabeledComboBox<String> resSiteComboBox;
	private Label message;
	private Label actualProblemsAssignedLbl;
	private CheckBox noProblemFoundCheckBox;
	public LocalAttributeMaintDialog(String title, String applicationId, DefectResultMaintModel model,List<QiDefectResult> selectedDefectResultList,String typeOfChange,List<QiRepairResult> selectedRepairResultList) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new LocalAttributeMaintController(model, applicationId, this,selectedDefectResultList,typeOfChange,selectedRepairResultList);
		initComponents();
		controller.initListeners();
	}

	/**
	 * This method is used to Initialize the components
	 */
	private void initComponents() {
		double screenResolutionWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenResolutionHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane migPane = new MigPane("","10[]5[]20", "20[]10");
		migPane.setPrefWidth(screenResolutionWidth/1.17);
		migPane.setPrefHeight(screenResolutionHeight/1.3);
		migPane.add(createTitledPane("Responsibilities",getResponsibilities(),screenResolutionWidth*0.4,screenResolutionHeight*0.6),"span 6");
		migPane.add(createTitledPane("Data Groupings",getDataGrouping(),screenResolutionWidth*0.4,screenResolutionHeight*0.6),"wrap");
		migPane.add(createButtonContainer(),"gapleft 100");
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
		if(QiCommonUtil.isUserInDataCorrectionLimitedGroup()) {
			localThemeComboBox.setDisable(true);
			originalyDefectStatusCombo.setDisable(true);
			noProblemFoundCheckBox.setDisable(true);
			updateDefectBtn.setDisable(true);
			gdpDefectButton.setDisable(true);
			nonGdpDefectButton.setDisable(true);
			trpuDefectButton.setDisable(true);
			nonTrpuDefectButton.setDisable(true);
		} else {
			gdpDefectButton.setDisable(false);
			nonGdpDefectButton.setDisable(false);
			trpuDefectButton.setDisable(false);
			nonTrpuDefectButton.setDisable(false);
			updateDefectBtn.setDisable(false);
			cancelDefectBtn.setDisable(false);
			localThemeComboBox.setDisable(false);
			originalyDefectStatusCombo.setDisable(false);
			noProblemFoundCheckBox.setDisable(false);
		}
		
	}

	/**
	 * This method is used to create the Combo box related to Responsibility levels
	 * @return
	 */
	private MigPane getResponsibilities(){
		MigPane pane = new MigPane("insets 5", "[left,grow,fill]", "");
		resSiteComboBox = createLabeledComboBox("siteComboBox", "Site", true, false);
		resSiteComboBox.getControl().getItems().addAll(getModel().findAllQiSite());
		plantComboBox= createLabeledComboBox("plantId", "Plant", true, true);
		departmentComboBox= createLabeledComboBox("departmentId", "Department", true, true);
		setResponsibleLevels();
		pane.add(resSiteComboBox,"wrap");
		pane.add(plantComboBox,"wrap"); 
		pane.add(departmentComboBox,"wrap");
		pane.add(responsibleLevel1ComboBox,"wrap");
		pane.add(responsibleLevel2ComboBox,"wrap");
		pane.add(responsibleLevel3ComboBox);
		return pane;
	}
	
	private Separator getHorizontalLine() {
		Separator separator = new Separator();
		separator.setPrefSize(550, 15);
		return separator;
	}
	
	/**
	 * This method is used to create the Buttons like 'Change' and 'Cancel'
	 * @return
	 */
	private MigPane createButtonContainer(){
        MigPane pane = new MigPane("insets 20", "[left,grow,fill]", ""); 
        changeBtn = createBtn(QiConstant.CHANGE, getController()); 
        cancelBtn = createBtn(QiConstant.CANCEL, getController()); 
        cancelBtn.setMinWidth(100);
        message = new Label("");
        message.setMinWidth(100);
        pane.add(changeBtn); 
        pane.add(cancelBtn,"wrap"); 
        pane.add(message); 
        return pane; 
	} 

	/**
	 * This method is used to create the Combo box related to Responsibility levels
	 */
	private void setResponsibleLevels() {
		responsibleLevel1ComboBox = createLabeledComboBoxWithKeyAndValuePair("responsibleLevel1Id", "Responsible Level 1", true, true);
		responsibleLevel2ComboBox = createLabeledComboBoxWithKeyAndValuePair("responsibleLevel2Id", "Responsible Level 2", true, false);
		responsibleLevel2ComboBox.getControl().setDisable(true);
		responsibleLevel2ComboBox.setStyle("-fx-opacity: 1.0;");
		responsibleLevel3ComboBox =createLabeledComboBoxWithKeyAndValuePair("responsibleLeve31Id", "Responsible Level 3", true, false);
		responsibleLevel3ComboBox.getControl().setDisable(true);
		responsibleLevel3ComboBox.setStyle("-fx-opacity: 1.0;");
	}
	
	/**
	 * This method is used to add the components like Reportable ,Tracking, Initial Status combo. to Migpane
	 * @return
	 */
	private MigPane getDataGrouping(){
		MigPane pane = new MigPane("insets 5", "[left,grow]", "");
		MigPane childPane = new MigPane("insets 5", "", "");
		
		setDefectValue();
				
		localThemeComboBox = createLabeledComboBox("Local Theme","Local Theme              ", true, false);
		originalyDefectStatusCombo =createLabeledComboBox("originalyDefectStatusId", "Original Defect Status", true, false);
		actualProblemsAssignedLbl=new Label("");
		noProblemFoundCheckBox = createNoProblemFoundCheckBox();

		iqsComboBox = createLabeledComboBoxDouble("", "", true, false);
		iqsScoreLabel = new Label("IQS Score");
		iqsScoreLabel.setPadding(new Insets(10,15,10,20));
		iqsScoreLabel.setFont(Fonts.DIALOG_BOLD_14);
		
		pane.add(updateDefectLabel,"wrap");
		pane.add(getHorizontalLine());
		pane.add(getHorizontalLine(),"wrap");
		
		pane.add(gdpDefectLabel);
		pane.add(gdpDefectButton);
		pane.add(nonGdpDefectButton);
		pane.add(getHorizontalLine());
		pane.add(getHorizontalLine(),"wrap");

		pane.add(trpuDefectLabel);
		pane.add(trpuDefectButton);
		pane.add(nonTrpuDefectButton,"wrap");
		pane.add(getHorizontalLine());
		pane.add(getHorizontalLine(),"wrap");
		
		childPane.add(updateDefectBtn);
		childPane.add(cancelDefectBtn);
		pane.add(childPane);
		pane.add(emptyLabel);
		pane.add(getHorizontalLine());
		pane.add(getHorizontalLine(),"wrap");

		
		pane.add(localThemeComboBox);
		pane.add(iqsScoreLabel, "wrap");
		pane.add(originalyDefectStatusCombo);
		pane.add(iqsComboBox,"wrap");
		pane.add(actualProblemsAssignedLbl,"wrap");
		pane.add(noProblemFoundCheckBox,"wrap");
		return pane;
	}

	/**
	 * This method is used to create the Radio buttons related to GDP/ TRPU Flags 
	 */
	private void setDefectValue() {
		updateDefectLabel = UiFactory.createLabel("updateDefectLabel", "Update GDP/TRPU Defects ");
		updateDefectLabel.setFont(Fonts.DIALOG_BOLD_14);
		gdpDefectLabel = UiFactory.createLabel("gdpDefectLabel", "GDP Defects ");
		gdpDefectLabel.setPadding(new Insets(10,15,10,20));
		ToggleGroup group1 = new ToggleGroup();
		gdpDefectButton = createRadioButton(QiConstant.YES, group1, false, getController());
		nonGdpDefectButton = createRadioButton(QiConstant.NO, group1, false, getController());
		gdpDefectButton.setPadding(new Insets(10,10,10,-290));
		gdpDefectButton.setMinHeight(60);
		gdpDefectButton.setMinWidth(80);
		nonGdpDefectButton.setPadding(new Insets(10,10,10,-270));
		nonGdpDefectButton.setMinHeight(60);
		nonGdpDefectButton.setMinWidth(80);
		
		trpuDefectLabel = UiFactory.createLabel("trpuDefectLabel", "TRPU Defects ");
		trpuDefectLabel.setPadding(new Insets(10,15,10,20));
		ToggleGroup group2 = new ToggleGroup();
		trpuDefectButton = createRadioButton(QiConstant.YES, group2, false, getController());
		nonTrpuDefectButton = createRadioButton(QiConstant.NO, group2, false, getController());
		trpuDefectButton.setPadding(new Insets(10,10,10,-290));
		trpuDefectButton.setMinHeight(60);
		trpuDefectButton.setMinWidth(80);
		nonTrpuDefectButton.setPadding(new Insets(10,10,10,-270));
		nonTrpuDefectButton.setMinHeight(60);
		nonTrpuDefectButton.setMinWidth(80);
		
		updateDefectBtn = createBtn(QiConstant.UPDATE, getController()); 
		updateDefectBtn.setMinSize(80, 60);
        cancelDefectBtn = createBtn(QiConstant.CANCEL, getController()); 
        cancelDefectBtn.setMinSize(80, 60);
        emptyLabel = UiFactory.createLabel("emptyLabel","");
	}


	/**
	 * This methiod is used to create the TitlePane
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
	
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(35);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		comboBox.getControl().setMinWidth(270);
		return comboBox;
	}
	
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<Double> createLabeledComboBoxDouble(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<Double> comboBox = new LabeledComboBox<Double>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(35);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		comboBox.getControl().setMinWidth(270);
		return comboBox;
	}

	/**
	 * This method is used to create Combobox with KeyValue Pair
	 * @param id
	 * @param labelName
	 * @param isHorizontal
	 * @param isMandatory
	 * @return
	 */
	public LabeledComboBox<KeyValue<Integer, String>> createLabeledComboBoxWithKeyAndValuePair(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<KeyValue<Integer, String>> comboBox = new LabeledComboBox<KeyValue<Integer, String>>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(35);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		comboBox.getControl().setMinWidth(270);
		return comboBox;
	}
	public LocalAttributeMaintController getController() {
		return controller;
	}

	public ComboBox<String> getPlantComboBox() {
		return plantComboBox.getControl();
	}

	public ComboBox<String> getDepartmentComboBox() {
		return departmentComboBox.getControl();
	}

	public ComboBox<KeyValue<Integer, String>> getResponsibleLevel1ComboBox() {
		return responsibleLevel1ComboBox.getControl();
	}

	public ComboBox<KeyValue<Integer, String>> getResponsibleLevel2ComboBox() {
		return responsibleLevel2ComboBox.getControl();
	}

	public ComboBox<KeyValue<Integer, String>> getResponsibleLevel3ComboBox() {
		return responsibleLevel3ComboBox.getControl();
	}

	public ComboBox<String> getLocalThemeComboBox() {
		return localThemeComboBox.getControl();
	}
	
	public ComboBox<String> getInitialStatusComboBox() {
		return originalyDefectStatusCombo.getControl();
	}

	public LoggedRadioButton getGdPRadioButton() {
		return gdpDefectButton;
	}

	public LoggedRadioButton getTrpuRadioButton() {
		return trpuDefectButton;
	}

	public LoggedRadioButton getNonGdPRadioButton() {
		return nonGdpDefectButton;
	}

	public LoggedRadioButton getNonTrpuRadioButton() {
		return nonTrpuDefectButton;
	}

	public LabeledComboBox<Double> getIqsComboBox() {
		return iqsComboBox;
	}

	public LoggedButton getUpdateDefectBtn() {
		return updateDefectBtn;
	}
	public LoggedButton getChangeBtn() {
		return changeBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}
	
	public LoggedButton getCancelDefectBtn() {
		return cancelDefectBtn;
	}
	
	public ComboBox<String> getResSiteComboBox() {
		return resSiteComboBox.getControl();
	}

	public Label getMessage() {
		return message;
	}

	public void setMessage(Label message) {
		this.message = message;
	}
	
	public Label getActualProblemsAssigned() {
		return actualProblemsAssignedLbl;
	}

	public void setActualProblemsAssigned(Label actualProblemsAssigned) {
		this.actualProblemsAssignedLbl = actualProblemsAssigned;
	}
	
	public CheckBox createNoProblemFoundCheckBox() {
		CheckBox checkBox = new CheckBox("No Problem Found");
		checkBox.setWrapText(true);
		checkBox.setPadding(new Insets(20));
		checkBox.getStyleClass().add(StyleUtil.getBtnStyle(12, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		checkBox.setDisable(true);
		return checkBox;
	}
	
	public CheckBox getNoProblemFoundCheckBox() {
		return noProblemFoundCheckBox;
	}
}
