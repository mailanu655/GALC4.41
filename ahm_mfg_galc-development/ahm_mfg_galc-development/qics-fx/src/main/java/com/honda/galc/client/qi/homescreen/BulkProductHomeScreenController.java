package com.honda.galc.client.qi.homescreen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.lang.StringUtils;
import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.enumtype.KeyboardEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.enumtype.ProductStatus;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.repairentry.AbstractRepairEntryView;
import com.honda.galc.client.qi.repairentry.RepairEntryModel;
import com.honda.galc.client.qi.repairentry.UpdateTrackingDialog;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.KeyboardEvent;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.schedule.TrainingModeEvent;
import com.honda.galc.client.schedule.UserResetEvent;
import com.honda.galc.client.ui.ClientStartUpProgress;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.event.CacheEvent;
import com.honda.galc.client.ui.event.CacheEventType;
import com.honda.galc.client.ui.event.ProductFinishedEvent;
import com.honda.galc.client.ui.event.ProductProcessEvent;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.SearchByProcessDto;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.DailyDepartmentScheduleUtil;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.util.StringUtil;
import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class BulkProductHomeScreenController extends AbstractQiProcessController<HomeScreenModel, BulkProductHomeScreenView> implements EventHandler<ActionEvent> {

	private QiPropertyBean property;

	private static final int MAX_LIST_SZ = 30;

	private DailyDepartmentScheduleUtil schedule;
	private QiStationConfiguration qiEntryStationConfigManagement;
	private ApplicationPropertyBean appBean;

	private List<SearchByProcessDto> searchByProcessDtoList = new ArrayList<SearchByProcessDto>();
	
	private boolean isUpcStation;
	private boolean inTrainingMode = true;
	private static Node node;
	private File template = null;

	private boolean isKeepMeSignedInSelected = false;

	public BulkProductHomeScreenController(HomeScreenModel model, BulkProductHomeScreenView view) {
		super(model, view);
		EventBusUtil.register(this);
	}

	@Override
	public void initEventHandlers() {
		isUpcStation = getModel().getProperty().isUpcStation();
		addAssociateTableListners();
		addSignedInCheckBoxListner();
		if(!isUpcStation){
			addEntryDeptComboboxListener();
			if(MultiLineHelper.getInstance(getProcessPointId()).isMultiLine())  {
				addEntryDept2ComboboxListener();
			}
		}
		addTabChangeListener();
		addProcessedProductTableListner();
		addScannedProductsTableListener();
		addScrappedProductsTableListener();
		addNonProcessableProductsTableListener();
		loadStationAndProductData();		
	}


	/**
	 * Method to load Station Counts, process products and associate info
	 */
	public void loadStationAndProductData() {
		schedule = ApplicationContext.getInstance().getDailyDepartmentScheduleUtil();
		loadDeptDropDown();
		loadDept2DropDown();
		addAssociateTableData();
		keepUserSignedIn();
		loadInitialData();	
		initTrainingModeButton();	

	}
	
	private void initTrainingModeButton() {
		qiEntryStationConfigManagement = getModel().findPropertyKeyValueByProcessPoint(getModel().getProcessPointId(),
				QiEntryStationConfigurationSettings.TRAINING_MODE.getSettingsName());
		if (qiEntryStationConfigManagement != null) {
			if (qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NO)) {
				inTrainingMode = false;
				getView().getEnterTrainingModeButton().setDisable(true);
				getView().getExitTrainingModeButton().setDisable(true);
				return;
			}
		}
		inTrainingMode = true;
		getView().getEnterTrainingModeButton().setDisable(false);
		getView().getExitTrainingModeButton().setDisable(false);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (QiConstant.COMMENT.equalsIgnoreCase(loggedButton.getId()))openCommentDialog(actionEvent);
			else if (QiConstant.REFRESH_CACHE.equalsIgnoreCase(loggedButton.getId())) openRefreshCacheDialog(actionEvent);
			else if (QiConstant.NEW.equalsIgnoreCase(loggedButton.getId()))openLoginPopUp(actionEvent);
			else if (QiConstant.LOGOUT.equalsIgnoreCase(loggedButton.getId())) openLogoutPopUp();
			else if (QiConstant.PROCESS_ALL_PRODUCTS.equalsIgnoreCase(loggedButton.getText()))processProducts();
			else if (QiConstant.CLEAR_PRODUCTS_TABLE.equalsIgnoreCase(loggedButton.getId()))clearSelectedTable();
			else if (QiConstant.EXPORT_TABLE.equalsIgnoreCase(loggedButton.getText())) exportTable();
			else if (QiConstant.ENTER_TRAINING_MODE.equalsIgnoreCase(loggedButton.getText()))setTrainingMode(actionEvent);
			else if (QiConstant.EXIT_TRAINING_MODE.equalsIgnoreCase(loggedButton.getText()))setExitTrainingMode(actionEvent);
			else if (QiConstant.IMPORT_PRODUCT_LIST.equalsIgnoreCase(loggedButton.getText()))selectAndImportFile(actionEvent);
		}
	}
	
	
	private void selectAndImportFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files","*.xls","*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        template = fileChooser.showOpenDialog(ClientMainFx.getInstance().getStage());
        if (template != null) {
        	loadData();
		}
	}
	
	
	public void loadData() {
		QiProgressBar qiProgressBar = null;
		try {
			Integer listSize = PropertyService.getPropertyInt(getProcessPointId(), "IMPORT_PRODUCT_LIST_SIZE", 1000);
			InputStream filePath = new FileInputStream(template); 
			XSSFWorkbook workbook = new XSSFWorkbook(filePath);
			XSSFSheet worksheet = null;
			worksheet = workbook.getSheetAt(0);
			List<String> productList = new ArrayList<String> ();
			int rowNum = worksheet.getLastRowNum() + 1;
			if(rowNum>listSize) {
				EventBusUtil.publish(new StatusMessageEvent("Failed to Import : Number of records are higher than allowed "+listSize+ " Limit", StatusMessageEventType.ERROR));
			}else {
				qiProgressBar = QiProgressBar.getInstance("Please wait ...", "Upoading Data size : "+rowNum,"Loading",getView().getStage(),true);	
				qiProgressBar.showMe();		
				for(int i = 0; i <rowNum; i++){
					XSSFRow row = worksheet.getRow(i);
					String value = null;
					if(row != null) {
						XSSFCell cell = row.getCell(0);
						value = cell.toString().trim();
						if(!StringUtil.isNullOrEmpty(value)) {
							productList.add(value);
							}
					}else {
						continue;
					}
				}

				if(productList != null && !productList.isEmpty()) {
					EventBusUtil.publish(new ProductEvent(productList, ProductEventType.PRODUCT_INPUT_RECIEVED));
				}else {
					MessageDialog.showInfo(getView().getStage(), "No records found in uploaded xsl file");
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			EventBusUtil.publish(new StatusMessageEvent("Failed to import the file ", StatusMessageEventType.ERROR));
		} catch (IOException e) {
			EventBusUtil.publish(new StatusMessageEvent("Failed to import the file ", StatusMessageEventType.ERROR));
			e.printStackTrace();
		} catch (Exception e) {
			EventBusUtil.publish(new StatusMessageEvent("Failed to import the file ", StatusMessageEventType.ERROR));
			e.printStackTrace();
		}
		finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}

	private void addProductsToTable(List<ProductSearchResult> productSearchResults) {
		boolean isNonProcessableProductProcessed = false;
		boolean isScrappedProductProcessed = false;
		boolean isScannedProductProcessed = false;
		for(ProductSearchResult result : productSearchResults) {
			if(!result.getErrorMessage().isEmpty() 
					&& result.getDefectStatus() != null 
					&& !result.getDefectStatus().name().equals(DefectStatus.NON_REPAIRABLE.name())
					|| (result.getDefectStatus() != null 
					&& (!result.getProductStatus().name().equals(ProductStatus.NO_DEFECT.name())))
					|| (result.getProductStatus().equals(ProductStatus.INVALID)) 
					|| (result.getProductStatus().equals(ProductStatus.NOT_EXIST))
					) {
				addProductToNonProcessablePrdTable(result);
				isNonProcessableProductProcessed= true;
			}else if((!result.getErrorMessage().isEmpty() && result.getDefectStatus() != null && result.getDefectStatus().name().equals(DefectStatus.NON_REPAIRABLE.name()))) {
				addProductToScrappedPrdTable(result);
				isScrappedProductProcessed = true;
			}
			else if(result.getDefectStatus() == null || result.getDefectStatus().name().equals(DefectStatus.FIXED.name())
					|| result.getDefectStatus().name().equals(DefectStatus.OUTSTANDING.name())
					|| result.getDefectStatus().name().equals(DefectStatus.NOT_REPAIRED.name())
					|| result.getDefectStatus().name().equals(DefectStatus.NOT_FIXED.name())) {
				addProductToScannedPrdTable(result);
				isScannedProductProcessed = true;
			}
		}
		//Open the product result table if all processed products goes to that table
		if(isScannedProductProcessed == true && (isScrappedProductProcessed || isNonProcessableProductProcessed) == false) {
			//All processed products are Scanned Products
			if(!getView().getScannedProductsTab().isSelected()) {
				getView().getTabbedPane().getSelectionModel().select(getView().getScannedProductsTab());
			}
		}else if(isNonProcessableProductProcessed == true && (isScrappedProductProcessed || isScannedProductProcessed) == false) {
			//All processed products are Non Processable Products
			if(!getView().getNonProcessableProductsTab().isSelected()) {
				getView().getTabbedPane().getSelectionModel().select(getView().getNonProcessableProductsTab());
			}			
		}else if(isScrappedProductProcessed == true && (isNonProcessableProductProcessed || isScannedProductProcessed) == false) {
			//All processed products are Scrapped Products
			if(!getView().getScrappedProductsTab().isSelected()) {
				getView().getTabbedPane().getSelectionModel().select(getView().getScrappedProductsTab());
			}
		}
		autoResizeColumns(getView().getNonProcessablePrdTable().getTable());
		autoResizeColumns(getView().getScrappedPrdTable().getTable());
		autoResizeColumns(getView().getScannedPrdTable().getTable());
	}
	
	private static void autoResizeColumns(TableView<?> tableView) {
		for(Object column : tableView.getColumns()) {

			try {
				getColumnResizeMethod().invoke(tableView.getSkin(), column, -1);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static Method getColumnResizeMethod() {
		Method columnResizeMethod = null;
		try {
			columnResizeMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
			columnResizeMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			Logger.getLogger().error("Failed to find column resize method");
		}
		return columnResizeMethod;
	}

	private void addProductToScannedPrdTable(ProductSearchResult result) {
		ObservableList<ProductSearchResult> products = getView().getScannedPrdTable().getTable().getItems();
		products.addAll(result);
		getView().getScannedPrdTable().getTable().setItems(products);
		int count = products == null ? 0 : products.size();
		getView().getScannedProductsTab().setText("Scanned Products(" + count + ")");
		if(!getView().getScannedProductsTab().isSelected()) {
			getView().getScannedProductsTab().setStyle(getView().getTabStyleSmallerReceived());
		} else {
			getView().getSubmitProductsButton().setDisable(false);
			getView().getRemoveProductsButton().setDisable(false);
			getView().getExportTableButton().setDisable(false);
		}
	}

	private void addProductToNonProcessablePrdTable(ProductSearchResult result) {
		ObservableList<ProductSearchResult> products = getView().getNonProcessablePrdTable().getTable().getItems();
		class MyList<E extends ProductSearchResult> extends ArrayList<E>  {
			public MyList(ObservableList<E> obsList) {
				super(obsList.stream().collect(Collectors.toList()));
			}
			public int containsAt(E thisResult)  {
				if(thisResult == null || thisResult.getProduct() == null)  return -1;
				for(int i = 0; i < this.size(); i++)  {
					ProductSearchResult item_i = this.get(i);
					if(item_i != null && item_i.getProduct().getProductId().trim().equalsIgnoreCase(thisResult.getProduct().getProductId().trim()))  return i;
				}
				return -1;
			}
		}
		MyList<ProductSearchResult> searchList = new MyList<>(products);
		int i = searchList.containsAt(result);
		if(i >= 0)  {
			searchList.set(i, result);
		}
		else  {
			searchList.add(result);
		}
		getView().getNonProcessablePrdTable().getTable().setItems(FXCollections.observableArrayList(searchList));
		int count = products == null ? 0 : searchList.size();
		getView().getNonProcessableProductsTab().setText("Invalid Products(" + count + ")");
		if(!getView().getNonProcessableProductsTab().isSelected()) {
			getView().getNonProcessableProductsTab().setStyle(getView().getTabStyleSmallerReceived());
		} else {
			getView().getRemoveProductsButton().setDisable(false);
			getView().getExportTableButton().setDisable(false);
			getView().getFileChooserButton().setDisable(true);
		}
	}
	
	private void addProductToScrappedPrdTable(ProductSearchResult result) {
        ObservableList<ProductSearchResult> products = getView().getScrappedPrdTable().getTable().getItems();
        products.add(result);
        getView().getScrappedPrdTable().getTable().setItems(products);
        int count = products == null ? 0 : products.size();
        getView().getScrappedProductsTab().setText("Scrapped Products(" + count + ")");
        if(!getView().getScrappedProductsTab().isSelected()) {
               getView().getScrappedProductsTab().setStyle(getView().getTabStyleSmallerReceived());
        } else {
			getView().getSubmitProductsButton().setDisable(false);
			getView().getRemoveProductsButton().setDisable(false);
			getView().getExportTableButton().setDisable(false);
		}
	}

	public void clearSelectedTable() {
		if(getView().getScannedProductsTab().isSelected()) {
			clearScannedProductsTable();
		}
		if(getView().getNonProcessableProductsTab().isSelected()) {
			clearNonProcessableProductsTable();
		}
		if(getView().getScrappedProductsTab().isSelected()) {
			clearScrappedProductsTable();
		}		
		getView().getSubmitProductsButton().setDisable(true);
		getView().getRemoveProductsButton().setDisable(true);
		getView().getExportTableButton().setDisable(true);
	}

	private void clearSearchedProductsTable() {
		clearScannedProductsTable();		
		clearNonProcessableProductsTable();		
		clearScrappedProductsTable();
	
		getView().getSubmitProductsButton().setDisable(true);
		getView().getRemoveProductsButton().setDisable(true);
		getView().getExportTableButton().setDisable(true);
	}
	
	public boolean isShowProductSequenceTab()  {
		boolean isProductSequenceTab = PropertyService.getPropertyBoolean(getProcessPointId(), "SHOW_PRODUCT_SEQUENCE_TAB", false);
		boolean isProductSequence = PropertyService.getPropertyBoolean(getProcessPointId(), "IS_PRODUCT_SEQUENCE_STATION", false);
		return (isProductSequence && isProductSequenceTab);
	}

	private void clearScannedProductsTable() {
		getView().getScannedPrdTable().getTable().getItems().clear();
		getView().getScannedProductsTab().setText("Scanned Products(0)");
		ProductSearchResult.clearDefectResultMap();
		ProductSearchResult.clearDefectsProcessingMap();
	}
	
	private void clearScrappedProductsTable() {
        getView().getScrappedPrdTable().getTable().getItems().clear(); 
        getView().getScrappedProductsTab().setText("Scrapped Products(0)");
        ProductSearchResult.clearDefectResultMap();
		ProductSearchResult.clearDefectsProcessingMap();
	}

	private void clearNonProcessableProductsTable() {
		getView().getNonProcessablePrdTable().getTable().getItems().clear();	
		getView().getNonProcessableProductsTab().setText("Invalid Products(0)");
	}

	private void processProducts() {
		if(getView().getScannedProductsTab().isSelected()) {
			processProducts(getView().getScannedPrdTable().getTable().getItems());
			getProductModel().setProcessedFromScrappedTable(false);
		}
		if(getView().getScrappedProductsTab().isSelected()) {
			processProducts(getView().getScrappedPrdTable().getTable().getItems());
			getProductModel().setProcessedFromScrappedTable(true);
		}
	}

	private void processProducts(List<ProductSearchResult> results) {
		if(verifyProcessingLimit(results) && checkForSameModel(results)) {
			getProductModel().setProductList(results);
			EventBusUtil.publish(new ProductProcessEvent(getProductModel()));
		}
	}

	private void updateTrackingStatus(List<ProductSearchResult> results) {
		getProductModel().setProductList(results);
		RepairEntryModel repairModel = RepairEntryModel.getInstance();
		repairModel.setProductModel(getProductModel());
		List<Line> listOfTrackingLines = RepairEntryModel.findAllTrackingLinesByProductType(getProductModel().getProductType());
		Collections.sort(listOfTrackingLines, getLineComparator());
		UpdateTrackingDialog myDialog = new UpdateTrackingDialog(currentWorkingProcessPointId, listOfTrackingLines, getProductModel().getProductType(), this, repairModel, results);
		myDialog.show();
	}

	public static Comparator<Line> getLineComparator()  {
		return new Comparator<Line>()  {
			public int compare(Line line1, Line line2)  {
				if(line1 == null && line1 == null)  return 0;
				String divName1 = StringUtils.isBlank(line1.getDivisionName()) ? "" : line1.getDivisionName().trim();
				String divName2 = StringUtils.isBlank(line2.getDivisionName()) ? "" : line2.getDivisionName().trim();				
				int divFlag = divName1.compareToIgnoreCase(divName2);
				if(divFlag != 0)  {
					return divFlag;
				}
				else  {
					int lineSeq1 = line1.getLineSequenceNumber();
					int lineSeq2 = line2.getLineSequenceNumber();

					if(lineSeq1 != lineSeq2)  {
						return Integer.compare(lineSeq1, lineSeq2);
					}
					else  {
						String lineName1 = StringUtils.isBlank(line1.getLineName()) ? "" : line1.getLineName().trim();
						String lineName2 = StringUtils.isBlank(line2.getLineName()) ? "" : line2.getLineName().trim();
						return lineName1.compareToIgnoreCase(lineName2);
					}
				}
			}};
	}
	
	private boolean verifyProcessingLimit(List<ProductSearchResult> results) {
		int processingLimit = getModel().getProcessingLimit();
		if( results.size() > processingLimit ) {
			displayErrorMessage("Cannot process " + results.size() + " products, maximum of " + processingLimit + " allowed");
			return false;
		}
		return true;
	}

private boolean checkForSameModel(List<ProductSearchResult> results) {
    if(results.size() > 1) {
        String modelCode = StringUtils.trim(results.get(0).getProduct().getModelCode());
        List<ProductSearchResult> differentModelResults = results.stream()
                .filter(n -> !StringUtils.trim(n.getProduct().getModelCode()).equals(modelCode))
                .collect(Collectors.toList());
        if(!differentModelResults.isEmpty()) {
            displayErrorMessage("Cannot process products that are not the same model.");
            return false;
        }
    }
    return true;
}

	private void openLoginPopUp(ActionEvent actionEvent) {
		clearSearchedProductsTable();
		EventBusUtil.publish(new UserResetEvent(null,QiConstant.NEW));
		EventBusUtil.publishAndWait(new KeyboardEvent(KeyboardEventType.HIDE_KEYBOARD));
		LoginStatus loginStatus = LoginDialog.login(ClientMainFx.getInstance().getStage(getModel().getProcessPointId()), true,true);
		if(loginStatus != LoginStatus.OK){
			EventBusUtil.publish(new EntryDepartmentEvent(null, QiConstant.ASSOCIATE_ID_SELECTED));
		} else {
			addAssociateTableData();
			getApplicationContext().setUserId(ClientMainFx.getInstance().getAccessControlManager().getUserName());
			getView().getMainWindow().getStatusMessagePane().getStatusPane().setUser(getApplicationContext().getUserId());
			EventBusUtil.publish(new EntryDepartmentEvent(ApplicationContext.getInstance().getUserId(), QiConstant.ASSOCIATE_ID_SELECTED));
		}
		
	}

	private void openLogoutPopUp() {
		clearSearchedProductsTable();
		getView().clearTrackingMenuItems();
		EventBusUtil.publish(new UserResetEvent(null,QiConstant.LOGOUT));
		getView().getMainWindow().logout();
		addAssociateTableData();
	}

	private void openCommentDialog(ActionEvent actionEvent) {
		EventBusUtil.publishAndWait(new KeyboardEvent(KeyboardEventType.HIDE_KEYBOARD));
		CommentDialog dialog;
		dialog = new CommentDialog(QiConstant.CREATE,  new HomeScreenModel(),getApplicationId());
		dialog.showDialog();
	}

	private void openRefreshCacheDialog(ActionEvent actionEvent) {
		RefreshCacheDialog dialog = new RefreshCacheDialog(QiConstant.REFRESH_CACHE, new HomeScreenModel(), getApplicationId(), getApplicationContext().getApplicationPropertyBean().getProductType());
		dialog.showDialog();
		if (!dialog.isCancelled() && dialog.getEntryModel() != null) {
			EventBusUtil.publish(new CacheEvent(CacheEventType.REFRESH_PDC_CACHE, dialog.getEntryModel().getId().getEntryModel(), getApplicationId()));
		}
	}

	@Override
	public void initializeListeners() {
		getView().getKeepSignedInChkBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				isKeepMeSignedInSelected = newValue;
			}
		});
	}

	private void loadInitialData() {
		WebStartClient webStart = getModel().getStationDetails(ApplicationContext.getInstance().getTerminalId());
		if(null!=schedule && null!=schedule.getCurrentSchedule()){
			getView().getShiftTextField().setText(schedule.getCurrentShift());
			StationResult stationResult = getModel().getInspectedDetails(getView().getStationNameTextField().getText(),getView().getShiftTextField().getText(),schedule.getCurrentSchedule().getId().getProductionDate());
			if(stationResult == null){
				stationResult = getModel().addStationResultForShiftAndProdDate(schedule.getCurrentSchedule().getId().getProductionDate(),schedule.getCurrentSchedule().getId().getShift(),getProcessPointId());
			}
			if(stationResult != null) {
				int productIdInspect = stationResult.getProductIdInspect();
				int scanCount = stationResult.getScanCount();
				int productIdDirectPassed = stationResult.getProductIdDirectPassed();
				int outstandingProductId = stationResult.getOutstandingProductId();
				int totalRejections = stationResult.getTotalRejections();
				int productIdWithDefects = stationResult.getProductIdWithDefects();
				int scrap = stationResult.getScrap();

				MultiLineHelper qiMultiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
				if (qiMultiLineHelper.isMultiLine()) {
					List<String> validProcessPoints = qiMultiLineHelper.getValidProcessPoints();
					String currentProcessPoint = getView().getStationNameTextField().getText();

					for (int i = 0; i < validProcessPoints.size(); i++) {
						if (!validProcessPoints.get(i).equals(currentProcessPoint)) {
							StationResult otherStationResult = getModel().getInspectedDetails(validProcessPoints.get(i),getView().getShiftTextField().getText(),schedule.getCurrentSchedule().getId().getProductionDate());
							if(otherStationResult != null){
								productIdInspect += otherStationResult.getProductIdInspect();
								scanCount += otherStationResult.getScanCount();
								productIdDirectPassed += otherStationResult.getProductIdDirectPassed();
								outstandingProductId += otherStationResult.getOutstandingProductId();
								totalRejections += otherStationResult.getTotalRejections();
								productIdWithDefects += otherStationResult.getProductIdWithDefects();
								scrap += otherStationResult.getScrap();
							}
						}
					}
				}

				getView().getInspectedTextField().setText(String.valueOf(productIdInspect));
				getView().getScanCountTextField().setText(String.valueOf(scanCount));
				getView().getDirectPassedTextField().setText(String.valueOf(productIdDirectPassed));
				getView().getNotRepairedRejectionsTextField().setText(String.valueOf(outstandingProductId));
				getView().getTotalRejectionsTextField().setText(String.valueOf(totalRejections));
				getView().getPrdsWithRejectionsTextField().setText(String.valueOf(productIdWithDefects));
				getView().getPrdsScrappedTextField().setText(String.valueOf(scrap));
			}
			TeamRotation teamRotationObj = getModel().getTeamDetails();
			if(null!=teamRotationObj)
				getView().getTeamTextField().setText(StringUtils.trimToEmpty(teamRotationObj.getId().getTeam()));
		}else{
			EventBusUtil.publish(new StatusMessageEvent("Production schedule data is not available. Please contact Administrator ", StatusMessageEventType.WARNING));
		}
		if(null!=webStart){
			getView().getExtentionTextField().setText(StringUtils.trimToEmpty(webStart.getPhoneExtension()));
			getView().getStationLocationTextField().setText(StringUtils.trimToEmpty(webStart.getColumnLocation()));
		}
		
		if (inTrainingMode && getView().getMainWindow().getStatusMessagePane().getBottom().toString().contains(QiConstant.TRAINING_MODE)){
			getView().getEnterTrainingModeButton().setVisible(false);
	        getView().getExitTrainingModeButton().setVisible(true);
	        if (AbstractRepairEntryView.getParentCachedDefectList() != null)
	        	AbstractRepairEntryView.getParentCachedDefectList().clear();
		}
	}

	private void loadDeptDropDown() {
		if(!isUpcStation){
			List<QiStationEntryDepartment> deptList = getModel().findAllEntryDeptByProcessPoint(getProcessPointId());
			if(deptList.size()>1){
				getView().getEntryDeptComboBox().setItems(FXCollections.observableArrayList(deptList));
				getView().getEntryDeptComboBox().setConverter(qiDivisionConverter());
			}

			QiStationEntryDepartment qiStationEntryDepartment = getModel().findDefaultEntryDeptByProcessPoint(getProcessPointId());
			if(qiStationEntryDepartment!=null){
				getView().getEntryDeptComboBox().getSelectionModel().select(qiStationEntryDepartment);
				getView().getEntryDeptComboBox().setConverter(qiDivisionConverter());
				getApplicationContext().setEntryDept(qiStationEntryDepartment.getId().getDivisionId());
			} else {
				getApplicationContext().setEntryDept(StringUtils.EMPTY);
			}
			EventBusUtil.publish(new EntryDepartmentEvent(getApplicationContext().getEntryDept(), QiConstant.ENTRY_DEPT_SELECTED));
		}
	}

	private void loadDept2DropDown() {
		if(!isUpcStation){
			MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
			if(!multiLineHelper.isMultiLine() || StringUtils.isBlank(multiLineHelper.getFirstAlternateStation())) {
				return;
			}

			List<QiStationEntryDepartment> deptList = getModel().findAllEntryDeptByProcessPoint(multiLineHelper.getFirstAlternateStation());
			if(deptList.size()>1){
				getView().getEntryDeptComboBox2().setItems(FXCollections.observableArrayList(deptList));
				getView().getEntryDeptComboBox2().setConverter(qiDivisionConverter());
			}

			QiStationEntryDepartment qiStationEntryDepartment = getModel().findDefaultEntryDeptByProcessPoint(multiLineHelper.getFirstAlternateStation());
			if(qiStationEntryDepartment!=null){
				getView().getEntryDeptComboBox2().getSelectionModel().select(qiStationEntryDepartment);
				getView().getEntryDeptComboBox2().setConverter(qiDivisionConverter());
				getApplicationContext().setEntryDept2(qiStationEntryDepartment.getId().getDivisionId());
			} else {
				getApplicationContext().setEntryDept2(StringUtils.EMPTY);
			}
			EventBusUtil.publish(new EntryDepartmentEvent(getApplicationContext().getEntryDept2(), QiConstant.ENTRY_DEPT2_SELECTED));
		}
	}

	private void addProcessedProductData() {
		if(null!=schedule && null!=schedule.getCurrentSchedule()){
			Timestamp shiftStartTimestamp = schedule.getShiftStartTimestamp(schedule.getCurrentShift());
			if (shiftStartTimestamp == null) {
				return;
			}
			List<QiDefectResultDto> defectResultDtos = getModel().getProcessedProductData(getModel().getProcessPointId(), shiftStartTimestamp, getMaxListSize());

			if(null!=defectResultDtos && defectResultDtos.size() > 0) {
				String productType = getApplicationContext().getApplicationPropertyBean().getProductType();
				if(isDieCastProduct()) {
					//Show MC and DC number for DieCast product instead of product Id
					List<QiDefectResultDto> results = new ArrayList<QiDefectResultDto>(defectResultDtos.size());
					for(QiDefectResultDto dto : defectResultDtos) {
						DieCast product = (DieCast) ProductTypeUtil.getProductDao(productType).findByKey(dto.getProductId());
						dto.setDcSerialNumber(product.getDcSerialNumber());
						dto.setMcSerialNumber(product.getMcSerialNumber());
						results.add(dto);
					}
					getView().getProcessedPrdTable().setData(results);
				}else
					getView().getProcessedPrdTable().setData(defectResultDtos);
			}

			int count = defectResultDtos == null ? 0: defectResultDtos.size();
			getView().getProcessedTab().setText("Processed Products(" + count + ")");					

			getView().getProcessedPrdTable().getTable().setRowFactory(new Callback<TableView<QiDefectResultDto>, TableRow<QiDefectResultDto>>() {
				@Override
				public TableRow<QiDefectResultDto> call(TableView<QiDefectResultDto> paramP) {
					return new TableRow<QiDefectResultDto>() {
						@Override
						protected void updateItem(QiDefectResultDto defectResultDto, boolean paramBoolean) {
							if(null!=defectResultDto && null!=defectResultDto.getProductId()){
								if(isProductScrapped(defectResultDto)){
									String style = "-fx-text-background-color: red ;";
									setStyle(style);
								}else {
									String style = "-fx-text-background-color: black ;";
									setStyle(style);
								}
							}
							super.updateItem(defectResultDto, paramBoolean);
						}
					};
				}
			});
		}	
	}

	private void addAssociateTableData() {
		String processPointId = getModel().getProcessPointId();
		this.appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, getApplicationContext().getApplicationId());

		Timestamp oldTimestamp = CommonUtil.findDateRange(appBean.getDateRangeForRecentUsers());
		if (appBean.isClearRecentUser()) {

			if (schedule != null&&!StringUtils.isEmpty(schedule.getCurrentShift())) oldTimestamp = schedule.getShiftStartTimestamp(schedule.getCurrentShift());
		}

		getView().getAssociateTable().setData(getModel().findAllRecentUsersByHostName(processPointId,oldTimestamp,appBean.getMaxRecentUsers()));

		//Check configurations from station config screen to gather Associate Id
		qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(processPointId,QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getSettingsName());
		if(qiEntryStationConfigManagement!=null ){
			if(qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NO)){
				getView().getAssociateTable().setDisable(true);
				getView().getNewButton().setDisable(true);
				getView().getKeepSignedInChkBox().setDisable(true);
			}
		}else if(QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.NO)){
			getView().getAssociateTable().setDisable(true);
			getView().getKeepSignedInChkBox().setDisable(true);
			getView().getNewButton().setDisable(true);
		}
		if(isAuthorizedForTracking())  {				
			getView().addTrackingMenuItems();
		}
		else  {
			getView().clearTrackingMenuItems();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addAssociateTableListners() {
		ObservableList<TableColumn<StationUser, ?>> columns  = getView().getAssociateTable().getTable().getColumns();
		TableColumn associateidColumn  = columns.get(1);

		associateidColumn.setCellFactory(new Callback<TableColumn<StationUser, String>, TableCell<StationUser, String>>() {
			@Override
			public TableCell<StationUser, String> call(TableColumn<StationUser, String> col) {
				final TableCell<StationUser, String> cell = new TableCell<StationUser, String>() {
					@Override
					public void updateItem(String firstName, boolean empty) {
						super.updateItem(firstName, empty);
						if (empty) {
							setText(null);
						} else {
							setText(firstName);
						}
					}
				};
				cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getClickCount() >= 1) {
							StationUser selectedUser = getView().getAssociateTable().getTable().getSelectionModel().getSelectedItem();
							if(null != selectedUser){
								String selectedUserId = selectedUser.getId().getUser();
								String lastUserId = getApplicationContext().getUserId();
								clearMessage();
								LoginStatus loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(selectedUserId);
								if(loginStatus == LoginStatus.OK){
									getApplicationContext().setUserId(selectedUserId);
									getView().getMainWindow().getStatusMessagePane().getStatusPane().setUser(selectedUserId);
									EventBusUtil.publish(new EntryDepartmentEvent(selectedUserId, QiConstant.ASSOCIATE_ID_SELECTED));
									if(!selectedUserId.equalsIgnoreCase(lastUserId))
										getModel().updateLastLogInUser(selectedUserId);
								}else{
									displayErrorMessage(selectedUserId.trim()+" does not exist");
									EventBusUtil.publish(new EntryDepartmentEvent(null, QiConstant.ASSOCIATE_ID_SELECTED));
								}
							}else{
								displayErrorMessage("Please select Associate id");
							}
						}
					}
				});
				return cell ;
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addScannedProductsTableListener() {
		ObservableList<TableColumn<ProductSearchResult, ?>> columns = getView().getScannedPrdTable().getTable().getColumns();

		TableColumn defectStatusColumn;
		TableColumn kickoutStatusColumn;
		TableColumn searchProcessPointColumn;
		TableColumn searchProcessPointTimestampColumn;

		if(isDieCastProduct()) {
			defectStatusColumn = columns.get(QiConstant.DEFECT_STATUS_COLUMN_DC);
			kickoutStatusColumn = columns.get(QiConstant.KICKOUT_STATUS_COLUMN_DC);
			searchProcessPointColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_COLUMN_DC);
			searchProcessPointTimestampColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_TIMESTAMP_COLUMN_DC);
		} else {
			defectStatusColumn = columns.get(QiConstant.DEFECT_STATUS_COLUMN);
			kickoutStatusColumn = columns.get(QiConstant.KICKOUT_STATUS_COLUMN);
			searchProcessPointColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_COLUMN);
			searchProcessPointTimestampColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_TIMESTAMP_COLUMN);
		}
		
		defectStatusColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> col) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String defectStatus, boolean empty) {
						super.updateItem(defectStatus, empty);
						if (defectStatus == null || defectStatus.equals("null")) {
							setText("");
						} else {
							setText(defectStatus);
						}
					}
				};
				return cell;
			}
		});
		
		kickoutStatusColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> col) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String kickoutStatus, boolean empty) {
						super.updateItem(kickoutStatus, empty);
						if (kickoutStatus == null || kickoutStatus.equals("null")) {
							setText("");
						} else {
							setText(kickoutStatus);
						}
					}
				};
				return cell;
			}
		});
		
		searchProcessPointColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> col) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String scannedProcessPoint, boolean empty) {
						super.updateItem(scannedProcessPoint, empty);
						if (scannedProcessPoint == null || scannedProcessPoint.equals("null")) {
							setText("");
						} else {
							setText(scannedProcessPoint);
						}
					}
				};
				return cell;
			}
		});
		
		searchProcessPointTimestampColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> col) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String scannedProcessPointTimestamp, boolean empty) {
						super.updateItem(scannedProcessPointTimestamp, empty);
						if (scannedProcessPointTimestamp == null || scannedProcessPointTimestamp.equals("null")) {
							setText("");
						} else {
							setText(scannedProcessPointTimestamp.toString());
						}
					}
				};
				return cell;
			}
		});

		getView().getScannedPrdTable().getTable().setRowFactory(new Callback<TableView<ProductSearchResult>, TableRow<ProductSearchResult>>() {

			@Override
			public TableRow<ProductSearchResult> call(TableView<ProductSearchResult> tableView) {
				final TableRow<ProductSearchResult> row = new TableRow<ProductSearchResult>() {
					@Override
					protected void updateItem(ProductSearchResult result, boolean empty) {
						if(result != null && result.getDefectStatus() != null) {
							if(result.getDefectStatus().name().equals(DefectStatus.OUTSTANDING.name())
									|| result.getDefectStatus().name().equals(DefectStatus.NOT_REPAIRED.name())
									|| result.getDefectStatus().name().equals(DefectStatus.NOT_FIXED.name())) {
								setStyle("-fx-text-background-color: #F57F17 ;");
							}else if (result.getDefectStatus().name().equals(DefectStatus.FIXED.name())) {
								setStyle("-fx-text-background-color: #2ecc71 ;");
							}else setStyle("");
						}else setStyle("");
						super.updateItem(result, empty);
					}

				};

				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						getView().getScannedPrdTable().getTable().requestFocus();
						int selectedIndex = getView().getScannedPrdTable().getTable().getSelectionModel().getSelectedIndex();
						if(e.getButton() == MouseButton.PRIMARY) {
							final int index = row.getIndex();
							//Add SHIFT + Mouse Click to do the range select
							if(selectedIndex >=0 && index >=0 && selectedIndex != index && e.isShiftDown()) {
								getView().getScannedPrdTable().getTable().getSelectionModel().selectRange(selectedIndex, index);
								getView().getScannedPrdTable().getTable().getSelectionModel().select(index);
							}else {
								if(index >= 0
										&& index < getView().getScannedPrdTable().getTable().getItems().size()
										&& getView().getScannedPrdTable().getTable().getSelectionModel().isSelected(index)) {
									getView().getScannedPrdTable().getTable().getSelectionModel().clearSelection(index);								

								} else {
									getView().getScannedPrdTable().getTable().getSelectionModel().select(index);
								}
							}
							e.consume();
						}
					}
				});

				getView().getRemoveAllMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						getView().getScannedPrdTable().getTable().getItems().clear();
						getView().getScannedPrdTable().getTable().getSelectionModel().clearSelection();
						getView().getScannedProductsTab().setText("Scanned Products(0)");
					}
				});

				getView().getRemoveSelectedMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						ObservableList<ProductSearchResult> products = getView().getScannedPrdTable().getSelectedItems();
						clearScannedProductsTable(products);
					}
				});

				getView().getProcessAllMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						processProducts();
					}
				});

				getView().getProcessSelectedMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ProductSearchResult> products = getView().getScannedPrdTable().getSelectedItems();

						processProducts(products);
						getProductModel().setProcessedFromScrappedTable(false);
					}
				});

				getView().getDeselectMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						getView().getScannedPrdTable().getTable().getSelectionModel().clearSelection();
					}
				});
				
				getView().getExportAllMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						exportTable();
					}
				});

				getView().getExportSelectedMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ProductSearchResult> products = getView().getScannedPrdTable().getSelectedItems();

						exportTable(products);
					}
				});

				getView().getUpdateAllTrackingMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ProductSearchResult> products = getView().getScannedPrdTable().getTable().getItems();
						updateTrackingStatus(products);
					}
				});

				getView().getUpdateTrackingMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ProductSearchResult> products = getView().getScannedPrdTable().getSelectedItems();
						updateTrackingStatus(products);
					}
				});

				row.contextMenuProperty().bind(
						Bindings.when(row.emptyProperty())
						.then((ContextMenu)null)
						.otherwise(getView().getScannedProductsTableMenu())
						);

				return row;
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addScrappedProductsTableListener() {
		ObservableList<TableColumn<ProductSearchResult, ?>> columns = getView().getScrappedPrdTable().getTable().getColumns();
		TableColumn productIdColumn;
		TableColumn mcSerialNumberColumn;
		TableColumn dcSerialNumberColumn;
		TableColumn ymtoColumn;
		TableColumn trackingStatusColumn;
		TableColumn defectStatusColumn;
		TableColumn kickoutStatusColumn;
		TableColumn searchProcessPointColumn;
		TableColumn searchProcessPointTimestampColumn;

		if(isDieCastProduct()) {
			mcSerialNumberColumn = columns.get(QiConstant.MC_SERIAL_NUMBER_COLUMN);
			dcSerialNumberColumn = columns.get(QiConstant.DC_SERIAL_NUMBER_COLUMN);
			ymtoColumn = columns.get(QiConstant.PRODUCT_SPEC_CODE_COLUMN_DC);
			trackingStatusColumn = columns.get(QiConstant.TRACKING_STATUS_COLUMN_DC);
			defectStatusColumn = columns.get(QiConstant.DEFECT_STATUS_COLUMN_DC);
			kickoutStatusColumn = columns.get(QiConstant.KICKOUT_STATUS_COLUMN_DC);
			searchProcessPointColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_COLUMN_DC);
			searchProcessPointTimestampColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_TIMESTAMP_COLUMN_DC);
			
			mcSerialNumberColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
				@Override
				public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
					final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
						@Override
						public void updateItem(String mcSerialNumber, boolean empty) {
							super.updateItem(mcSerialNumber, empty);
							if(mcSerialNumber == null || mcSerialNumber.equals("null")) {
								setText("");
							} else {
								setText(mcSerialNumber);
							}
						}
					};
					return cell;
				}
			});

			dcSerialNumberColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
				@Override
				public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> args) {
					final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
						@Override
						public void updateItem(String mcSerialNumber, boolean empty) {
							super.updateItem(mcSerialNumber, empty);
							if(mcSerialNumber == null || mcSerialNumber.equals("null")) {
								setText("");
							} else {
								setText(mcSerialNumber);
							}
						}
					};
					return cell;
				}
			});
			
		} else {
			productIdColumn = columns.get(QiConstant.PRODUCT_ID_COLUMN);
			ymtoColumn = columns.get(QiConstant.PRODUCT_SPEC_CODE_COLUMN);
			trackingStatusColumn = columns.get(QiConstant.TRACKING_STATUS_COLUMN);
			defectStatusColumn = columns.get(QiConstant.DEFECT_STATUS_COLUMN);
			kickoutStatusColumn = columns.get(QiConstant.KICKOUT_STATUS_COLUMN);
			searchProcessPointColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_COLUMN);
			searchProcessPointTimestampColumn = columns.get(QiConstant.SEARCH_PROCESS_POINT_TIMESTAMP_COLUMN);

			productIdColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
				@Override
				public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> args) {
					final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
						@Override
						public void updateItem(String productId, boolean empty) {
							super.updateItem(productId, empty);
							if(productId == null || productId.equals("null")) {
								setText("");
							} else {
								setText(productId);
							}
						}
					};
					return cell;
				}
			});
		}

		
		ymtoColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String ymto, boolean empty) {
						super.updateItem(ymto, empty);
						if(ymto == null || ymto.equals("null")) {
							setText("");
						} else {
							setText(ymto);
						}
					}
				};
				return cell;
			}
		});

		trackingStatusColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String trackingStatus, boolean empty) {
						super.updateItem(trackingStatus, empty);
						if(trackingStatus == null || trackingStatus.equals("null")) {
							setText("");
						} else {
							setText(trackingStatus);
						}
					}
				};
				return cell;
			}
		});
		
		defectStatusColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String defectStatus, boolean empty) {
						super.updateItem(defectStatus, empty);
						if(defectStatus == null || defectStatus.equals("null")) {
							setText("");
						} else {
							setText(defectStatus);
						}
					}
				};
				return cell;
			}
		});
	
		kickoutStatusColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String kickoutStatus, boolean empty) {
						super.updateItem(kickoutStatus, empty);
						if(kickoutStatus == null || kickoutStatus.equals("null")) {
							setText("");
						} else {
							setText(kickoutStatus);
						}
					}
				};
				return cell;
			}
		});
		
		searchProcessPointColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String searchProcessPoint, boolean empty) {
						super.updateItem(searchProcessPoint, empty);
						if(searchProcessPoint == null || searchProcessPoint.equals("null")) {
							setText("");
						} else {
							setText(searchProcessPoint);
						}
					}
				};
				return cell;
			}
		});
		
		searchProcessPointTimestampColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String searchProcessPointTimestamp, boolean empty) {
						super.updateItem(searchProcessPointTimestamp, empty);
						if(searchProcessPointTimestamp == null || searchProcessPointTimestamp.equals("null")) {
							setText("");
						} else {
							setText(searchProcessPointTimestamp);
						}
					}
				};
				return cell;
			}
		});
		
		getView().getScrappedPrdTable().getTable().setRowFactory(new Callback<TableView<ProductSearchResult>, TableRow<ProductSearchResult>>() {
			@Override
			public TableRow<ProductSearchResult> call(TableView<ProductSearchResult> tableView) {
				final TableRow<ProductSearchResult> row = new TableRow<ProductSearchResult>() {
					@Override
					protected void updateItem(ProductSearchResult result, boolean empty) {
						if(result != null && result.getDefectStatus() != null) {
							if (result.getDefectStatus().name().equals(DefectStatus.NON_REPAIRABLE.name())) {
								setStyle("-fx-text-background-color: #fc5c65 ;");
							}
						} else setStyle("");
						super.updateItem(result, empty);
					}
				};

				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						getView().getScrappedPrdTable().getTable().requestFocus();
						if(e.getButton() == MouseButton.PRIMARY) {
							final int index = row.getIndex();
							if(index >= 0
									&& index < getView().getScrappedPrdTable().getTable().getItems().size()
									&& getView().getScrappedPrdTable().getTable().getSelectionModel().isSelected(index)) {
								getView().getScrappedPrdTable().getTable().getSelectionModel().clearSelection(index);

							} else {
								getView().getScrappedPrdTable().getTable().getSelectionModel().select(index);
							}
							e.consume();
						}
					}
				});

				getView().getRemoveAllMenuItemScrap().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						getView().getScrappedPrdTable().getTable().getItems().clear();
						getView().getScrappedPrdTable().getTable().getSelectionModel().clearSelection();
						getView().getScrappedProductsTab().setText("Scrapped Products(0)");
					}
				});

				getView().getRemoveSelectedMenuItemScrap().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						ObservableList<ProductSearchResult> products = getView().getScrappedPrdTable().getSelectedItems();
						clearScrappedProductsTable(products);
					}
				});

				getView().getProcessAllMenuItemScrap().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						processProducts();
					}
				});

				getView().getProcessSelectedMenuItemScrap().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ProductSearchResult> products = getView().getScrappedPrdTable().getSelectedItems();

						processProducts(products);
						getProductModel().setProcessedFromScrappedTable(true);
					}
				});

				getView().getDeselectMenuItemScrap().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						getView().getScrappedPrdTable().getTable().getSelectionModel().clearSelection();
					}
				});
				
				getView().getExportAllMenuItemScarp().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						exportTable();
					}
				});
				
				getView().getExportSelectedMenuItemScrap().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ProductSearchResult> products = getView().getScrappedPrdTable().getSelectedItems();

						exportTable(products);
					}
				});
				
				row.contextMenuProperty().bind(
						Bindings.when(row.emptyProperty())
						.then((ContextMenu)null)
						.otherwise(getView().getScrappedProductsTableMenu())
						);

				return row;
			}
		});
	}

	public void clearScannedProductsTable(List<ProductSearchResult> products) {
		getView().getScannedPrdTable().getTable().getItems().removeAll(products);
		getView().getScannedPrdTable().getTable().getSelectionModel().clearSelection();
		List<ProductSearchResult> remainingProducts = getView().getScannedPrdTable().getTable().getItems();
		int count = remainingProducts == null ? 0 : remainingProducts.size();
		getView().getScannedProductsTab().setText("Scanned Products(" + count + ")");
	}
	
	public void clearScrappedProductsTable(List<ProductSearchResult> products) {
		getView().getScrappedPrdTable().getTable().getItems().removeAll(products);
		getView().getScrappedPrdTable().getTable().getSelectionModel().clearSelection();
		List<ProductSearchResult> remainingProducts = getView().getScrappedPrdTable().getTable().getItems();
		int count = remainingProducts == null ? 0 : remainingProducts.size();
		getView().getScrappedProductsTab().setText("Scrapped Products(" + count + ")");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addNonProcessableProductsTableListener() {
		ObservableList<TableColumn<ProductSearchResult, ?>> columns = getView().getNonProcessablePrdTable	().getTable().getColumns();
		
		TableColumn mcSerialNumberColumn = columns.get(QiConstant.MC_SERIAL_NUMBER_COLUMN);
		TableColumn dcSerialNumberColumn = columns.get(QiConstant.DC_SERIAL_NUMBER_COLUMN);
		TableColumn ymtoColumn = columns.get(QiConstant.PRODUCT_SPEC_CODE_COLUMN_DC);
		TableColumn trackingStatusColumn = columns.get(QiConstant.TRACKING_STATUS_COLUMN_DC);

		mcSerialNumberColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String mcSerialNumber, boolean empty) {
						super.updateItem(mcSerialNumber, empty);
						if(mcSerialNumber == null || mcSerialNumber.equals("null")) {
							setText("");
						} else {
							setText(mcSerialNumber);
						}
					}
				};
				return cell;
			}
		});

		dcSerialNumberColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> args) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String dcSerialNumber, boolean empty) {
						super.updateItem(dcSerialNumber, empty);
						if(dcSerialNumber == null || dcSerialNumber.equals("null")) {
							setText("");
						} else {
							setText(dcSerialNumber);
						}
					}
				};
				return cell;
			}
		});

		ymtoColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String ymto, boolean empty) {
						super.updateItem(ymto, empty);
						if(ymto == null || ymto.equals("null")) {
							setText("");
						} else {
							setText(ymto);
						}
					}
				};
				return cell;
			}
		});

		trackingStatusColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String trackingStatus, boolean empty) {
						super.updateItem(trackingStatus, empty);
						if(trackingStatus == null || trackingStatus.equals("null")) {
							setText("");
						} else {
							setText(trackingStatus);
						}
					}
				};
				return cell;
			}
		});

		mcSerialNumberColumn.setCellFactory(new Callback<TableColumn<ProductSearchResult, String>, TableCell<ProductSearchResult, String>>() {
			@Override
			public TableCell<ProductSearchResult, String> call(TableColumn<ProductSearchResult, String> arg0) {
				final TableCell<ProductSearchResult, String> cell = new TableCell<ProductSearchResult, String>() {
					@Override
					public void updateItem(String mcSerialNumber, boolean empty) {
						super.updateItem(mcSerialNumber, empty);
						if(mcSerialNumber == null || mcSerialNumber.equals("null")) {
							setText("");
						} else {
							setText(mcSerialNumber);
						}
					}
				};
				return cell;
			}
		});
		getView().getNonProcessablePrdTable().getTable().setRowFactory(new Callback<TableView<ProductSearchResult>, TableRow<ProductSearchResult>>() {

			@Override
			public TableRow<ProductSearchResult> call(TableView<ProductSearchResult> tableView) {
				return new TableRow<ProductSearchResult>() {
					@Override
					protected void updateItem(ProductSearchResult result, boolean empty) {

						if(result != null && result.getDefectStatus() != null) {
							if(result.getDefectStatus().name().equals(DefectStatus.OUTSTANDING.name())
									|| result.getDefectStatus().name().equals(DefectStatus.NOT_REPAIRED.name())
									|| result.getDefectStatus().name().equals(DefectStatus.NOT_FIXED.name())) {

								setStyle("-fx-text-background-color: #F57F17 ;");
							}else if (result.getDefectStatus().name().equals(DefectStatus.FIXED.name())) {
								setStyle("-fx-text-background-color: #2ecc71 ;");
							}else if (result.getDefectStatus().name().equals(DefectStatus.NON_REPAIRABLE.name())) {
								setStyle("-fx-text-background-color: #fc5c65 ;");
							}else setStyle("");
						}else setStyle("");
						super.updateItem(result, empty);
					}

				};
			}
		});
	}

	private void addSignedInCheckBoxListner() {
		getView().getKeepSignedInChkBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				if(new_val){
					ClientMainFx.getInstance().stopActivityListener();
				}else{
					ClientMainFx.getInstance().startActivityListener();
				}
			}
		});	
	}

	/**
	 * This method is used to add Entry Dept Combobox Listener
	 */
	private void addEntryDeptComboboxListener() {
		getView().getEntryDeptComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiStationEntryDepartment>() {
			@Override
			public void changed(
					ObservableValue<? extends QiStationEntryDepartment> arg0,
					QiStationEntryDepartment oldValue, QiStationEntryDepartment newValue) {
				clearMessage();
				if (newValue != null) {
					getApplicationContext().setEntryDept(newValue.getId().getDivisionId());
					EventBusUtil.publish(new EntryDepartmentEvent(newValue.getId().getDivisionId(), QiConstant.ENTRY_DEPT_SELECTED));
				}
			}
		});
	}

	/**
	 * This method is used to add Entry Dept Combobox Listener
	 */
	private void addEntryDept2ComboboxListener() {
		getView().getEntryDeptComboBox2().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiStationEntryDepartment>() {
			@Override
			public void changed(
					ObservableValue<? extends QiStationEntryDepartment> arg0,
					QiStationEntryDepartment oldValue, QiStationEntryDepartment newValue) {
				clearMessage();
				getApplicationContext().setEntryDept2(newValue.getId().getDivisionId());
				EventBusUtil.publish(new EntryDepartmentEvent(newValue.getId().getDivisionId(), QiConstant.ENTRY_DEPT2_SELECTED));
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void addProcessedProductTableListner() {

		ObservableList<TableColumn<QiDefectResultDto, ?>> columns  = getView().getProcessedPrdTable().getTable().getColumns();
		TableColumn productIdColumn  = columns.get(1);

		productIdColumn.setCellFactory(new Callback<TableColumn<QiDefectResultDto, String>, TableCell<QiDefectResultDto, String>>() {
			@Override
			public TableCell<QiDefectResultDto, String> call(TableColumn<QiDefectResultDto, String> col) {
				final TableCell<QiDefectResultDto, String> cell = new TableCell<QiDefectResultDto, String>() {
					@Override
					public void updateItem(String firstName, boolean empty) {
						super.updateItem(firstName, empty);
						if (empty) {
							setText(null);
						} else {
							setText(firstName);
						}
					}
				};
				cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getClickCount() > 1) {
							if(null!=getView().getEntryDeptComboBox().getSelectionModel().getSelectedItem()){
								EventBusUtil.publish(new ProductEvent(getView().getProcessedPrdTable().getSelectedItem().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
							}else{
								displayErrorMessage("Please select Entry Department");
							}

						}
					}
				});
				return cell ;
			}
		});	
	}

	public void addTabChangeListener(){
		getView().getTabbedPane().getSelectionModel().selectedItemProperty().addListener(listener);
	}

	ChangeListener<Tab> listener= new ChangeListener<Tab>() {
		@Override
		public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

			if(newValue != null && newValue.getId() != null){
				Logger.getLogger().check("ProductSequence/ProcessedProduct:: " + newValue.getText() + " selected");

				if(newValue.getId().equals(getView().getProcessedTab().getId())){
					addProcessedProductData();
					getView().getSubmitProductsButton().setDisable(true);
					getView().getRemoveProductsButton().setDisable(true);
					getView().getExportTableButton().setDisable(true);
					getView().getFileChooserButton().setDisable(true);
				}
				else if(newValue.getId().equals(getView().getScannedProductsTab().getId())){
					updateButtonStates();

				} else if(newValue.getId().equals(getView().getScrappedProductsTab().getId())){
					updateButtonStatesForScrappedProducts();

				}else if(newValue.getId().equals(getView().getNonProcessableProductsTab().getId())){
					getView().getNonProcessableProductsTab().setStyle(getView().getTabStyleSmaller());
					getView().getSubmitProductsButton().setDisable(true);
					getView().getExportTableButton().setDisable(true);
					getView().getFileChooserButton().setDisable(true);

					if(getView().getNonProcessablePrdTable().getTable().getItems().isEmpty()) {
						getView().getRemoveProductsButton().setDisable(true);
					} else {
						getView().getRemoveProductsButton().setDisable(false);
						getView().getExportTableButton().setDisable(false);
					}
				} else {
					getView().getSubmitProductsButton().setDisable(true);
					getView().getRemoveProductsButton().setDisable(true);
					getView().getExportTableButton().setDisable(true);
				}
			}
		}
	};

	/**
	 * This method is used to keep associate id selected when we return to home screen
	 */
	public void keepUserSignedIn() {
		if(isKeepMeSignedInSelected) {
			String userId = getView().getMainWindow().getUserId();
			getView().getKeepSignedInChkBox().setSelected(true);
			for(StationUser user : getView().getAssociateTable().getTable().getItems()) {
				if(user.getId().getUser().equalsIgnoreCase(userId)) {
					getView().getAssociateTable().getTable().getSelectionModel().select(user);
					userId = user.getId().getUser();
					break;
				}
			}
			EventBusUtil.publish(new EntryDepartmentEvent(userId, QiConstant.ASSOCIATE_ID_SELECTED));
		} else {
			EventBusUtil.publish(new EntryDepartmentEvent(null, QiConstant.ASSOCIATE_ID_SELECTED));
		}
	}

	private void updateButtonStates() {
		getView().getScannedProductsTab().setStyle(getView().getTabStyleSmaller());

		if(getView().getScannedPrdTable().getTable().getItems().isEmpty()) {
			getView().getSubmitProductsButton().setDisable(true);
			getView().getRemoveProductsButton().setDisable(true);
			getView().getExportTableButton().setDisable(true);
			getView().getFileChooserButton().setDisable(false);
		} else {
			getView().getSubmitProductsButton().setDisable(false);
			getView().getRemoveProductsButton().setDisable(false);
			getView().getExportTableButton().setDisable(false);
			getView().getFileChooserButton().setDisable(false);
		}
	}
	
	private void updateButtonStatesForScrappedProducts() {
		getView().getScrappedProductsTab().setStyle(getView().getTabStyleSmaller());

		if(getView().getScrappedPrdTable().getTable().getItems().isEmpty()) {
			getView().getSubmitProductsButton().setDisable(true);
			getView().getRemoveProductsButton().setDisable(true);
			getView().getExportTableButton().setDisable(true);
			getView().getFileChooserButton().setDisable(true);
		} else {
			getView().getSubmitProductsButton().setDisable(false);
			getView().getRemoveProductsButton().setDisable(false);
			getView().getExportTableButton().setDisable(false);
			getView().getFileChooserButton().setDisable(true);
		}
	}

	private int getMaxListSize()  {

		String maxListSz = "";
		int maxSize = 0;
		QiStationConfiguration qiStationConfigEntry = getModel().findPropertyKeyValueByProcessPoint(
				getProcessPointId(), QiEntryStationConfigurationSettings.MAX_PROCESSED_LIST_SZ.getSettingsName());
		if(qiStationConfigEntry != null && qiStationConfigEntry.getPropertyValue() != null)  {
			maxListSz = qiStationConfigEntry.getPropertyValue();
		}
		if(StringUtils.isBlank(maxListSz))  {
			maxListSz = QiEntryStationConfigurationSettings.MAX_PROCESSED_LIST_SZ.getDefaultPropertyValue();				
		}
		try {
			maxSize = Integer.parseInt(maxListSz);
		} catch (NumberFormatException e) {
			maxSize = MAX_LIST_SZ;
		}
		return maxSize;
	}

	private StringConverter<QiStationEntryDepartment> qiDivisionConverter() {
		return new StringConverter<QiStationEntryDepartment>() {

			@Override
			public QiStationEntryDepartment fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(QiStationEntryDepartment arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getId().getDivisionId() + "-" + arg0.getDivision().getDivisionName();
				}
			}
		};
	}

	private boolean isProductScrapped(QiDefectResultDto defectResultDto) {
		boolean isProductScraped = false;
		List<QiDefectResult>  defectResults =  getModel().getCurrentDefectStatus(defectResultDto.getProductId());
		for (QiDefectResult dto : defectResults) {
			if (DefectStatus.NON_REPAIRABLE.getId() == dto.getCurrentDefectStatus()) {
				isProductScraped = true;
				break;
			}
		}
		return isProductScraped;
	}

	// === events ===//
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if(null !=event) {
			List<String> productIdsMayBeDuplicates = new ArrayList<String>();
			List<String> productIds = new ArrayList<String>();

			if (event.getEventType().equals(ProductEventType.PRODUCT_INPUT_RECIEVED)) {

				if(event.getTargetObject() instanceof String) {
					String productId = (String) event.getTargetObject();
					if(productId != null) {
						productIds.add(productId);
					}					
				} else if(event.getTargetObject() instanceof List<?> && !((List<?>)event.getTargetObject()).isEmpty())  {
					if(((List) event.getTargetObject()).get(0) instanceof String) {
						productIdsMayBeDuplicates = (List<String>) event.getTargetObject();
						Set<String> uniqueProductIds = new HashSet<String>(productIdsMayBeDuplicates);
						for (String uniqueproductId : uniqueProductIds) {
							productIds.add(uniqueproductId) ;
						}
					
					} else {
						searchByProcessDtoList = (List<SearchByProcessDto>) event.getTargetObject();
						productIdsMayBeDuplicates = searchByProcessDtoList.stream().map(e -> e.getProductId()).collect(Collectors.toList());
						Set<String> uniqueProductIds = new HashSet<String>(productIdsMayBeDuplicates);
						for (String uniqueproductId : uniqueProductIds) {
							productIds.add(uniqueproductId) ;
						}
					}
				}

				if(!productIds.isEmpty()) {
					EventBusUtil.publish(new ProductEvent(productIds, ProductEventType.PRODUCT_INPUT_PROCESS));
				}
			} else if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
				ProductModel productModel = (ProductModel) event.getTargetObject();
				List<ProductSearchResult> products = removeDuplicateResults(productModel.getProductList());

				for(ProductSearchResult result : products) {
					for(SearchByProcessDto dto : searchByProcessDtoList) {
						if(StringUtils.trim(result.getProduct().getProductId()).equals(StringUtils.trim(dto.getProductId()))) {
							result.setSearchProcessPointId(dto.getProcessPointId());
							result.setSearchPpidTimestamp(dto.getTimestamp());
						}
					}
				}
				searchByProcessDtoList.clear();
				addProductsToTable(products);
				clearMessage();
			} 
			else if(event.getEventType().equals(ProductEventType.PRODUCT_UPDATE_TRACKING)) {
				List<ProductSearchResult> searchResults = getView().getScannedPrdTable().getTable().getItems();
				for(ProductSearchResult r : searchResults)  {
					productIds.add(r.getProduct().getProductId());
				}
				clearScannedProductsTable();
				ProductEvent pEv = new ProductEvent(productIds, ProductEventType.PRODUCT_INPUT_PROCESS);
				pEv.addArgument(ObservableListChangeEventType.CLEAR, true);
				EventBusUtil.publish(pEv);
			}		
		}
	}
	
	private List<ProductSearchResult> removeDuplicateResults(List<ProductSearchResult> searchResults) {
		List<String> productsInTable = getView().getScannedPrdTable().getTable().getItems().stream()
				.map(n -> n.getProduct().getProductId()).collect(Collectors.toList());

		List<String> productsInScrappedTable = getView().getScrappedPrdTable().getTable().getItems().stream()
				.map(n -> n.getProduct().getProductId()).collect(Collectors.toList());

		productsInTable.addAll(productsInScrappedTable);
		
		List<String> duplicateProductIds = new ArrayList<String>();
		List<String> idsToBeAdded = new ArrayList<String>();
		List<ProductSearchResult> productsToBeAdded = new ArrayList<ProductSearchResult>();
		
		for(ProductSearchResult result : searchResults) {
			String productId = result.getProduct().getProductId();
			if(productsInTable.contains(productId) || idsToBeAdded.contains(productId)) {
				duplicateProductIds.add(productId);
			} else {
				productsToBeAdded.add(result);
				idsToBeAdded.add(productId);
			}
		}
			
		if (!duplicateProductIds.isEmpty()) {
			getLogger().info(
					"The following products have already been added to table : " + duplicateProductIds.toString());
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					MessageDialog.showScrollingInfo(getView().getStage(),
							"The following products have already been added to table : "
									+ duplicateProductIds.toString());
				}
			});
		}
		return productsToBeAdded;
	}

	@Subscribe
	public void received(ProductFinishedEvent event) {
		ProductSearchResult.removeProcessedProducts(event.getProductModel().getProcessedProducts());
		if(getProductModel().isProcessedFromScrappedTable()) {
			clearScrappedProductsTable(event.getProductModel().getProductList());
			updateButtonStatesForScrappedProducts();
		}else {
			clearScannedProductsTable(event.getProductModel().getProductList());
			updateButtonStates();
		}
	}

	public QiPropertyBean getProperty() {
		if(property == null) {
			property= PropertyService.getPropertyBean(QiPropertyBean.class, getProcessPointId());
		}
		return property;
	}
	
	private void exportTable() {
		if(getView().getScannedProductsTab().isSelected()) 
			exportTable(getView().getScannedPrdTable().getTable().getItems());
		if(getView().getScrappedProductsTab().isSelected()) 
			exportTable(getView().getScrappedPrdTable().getTable().getItems());	
		if(getView().getNonProcessableProductsTab().isSelected()) 
			exportTable(getView().getNonProcessablePrdTable().getTable().getItems());	
	}
	
	/**
	 * This method is used to export product information from Scanned Products table or Scrapped Product table to a CSV file
	 * @param event
	 */
	private void exportTable(List<ProductSearchResult> results) {
		String fileName = getFileName();
		if(fileName != null) {
			File file = new File(fileName);
			FileWriter fileWriter = null;
			BufferedWriter buffWriter = null;
			try {
				fileWriter = new FileWriter(file);
				buffWriter =new BufferedWriter(fileWriter);
				String header = null;
				//Write Header
				if(isDieCastProduct()) {
					if(isProductDunnagable()) {
						header ="Product Id,DC Serial Number,MC Serial Number,Product Spec Code,Dunnage,Tracking Status,Defect Status,Kickout Location,Last Passing Process Point,Last Passing Process Point Timestamp,Searched Process Point,Searched ProcessPoint Timestamp\n";
					}else {
						header ="Product Id,DC Serial Number,MC Serial Number,Product Spec Code,Tracking Status,Defect Status,Kickout Location,Last Passing Process Point,Last Passing Process Point Timestamp,Searched Process Point,Searched ProcessPoint Timestamp\n";
					}
				} else {
					if(isProductDunnagable()) {
						header ="Product Id,Product Spec Code,Dunnage,Tracking Status,Defect Status,Kickout Location,Last Passing Process Point,Last Passing Process Point Timestamp,Searched Process Point,Searched ProcessPoint Timestamp\n";
					}else {
						header ="Product Id,Product Spec Code,Tracking Status,Defect Status,Kickout Location,Last Passing Process Point,Last Passing Process Point Timestamp,Searched Process Point,Searched ProcessPoint Timestamp\n";
					}
				}
				buffWriter.write(header);
				for(ProductSearchResult result : results) {
					//Write the content to csv file
					StringBuilder rowSb = new StringBuilder();

					rowSb.append(getTableExportCellContents(result.getProduct().getProductId())).append(",");
					if(isDieCastProduct()) {
						rowSb.append(getTableExportCellContents(((DieCast) result.getProduct()).getDcSerialNumber())).append(",");
						rowSb.append(getTableExportCellContents(((DieCast) result.getProduct()).getMcSerialNumber())).append(",");
					}
					rowSb.append(getTableExportCellContents(result.getProduct().getProductSpecCode())).append(",");
					if(isProductDunnagable()) {
						rowSb.append(getTableExportCellContents(result.getProduct().getDunnage())).append(",");
					}
					rowSb.append(getTableExportCellContents(result.getProduct().getTrackingStatus())).append(",");
					rowSb.append(result.getDefectStatus() == null? "" : result.getDefectStatus().name()).append(",");
					rowSb.append(getTableExportCellContents(result.getKickoutProcessPointName())).append(",");
					ProcessPoint processPoint = null;
					String processPointName = "";
					if(result.getProduct().getLastPassingProcessPointId() != null) {
						processPoint = getModel().findProcessPoint(getTableExportCellContents(result.getProduct().getLastPassingProcessPointId()));
						processPointName = processPoint == null ? "" : processPoint.getProcessPointName();
					}
					rowSb.append(processPointName).append(",");
					String timestamp = "";
					if(result.getProduct().getUpdateTimestamp() != null) {
						timestamp = getTableExportCellContents(result.getFormattedProductUpdateTimestamp());
					}
					
					rowSb.append(getTableExportCellContents(timestamp)).append(",");
					rowSb.append(getTableExportCellContents((String) result.getSearchProcessPointId())).append(",");
					rowSb.append(getTableExportCellContents(result.getFormattedSearchPpIdTimestamp())).append("\n");
					

					buffWriter.write(rowSb.toString().replaceAll("null", ""));		
				}
				EventBusUtil.publish(new StatusMessageEvent("Exported Products to CSV file Successfully ", StatusMessageEventType.INFO));
			} catch (IOException e){
				e.printStackTrace();
				getLogger().error("Export data To CSV file error: " + e);
			} finally {
				try {
					buffWriter.close();
					fileWriter.close();			
				} catch (IOException e) {
					e.printStackTrace();
					getLogger().error("Export data To CSV file error: " + e);
				}
			}
		}
	}
	
	private String getTableExportCellContents(String  contents) {
		return contents == null ? "" : contents;
	}
	
	/*
	 * This method is allow user to type the export CSV file name
	 */
	private String getFileName() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(File.listRoots()[0]);
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
		File file = fileChooser.showSaveDialog(null);
		return file == null ? null : file.getAbsolutePath();
	}

	private void setTrainingMode(ActionEvent actionEvent) {
		LoggedLabel trainingModel = UiFactory.createLabel("trainingModel", "TRAINING MODE(Collected data won't be saved)");
		trainingModel.setMaxWidth(Double.MAX_VALUE);
		trainingModel.setStyle("-fx-font-size: 24; -fx-font-weight: bolder; -fx-text-fill: white; -fx-background-color:blue");
		trainingModel.setAlignment(Pos.CENTER);
		
		FadeTransition ft = new FadeTransition(Duration.millis(500), trainingModel);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(10);
        ft.setAutoReverse(true);
        
        ft.play();
        
        getView().getEnterTrainingModeButton().setVisible(false);
        getView().getExitTrainingModeButton().setVisible(true);
        node = getView().getMainWindow().getStatusMessagePane().getBottom();
        getView().getMainWindow().getStatusMessagePane().setBottom(trainingModel);
        if(getProductModel().getCachedDefectsForTraingMode() != null  ) {
			getProductModel().getCachedDefectsForTraingMode().clear();
		}
        getProductModel().setTrainingMode(true);   
        EventBusUtil.publish(new TrainingModeEvent(true, QiConstant.TRAINING_MODE_ON));
		
	}
	
	private void setExitTrainingMode(ActionEvent actionEvent){
		getView().getEnterTrainingModeButton().setVisible(true);
		getView().getExitTrainingModeButton().setVisible(false);
		getView().getMainWindow().getStatusMessagePane().setBottom(node);
		if(AbstractRepairEntryView.getParentCachedDefectList() != null)
			AbstractRepairEntryView.getParentCachedDefectList().clear();
		if(getProductModel().getCachedDefectsForTraingMode() != null  ) {
			getProductModel().getCachedDefectsForTraingMode().clear();
		}
		//Clear product search result tables
		clearScannedProductsTable();
		clearNonProcessableProductsTable();
		clearScrappedProductsTable();
		
		getProductModel().setTrainingMode(false);
		EventBusUtil.publish(new TrainingModeEvent(true, QiConstant.TRAINING_MODE_OFF));
	}
	
	public boolean isDieCastProduct() {
		return ProductTypeUtil.isDieCast(getProductType());
	}
	
	public boolean isFrameProduct() {
		return ProductTypeUtil.isFrameProduct(getProductType());
	}
	
	public boolean isProductDunnagable() {
		return ProductTypeUtil.isDunnagable(getProductType());
	}
	
	public ProductType getProductType(){
		return ProductTypeCatalog.getProductType(getApplicationContext().getProductTypeData().getProductTypeName());
	}
	
	public boolean isAuthorizedForTracking()  {
		
		boolean isAuth = false;
		String trackingAuthRole = getProperty().getTrackingAuthGroup();
		if(!StringUtils.isBlank(trackingAuthRole))  {
			String[] authRoles = trackingAuthRole.split(",");
			List<String> roleList = new ArrayList<String>();
			if(authRoles != null && authRoles.length > 0)  {
				roleList = Arrays.asList(authRoles);
			}
			for(String role : roleList)  {
				isAuth = ClientMainFx.getInstance().getAccessControlManager().isAuthorized(role);
				if(isAuth)  break;
			}
		}
		return isAuth;
	}
}