package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.teamleader.qi.controller.QiSitePanelController;
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
 * <code>QiSitePanel</code> is the Panel class for Site in QiResponsibilityAssignmentView in Hierarchy Screen.
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
public class QiSitePanel<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanel{

	private QiSitePanelController<V> controller;
	private QiResponsibilityAssignmentModel model;
	private UpperCaseFieldBean siteNameText;
	
	
	public QiSitePanel(QiResponsibilityAssignmentModel model, MainWindow window, V mainView, Node node) {
		super(window);
		controller = new QiSitePanelController<V>(model, this, mainView);
		this.model = model;
		initPanel(node, createDialogContent());
		controller.initListeners();
	}
	
	public QiSitePanel(QiResponsibilityAssignmentModel model, MainWindow window, V mainView) {
		super(window);
		controller = new QiSitePanelController<V>(model, this, mainView);
		this.model = model;
		if(mainView instanceof QiLocalResponsibilityAssignmentView){
			initPanel(((QiLocalResponsibilityAssignmentView)mainView).getContentPane(), createPanelContent());
		}
		else{
			initPanel(((QiRegionalResponsibilityAssignmentView)mainView).getContentPane(), createPanelContent());
		}
		controller.populateData();
		controller.initListeners();
	}

	/** This method will return content for Site Panel
	 * 
	 * @return
	 */
	private Node createPanelContent() {
		final boolean setDisabled = getController().getView() instanceof QiLocalResponsibilityAssignmentView;
		
		GridPane fieldContainer = (GridPane) createSiteGrid();
		createButton("Create Plant", getController(), setDisabled);
		fieldContainer.add(getPanelButton(), 0, 3);
		createButton(QiConstant.UPDATE, getController(), setDisabled);
		fieldContainer.add(getPanelButton(), 1, 3);
		fieldContainer.setAlignment(Pos.CENTER);
		return fieldContainer;
	}
	
	/** This method will return common content for Site Panel and dialog
	 * 
	 * @return
	 */
	private Node createSiteGrid() {
		GridPane fieldContainer = createGridPane();
		getRadioButtons(getController());
		fieldContainer.add(getActiveRadioBtn(), 0, 0);
		fieldContainer.add(getInactiveRadioBtn(), 1, 0);

		fieldContainer.add(createLabelContainer(createLoggedLabel("siteNameLabel", "Site Name ", "display-label-14")), 0, 1);
		siteNameText = UiFactory.createUpperCaseFieldBean("siteNameText", 10, Fonts.SS_DIALOG_PLAIN(14), TextFieldState.EDIT, Pos.TOP_LEFT);
		siteNameText.setMaxWidth(235);
		fieldContainer.add(siteNameText, 1, 1);

		fieldContainer.add(createLoggedLabel("siteDescLabel", "Site Description ", "display-label-14"), 0, 2);
		initDescriptionTextArea(3,235);
		fieldContainer.add(getDescriptionTextArea(), 1, 2);
		
		if (getController().getView() instanceof QiLocalResponsibilityAssignmentView) {
			fieldContainer.setDisable(true);
		}

		return fieldContainer;
	}

	/** This method will return content for Site dialog
	 * 
	 * @return
	 */
	private Node createDialogContent() {
		GridPane fieldContainer=(GridPane)createSiteGrid();
		
		createButton(QiConstant.SAVE, getController());
		fieldContainer.add(getPanelButton(), 0, 3);
		createButton(QiConstant.CANCEL, getController());
		fieldContainer.add(getPanelButton(),1, 3);
		fieldContainer.setAlignment(Pos.CENTER);
		
		return fieldContainer;
	}
	
	public Node getDialogContent(){
		return createDialogContent();
	}
	public QiSitePanelController<V> getController() {
		return controller;
	}

	public void setController(QiSitePanelController<V> controller) {
		this.controller = controller;
	}

	public QiResponsibilityAssignmentModel getModel() {
		return model;
	}

	public void setModel(QiResponsibilityAssignmentModel model) {
		this.model = model;
	}
	
	public UpperCaseFieldBean getSiteNameText() {
		return siteNameText;
	}

	public void setSiteNameText(UpperCaseFieldBean siteNameText) {
		this.siteNameText = siteNameText;
	}

	public Node getCommonContent() {
		return createPanelContent();
	}

}
