package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.teamleader.qi.controller.QiCompanyPanelController;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>	
 * <code>QiCompanyPanel</code> is the Panel class for Company in QiRegionalResponsibilityAssignmentView in Hierarchy Screen.
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
public class QiCompanyPanel<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanel {
	private QiCompanyPanelController<V> controller;
	private QiResponsibilityAssignmentModel model;
	private UpperCaseFieldBean companyNameText;
	
	public QiCompanyPanel(QiResponsibilityAssignmentModel model, MainWindow window, V mainView) {
		super(window);
		controller = new QiCompanyPanelController<V>(model, this, mainView);
		this.model = model;
		if(mainView instanceof QiRegionalResponsibilityAssignmentView){
			initPanel(((QiRegionalResponsibilityAssignmentView)mainView).getContentPane(), createPanelContent());
		}
		else{
			initPanel(((QiLocalResponsibilityAssignmentView)mainView).getContentPane(), createPanelContent());
		}
			
		controller.initListeners();
		controller.populateData();
	}

	public QiCompanyPanel(QiResponsibilityAssignmentModel model, MainWindow window, V mainView, BorderPane node) {
			super(window);
			controller = new QiCompanyPanelController<V>(model, this, mainView);
			this.model = model;
			initPanel(node, createDialogContent());
		}

	/** This method will return content for Company Panel
	 * 
	 * @return
	 */
	private Node createPanelContent() {
		
		GridPane fieldContainer = (GridPane) createCompanyGrid();

		final boolean setDisabled = getController().getView() instanceof QiLocalResponsibilityAssignmentView;
		
		createButton("Create Company", getController(),setDisabled);
		fieldContainer.add(getPanelButton(), 0, 3);
		
		HBox container = new HBox();
		container.setSpacing(30);
		container.setPadding(new Insets(10));
		createButton("Create Site", getController(),setDisabled);
		container.getChildren().add(getPanelButton());
		createButton(QiConstant.UPDATE, getController(),setDisabled);
		container.getChildren().add(getPanelButton());

		fieldContainer.add(container, 1, 3);
		fieldContainer.setAlignment(Pos.CENTER);
		return fieldContainer;
	}
	
	/** This method will return content for Company dialog
	 * 
	 * @return
	 */
	private Node createDialogContent() {
		GridPane fieldContainer = (GridPane) createCompanyGrid();
		createButton(QiConstant.SAVE, getController());
		fieldContainer.add(getPanelButton(), 0, 3);
		createButton(QiConstant.CANCEL, getController());
		fieldContainer.add(getPanelButton(), 1, 3);
		fieldContainer.setAlignment(Pos.CENTER);
		return fieldContainer;
	}

	/** This method will return common content for Company Panel and dialog
	 * 
	 * @return
	 */
	private Node createCompanyGrid() {
		GridPane fieldContainer=createGridPane();
		getRadioButtons(getController());
		fieldContainer.add(getActiveRadioBtn(), 0, 0);
		fieldContainer.add(getInactiveRadioBtn(), 1, 0);
		
		fieldContainer.add(createLabelContainer(createLoggedLabel("CompanyNameLabel", "Company Name ", "display-label-14")), 0, 1);
		companyNameText=UiFactory.createUpperCaseFieldBean("companyNameText", 25, Fonts.SS_DIALOG_PLAIN(14), TextFieldState.EDIT, Pos.TOP_LEFT);
		fieldContainer.add(companyNameText, 1, 1);
		
		fieldContainer.add(createLoggedLabel("companyDescLabel", "Company Description ", "display-label-14"), 0, 2);
		initDescriptionTextArea(3,300);
		fieldContainer.add(getDescriptionTextArea(), 1, 2);
		
		if (getController().getView() instanceof QiLocalResponsibilityAssignmentView) {
			fieldContainer.setDisable(true);
		}
		
		return fieldContainer;
	}

	/** This method will be used to set content for Company dialog to abstract dialog
	 * 
	 * @return
	 */
	public Node getDialogContent(){
		return createDialogContent();
	}
	public QiCompanyPanelController<V> getController() {
		return controller;
	}

	public void setController(QiCompanyPanelController<V> controller) {
		this.controller = controller;
	}
	
	public QiResponsibilityAssignmentModel getModel() {
		return model;
	}

	public void setModel(QiResponsibilityAssignmentModel model) {
		this.model = model;
	}

	public UpperCaseFieldBean getCompanyNameText() {
		return companyNameText;
	}

	public void setCompanyNameText(UpperCaseFieldBean companyNameText) {
		this.companyNameText = companyNameText;
	}

}
