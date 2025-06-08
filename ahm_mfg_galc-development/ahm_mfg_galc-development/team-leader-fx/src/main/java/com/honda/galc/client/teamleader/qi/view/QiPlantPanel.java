package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import com.honda.galc.client.teamleader.qi.controller.QiPlantPanelController;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>	
 * <code>QiPlantPanel</code> is the Panel class for Plant in QiResponsibilityAssignmentView in Hierarchy Screen.
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
public class QiPlantPanel<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanel{

	private QiPlantPanelController<V> controller;
	private QiResponsibilityAssignmentModel model;
	
	private UpperCaseFieldBean plantNameText;
	private UpperCaseFieldBean pddaPlantCodeText;
	private LoggedTextField entrySiteText;
	private LoggedTextField entryPlantText;
	private UpperCaseFieldBean productLineNoText;
	private UpperCaseFieldBean pddaLineText;
	private ComboBox<String> proudctKindComboBox;
	
	
	public QiPlantPanel(QiResponsibilityAssignmentModel model, MainWindow window,V mainView) {
		super(window);
		controller=new QiPlantPanelController<V>(model,this,mainView);
		this.model=model;
		if(mainView instanceof QiLocalResponsibilityAssignmentView){
			initPanel(((QiLocalResponsibilityAssignmentView)mainView).getContentPane(), createPanelContent());
		}
		else{
			initPanel(((QiRegionalResponsibilityAssignmentView)mainView).getContentPane(), createPanelContent());
		}
		
		controller.initListeners();
		controller.populateData();
	}

	public QiPlantPanel(QiResponsibilityAssignmentModel model, MainWindow window, V mainView, Node node) {
		super(window);
		controller = new QiPlantPanelController<V>(model, this, mainView);
		this.model = model;
		initPanel(node, createDialogContent());
	}

	/** This method will return content for Company dialog
	 * 
	 * @return
	 */
	private Node createDialogContent() {
		GridPane fieldContainer = (GridPane) createPlantGrid();
		createButton(QiConstant.SAVE, getController());
		fieldContainer.add(getPanelButton(), 0, 9);
		createButton(QiConstant.CANCEL, getController());
		getPanelButton().setAlignment(Pos.CENTER);
		fieldContainer.add(getPanelButton(), 1, 9);
		fieldContainer.setAlignment(Pos.CENTER);
		return fieldContainer;
	}

	/** This method will return content for Plant Panel
	 * 
	 * @return
	 */
	private Node createPanelContent() {

		final boolean setDisabled = getController().getView() instanceof QiLocalResponsibilityAssignmentView;
		
		GridPane fieldContainer = (GridPane) createPlantGrid();
		createButton("Create Department", getController(), setDisabled);
		fieldContainer.add(getPanelButton(), 0, 9);
		createButton(QiConstant.UPDATE, getController(), setDisabled);
		getPanelButton().setAlignment(Pos.CENTER);
		fieldContainer.add(getPanelButton(), 1, 9);
		fieldContainer.setAlignment(Pos.CENTER);
		return fieldContainer;
	}
	

