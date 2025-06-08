package com.honda.galc.client.teamleader.fx;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import com.honda.galc.client.dto.CureTimerTrackingDTO;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class CureTimerTrackingPanel extends TabbedPanel {

	protected static final String PROCESS_POINT_ID = "CURE_TRACKING";
	protected String dateFormat;
	protected int maxRows;
	protected int rowHeight;
	protected int fontSize;
	protected boolean displayManualRefresh;
	protected int autoRefreshTimer;
	protected int requiredCureTime;
	protected List<String> triggerLineIds;
	protected List<String> triggerOnParts;
	protected List<String> triggerOffProcessPointIds;

	protected Label labelRefreshTime = null;
	protected ObservableList<CureTimerTrackingDTO> tableItems = null;
	protected Timeline refreshTimer = null;

	private RequiredPartDao requiredPartDao;
	private InProcessProductDao inProcessProductDao;
	private InstalledPartDao installedPartDao;

	public CureTimerTrackingPanel() {
		super("Cure Timer Tracking", KeyEvent.VK_P);
		init();
	}

	private void init() {
		onTabSelected();
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) {
			return;
		} 

		initProperties();
		initComponents();
		Platform.runLater(new Runnable() {

			public void run() {
				bindWidth();
			}

		});
		isInitialized = true;
	}

	private void initProperties() {
		this.dateFormat = PropertyService.getProperty(PROCESS_POINT_ID, "DATE_FORMAT", "MM/dd/yyyy HH:mm");
		this.maxRows = PropertyService.getPropertyInt(PROCESS_POINT_ID, "MAX_ROWS", 14);
		this.rowHeight = PropertyService.getPropertyInt(PROCESS_POINT_ID, "ROW_HEIGHT", 35);
		this.fontSize = PropertyService.getPropertyInt(PROCESS_POINT_ID, "FONT_SIZE", 18);
		if (fontSize < 8) fontSize = 8; // ensure fontSize is at least the minimum font size
		if (fontSize > 72) fontSize = 72; // ensure fontSize is at most the maximum font size
		this.displayManualRefresh = PropertyService.getPropertyBoolean(PROCESS_POINT_ID, "DISPLAY_MANUAL_REFRESH", false);
		this.autoRefreshTimer = PropertyService.getPropertyInt(PROCESS_POINT_ID, "AUTO_REFRESH_TIMER", 60);
		this.requiredCureTime = PropertyService.getPropertyInt(PROCESS_POINT_ID, "REQUIRED_CURE_TIME", 180) * CureTimerTrackingDTO.MINUTE;
		this.triggerLineIds = new ArrayList<String>();
		List<ComponentProperty> lineIds = PropertyService.getProperties(PROCESS_POINT_ID, "TRIGGER_LINE_ID\\{[0-9]+\\}");
		if (lineIds != null) {
			for (ComponentProperty lineId : lineIds) {
				this.triggerLineIds.add(lineId.getPropertyValue());
			}
		}
		if (this.triggerLineIds == null || this.triggerLineIds.isEmpty()) {
			throw new RuntimeException("TRIGGER_LINE_ID is not specified.");
		}
		this.triggerOnParts = new ArrayList<String>();
		List<ComponentProperty> parts = PropertyService.getProperties(PROCESS_POINT_ID, "TRIGGER_ON_PART\\{[0-9]+\\}");
		if (parts != null) {
			for (ComponentProperty part : parts) {
				this.triggerOnParts.add(part.getPropertyValue());
			}
		}
		if (this.triggerOnParts == null || this.triggerOnParts.isEmpty()) {
			throw new RuntimeException("TRIGGER_ON_PART is not specified.");
		}
		this.triggerOffProcessPointIds = new ArrayList<String>();
		List<ComponentProperty> ppIds = PropertyService.getProperties(PROCESS_POINT_ID, "TRIGGER_OFF_PROCESS_POINT_ID\\{[0-9]+\\}");
		if (ppIds != null) {
			for (ComponentProperty ppId : ppIds) {
				this.triggerOffProcessPointIds.add(ppId.getPropertyValue());
			}
		}
		if (this.triggerOffProcessPointIds == null || this.triggerOffProcessPointIds.isEmpty()) {
			throw new RuntimeException("TRIGGER_OFF_PROCESS_POINT_ID is not specified.");
		}
	}

	private void initComponents() {
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setPadding(new Insets(20,20,20,20));
		VBox vBox = new VBox(10);
		vBox.setPadding(new Insets(20,20,20,20));
		addTopDisplayBoxes(vBox);
		addQsdTrackingTable(vBox);
		anchorPane.getChildren().add(vBox);
		this.setCenter(anchorPane);
		refresh();
		startRefreshTimer();
	}

	private void addTopDisplayBoxes(VBox vBox) {
		HBox hBox = new HBox();
		addManualRefreshButton(hBox);
		addRefreshTimeBox(hBox);
		vBox.getChildren().add(hBox);
	}

	private void bindWidth() {
		try {
			HBox hBox = (HBox) ((VBox) ((AnchorPane) this.getCenter()).getChildren().get(0)).getChildren().get(0);
			hBox.spacingProperty().bind(this.getScene().getWindow().widthProperty().subtract(530));
			getLogger().info("Window width binding successful.");
		} catch (Exception e) {
			getLogger().error(e, "Window width binding failed.");
		}
	}

	private void addManualRefreshButton(HBox hBox) {
		Button manualRefreshButton = UiFactory.createButton("Manual Refresh");
		manualRefreshButton.setVisible(displayManualRefresh);
		manualRefreshButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				getLogger().info("Manual refresh requested");
				refreshTimer.stop();
				refresh();
				refreshTimer.play();
			}
		});
		hBox.getChildren().add(manualRefreshButton);
	}

	private void addRefreshTimeBox(HBox hBox) {
		Label labelLastRefresh = UiFactory.createLabel("labelLastRefresh", "  Last Refresh:", Fonts.SS_DIALOG_BOLD(18), TextAlignment.RIGHT);
		labelRefreshTime = UiFactory.createLabel("labelRefreshTime", "", Fonts.SS_DIALOG_BOLD(18), TextAlignment.RIGHT);

		HBox innerBox = new HBox(12);
		innerBox.setAlignment(javafx.geometry.Pos.CENTER);
		innerBox.getChildren().add(labelLastRefresh);
		innerBox.getChildren().add(labelRefreshTime);
		hBox.getChildren().add(innerBox);
	}

	@SuppressWarnings("unchecked")
	private void addQsdTrackingTable(VBox vBox) {
		// create the table
		TableView<CureTimerTrackingDTO> table = new TableView<CureTimerTrackingDTO>();
		table.setId("font-size-" + fontSize);
		table.getStylesheets().add(this.getClass().getResource("/resource/com/honda/galc/client/teamleader/fx/CureTimerTrackingStyle.css").toExternalForm());

		// create sorting capability for the table
		Callback<CureTimerTrackingDTO,Observable[]> comparatorCallback = new Callback<CureTimerTrackingDTO,Observable[]>(){
			public Observable[] call(CureTimerTrackingDTO dto) {
				return new Observable[]{ dto.checkInTimeProperty() };
			}
		};
		tableItems = javafx.collections.FXCollections.observableArrayList(comparatorCallback);
		SortedList<CureTimerTrackingDTO> sortedItems = new SortedList<CureTimerTrackingDTO>(tableItems);
		sortedItems.setComparator(new Comparator<CureTimerTrackingDTO>() {
			public int compare(CureTimerTrackingDTO dto1, CureTimerTrackingDTO dto2) { // SortedList breaks when using a Comparator which compares non-constant fields
				long comparison = dto1.getQsdStartTime() - dto2.getQsdStartTime();
				if (comparison < 0) return 1;
				if (comparison > 0) return -1;
				return 0;
			}
		});
		table.setItems(sortedItems);

		// size the table to display MAX_ROWS rows
		table.setFixedCellSize(rowHeight);
		table.prefHeightProperty().bind(javafx.beans.binding.Bindings.min(maxRows, javafx.beans.binding.Bindings.size(tableItems)).multiply(table.getFixedCellSize()).add(table.getFixedCellSize() * 1.25));

		// create the columns for the table
		
		if (PropertyService.getProperty(PROCESS_POINT_ID, "EXCLUDE_COLUMN_CHECK_IN_TIME", "FALSE").equals("FALSE")){
			TableColumn<CureTimerTrackingDTO,String> checkInTimeCol = makeCureTimerTrackingColumn(PropertyService.getProperty(PROCESS_POINT_ID, "COLUMN_HEADER_CHECK_IN_TIME", "Check In Time"), "checkInTime");
			table.getColumns().add(table.getColumns().size(), checkInTimeCol);
		}
		
		if (PropertyService.getProperty(PROCESS_POINT_ID, "EXCLUDE_COLUMN_TIME_IN_QSD", "FALSE").equals("FALSE")){
			TableColumn<CureTimerTrackingDTO,String> timeInQsdCol = makeCureTimerTrackingColumn(PropertyService.getProperty(PROCESS_POINT_ID, "COLUMN_HEADER_TIME_IN_QSD", "Time in QSD"), "timeInQsd");
			table.getColumns().add(table.getColumns().size(), timeInQsdCol);
		}
		
		TableColumn<CureTimerTrackingDTO,String> engineNumberCol = makeCureTimerTrackingColumn(PropertyService.getProperty(PROCESS_POINT_ID, "COLUMN_HEADER_ENGINE_NUMBER", "Engine #"), "engineNumber");
		TableColumn<CureTimerTrackingDTO,String> cureTimeRemainingCol = makeCureTimerTrackingColumn(PropertyService.getProperty(PROCESS_POINT_ID, "COLUMN_HEADER_TIME_REMAINING", "Time Remaining"), "cureTimeRemaining");
		table.getColumns().add(table.getColumns().size(), engineNumberCol);
		table.getColumns().add(table.getColumns().size(), cureTimeRemainingCol);
		
		if (PropertyService.getProperty(PROCESS_POINT_ID, "EXCLUDE_COLUMN_LAST_FIPG", "FALSE").equals("FALSE")){
			TableColumn<CureTimerTrackingDTO,String> lastFipgProcessCol = makeCureTimerTrackingColumn(PropertyService.getProperty(PROCESS_POINT_ID, "COLUMN_HEADER_LAST_FIPG_PROCESS", "Last FIPG Process"), "lastFipgProcess");
			table.getColumns().add(table.getColumns().size(), lastFipgProcessCol);
		}
		
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		vBox.getChildren().add(table);
	}

	private TableColumn<CureTimerTrackingDTO,String> makeCureTimerTrackingColumn(String header, String id) {
		TableColumn<CureTimerTrackingDTO,String> cureTimerTrackingColumn = new TableColumn<CureTimerTrackingDTO,String>(header);
		cureTimerTrackingColumn.setCellValueFactory(new PropertyValueFactory<CureTimerTrackingDTO,String>(id));
		cureTimerTrackingColumn.setCellFactory(makeCellFactory());
		return cureTimerTrackingColumn;
	}

	private Callback<TableColumn<CureTimerTrackingDTO,String>, TableCell<CureTimerTrackingDTO,String>> makeCellFactory() {
		return new Callback<TableColumn<CureTimerTrackingDTO,String>, TableCell<CureTimerTrackingDTO,String>>() {
			public TableCell<CureTimerTrackingDTO,String> call(TableColumn<CureTimerTrackingDTO,String> param) {
				return new TableCell<CureTimerTrackingDTO, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							this.setFont(getTableRow() != null && getTableRow().getIndex() == 0 ? Font.font(null, FontWeight.BOLD, fontSize) : Font.font(null, fontSize));
							this.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
							setText(item);
						}
					}
				};
			}
		};
	}

	private void startRefreshTimer() {
		refreshTimer = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(autoRefreshTimer), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				refresh();
			}
		}));
		refreshTimer.setCycleCount(Timeline.INDEFINITE);
		refreshTimer.play();
	}

	public void refresh() {
		final long refreshTime = now();

		List<Object[]> enginesInQsd = getInProcessProductDao().getCureTimerInventory(triggerLineIds, triggerOnParts, triggerOffProcessPointIds);
		removeEnginesNotInQsd(enginesInQsd);
		addEnginesNewInQsd(enginesInQsd);

		for (CureTimerTrackingDTO item : tableItems) {
			item.refresh(refreshTime);
		}

		labelRefreshTime.setText(prettyPrintTime(refreshTime));
	}

	private void removeEnginesNotInQsd(List<Object[]> enginesInQsd) {
		int i = 0;
		while (i < tableItems.size()) {
			if (!containsEngineNumber(enginesInQsd, tableItems.get(i).getEngineNumber())) {
				tableItems.remove(i);
			}
			else {
				updateDTO(tableItems.get(i));
				i++;
			}
		}
	}

	private void addEnginesNewInQsd(List<Object[]> enginesInQsd) {
		if (enginesInQsd == null) return;
		for (Object[] engineInQsd : enginesInQsd) {
			if (tableItems.size() >= maxRows) {
				break;
			}
			if (!containsEngineNumber(tableItems, (String) engineInQsd[0])) {
				ProductSpec productSpec = makeEngineProductSpec((String) engineInQsd[2]);
				List<InstalledPart> cureTimerResults = getCureTimerResults((String) engineInQsd[0], productSpec);
				boolean isValid = validateInstalledParts(cureTimerResults);
				InstalledPart latestCureTimerResult = getLatestInstalledPart(cureTimerResults);
				tableItems.add(new CureTimerTrackingDTO(
						((java.util.Date) engineInQsd[1]).getTime(), // qsdStartTime
						requiredCureTime, // requiredCureTime
						(isValid ? latestCureTimerResult.getActualTimestamp().getTime() : -1), // fipgStartTime
						(String) engineInQsd[0], // engineNumber
						productSpec, // productSpec
						latestCureTimerResult.getPartName(), // lastFipgProcess
						dateFormat));   // dataFormat for qsdStartTime
			}
		}
	}

	private boolean containsEngineNumber(List<Object[]> engines, String engineNumber) {
		if (engines == null || engineNumber == null) return false;
		for (Object[] engine : engines) {
			if (((String) engine[0]).equals(engineNumber)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsEngineNumber(ObservableList<CureTimerTrackingDTO> engines, String engineNumber) {
		if (engines == null || engineNumber == null) return false;
		for (CureTimerTrackingDTO engine : engines) {
			if (engine.getEngineNumber().equals(engineNumber)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the given item's FipgStartTime and LastFipgProcess to reflect the most recent operation.<br>
	 * Returns true iff all of the given item's cure timer operations have an OK status.
	 */
	private boolean updateDTO(CureTimerTrackingDTO item) {
		List<InstalledPart> cureTimerResults = getCureTimerResults(item.getEngineNumber(), item.getProductSpec());
		boolean isValid = validateInstalledParts(cureTimerResults);
		InstalledPart latestCureTimerResult = getLatestInstalledPart(cureTimerResults);
		item.setFipgStartTime(isValid ? latestCureTimerResult.getActualTimestamp().getTime() : -1);
		item.setLastFipgProcess(latestCureTimerResult.getPartName());
		return isValid;
	}

	/**
	 * Returns the InstalledPart from the given list of InstalledParts which has the most recent actualTimestamp.
	 */
	private InstalledPart getLatestInstalledPart(List<InstalledPart> installedParts) {
		InstalledPart latestInstalledPart = null;
		if (installedParts != null) {
			for (InstalledPart installedPart : installedParts) {
				if (installedPart != null && installedPart.getActualTimestamp() != null) {
					if (latestInstalledPart == null) {
						latestInstalledPart = installedPart;
					}
					else if (installedPart.getActualTimestamp().compareTo(latestInstalledPart.getActualTimestamp()) > 0) {
						latestInstalledPart = installedPart;
					}
				}
			}
		}
		return (latestInstalledPart == null ? makeMissingInstalledPart("", "") : latestInstalledPart);
	}

	/**
	 * Returns true iff all given InstalledParts have an OK installed part status.
	 */
	private boolean validateInstalledParts(List<InstalledPart> installedParts) {
		if (installedParts == null || installedParts.isEmpty()) return false;
		for (InstalledPart installedPart : installedParts) {
			if (installedPart.getInstalledPartStatus() != com.honda.galc.entity.enumtype.InstalledPartStatus.OK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the list of InstalledParts for the given engineNumber and productSpec whose part names are configured for PROCESS_POINT_ID.
	 */
	private List<InstalledPart> getCureTimerResults(String engineNumber, ProductSpec productSpec) {
		List<RequiredPart> requiredParts = getCureTimerRequiredParts(productSpec);
		if (requiredParts == null || requiredParts.isEmpty()) return null;
		List<String> partNames = new ArrayList<String>();
		for (RequiredPart requiredPart : requiredParts) {
			partNames.add(requiredPart.getId().getPartName());
		}
		List<InstalledPart> cureTimerResults = getInstalledPartDao().findAllByProductIdAndPartNames(engineNumber, partNames);
		if (cureTimerResults == null) cureTimerResults = new ArrayList<InstalledPart>();
		for (String partName : partNames) {
			if (!containsPartName(cureTimerResults, partName)) {
				cureTimerResults.add(makeMissingInstalledPart(engineNumber, partName));
			}
		}
		return cureTimerResults;
	}

	private boolean containsPartName(List<InstalledPart> cureTimerResults, String partName) {
		for (InstalledPart cureTimerResult : cureTimerResults) {
			if (cureTimerResult.getPartName().equals(partName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns an InstalledPart with the given engineNumber and partName with an installed part status of MISSING.
	 */
	private InstalledPart makeMissingInstalledPart(String engineNumber, String partName) {
		InstalledPart result = new InstalledPart();
		result.setProductId(engineNumber);
		result.setPartName(partName);
		result.setInstalledPartStatus(com.honda.galc.entity.enumtype.InstalledPartStatus.MISSING);
		result.setActualTimestamp(new java.sql.Timestamp(0));
		return result;
	}

	/**
	 * Returns the list of RequiredParts for the given productSpec which are configured for PROCESS_POINT_ID.
	 */
	private List<RequiredPart> getCureTimerRequiredParts(ProductSpec productSpec) {
		return getRequiredPartDao().findAllByProcessPointAndProdSpec(PROCESS_POINT_ID, productSpec);
	}

	private ProductSpec makeEngineProductSpec(String productSpecCode) {
		ProductSpec productSpec = new com.honda.galc.entity.product.EngineSpec();
		productSpec.setProductSpecCode(productSpecCode);
		productSpec.setModelYearCode(ProductSpec.extractModelYearCode(productSpecCode));
		productSpec.setModelCode(ProductSpec.extractModelCode(productSpecCode));
		productSpec.setModelTypeCode(ProductSpec.extractModelTypeCode(productSpecCode));
		productSpec.setModelOptionCode(ProductSpec.extractModelOptionCode(productSpecCode));
		return productSpec;
	}

	private long now() {
		return new java.util.Date().getTime();
	}

	private String prettyPrintTime(long time) {
		return new java.text.SimpleDateFormat(this.dateFormat).format(new java.util.Date(time));
	}

	/**
	 * DAO for REQUIRED_PARTS_TBX
	 */
	protected RequiredPartDao getRequiredPartDao() {
		if (requiredPartDao == null) requiredPartDao = ServiceFactory.getDao(RequiredPartDao.class);
		return requiredPartDao;
	}

	/**
	 * DAO for GAL176TBX
	 */
	protected InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null) inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		return inProcessProductDao;
	}

	/**
	 * DAO for GAL185TBX
	 */
	protected InstalledPartDao getInstalledPartDao() {
		if (installedPartDao == null) installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		return installedPartDao;
	}

	@Override
	public void handle(ActionEvent event) {
	}

}
