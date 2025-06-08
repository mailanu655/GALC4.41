package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.utils.QiConstant;

import javafx.scene.layout.BorderPane;

public class QiResponsibilityPanelDialog<P, V extends QiAbstractTabbedView<?,?>> extends QiFxDialog<QiResponsibilityAssignmentModel> {

	private P panel;
	
	private BorderPane dialogContentPane;
	private MainWindow window;
	private V mainView;
	private String panelType;
	
	public QiResponsibilityPanelDialog(String title, MainWindow window, V mainView, QiResponsibilityAssignmentModel model, String panelType,String applicationId) {
		super(title, ClientMainFx.getInstance().getStage(applicationId), model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.panelType = panelType;
		this.model = model;
		this.mainView = mainView;
		initContentByPanelName();
		this.window = window;
	}


	@SuppressWarnings("unchecked")
	private void initContentByPanelName() {
		if (panelType.equalsIgnoreCase(QiConstant.COMPANY_NODE)) {
			panel =(P) new QiCompanyPanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiCompanyPanel<V>)panel).getDialogContent());
		}
		
		else if (panelType.equalsIgnoreCase(QiConstant.SITE_NODE)) {
			panel =(P) new QiSitePanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiSitePanel<V>)panel).getDialogContent());
		}
		
		else if (panelType.equalsIgnoreCase(QiConstant.PLANT_NODE)) {
			panel =(P) new QiPlantPanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiPlantPanel<V>) panel).getDialogContent());
		}
		
		else if (panelType.equalsIgnoreCase(QiConstant.DEPARTMENT_NODE)) {
			panel =(P) new QiDepartmentPanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiDepartmentPanel<V>) panel).getDialogContent());
		}
		
		
		else if (panelType.equalsIgnoreCase(QiConstant.RESPONSIBLE_LEVEL3_NODE)) {
			panel =(P) new QiResponsibleLevelPanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiResponsibleLevelPanel<V>) panel).getDialogContent());
		}
		
		else if (panelType.equalsIgnoreCase(QiConstant.RESPONSIBLE_LEVEL2_NODE)) {
			panel =(P) new QiResponsibleLevelPanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiResponsibleLevelPanel<V>) panel).getDialogContent());
		}
		
		else if (panelType.equalsIgnoreCase(QiConstant.RESPONSIBLE_LEVEL1_NODE)) {
			panel =(P) new QiResponsibleLevelPanel<V>(model, window, mainView, getContentPane());
			((BorderPane) this.getScene().getRoot()).setCenter(((QiResponsibleLevelPanel<V>) panel).getDialogContent());
		}
		
	}
	
	public BorderPane getContentPane() {
		dialogContentPane = new BorderPane();
		return dialogContentPane;
	}

	public void setContentPane(BorderPane contentPane) {
		this.dialogContentPane = contentPane;
	}
	
	public P getPanel() {
		return panel;
	}

	public void setPanel(P panel) {
		this.panel = panel;
	}

	public MainWindow getWindow() {
		return window;
	}

	public void setWindow(TabbedMainWindow window) {
		this.window = window;
	}
}
