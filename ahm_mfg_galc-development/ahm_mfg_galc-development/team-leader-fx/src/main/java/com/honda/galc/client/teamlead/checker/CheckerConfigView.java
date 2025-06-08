package com.honda.galc.client.teamlead.checker;

import com.honda.galc.checkers.CheckerType;
import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamlead.checker.compare.CompareCheckerDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CheckerConfigView extends AbstractTabbedView<CheckerConfigModel, CheckerConfigController>{
	
	private LabeledComboBox<String> checkerTypeComboBox;
	private LabeledComboBox<String> divisionIdComboBox;
	private ScrollPane scrollPane;
	
	public CheckerConfigView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}
	
	@Override
	public void initView() {
		getScrollPane().setContent(createCheckerType());
		this.setTop(createCheckerType());
		getController().initEventHandlers();
	}
	
	private HBox createCheckerType() {
		HBox checkerType = new HBox();
		checkerTypeComboBox =  createLabeledComboBox("Checker Type", true, new Insets(3), true, true);
		checkerTypeComboBox.getControl().setMinHeight(30.0);
		checkerTypeComboBox.getControl().setMinWidth(200.0);
		checkerTypeComboBox.getLabel().setPadding(new Insets(0,15,0,0));
		checkerTypeComboBox.getLabel().getStyleClass().add(Fonts.SS_DIALOG_BOLD(12));
		
		divisionIdComboBox =  createLabeledComboBox("Department", true, new Insets(3), true, false);
		divisionIdComboBox.getControl().setMinHeight(30.0);
		divisionIdComboBox.getControl().setMinWidth(200.0);
		divisionIdComboBox.getLabel().setPadding(new Insets(0,15,0,0));
		divisionIdComboBox.getLabel().getStyleClass().add(Fonts.SS_DIALOG_BOLD(12));
		divisionIdComboBox.getControl().setPromptText("Select");
		
		checkerType.getChildren().addAll(divisionIdComboBox, checkerTypeComboBox, createClickLink());
		checkerType.setSpacing(80);
		checkerType.setStyle("-fx-border-color: #cbc7c7;-fx-border-style: none solid;");
		setCheckerTypeComboBoxValue();
		populateDepartmentComboBox();
		return checkerType;
	}
	
	private void setCheckerTypeComboBoxValue() {
		getCheckerTypeComboBox().getItems().clear();
		getCheckerTypeComboBox().setPromptText("Select");
		
		for (CheckerType checker : CheckerType.values()) {
			getCheckerTypeComboBox().getItems().add(checker.toString());
		}
	}
	
	private void populateDepartmentComboBox() {
		getDivisionIdComboBox().getItems().clear();
		getDivisionIdComboBox().getItems().add(CheckerConstants.ALL);
		getDivisionIdComboBox().getItems().addAll(getModel().findAllDivision());
	}
	
	private TextFlow createClickLink() {
		Hyperlink compareLink = new Hyperlink("Compare");
		compareLink.setStyle("-fx-font-size: 14pt;-fx-font-weight: bold ;");
		Hyperlink copyLink = new Hyperlink("Copy");
		copyLink.setStyle("-fx-font-size: 14pt;-fx-font-weight: bold ;");
		TextFlow flow = new TextFlow(compareLink, new Text(" / "), copyLink , new Text(" all the checkers from one platform to other"));
		flow.setStyle("-fx-font-size: 13pt; -fx-font-family: arial;");
		flow.setPadding(new Insets(5,15,0,15));
		
		compareLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	pddaPlatformCompareAction(actionEvent);
            }
        });
		copyLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	pddaPlatformCopyAction(actionEvent);
            }
        });
		return flow;
	}
	
	private void pddaPlatformCopyAction(ActionEvent actionEvent) {
		CopyCheckerPddaPlateform dialog = new CopyCheckerPddaPlateform("", getModel());
		dialog.showDialog();
	}
	
	private void pddaPlatformCompareAction(ActionEvent actionEvent) {
		CompareCheckerDialog dialog = new CompareCheckerDialog(getModel());
		dialog.showDialog();
	}
	
	@Override
	public void onTabSelected() {
		
	}

	@Override
	public String getScreenName() {
		return "Checker Configuration";
	}

	@Override
	public void reload() {
	
	}

	@Override
	public void start() {

	}
	
	private LabeledComboBox<String> createLabeledComboBox(String label,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(label,isHorizontal,insets,isLabelBold,isMandaotry);
		return comboBox;
	}

	public ComboBox<String> getCheckerTypeComboBox() {
		return checkerTypeComboBox.getControl();
	}

	public void setCheckerTypeComboBox(LabeledComboBox<String> checkerTypeComboBox) {
		this.checkerTypeComboBox = checkerTypeComboBox;
	}

	public ScrollPane getScrollPane() {
		if(scrollPane == null)
			scrollPane = new ScrollPane();
		return scrollPane;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	
	public ComboBox<String> getDivisionIdComboBox() {
		return divisionIdComboBox.getControl();
	}
}
