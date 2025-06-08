package com.honda.galc.client.teamlead.vios.opconfig;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamlead.vios.AbstractViosTabbedView;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosPlatform;
import com.honda.galc.client.teamlead.vios.ViosPlatformPane;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.PddaPlatformUtil;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public class ViosUnitNoConfigView extends AbstractViosTabbedView<ViosUnitNoConfigModel, ViosUnitNoConfigController> {

	private VBox mainBox;
	private ViosPlatformPane platformTitledPane;

	private MCViosMasterPlatform platform;

	private ObjectTablePane<MCViosMasterOperation> unitNumberTablePane;
	private ObjectTablePane<MCViosMasterOperationChecker> checkerDetailsTable;

	private LabeledTextField filterTextField;

	private LoggedButton addButton;
	private LoggedButton deleteButton;
	

	public ViosUnitNoConfigView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public String getScreenName() {
		return "VIOS Unit Number Config";
	}

	@Override
	public void reload() {
		filterTextField.clear();
		reload(StringUtils.EMPTY);
	}
	
	public void reload(String filter) {
		if(platform != null) {
			List<MCViosMasterOperation> masterOpList = ServiceFactory.getDao(MCViosMasterOperationDao.class).findAllFilteredOperations(platform.getGeneratedId(), filter);
			unitNumberTablePane.setData(masterOpList);
		}
	}

	@Override
	public void start() {

	}
	
	@Override
	public Node getTopBody() {
		platformTitledPane = new ViosPlatformPane(this) {
			@Override
			public void vmcComboboxListener(String value) {
				addVMCComboBoxListener(value);
			}
		};
		BorderPane.setMargin(platformTitledPane, new Insets(0, 0, 10, 0));
		return platformTitledPane;
	}

	@Override
	public Node getCenterBody() {
		VBox unitNoBox = new VBox();
		unitNoBox.setAlignment(Pos.CENTER);
		unitNoBox.setSpacing(10);
		unitNoBox.setPadding(new Insets(10));

		HBox filterBox = new HBox();
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setSpacing(20);

		filterTextField = new LabeledTextField("Search", true, new Insets(0), true, false);
		filterTextField.getControl().setMinWidth(250);
		filterTextField.getControl().setMinHeight(25);
		BorderPane.setMargin(filterTextField.getControl(), new Insets(0, 0, 0, 10));

		filterBox.getChildren().addAll(filterTextField);

		unitNumberTablePane = createUnitNumberTable();

		unitNoBox.getChildren().addAll(filterBox, unitNumberTablePane);

		checkerDetailsTable = createCheckerDetailsTable();

		VBox checkerBox = new VBox();
		checkerBox.setAlignment(Pos.CENTER);
		checkerBox.setSpacing(10);
		checkerBox.setPadding(new Insets(10));

		checkerBox.getChildren().add(checkerDetailsTable);

		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(10);

		addButton = createBtn(ViosConstants.ADD, getController());
		deleteButton = createBtn(ViosConstants.DELETE, getController());
		

		buttonContainer.getChildren().addAll(addButton, deleteButton );

		TitledPane unitNoTitledPane = createTitiledPane("Unit Number Details", unitNoBox);
		TitledPane checkerDetailsTitledPane = createTitiledPane("Operation Checker Details", checkerBox);

		VBox detailsBox = new VBox();
		detailsBox.setAlignment(Pos.CENTER);
		detailsBox.setSpacing(10);
		detailsBox.getChildren().addAll(unitNoTitledPane, checkerDetailsTitledPane);

		mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setDisable(true);

		mainBox.getChildren().addAll(detailsBox, buttonContainer);
		return mainBox;
	}

	@Override
	public void onScreenSelected() {
		PddaPlatformDto platformDto = ViosPlatform.getInstance().getPddaPlatformDto();
		if(!PddaPlatformUtil.isBlankPddaPlatform(platformDto)) {
			MCViosMasterPlatform selectedPlatform = new MCViosMasterPlatform(platformDto);
			platformTitledPane.selectPlatform(selectedPlatform);
			platformTitledPane.setDisable(true);
		}
	}

	private ObjectTablePane<MCViosMasterOperation> createUnitNumberTable() {

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Unit Number", "id.unitNo")
				.put("Common Name", "commonName")
				.put("View", "view")
				.put("Processor", "processor")
				.put("Op Check", "opCheckAsString");
		Double[] columnWidth = new Double[] {
				0.15, 0.15, 0.30, 0.30, 0.10
		}; 
		ObjectTablePane<MCViosMasterOperation> subTable = new ObjectTablePane<MCViosMasterOperation>(columnMappingList,columnWidth,true);

		subTable.setConstrainedResize(false);
		return subTable;
	}

	private ObjectTablePane<MCViosMasterOperationChecker> createCheckerDetailsTable() {

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Check Point", "id.checkPoint")
				.put("Check Seq", "id.checkSeq")
				.put("Check Name","checkName")
				.put("Checker","checker")
				.put("Reaction Type","reactionTypeAsString");
		Double[] columnWidth = new Double[] {
				0.15, 0.10, 0.15, 0.40, 0.20
		}; 
		ObjectTablePane<MCViosMasterOperationChecker> subTable = new ObjectTablePane<MCViosMasterOperationChecker>(columnMappingList,columnWidth, true);
		subTable.setConstrainedResize(false);
		subTable.getTable().setSelectionModel(null);
		return subTable;
	}

	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setOnAction(handler);
		btn.getStyleClass().add("action-btn");
		btn.setPrefWidth(150);
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.45 * btn.getWidth())));
		return btn;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font(StringUtils.EMPTY, FontWeight.BOLD, 12));
		titledPane.setContent(content);
		titledPane.setCollapsible(false);
		titledPane.setContentDisplay(ContentDisplay.CENTER);
		return titledPane;
	}

	private void addVMCComboBoxListener(String value) {
		clearErrorMessage();
		if(value != null) {
			mainBox.setDisable(false);
			platform = new MCViosMasterPlatform(platformTitledPane.getPlantCombobox().getSelectionModel().getSelectedItem(), 
					platformTitledPane.getDeptCombobox().getSelectionModel().getSelectedItem(), 
					new BigDecimal(platformTitledPane.getModelYearCombobox().getSelectionModel().getSelectedItem()), 
					new BigDecimal(platformTitledPane.getProdRateCombobox().getSelectionModel().getSelectedItem()), 
					platformTitledPane.getLineNoCombobox().getSelectionModel().getSelectedItem(), 
					platformTitledPane.getVmcCombobox().getSelectionModel().getSelectedItem());
			reload();
		} else {
			mainBox.setDisable(true);
			filterTextField.getControl().clear();
			unitNumberTablePane.getTable().getItems().clear();
			checkerDetailsTable.getTable().getItems().clear();
			platform = null;
		}
	}

	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	public ViosPlatformPane getPlatformTitledPane() {
		return platformTitledPane;
	}

	public MCViosMasterPlatform getPlatform() {
		return platform;
	}

	public ObjectTablePane<MCViosMasterOperation> getUnitNumberTablePane() {
		return unitNumberTablePane;
	}

	public ObjectTablePane<MCViosMasterOperationChecker> getCheckerDetailsTable() {
		return checkerDetailsTable;
	}

	public LabeledTextField getFilterTextField() {
		return filterTextField;
	}

	public LoggedButton getAddButton() {
		return addButton;
	}

	public LoggedButton getDeleteButton() {
		return deleteButton;
	}

}
