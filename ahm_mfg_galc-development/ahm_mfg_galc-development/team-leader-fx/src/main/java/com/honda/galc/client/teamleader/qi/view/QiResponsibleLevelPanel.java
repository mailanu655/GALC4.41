package com.honda.galc.client.teamleader.qi.view;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiResponsibleLevelPanelController;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>	
 * <code>QiResponsibleLevelPanel</code> is the Panel class for Responsible Level in QiResponsibilityAssignmentView in Hierarchy Screen.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>28/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class QiResponsibleLevelPanel<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanel{

	private QiResponsibleLevelPanelController<V> controller;
	private QiResponsibilityAssignmentModel model;
	private UpperCaseFieldBean responsibleLevelNameText;
	private ComboBox<String> upperResponsibleLevelComboBox;
	private ComboBox<String> responsibleLevelComboBox;
	private ComboBox<String> higherUpperResponsibleLevelComboBox;
	private LoggedLabel higherUpperResponsibleLevellabel;
	public boolean isDialogPanel = false;
	
	public QiResponsibleLevelPanel(QiResponsibilityAssignmentModel model, MainWindow window,V mainView,Node node) {
		super(window);
		controller=new QiResponsibleLevelPanelController<V>(model,this,mainView);
		this.model=model;
		initPanel(node,createDialogContent());
		controller.initListeners();
	}
	
	public QiResponsibleLevelPanel(QiResponsibilityAssignmentModel model, MainWindow window,V mainView) {
		super(window);
		controller=new QiResponsibleLevelPanelController<V>(model,this,mainView);
		this.model=model;
		if(mainView instanceof QiRegionalResponsibilityAssignmentView){
			initPanel(((QiRegionalResponsibilityAssignmentView)mainView).getContentPane(), createContentPane());
		}
		else{
			initPanel(((QiLocalResponsibilityAssignmentView)mainView).getContentPane(), createContentPane());
		}
		controller.populateData();
		controller.initListeners();
	}
	
	private Node createContentPane() {
		GridPane fieldContainer=(GridPane)createResponsibleLevelGrid("Panel");
		
		String checkNode = StringUtils.trimToEmpty(getController().getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0]);
		if(checkNode.equals("Department") ||checkNode.equals("Responsible Level 2") || checkNode.equals("Responsible Level 3")){
		createButton("Create Responsible Level", getController());
		fieldContainer.add(getPanelButton(), 0, 6);
		}
		createButton(QiConstant.UPDATE, getController(), false);
		fieldContainer.add(getPanelButton(), 1, 6);
		fieldContainer.setAlignment(Pos.CENTER);
		return fieldContainer;
	}

	private Node createResponsibleLevelGrid(String gridType) {
		GridPane fieldContainer=createGridPane();
		getRadioButtons(getController());
		fieldContainer.add(getActiveRadioBtn(), 0, 0);
		getActiveRadioBtn().setId(QiConstant.ACTIVE);
		fieldContainer.add(getInactiveRadioBtn(), 1, 0);
		getInactiveRadioBtn().setId(QiConstant.INACTIVE);
		
		if(gridType.equals("Dialog")){
			fieldContainer.add(createLabelContainer(createLoggedLabel("responsibleLevelLabel", "Responsible Level ", "display-label-14")), 0,1);
			responsibleLevelComboBox=createComboBox("responsibleLevelComboBox", 80, "combo-box-med");
			fieldContainer.add(responsibleLevelComboBox, 1, 1);
			responsibleLevelComboBox.setPromptText("Select");
			
			String checkNode = StringUtils.trimToEmpty(getController().getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0]);
			if(checkNode.equals("Department")){
				responsibleLevelComboBox.getItems().addAll("Level 3","Level 2","Level 1");
			}
			 else if(checkNode.equals("Responsible Level 3")){
				 responsibleLevelComboBox.getItems().addAll("Level 2","Level 1");
			 }
			 else if(checkNode.equals("Responsible Level 2")){
				 responsibleLevelComboBox.getItems().addAll("Level 1");
			 }
			 else if(checkNode.equals("Responsible Level 1")){
				 responsibleLevelComboBox.setDisable(true);
			 }
			
		}
		
		fieldContainer.add(createLabelContainer(createLoggedLabel("responsibleLevelNameLabel", "Responsible Level Name ", "display-label-14")), 0,2);
		responsibleLevelNameText=UiFactory.createUpperCaseFieldBean("responsibleLevelNameText", 25, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		fieldContainer.add(responsibleLevelNameText, 1, 2);
		
		fieldContainer.add(createLoggedLabel("responsibleLevelDescLabel", "Responsible Level Description ", "display-label-14"), 0, 3);
		initDescriptionTextArea(2,300);
		getDescriptionTextArea().setId("qiRespLeveDescTxtArea");
		fieldContainer.add(getDescriptionTextArea(), 1, 3);
		
		fieldContainer.add(createLoggedLabel("upperResponsibleLevelLabel", "Upper Responsible Level ", "display-label-14"), 0, 4);
		upperResponsibleLevelComboBox=createComboBox("upperResponsibleLevelComboBox", 350, "combo-box-med");
		upperResponsibleLevelComboBox.setPromptText("Select");
		fieldContainer.add(upperResponsibleLevelComboBox, 1, 4);
		
		if(StringUtils.trimToEmpty(getController().getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0]).equals("Responsible Level 1")){
			higherUpperResponsibleLevellabel = createLoggedLabel("higherUpperResponsibleLevelLabel", "Higher Upper Responsible Level ", "display-label-14");
			fieldContainer.add(higherUpperResponsibleLevellabel, 0, 5);
			higherUpperResponsibleLevelComboBox=createComboBox("higherUpperResponsibleLevelComboBox", 350, "combo-box-med");
			higherUpperResponsibleLevelComboBox.setPromptText("Select");
			fieldContainer.add(higherUpperResponsibleLevelComboBox, 1, 5);
			higherUpperResponsibleLevellabel.setVisible(true);
			higherUpperResponsibleLevelComboBox.setVisible(true);
			higherUpperResponsibleLevelComboBox.setDisable(true);
		}
		return fieldContainer;
	}
	
	
	private Node createDialogContent() {
		setDialogPanel(true);
		GridPane fieldContainer=(GridPane)createResponsibleLevelGrid("Dialog");
		createButton(QiConstant.SAVE, getController());
		fieldContainer.add(getPanelButton(), 0, 6);
		createButton(QiConstant.CANCEL, getController());
		fieldContainer.add(getPanelButton(),1, 6);
		fieldContainer.setAlignment(Pos.CENTER);
		
		return fieldContainer;
	}
	
	public Node getDialogContent() {
		return createDialogContent();
	}
	
	public QiResponsibleLevelPanelController<V> getController() {
		return controller;
	}

	public void setController(QiResponsibleLevelPanelController<V> controller) {
		this.controller = controller;
	}
	
	public QiResponsibilityAssignmentModel getModel() {
		return model;
	}

	public void setModel(QiResponsibilityAssignmentModel model) {
		this.model = model;
	}

	public UpperCaseFieldBean getResponsibleLevelNameText() {
		return responsibleLevelNameText;
	}

	public void setResponsibleLevelNameText(UpperCaseFieldBean responsibleLevelNameText) {
		this.responsibleLevelNameText = responsibleLevelNameText;
	}
	
	public ComboBox<String> getResponsibleLevelComboBox() {
		return responsibleLevelComboBox;
	}

	public void setResponsibleLevelComboBox(ComboBox<String> responsibleLevelComboBox) {
		this.responsibleLevelComboBox = responsibleLevelComboBox;
	}
	

	public ComboBox<String> getUpperResponsibleLevelComboBoxComboBox() {
		return upperResponsibleLevelComboBox;
	}

	public void setUpperResponsibleLevelComboBoxComboBox(ComboBox<String> upperResponsibleLevelComboBox) {
		this.upperResponsibleLevelComboBox = upperResponsibleLevelComboBox;
	}
	
	public ComboBox<String> getHigherUpperResponsibleLevelComboBox() {
		return higherUpperResponsibleLevelComboBox;
	}

	public void setHigherUpperResponsibleLevelComboBoxComboBox(ComboBox<String> higherUpperResponsibleLevelComboBox) {
		this.higherUpperResponsibleLevelComboBox = higherUpperResponsibleLevelComboBox;
	}

	public boolean isDialogPanel() {
		return isDialogPanel;
	}

	public void setDialogPanel(boolean isDialogPanel) {
		this.isDialogPanel = isDialogPanel;
	}

	public LoggedLabel getHigherUpperResponsibleLevellabel() {
		return higherUpperResponsibleLevellabel;
	}

	public void setHigherUpperResponsibleLevellabel(LoggedLabel higherUpperResponsibleLevellabel) {
		this.higherUpperResponsibleLevellabel = higherUpperResponsibleLevellabel;
	}
	
	
	
}