	/** This method will return common content for Company Panel and dialog
	 * 
	 * @return
	 */
	public Node createPlantGrid() {
		GridPane fieldContainer= new GridPane();
		fieldContainer.setHgap(25);
		fieldContainer.setVgap(25);
		fieldContainer.setPadding(new Insets(35, 100, 35, 100));
		
		getRadioButtons(getController());
		fieldContainer.add(getActiveRadioBtn(), 0, 0);
		fieldContainer.add(getInactiveRadioBtn(), 1, 0);
		
		fieldContainer.add(createLabelContainer(createLoggedLabel("plantNameLabel", "Plant Name ", "display-label-14")), 0,1);
		
		plantNameText=UiFactory.createUpperCaseFieldBean("plantNameText", 10, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		plantNameText.setMaxWidth(235);
		fieldContainer.add(plantNameText, 1, 1);
		
		fieldContainer.add(createLoggedLabel("PlantCodeLabel", "PDDA Plant Code ", "display-label-14"), 0, 2);
		pddaPlantCodeText=UiFactory.createUpperCaseFieldBean("pddaPlantCodeText", 10, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		pddaPlantCodeText.setMaxWidth(40);
		fieldContainer.add(pddaPlantCodeText, 1, 2);
		
		fieldContainer.add(createLabelContainer(createLoggedLabel("productKindLabel", "Product Kind ", "display-label-14")), 0, 3);
		proudctKindComboBox=createComboBox("proudctKindComboBox",245 , "combo-box-med");
		fieldContainer.add(proudctKindComboBox, 1, 3);
		
		fieldContainer.add(createLoggedLabel("entrySiteLabel", "Entry Site", "display-label-14"), 0, 4);
		entrySiteText=UiFactory.createTextField("entrySiteText", 10, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		entrySiteText.setMaxWidth(235);
		fieldContainer.add(entrySiteText, 1, 4);
		
		fieldContainer.add(createLoggedLabel("entryPlantLabel", "Entry Plant", "display-label-14"), 0, 5);
		entryPlantText=UiFactory.createTextField("entryPlantText", 10, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		entryPlantText.setMaxWidth(235);
		fieldContainer.add(entryPlantText, 1, 5);
		
		fieldContainer.add(createLoggedLabel("productLineNotLabel", "Product Line No", "display-label-14"), 0, 6);
		productLineNoText=UiFactory.createUpperCaseFieldBean("productLineNoText", 10, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		productLineNoText.setMaxWidth(60);
		fieldContainer.add(productLineNoText, 1, 6);

		fieldContainer.add(createLoggedLabel("pddaLineLabel", "PDDA Line ", "display-label-14"), 0, 7);
		pddaLineText=UiFactory.createUpperCaseFieldBean("pddaLineText", 10, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		pddaLineText.setMaxWidth(40);
		fieldContainer.add(pddaLineText, 1, 7);
		
		fieldContainer.add(createLoggedLabel("plantDescLabel", "Plant Description ", "display-label-14"), 0, 8);
		initDescriptionTextArea(2,246);
		fieldContainer.add(getDescriptionTextArea(), 1, 8);
		
		if (getController().getView() instanceof QiLocalResponsibilityAssignmentView) {
			fieldContainer.setDisable(true);
		}
		
		return fieldContainer;
	}

	/** This method will be used to set content for Company dialog to abstract dialog
	 * 
	 * @return
	 */
	public Node getDialogContent() {
		return createDialogContent();
	}
	
	public QiPlantPanelController<V> getController() {
		return controller;
	}

	public void setController(QiPlantPanelController<V> controller) {
		this.controller = controller;
	}

	public QiResponsibilityAssignmentModel getModel() {
		return model;
	}

	public void setModel(QiResponsibilityAssignmentModel model) {
		this.model = model;
	}

	public UpperCaseFieldBean getPlantNameText() {
		return plantNameText;
	}

	public void setPlantNameText(UpperCaseFieldBean plantNameText) {
		this.plantNameText = plantNameText;
	}

	public UpperCaseFieldBean getPddaPlantCodeText() {
		return pddaPlantCodeText;
	}

	public void setPddaPlantCodeText(UpperCaseFieldBean pddaPlantCodeText) {
		this.pddaPlantCodeText = pddaPlantCodeText;
	}
	
	public LoggedTextField getEntrySiteText() {
		return entrySiteText;
	}

	public void setEntrySiteText(LoggedTextField entrySiteText) {
		this.entrySiteText = entrySiteText;
	}

	public LoggedTextField getEntryPlantText() {
		return entryPlantText;
	}

	public void setEntryPlantText(LoggedTextField entryPlantText) {
		this.entryPlantText = entryPlantText;
	}
	
	public UpperCaseFieldBean getProductLineNoText() {
		return productLineNoText;
	}

	public void setProductLineNoText(UpperCaseFieldBean productLineNoText) {
		this.productLineNoText = productLineNoText;
	}

	public UpperCaseFieldBean getPddaLineText() {
		return pddaLineText;
	}

	public void setPddaLineText(UpperCaseFieldBean pddaLineText) {
		this.pddaLineText = pddaLineText;
	}

	public ComboBox<String> getProudctKindComboBox() {
		return proudctKindComboBox;
	}

	public void setProudctKindComboBox(List<String> proudctKinds) {
		proudctKindComboBox.getItems().addAll(proudctKinds);
	}
	

}

