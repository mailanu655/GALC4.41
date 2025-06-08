package com.honda.galc.client.teamleader.qi.productRecovery;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.ManualLotControlRepairConstants;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.InstalledPartShipStatus;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.datacollection.HeadlessDataMapping;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartNameVisibleType;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.property.ProductionInfoPropertyBean;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.LotControlRuleUtil;
import com.honda.galc.util.ProductResultUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * 
 * <h3>ManualLotControlRepairController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairController description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Aug 18, 2010
 *
 */
/**
 * 
 * @author L&T infotech
 * Aug 18, 2017
 * ver 2
 */
public abstract class ManualLotControlRepairController<T extends BaseProduct, B extends ProductBuildResult> 
implements EventHandler<ActionEvent>{
	protected ApplicationContext appContext;
	private ManualLotControlRepairPanel view;
	protected ManualLotControlRepairPropertyBean property;
	protected List<B> productBuildResulits;
	protected PartResult partResult;
	protected IManualLtCtrResultEnterViewManager resultEnterViewManager;
	protected List<PartResult> lotControlPartResultData;
	protected List<PartResult> displayLotControlPartResultData;
	protected boolean hasDuplicateParts;
	protected BaseProductSpec productSpec	;
	protected T product;
	private DeviceDao deviceDao;

	private String subId;
	private MainWindow mainWin;
	protected List<Object[]> rulesAndProcessPoints;
	protected abstract void loadProductBuildResults();
	private ProductType productType;
	protected ProductTypeData productTypeData;
	boolean shippingAndExceptionStatusFlag;
	boolean ownerShipped;
	private String selectedDivisionId;
	private String selectedLineId;
	private Set<String> terminalProcessPoints;
	private List<Division> divisions;
	private List<Line> lines;
	List<String> requiredPartShipProcessList = null;

	private static final String ALLOW_REPAIR_AFTER_SHIPPING = "ALLOW_REPAIR_AFTER_SHIPPING";


	public ManualLotControlRepairController(MainWindow mainWin, ManualLotControlRepairPanel 
			repairPanel, IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		this(mainWin, repairPanel);
		this.resultEnterViewManager = resultEnterViewManager;
	}

	public ManualLotControlRepairController(MainWindow mainWin, ManualLotControlRepairPanel repairPanel) {
		this.mainWin = mainWin;
		this.appContext = mainWin.getApplicationContext();
		this.view = repairPanel;
	}

	public void initialize() {
		productType = view.getProductType();
		property = view.getProperty();
		productTypeData = view.getProductTypeData();
		lotControlPartResultData = new ArrayList<PartResult>();
		selectedLineId = property.getLineId();
		selectedDivisionId = property.getDivisionId();
		terminalProcessPoints = new HashSet<String>();
		hasDuplicateParts = false;
		addPartStatusTableListener();
		requiredPartShipProcessList=Arrays.asList(getProperty().getRequiredPartsShipProcess());
	}

	private void addPartStatusTableListener() {	
		getView().getPartStatusPanel().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		getView().getPartStatusPanel().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PartResult>() {
			public void changed(ObservableValue<? extends PartResult> arg0, PartResult arg1, PartResult arg2) {
				partResult = getView().getPartStatusPanel().getSelectedItem();
				if(null != partResult){
					if(property.isEnableSetResultNg())
						getView().getSetResultButton().setDisable(false);
					if(property.isRepairEnabled() && partResult.isHeadLess() && partResult.isQuickFix())
						getView().getEnterMultiResultButton().setDisable(false);
					else
						getView().getEnterMultiResultButton().setDisable(true);
					getView().getEnterResultButton().setDisable(false);
					getView().getRemoveResultButton().setDisable(false);
				}
				else{
					getView().getSetResultButton().setDisable(true);
					getView().getEnterMultiResultButton().setDisable(true);
					getView().getEnterResultButton().setDisable(true);
					getView().getRemoveResultButton().setDisable(true);
				}
			}
		});
	}

	public void setResultNg() {
		try {
			if(isPartReadOnly())
			{
				MessageDialog.showInfo(getView().getStage(), "The selected part is Read Only and cannot modify the result for this Part.");
				return;
			}
			if(partResult.getBuildResult() == null) {
				ProductBuildResult buildResult = ProductTypeUtil.getTypeUtil(productType).createBuildResult(product.getProductId(),partResult.getPartName());
				partResult.setBuildResult(buildResult);
			}

			//set installed part status NG - currently support installed part only
			partResult.getBuildResult().setInstalledPartStatus(InstalledPartStatus.NG);
			partResult.getInstalledPart().setMeasurementsNg(getMeasurementCount(partResult));

			List<InstalledPart> resultList = new ArrayList<InstalledPart>();
			resultList.add((InstalledPart)partResult.getBuildResult());
			ProductResultUtil.saveAll(appContext.getApplicationId(),resultList);

		} catch (Exception e) {
			MessageDialog.showError(getView().getStage(), "Failed to delete data:" + e.getMessage());
		}

		loadProductBuildResultStatus(true);

	}

	protected void enterResult() {
		if(isPartReadOnly())
		{
			MessageDialog.showError(getView().getStage(),"The selected part is Read Only and cannot enter result for this Part.");
			return;
		}
		if(partResult.getBuildResult() != null &&
				partResult.getStatus() == InstalledPartStatus.OK &&
				isMeasurementStatusOk()){

			MessageDialog.showError(getView().getStage(), "The selected part installation status is ok, no need to repair.");
			Logger.getLogger().debug("The selected part installation status is ok, no need to repair dialog was displayed");
			return;
		}

		List<PartResult> toBeFixed = new ArrayList<PartResult>();
		toBeFixed.add(partResult);

		try {
			resultEnterViewManager.subScreenOpen(view, toBeFixed);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to open data input screen.");
			mainWin.setErrorMessage(e.getMessage());
		}
	}

	protected void enterMultipleResults() {
		if(partResult.getLotControlRule().getPartName().getPartVisible()==PartNameVisibleType.READ_ONLY.getId())
		{
			MessageDialog.showInfo(getView().getStage(), "The selected part is Read Only and cannot enter result for this Part.");
			return;
		}

		List<PartResult> allResults = getView().getPartStatusPanel().getTable().getItems();
		List<PartResult> toBeFixed = new ArrayList<PartResult>();
		for(PartResult pr : allResults) {
			if(pr.isHeadLess() && pr.isQuickFix() && partResult.getProcessPointId() != null
					&& partResult.getProcessPointId().equalsIgnoreCase(pr.getProcessPointId()) && !InstalledPartStatus.OK.equals(pr.getStatus())) {
				toBeFixed.add(pr);
			}
		}

		if(partResult.getBuildResult() != null &&
				partResult.getStatus() == InstalledPartStatus.OK &&
				isMeasurementStatusOk()){

			MessageDialog.showInfo(getView().getStage(), "The selected part installation status is ok, no need to repair.");
			Logger.getLogger().debug("The selected part installation status is ok, no need to repair dialog was displayed");
			return;
		}

		try {
			resultEnterViewManager.subScreenOpen(view, toBeFixed);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to open data input screen.");
			mainWin.setErrorMessage(e.getMessage());
		}
	}

	protected boolean isMeasurementStatusOk() {
		return (partResult.getLotControlRule().getParts().size() == 0 ||
				partResult.getLotControlRule().getParts().get(0).getMeasurementCount() == 0 ||
				MeasurementStatus.OK == partResult.getStatusMeasure());
	}

	protected void removePartResult() {
		if(isPartReadOnly())
		{
			MessageDialog.showError(getView().getStage(), "The selected part is Read Only and cannot remove result for this Part.");
			return;
		}
		if(partResult.getBuildResult() == null) {
			MessageDialog.showError(getView().getStage(), "No data to remove.");
			return;
		}

		if(!MessageDialog.confirm(getView().getStage(),"Confirm remove data ?")) return;

		try {
			deleteDefect();
			removeInstalledPart();
		} catch (Exception e) {
			MessageDialog.showError(getView().getStage(), "Failed to remove data:" + e.getMessage());
		}
		loadProductBuildResultStatus(true);
	}
	
	protected void deleteDefect() {
		//mark defect as deleted if existing
		if (partResult.getLotControlRule().isQicsDefect()) {
			List<String> productIdList = new ArrayList<String>();
			List<String> partNameList = new ArrayList<String>();
			productIdList.add(partResult.getBuildResult().getProductId());
			partNameList.add(partResult.getBuildResult().getPartName());
			List<Long> defectRefIds= ProductTypeUtil.getProductBuildResultDao(getProductType()).findDefectRefIds(productIdList, partNameList);
			try {
				ServiceFactory.getService(QiHeadlessDefectService.class).deleteDefect(QiExternalSystem.LOT_CONTROL.name(), defectRefIds.get(0));
			} catch (Exception ex) {
				Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
			}		
		}
	}

	protected void removeInstalledPart() {
		removeInstalledPart(partResult);
	}

	protected void removeInstalledPart(PartResult result) {
		if(result.getInstalledPart() == null || result.getInstalledPart().getMeasurements() == null)
			return;
		
		removeMeasurementData(result);

		removeInstalledPartData(result);
		
		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				System.getProperty("line.separator") + partResult.getInstalledPart());

	}

	protected void removeInstalledPartData(PartResult result) {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		result.getInstalledPart().setPartSerialNumber(null);
		result.getInstalledPart().setInstalledPartStatus(InstalledPartStatus.REMOVED);
		result.getInstalledPart().setActualTimestamp(null);
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		installedParts.add(result.getInstalledPart());
		installedPartDao.saveAll(installedParts);
	}

	protected void removeMeasurementData(PartResult result) {
		for(Measurement measurement : result.getInstalledPart().getMeasurements()){
			measurement.setMeasurementAngle(0.0);
			measurement.setMeasurementValue(0.0);
			measurement.setPartSerialNumber(null);
			measurement.setMeasurementName(null);
			measurement.setMeasurementStringValue(null);
			measurement.setActualTimestamp(null);
			measurement.setMeasurementStatus(MeasurementStatus.REMOVED);
		}
	}
	public void loadProductId(boolean refresh,String productId) {
		try {
			getView().clearDisplayMessage();
			product = checkProductOnServer(productId);
			loadProductBuildResultStatus(refresh);
			if(ownerShipped || checkShippedAndException(product)) {
				shippingAndExceptionStatusFlag = true;
				getView().getRemoveResultButton().setDisable(true);
				getView().getEnterResultButton().setDisable(true);
				getView().getEnterMultiResultButton().setDisable(true);
				if(getView().getProductType() != null) {
					List<ProductTypeData> datas = getDao(ProductTypeDao.class).findAllByOwnerProduct(productTypeData.getProductTypeName());
					for(ProductTypeData productType : datas){
						EventBusUtil.publish(new ShipScrapEvent(productType.getProductType(), true));
					}
				}
			}
			
		} catch (TaskException e) {
			displayErrorMessage(e.getMessage());
			Logger.getLogger().warn(e.getMessage());
			
		} catch (Exception e) {
			Logger.getLogger().warn(e.getMessage());
		}
	}

	private boolean checkShippedAndException(BaseProduct product){
		String lastPassingProcessPointId = product.getLastPassingProcessPointId();
		List<String> shippingProcessPointIds = getShippingProcessPointIds(lastPassingProcessPointId, productTypeData);
		List<ExceptionalOut> exceptionalList = getDao(ExceptionalOutDao.class).findAllByProductId(product.getProductId());
		boolean canRepairAfterShipping = PropertyService.getProperty(appContext.getTerminal().getHostName(), ALLOW_REPAIR_AFTER_SHIPPING, "FALSE").equalsIgnoreCase("TRUE");
		if(shippingProcessPointIds != null && shippingProcessPointIds.contains(lastPassingProcessPointId) && !canRepairAfterShipping){
			displayErrorMessage("The "+ productTypeData.getProductIdLabel()+" "+ product.getProductId() +" is Shipped");
			return true;
		}
		else if (exceptionalList != null && !(exceptionalList.isEmpty())) {
			displayErrorMessage("This "+ productTypeData.getProductIdLabel()+" "+ product.getProductId() +" is either Scrapped or Exceptional out");
			return true;
		}
		return false;
	}

	private List<String> getShippingProcessPointIds(String lastPassingProcessPointId, ProductTypeData productTypeData){
		ProductionInfoPropertyBean property = PropertyService.getPropertyBean(ProductionInfoPropertyBean.class);
		String productType = productTypeData.getProductTypeName();

		if(property == null || property.getShippedPpIds(String[].class) == null) {
			return null;
		} else {
			String[] shippingProcessPointIds = property.getShippedPpIds(String[].class).get(productType);
			return shippingProcessPointIds.length == 0 ? null : Arrays.asList(shippingProcessPointIds);
		}
	}


	@SuppressWarnings("unchecked")
	protected T checkProductOnServer(String productId) {
		try {

			ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(getProductType());
			return (T)productDao.findByKey(productId);
		} catch (Exception e) {
			String msg = "failed to load " + getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}

	}

	protected void loadProductBuildResultStatus(boolean refresh) {

		// Lot control rules are loaded in
		// 1: first time to load lot control rules
		// 2: product spec code changed
		if (isNewProductSpec(product)) {
			productSpec = loadProductSpec(product);

			if (productSpec == null)
				MessageDialog.showError("Product Spec is not defined:"+ product.getProductSpecCode());

			loadLotControlRules();
		} else {
			if (!StringUtils.isEmpty(product.getSubId()) && !refresh && isNewSubId(product)) {
				subId = product.getSubId();
				assembleLotControl();
			}
			cleanInstalledPartData();
		}

		loadProductBuildResults();
		for(PartResult pr : lotControlPartResultData)  {
			setShipStatus(pr);
		}
		renderProductBuildResult();
	}

	
	public void setShipStatus(PartResult pr)
	{	
		boolean isRequiredPart=false;
		if(requiredPartShipProcessList!=null && !requiredPartShipProcessList.isEmpty() && requiredPartShipProcessList.contains(pr.getProcessPointId()))  {
			List<RequiredPart> requiredPartsList = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPoint(pr.getProcessPointId());
			for(RequiredPart requiredPart:requiredPartsList){
				if(requiredPart.getId().getPartName().equals(pr.getPartName()))  {
					isRequiredPart=true;
					break;
				}
			}
		}
		if(!pr.getLotControlRule().isPartConfirm() && !isRequiredPart)  {
			pr.setGoodShipStatus(true);
		}
		else{
			if(pr.getBuildResult() == null)  {				
				pr.setGoodShipStatus(false);
			}
			else  {
				if (!(pr.getBuildResult().getInstalledPartStatus().equals(InstalledPartStatus.NG) || 
						pr.getBuildResult().getInstalledPartStatus().equals(InstalledPartStatus.NC)|| 
						pr.getBuildResult().getInstalledPartStatus().equals(InstalledPartStatus.REMOVED)|| 
						pr.getStatusMeasure() == MeasurementStatus.REMOVED ||
						pr.getStatusMeasure() == MeasurementStatus.NG))
				{
					pr.setGoodShipStatus(true);
				}
				else  {
					pr.setGoodShipStatus(false);
				}
			}
		}
	}

   public enum CellType {
    	PART, MEASUREMENT, SHIP_STATUS, OTHER;
    };
   private enum CellColour { Red, Green, Blue, Orange, White };
   
   class EditingCell extends TableCell<PartResult, Object> {
    	 
        private TextField textField;
        private CellType cellType = CellType.OTHER;
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField("editCell");
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
           changeBackgroundColour(CellColour.White);
 		   if(getTableRow().getItem() != null && getCellType() == CellType.SHIP_STATUS)  {
			    PartResult rowItem = (PartResult)getTableRow().getItem();
			    if(InstalledPartShipStatus.OK.toString().equalsIgnoreCase(rowItem.getShipStatus()))  {
			    	changeBackgroundColour(CellColour.Green);
			    }
			    else if(InstalledPartShipStatus.NG.toString().equalsIgnoreCase(rowItem.getShipStatus()))  {
			    	changeBackgroundColour(CellColour.Red);
			    }
		   }
 		   else if(getTableRow().getItem() != null && getCellType() == CellType.PART)  {
				PartResult rowItem = (PartResult)getTableRow().getItem();
				if(rowItem.getStatus() == InstalledPartStatus.OK || rowItem.getStatus() == InstalledPartStatus.ACCEPT)  {
			    	changeBackgroundColour(CellColour.Green);
				}
				else if(rowItem.getStatus() == InstalledPartStatus.NG || rowItem.getStatus() == InstalledPartStatus.MISSING
						|| rowItem.getStatus() == InstalledPartStatus.REJECT || rowItem.getStatus() == InstalledPartStatus.NM)  {
			    	changeBackgroundColour(CellColour.Red);
				}
				else if(rowItem.getStatus() == InstalledPartStatus.NC || rowItem.getStatus() == InstalledPartStatus.REPAIRED
						|| rowItem.getStatus() == InstalledPartStatus.PENDING)  {
			    	changeBackgroundColour(CellColour.Orange);
				}
		   }
 		   else if(getTableRow().getItem() != null && getCellType() == CellType.MEASUREMENT)  {
			    PartResult rowItem = (PartResult)getTableRow().getItem();
			    if(rowItem.getStatusMeasure() == MeasurementStatus.OK)  {
			    	changeBackgroundColour(CellColour.Green);
			    }
			    else if(rowItem.getStatusMeasure() == MeasurementStatus.NG)  {
			    	changeBackgroundColour(CellColour.Red);
			    }
		   }
	    }
 
        private void changeBackgroundColour(CellColour rgbo)  {
	    	getStyleClass().removeAll("table-cell-white","table-cell-orange", "table-cell-green","table-cell-red");
	    	if(rgbo == CellColour.Red)  {
		    	getStyleClass().add("table-cell-red");	    		
	    	}
	    	else if(rgbo == CellColour.Green)  {
		    	getStyleClass().add("table-cell-green");	    		
	    	}
	    	else if(rgbo == CellColour.Blue)  {
		    	getStyleClass().add("table-cell-Blue");	    		
	    	}
	    	else if(rgbo == CellColour.Orange)  {
		    	getStyleClass().add("table-cell-orange");	    		
	    	}
	    	else if(rgbo == CellColour.White)  {
		    	getStyleClass().add("table-cell-white");	    		
	    	}
        }
        
        private void createTextField(String id) {
            textField = UiFactory.createTextField(id, getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

		public CellType getCellType() {
			return cellType;
		}

		public void setCellType(CellType cellType) {
			this.cellType = cellType;
		}
    }
    
	protected void renderProductBuildResult() {
		if(hasDuplicateParts){
			getView().getPartStatusPanel().setDataWithPreserveSort(displayLotControlPartResultData);
		}
		else  {
			getView().getPartStatusPanel().setDataWithPreserveSort(lotControlPartResultData);
		}
		List<TableColumn<PartResult, ?>> myList = (List<TableColumn<PartResult, ?>>)getView().getPartStatusPanel().getTable().getColumns();
		if(myList != null && !myList.isEmpty())  {
			for(TableColumn<PartResult, ?> myCol : myList)  {
				if(myCol == null || StringUtils.isBlank(myCol.getText()))  continue;
				TableColumn<PartResult, Object> thisCol = (TableColumn<PartResult, Object>)myCol;
				if(thisCol.getText().equals(ManualLotControlRepairConstants.SHIP_STATUS))  {
					thisCol.setCellFactory(
							new Callback<TableColumn<PartResult,Object>, TableCell<PartResult,Object>>() {
								   @Override public TableCell<PartResult,Object> call(TableColumn<PartResult,Object> col) {
									   TableCell<PartResult,Object> newCell = new EditingCell();
									   ((EditingCell)newCell).setCellType(CellType.SHIP_STATUS);
									   return newCell;
								   }
							});
				}
				if(thisCol.getText().equals(ManualLotControlRepairConstants.STATUS))  {
					thisCol.setCellFactory(
							new Callback<TableColumn<PartResult,Object>, TableCell<PartResult,Object>>() {
								   @Override public TableCell<PartResult,Object> call(TableColumn<PartResult,Object> col) {
									   TableCell<PartResult,Object> newCell = new EditingCell();
									   ((EditingCell)newCell).setCellType(CellType.PART);
									   return newCell;
								   }
							});
				}
				if(thisCol.getText().equals(ManualLotControlRepairConstants.STATUS_MEASURE))  {
					thisCol.setCellFactory(
							new Callback<TableColumn<PartResult,Object>, TableCell<PartResult,Object>>() {
								   @Override public TableCell<PartResult,Object> call(TableColumn<PartResult,Object> col) {
									   TableCell<PartResult,Object> newCell = new EditingCell();
									   ((EditingCell)newCell).setCellType(CellType.MEASUREMENT);
									   return newCell;
								   }
							});
				}
			}
		}
		getView().getRefreshButton().setDisable(false);
	}



	/**
	 * Default implementation for Engine, Frame and Knucles
	 * @param product
	 */
	protected void loadInstalledParts(List<InstalledPart> installedPartList) {
		getMeasurementsForInstalledParts(installedPartList);
		addInstalledPartResult(installedPartList);
	}

	protected void getMeasurementsForInstalledParts(List<InstalledPart> installedParts) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		List<Measurement> allMeasurements = measurementDao.findAllByProductId(product.getProductId());
		for(InstalledPart part : installedParts){
			List<Measurement> measuremnts = new ArrayList<Measurement>();
			for(Measurement measurement : allMeasurements){
				if(part.getId().getPartName().equals(measurement.getId().getPartName()))
					measuremnts.add(measurement);
			}
			part.setMeasurements(measuremnts);
		}

	}

	@SuppressWarnings("rawtypes")
	protected BaseProductSpec loadProductSpec(BaseProduct product) {
		BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao(productTypeData.getProductType());
		return productSpecDao.findByProductSpecCode(getProductSpecCode(product), getProductType().name());
	}

	private String getProductSpecCode(BaseProduct product) {
		return product.getProductSpecCode();
	}

	private boolean isNewSubId(BaseProduct product) {
		if(product.getSubId() == null) return false;
		return !product.getSubId().equals(subId);
	}

	private boolean isNewProductSpec(BaseProduct product) {
		if(productSpec == null) return true;
		return !product.getProductSpecCode().equals(productSpec.getProductSpecCode());
	}

	@SuppressWarnings("unchecked")
	protected void loadLotControlRules() {

		LotControlRuleId ruleId = new LotControlRuleId((ProductSpec)productSpec);

		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		rulesAndProcessPoints = lotControlRuleDao.findAllRulesAndProcessPointsAssemble(ruleId, getProductType().name());

		assembleLotControl();

		Logger.getLogger().debug("number of part:" + lotControlPartResultData.size());

	}

	private List<Object[]> addDeviceDrivenRulesAndProcessPoints() {
		List<Object[]> deviceDrivenRulesAndProcessPoints = new ArrayList<Object[]>();
		HeadLessPropertyBean headlessProperty = null;
		List<ProcessPoint> headlessProcessPointList = ServiceFactory.getDao(ProcessPointDao.class).findDeviceDrivenHeadlessProcessPoints();
		for(ProcessPoint pp : headlessProcessPointList){
			headlessProperty=PropertyService.getPropertyBean(HeadLessPropertyBean.class, pp.getProcessPointId()); 
			if(!headlessProperty.getProductType().equals(productType.name()) || headlessProperty.isLotControl()) continue;
			if((headlessProperty.isDeviceDriven() || 
					headlessProperty.getPlcDataMapping().equals(HeadlessDataMapping.MAP_BY_PART_NAME.name()))){

				List<Device> deviceList = getDeviceDao().findAllInputDeviceByProcessPointId(pp.getProcessPointId());
				if(deviceList == null || deviceList.size() == 0) return deviceDrivenRulesAndProcessPoints;

				for(Device device : deviceList){
					List<LotControlRule> deduceLotControlRules = LotControlRuleUtil.deduceLotControlRules(device, productType.name(), getProductName(headlessProperty.getProducts()), Logger.getLogger());
					if(deduceLotControlRules != null && deduceLotControlRules.size() > 0)
						for(LotControlRule r : deduceLotControlRules){
							deviceDrivenRulesAndProcessPoints.add(new Object[]{r, pp});
						}
				}
			}		
		}

		return deviceDrivenRulesAndProcessPoints;


	}

	private String getProductName(String products) {
		if(!StringUtils.isEmpty(products)) {
			List<String> list = CommonUtil.toList(products);
			if(list.size() > 0) return list.get(0);
		}

		return StringUtils.EMPTY;
	}

	private DeviceDao getDeviceDao() {
		if(deviceDao == null)
			deviceDao = ServiceFactory.getDao(DeviceDao.class);

		return deviceDao;
	}

	protected void assembleLotControl() {

		if(lotControlPartResultData != null)  {
			lotControlPartResultData.clear();
		}
		if(displayLotControlPartResultData != null)  {
			displayLotControlPartResultData.clear();
		}
		hasDuplicateParts = false;
		String requiredParts=getRequiredPartLists();
		filterRules(rulesAndProcessPoints,requiredParts);			


		for(PartResult partResult : lotControlPartResultData){

			if(partResult.getLotControlRule().getPartByProductSpecs().size() == 0) continue;
			List<LotControlRule> theMostMatchedRules = LotControlPartUtil.getLotControlRuleByProductSpec(productSpec, partResult.getLotControlRules());
			if(theMostMatchedRules.size() > 0 ){
				partResult.setLotControlRule(theMostMatchedRules.get(0));
			}
		}

		//Repair Device Driven Data 
		if(property.isRepairDeviceDrivenData()) {
			List<Object[]> drivenRulesAndProcessPoints = addDeviceDrivenRulesAndProcessPoints();
			if(drivenRulesAndProcessPoints.size() > 0){
				filterRules(drivenRulesAndProcessPoints,requiredParts);
				getRulesAndProcessPoints().addAll(drivenRulesAndProcessPoints);
			}
		}
	}

	private void filterRules(List<Object[]> rulesAndPpIds, String requiredParts) {

		if(rulesAndPpIds == null || rulesAndPpIds.size() == 0) return;

		if (getTerminalProcessPoints().isEmpty()) {
			loadTerminalProcessPoints();
		}

		List<String> divisionIds = CommonUtil.splitStringList(StringUtils.trimToEmpty((getSelectedDivisionId())));
		List<String> lineIds = CommonUtil.splitStringList(StringUtils.trimToEmpty(getSelectedLineId()));

		for(Object[] objects : rulesAndPpIds){
			LotControlRule rule = (LotControlRule)objects[0];

			if(ruleFilterOutCriteria(rule)) continue;

			ProcessPoint processPoint = (ProcessPoint) objects[1];
			if (!divisionIds.isEmpty() && !divisionIds.contains(processPoint.getDivisionId())) {
				continue;
			}

			if (!lineIds.isEmpty() && !lineIds.contains(processPoint.getLineId())) {
				continue;
			}

			if(!StringUtils.isEmpty(property.getFilterByInstalledParts()) && !CommonUtil.isInList(((ProcessPoint)objects[1]).getProcessPointId(), property.getFilterByInstalledParts()))
				continue;//filter out installed process point

			if(property.getFilterByRequiredParts().length>0 && !CommonUtil.isInList(rule.getPartName().getPartName().toString(), requiredParts))
				continue;//filter out required parts

			if(rule.getPartName().getPartVisible()==PartNameVisibleType.HIDDEN.getId())
				continue;

			PartResult partResult = findByLotControlRule(rule);
			if (partResult == null) {
				boolean headless = !getTerminalProcessPoints().contains(rule.getId().getProcessPointId());
				lotControlPartResultData.add(new PartResult(rule, (ProcessPoint) objects[1], headless));
			} else {
				partResult.add(rule);
			}
		}
	}

	protected boolean ruleFilterOutCriteria(LotControlRule rule) {
		return !StringUtils.isEmpty(rule.getSubId()) && !rule.getSubId().equals(product.getSubId());
	}

	private String getRequiredPartLists(){
		StringBuilder requiredParts=new StringBuilder();
		if(property.getFilterByRequiredParts().length>0){
			boolean firstString=true;				
			for(String requiredPartProcessPoint :property.getFilterByRequiredParts()){
				if(isValidProcesPoint(requiredPartProcessPoint)){
					List<RequiredPart> requiredPartsList = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPointAndProdSpec(requiredPartProcessPoint,productSpec);
					for(RequiredPart rp:requiredPartsList){
						if(!firstString){
							requiredParts.append(",");
						}
						requiredParts.append(rp.getId().getPartName());
						firstString=false;
					}
				}
			}
		}
		return requiredParts.toString();
	}

	private boolean isValidProcesPoint(String processPoint){
		if(StringUtils.isEmpty(processPoint)) return false;
		ProcessPoint pp=ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint);
		return pp==null?false:true;
	}

	protected void assembleLotControlDiecast() {
		Set<String> processPoints = new HashSet<String>();
		for(Object[] objects : rulesAndProcessPoints){
			ProcessPoint processPoint = (ProcessPoint)objects[1];
			if(!processPoints.contains(processPoint.getProcessPointId())){
				processPoints.add(processPoint.getProcessPointId());
				String partName = getPartNameProperty(processPoint);
				if(!StringUtils.isEmpty(partName))
					lotControlPartResultData.add(new PartResult(createDummyRule(partName, processPoint.getProcessPointId()), processPoint));
			}
		}
	}

	/**
	 * Create a dummy lot control rule for the Head/Block part overral status
	 * @param processPointId 
	 * @param processPoint
	 * @return
	 */
	private LotControlRule createDummyRule(String partName, String processPointId) {
		LotControlRule rule = new LotControlRule();
		LotControlRuleId id = new LotControlRuleId((ProductSpec)productSpec);
		id.setProcessPointId(processPointId);
		id.setPartName(partName);
		rule.setId(id);

		return rule;
	}

	private String getPartNameProperty(ProcessPoint processPoint) {
		HeadLessPropertyBean bean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPoint.getProcessPointId());
		return bean.getOverallStatusPartName();
	}


	private void cleanInstalledPartData() {
		partResult = null;
		for(PartResult result: lotControlPartResultData){
			result.setBuildResult(null);
		}

	}

	private PartResult findByLotControlRule(LotControlRule r) {
		for(PartResult result : lotControlPartResultData)
			if(result.getProcessPointId().equals(r.getId().getProcessPointId()) &&
					result.getPartName().equals(r.getId().getPartName()))
				return result;

		return null;
	}



	protected void addInstalledPartResult(List<InstalledPart> installedParts) {
		List<PartResult> filterOutList = new ArrayList<PartResult>();
		for(PartResult partResult: lotControlPartResultData){

			if(partResult.isPartMark() ||
					LotControlPartUtil.isExcludedToSave(partResult.getPartName(), getExcludedToSaveParts())){

				filterOutList.add(partResult);
				continue;
			}

			InstalledPart installedPart = getInstalledPart(partResult.getLotControlRule(), installedParts);
			partResult.setBuildResult(installedPart);
		}

		lotControlPartResultData.removeAll(filterOutList);
		handleDuplicateParts();

	}

	protected void loadProductBuildResults(List<? extends ProductBuildResult> productBuildResulits) {
		List<PartResult> filterOutList = new ArrayList<PartResult>();
		for(PartResult partResult: lotControlPartResultData){

			if(LotControlPartUtil.isExcludedToSave(partResult.getPartName(), getExcludedToSaveParts())){

				filterOutList.add(partResult);
				continue;
			}

			ProductBuildResult installedPart = getProductBuildResult(partResult.getLotControlRule(), productBuildResulits);
			partResult.setBuildResult(installedPart);
		}

		lotControlPartResultData.removeAll(filterOutList);
		handleDuplicateParts();
	}

	private ProductBuildResult getProductBuildResult(LotControlRule lotControlRule,
			List<? extends ProductBuildResult> productBuildResulits) {

		for(ProductBuildResult part : productBuildResulits){

			if(part.getPartName().trim().equalsIgnoreCase(lotControlRule.getId().getPartName().trim())){
				return part;
			}

		}
		return null;
	}

	private String getExcludedToSaveParts() {
		return PropertyService.getProperty("Default_LotControl", "EXCLUDE_PARTS_TO_SAVE");
	}

	private InstalledPart getInstalledPart(LotControlRule lotControlRule, List<InstalledPart> installedParts) {
		for(InstalledPart part : installedParts){

			if(part.getId().getPartName().trim().equalsIgnoreCase(lotControlRule.getId().getPartName().trim())){
				return part;
			}
		}
		return null;
	}

	protected void loadTerminalProcessPoints() {
		TerminalDao terminalDao = ServiceFactory.getService(TerminalDao.class);
		List<Terminal> terminals = terminalDao.findAllLotControlTerminals();
		getTerminalProcessPoints().clear();
		for (Terminal t : terminals) {
			getTerminalProcessPoints().add(t.getLocatedProcessPointId());
		}
	}

	//Getter & Setters
	public ApplicationContext getAppContext() {
		return appContext;
	}

	public ManualLotControlRepairPanel getView() {
		return view;
	}

	public BaseProductSpec getProductSpec() {
		return productSpec;
	}

	public ProductType getProductType() {
		if(productType == null){
			productType = ProductType.valueOf(property.getProductType());
		}
		return productType;
	}

	public ManualLotControlRepairPropertyBean getProperty() {
		return property;
	}



	private List<Object[]> getRulesAndProcessPoints() {
		if(rulesAndProcessPoints == null)
			rulesAndProcessPoints = new ArrayList<Object[]>();
			return rulesAndProcessPoints;
	}

	public String getSelectedDivisionId() {
		return selectedDivisionId;
	}

	public String getSelectedLineId() {
		return selectedLineId;
	}

	public void setSelectedDivisionId(String selectedDivisionId) {
		this.selectedDivisionId = selectedDivisionId;
	}

	public void setSelectedLineId(String selectedLineId) {
		this.selectedLineId = selectedLineId;
	}

	protected int getMeasurementCount(PartResult pr) {
		if(pr.getLotControlRule() != null && pr.getLotControlRule().getParts() != null && pr.getLotControlRule().getParts().size() > 0){
			return pr.getLotControlRule().getParts().get(0).getMeasurementCount();
		}

		return 0;
	};

	protected Set<String> getTerminalProcessPoints() {
		return terminalProcessPoints;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	protected String[] getColumns(){

		String[] columns = property.getMlcrColumns();

		return columns;
	}

	protected void handleDuplicateParts(){
		if(displayLotControlPartResultData != null)  {
			displayLotControlPartResultData.clear();
		}
		hasDuplicateParts = false;
		Map<String,PartResult> mapByPartNameInstalledPart = new HashMap<String, PartResult>();
		List<PartResult> duplicatePartList = new ArrayList<PartResult>();
		for(PartResult partResult: lotControlPartResultData){
			if(mapByPartNameInstalledPart.keySet().contains(partResult.getPartName())){
				ProductBuildResult installedPart = partResult.getBuildResult();
				if((partResult.getProcessPointId() != null  && installedPart != null && installedPart.getProcessPointId() != null )&& 
						(partResult.getProcessPointId().equalsIgnoreCase(installedPart.getProcessPointId()))){
					duplicatePartList.add(mapByPartNameInstalledPart.get(partResult.getPartName()));
					mapByPartNameInstalledPart.put(partResult.getPartName(), partResult);
				}else{
					duplicatePartList.add(partResult); 
				}
			}else{	
				mapByPartNameInstalledPart.put(partResult.getPartName(), partResult);
			}
		}

		if(duplicatePartList != null && duplicatePartList.size() > 0){
			displayLotControlPartResultData = new ArrayList<PartResult>();
			displayLotControlPartResultData.addAll(lotControlPartResultData);
			displayLotControlPartResultData.removeAll(duplicatePartList);
			hasDuplicateParts = true;
		}
		else  {
			hasDuplicateParts = false;
		}

	}

	public boolean isPartReadOnly()
	{
		return (partResult.getLotControlRule().getPartName().getPartVisible()==PartNameVisibleType.READ_ONLY.getId());
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (ManualLotControlRepairConstants.ENTER_RESULT.equals(loggedButton.getText())) enterResult();
			else if (ManualLotControlRepairConstants.REMOVE_RESULT.equals(loggedButton.getText())) removePartResult();
			else if (ManualLotControlRepairConstants.SET_RESULT_NG.equals(loggedButton.getText())) setResultNg();
			else if (QiConstant.REFRESH.equals(loggedButton.getText())) loadProductId(true,product.getProductId());
			else if (ManualLotControlRepairConstants.ENTER_MULTIPLE_RESULT.equals(loggedButton.getText())) enterMultipleResults();
		}
	}
	
	/**
	 * This method is used to display error message.
	 * @param loggerMsg
	 * @param errMsg
	 */
	public void displayErrorMessage(String errMsg)
	{
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
	}

}
