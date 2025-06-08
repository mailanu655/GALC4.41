package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.teamleader.qi.controller.QiDepartmentPanelController;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>	
 * <code>QiDepartmentPanel</code> is the Panel class for Department in QiResponsibilityAssignmentView in Hierarchy Screen.
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
public class QiDepartmentPanel<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanel {

	private QiDepartmentPanelController<V> controller;
	private QiResponsibilityAssignmentModel model;
	private UpperCaseFieldBean deptAbbrText;
	private UpperCaseFieldBean deptNameText;
	private UpperCaseFieldBean pddaDeptCodeText;
	
	public QiDepartmentPanel(QiResponsibilityAssignmentModel model, MainWindow window,V mainView,Node node) {
		super(window);
		controller=new QiDepartmentPanelController<V>(model,this,mainView);
		this.model=model;
		initPanel(node,createDialogContent());
		controller.initListeners();
	}

	
	public QiDepartmentPanel(QiResponsibilityAssignmentModel model, MainWindow window,V mainView) {
		super(window);
		controller=new QiDepartmentPanelController<V>(model,this,mainView);
		this.model=model;
		if(mainView instanceof QiLocalResponsibilityAssignmentView){
			initPanel(((QiLocalResponsibilityAssignmentView)mainView).getContentPane(), createContentPane());
		}
		else{
			initPanel(((QiRegionalResponsibilityAssignmentView)mainView).getContentPane(), createContentPane());
		}
		controller.populateData();
		controller.initListeners();
	}

	private Node createContentPane() {
		final boolean setDisabled = getController().getView() instanceof QiLocalResponsibilityAssignmentView;
		
		GridPane fieldContainer=(GridPane)createDepartmentGrid(setDisabled);
		if (getController().getView() instanceof QiLocalResponsibilityAssignmentView) {
			createButton("Create Responsible Level", getController(), false);
			fieldContainer.add(getPanelButton(), 0, 5);
		}
		createButton(QiConstant.UPDATE, getController(), setDisabled);
		fieldContainer.add(getPanelButton(), 1, 5);
		fieldContainer.setAlignment(Pos.CENTER);
		
		return fieldContainer;
	}
	
	private Node createDepartmentGrid(final boolean setDisabled) {
		GridPane fieldContainer=createGridPane();
		getRadioButtons(getController());
		fieldContainer.add(getActiveRadioBtn(), 0, 0);
		fieldContainer.add(getInactiveRadioBtn(), 1, 0);
		getActiveRadioBtn().setDisable(setDisabled);
		getInactiveRadioBtn().setDisable(setDisabled);
		
		fieldContainer.add(createLabelContainer(createLoggedLabel("deptAbbrLabel", "Dept Abbr ", "display-label-14", setDisabled)), 0,1);
		deptAbbrText=UiFactory.createUpperCaseFieldBean("deptAbbrText", 25, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		deptAbbrText.setMaxWidth(235);
		deptAbbrText.setDisable(setDisabled);
		fieldContainer.add(deptAbbrText, 1, 1);
		
		fieldContainer.add(createLabelContainer(createLoggedLabel("deptNameLabel", "Dept Name ", "display-label-14", setDisabled)), 0,2);
		deptNameText=UiFactory.createUpperCaseFieldBean("deptNameText", 25, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		deptNameText.setDisable(setDisabled);
		fieldContainer.add(deptNameText, 1, 2);
		
		fieldContainer.add(createLoggedLabel("deptCodeLabel", "PDDA Dept Code ", "display-label-14", setDisabled), 0,3);
		pddaDeptCodeText=UiFactory.createUpperCaseFieldBean("deptNameText", 25, Fonts.SS_DIALOG_PLAIN(14),TextFieldState.EDIT, Pos.TOP_LEFT);
		pddaDeptCodeText.setMaxWidth(235);
		pddaDeptCodeText.setDisable(setDisabled);
		fieldContainer.add(pddaDeptCodeText, 1, 3);
		
		fieldContainer.add(createLoggedLabel("departmentDescLabel", "Department Description ", "display-label-14", setDisabled), 0, 4);
		initDescriptionTextArea(4,300);
		getDescriptionTextArea().setDisable(setDisabled);
		fieldContainer.add(getDescriptionTextArea(), 1, 4);
		
		return fieldContainer;
	}
	
	private Node createDialogContent() {
		GridPane fieldContainer=(GridPane)createDepartmentGrid(false);
		
		createButton(QiConstant.SAVE, getController());
		fieldContainer.add(getPanelButton(), 0, 5);
		createButton(QiConstant.CANCEL, getController());
		fieldContainer.add(getPanelButton(),1, 5);
		fieldContainer.setAlignment(Pos.CENTER);
		
		return fieldContainer;
	}
	
	public Node getDialogContent() {
		return createDialogContent();
	}
	public QiDepartmentPanelController<V> getController() {
		return controller;
	}

	public void setController(QiDepartmentPanelController<V> controller) {
		this.controller = controller;
	}
	
	public QiResponsibilityAssignmentModel getModel() {
		return model;
	}

	public void setModel(QiResponsibilityAssignmentModel model) {
		this.model = model;
	}
	
	public UpperCaseFieldBean getDepartmentAbbrText() {
		return deptAbbrText;
	}


	public void setDepartmentAbbrText(UpperCaseFieldBean deptAbbrText) {
		this.deptAbbrText = deptAbbrText;
	}

	public UpperCaseFieldBean getDepartmentNameText() {
		return deptNameText;
	}

	public void setDepartmentNameText(UpperCaseFieldBean deptNameText) {
		this.deptNameText = deptNameText;
	}
	
	public UpperCaseFieldBean getDepartmentCodeText() {
		return pddaDeptCodeText;
	}

	public void setPddaDeptCodeText(UpperCaseFieldBean pddaDeptCodeText) {
		this.pddaDeptCodeText = pddaDeptCodeText;
	}

	public UpperCaseFieldBean getPddaDeptCodeText() {
	return pddaDeptCodeText;
	}	
	
}
