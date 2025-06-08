package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.event.QsrCreatedEvent;
import com.honda.galc.client.teamleader.hold.qsr.event.QsrEvent;
import com.honda.galc.client.teamleader.hold.qsr.event.QsrUpdatedEvent;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.enumtype.ShippedStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldProductsAction</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class HoldProductsAction extends QsrAction<HoldProductPanel> implements ActionListener {

	private HoldDialog dialog;
	private List<BroadcastDestination> broadcastDestinations;
	private String applicationId = getView().getApplicationId();
	private QsrMaintenancePropertyBean propertyBean=null;
	private Map<ProductStateEnum,List<BaseProduct>> productStateMap;
	private enum ProductStateEnum {OK, MISSING, SHIPPED, FAILED}; 

	public HoldProductsAction(HoldDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
		propertyBean = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getView().getApplicationId());
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		String msg = "Are you sure you want to hold " + getDialog().getFilteredProducts().size() + " products?";
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Hold Products", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) return;
		
		updateProductStates(getDialog().getFilteredProducts());
		if (getProductStateMap().get(ProductStateEnum.OK).isEmpty()) {
			refreshProductTable();
			getDialog().dispose();
			showScrollDialog(getProductStateMessage());
			Logger.getLogger(applicationId).info("Hold action performed by: "
					+ getDialog().getInputPanel().getAssociateIdInput().getText() + "\n" + getProductStateMessage());
			return;
		}
		 
		Division division = getDialog().getParentPanel().getDivision();
		ProductType productType = getView().getProductType();
		HoldAccessType holdAccessType = (HoldAccessType) getDialog().getInputPanel().getHoldAccessTypeInput().getSelectedItem();
		try {
			Qsr qsr = null;
			QsrEvent qsrEvent = null;
			if (getDialog().getInputPanel().getAddToQsrInput().isSelected()) {
				qsr = (Qsr) getDialog().getInputPanel().getQsrInput().getSelectedItem();
				qsrEvent = new QsrUpdatedEvent();
			} else {
				qsr = assembleQsr(division, productType,holdAccessType);
				qsrEvent = new QsrCreatedEvent();
			}

			DailyDepartmentSchedule schedule = getDailyDepartmentScheduleDao().find(division.getDivisionId(), new Timestamp(System.currentTimeMillis()));
			List<HoldResult> holdResults = assembleHoldResults(division, schedule, getProductStateMap().get(ProductStateEnum.OK));

			int partitionSize = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, this.applicationId).getHoldPartitionSize();
			List<List<HoldResult>> partitionedList = this.partitionHoldResults(holdResults, partitionSize);
			int currPartition = 0;
			
			try {
				this.getDialog().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				for (currPartition=0 ; currPartition < partitionedList.size(); currPartition++) {
					qsr = getQsrDao().holdProducts(productType, partitionedList.get(currPartition), qsr);
					Thread.sleep(2000);
				} 
			} catch (Exception ex) {
				this.getLogger().error(ex, ex.getMessage());

				List<HoldResult> processedHoldResults = getHoldResultDao().findAllByQsrId(qsr.getId());
				List<String> processedProductIdList = new ArrayList<String>();
				for (HoldResult processedHoldResult : processedHoldResults) {
					processedProductIdList.add(processedHoldResult.getId().getProductId());
				}
				
				List<BaseProduct> processedProductList = new ArrayList<BaseProduct>();
				List<BaseProduct> unprocessedProductList = new ArrayList<BaseProduct>();
				for (BaseProduct product : getProductStateMap().get(ProductStateEnum.OK)) {
					if (processedProductIdList.contains(product.getProductId()))
						processedProductList.add(product);
					else
						unprocessedProductList.add(product);
				}
				getProductStateMap().put(ProductStateEnum.OK, processedProductList);
				getProductStateMap().put(ProductStateEnum.FAILED, unprocessedProductList);
			}

			if (propertyBean.isBroadcastEnabled()) {
				List<BaseProduct> selectedProducts = getProductStateMap().get(ProductStateEnum.OK);
				if (selectedProducts!= null && selectedProducts.size()>0) {
					loadBroadcastDestinations();
					if (!this.broadcastDestinations.isEmpty()) {
						for (BaseProduct baseProduct : selectedProducts) {
							for (BroadcastDestination destination : this.broadcastDestinations) {
								int seqNo = destination.getSequenceNumber();
								String holdReason = (String) getDialog().getInputPanel().getReasonInput().getSelectedItem();
								Logger.getLogger(applicationId).info("Broadcast Services started for " + destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")");
								invokeBroadcastService(baseProduct,seqNo,holdReason);
							}
						}
					}
				}
			}

			ProductTypeData typeData;
			if ((typeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(productType.toString())) == null){
				this.getMainWindow().setErrorMessage("Product type " + productType.toString() + " is invalid.");
				return;
			}
			
			if (Config.isOwnerProductHold(typeData.getProductTypeName())){
				List<BaseProduct> ownerProducts = getOwnerProducts(holdResults);
				if (ownerProducts.size() > 0){
					List<HoldResult> ownerProductHoldResults = assembleHoldResults(division, schedule, ownerProducts);
					ServiceFactory.getDao(HoldResultDao.class).saveAll(ownerProductHoldResults);
				}
			}
			
			String message = "QSR " + qsr.getId() + " - \"" + qsr.getDescription() + "\"\n" + getProductStateMessage();
			showScrollDialog(message);
			Logger.getLogger(applicationId).info("Hold action performed by: "
					+ getDialog().getInputPanel().getAssociateIdInput().getText() + "\n" + message);
			
			refreshProductTable();
			qsrEvent.setQsr(qsr);
			EventBus.publish(qsrEvent);
		} finally {
			getDialog().dispose();
		}
	}
	
	private void updateProductStates(List<BaseProduct> productList) {
		List<BaseProduct> updatedProductList = dialog.getParentPanel().updateProducts(productList);
		List<BaseProduct> missingProductList = getMissingProducts(productList, updatedProductList);
		if (Config.isDisableProductIdCheck(this.getView().getProductType().toString())){
			updatedProductList.addAll(missingProductList);
		} else {
			getProductStateMap().put(ProductStateEnum.MISSING, missingProductList);
		}
		getProductStateMap().put(ProductStateEnum.OK, updatedProductList);
		
		Map<Integer,List<BaseProduct>> productsByShipStatusMap = groupByShipStatus(getProductStateMap().get(ProductStateEnum.OK));
		getProductStateMap().put(ProductStateEnum.OK, productsByShipStatusMap.get(ShippedStatus.UNSHIPPED.getId()));
		getProductStateMap().put(ProductStateEnum.SHIPPED, productsByShipStatusMap.get(ShippedStatus.SHIPPED.getId()));
	}
	
	private List<BaseProduct> getMissingProducts(List<BaseProduct> currentProductList, List<BaseProduct> updatedProductList) {
		List<BaseProduct> missingProducts = new ArrayList<BaseProduct>();
		List<String> updatedProductIdList = updatedProductList.stream().map(BaseProduct::getProductId).collect(Collectors.toList());
		for (BaseProduct currentProduct : currentProductList) {
			if (updatedProductIdList.contains(currentProduct.getProductId())) continue;
			missingProducts.add(currentProduct);
		}
		return missingProducts;
	}
	
	protected Map<Integer,List<BaseProduct>> groupByShipStatus(List<BaseProduct> productList){
		Map<Integer,List<BaseProduct>> productsByShipStatusMap = new HashMap<Integer,List<BaseProduct>>();
		productsByShipStatusMap.put(ShippedStatus.SHIPPED.getId(), new ArrayList<BaseProduct>());
		productsByShipStatusMap.put(ShippedStatus.UNSHIPPED.getId(), new ArrayList<BaseProduct>());
		List<String> lineIdList = getShipLines();
		if (lineIdList == null || lineIdList.isEmpty()) {
			productsByShipStatusMap.put(ShippedStatus.UNSHIPPED.getId(), productList);
			return productsByShipStatusMap;
		}
		for (BaseProduct product : productList) {
			String trackingStatus = product.getTrackingStatus();
			if (trackingStatus != null && lineIdList.contains(trackingStatus.trim())) {
				List<BaseProduct> shippedProducts = productsByShipStatusMap.get(ShippedStatus.SHIPPED.getId());
				shippedProducts.add(product);
				productsByShipStatusMap.put(ShippedStatus.SHIPPED.getId(), shippedProducts);
			} else {
				List<BaseProduct> unshippedProducts = productsByShipStatusMap.get(ShippedStatus.UNSHIPPED.getId());
				unshippedProducts.add(product);
				productsByShipStatusMap.put(ShippedStatus.UNSHIPPED.getId(), unshippedProducts);
			}
		}
		return productsByShipStatusMap;
	}
	
	protected List<Map<String, Object>> getRemainingRecords(List<BaseProduct> processedProducts){
		Set<String> processProductIds = processedProducts.stream().map(BaseProduct::getProductId).collect(Collectors.toSet());
		List<Map<String, Object>> remainingRecords = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> productRecord : this.getView().getProductPanel().getItems()) {
			BaseProduct product = (BaseProduct)productRecord.get("product");
			if (processProductIds.contains(product.getId())) continue;
			remainingRecords.add(productRecord);
		}
		return remainingRecords;
	}

	protected Qsr assembleQsr(Division division, ProductType productType,HoldAccessType holdAccessType) {
		Qsr qsr = new Qsr();
		String holdReason = (String) getDialog().getInputPanel().getReasonInput().getSelectedItem();
		Division originatingDepartment = (Division)getDialog().getInputPanel().getOriginDptComboBox().getComponent().getSelectedItem();
		if(originatingDepartment!=null && originatingDepartment.getId()!=null)
			qsr.setProcessLocation(originatingDepartment.getId());
		qsr.setProductType(productType.name());
		qsr.setDescription(holdReason);
		qsr.setStatus(QsrStatus.ACTIVE.getIntValue());
		qsr.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		Division responsibleDepartment = (Division)getDialog().getInputPanel().getRespDptComboBox().getComponent().getSelectedItem();
		if(responsibleDepartment!=null && responsibleDepartment.getId()!=null)
			qsr.setResponsibleDepartment(responsibleDepartment.getId());
		qsr.setHoldAccessType(holdAccessType.getId().getTypeId());
		return qsr;
	}

	protected List<HoldResult> assembleHoldResults(Division division, DailyDepartmentSchedule schedule, List<BaseProduct> productList) {

		List<HoldResult> holdResults = new ArrayList<HoldResult>();

		Date productionDate = null;
		if (schedule != null) {
			productionDate = schedule.getId().getProductionDate();
		}

		int holdType = getDialog().getInputPanel().getHoldAtShippingInput().isSelected() ? 1 : 0;
		ProcessPoint kickOutprocessPoint = (ProcessPoint)getDialog().getInputPanel().getProcessPointComboBoxComponent().getSelectedItem();
		String kickOutProcessPointId = getDialog().getInputPanel().getKickoutInput().isSelected()?kickOutprocessPoint.getProcessPointId() : null;
		String spindleNumber = getDialog().getInputPanel().getSpindleInput() == null? "N/A":getDialog().getInputPanel().getSpindleInput().getText();
		String holdReason = (String) getDialog().getInputPanel().getReasonInput().getSelectedItem();
		holdReason = spindleNumber == null || spindleNumber.trim().length() == 0 ? holdReason : spindleNumber.trim() + "-" + holdReason;
		String associateId = getDialog().getInputPanel().getAssociateIdInput().getText();
		String associateName = getDialog().getInputPanel().getAssociateNameInput().getText();
		String phone = getDialog().getInputPanel().getPhoneInput().getText();
		
		int releasePermission = getDialog().getProperty().getHoldBy();

		for (BaseProduct bp : productList) {
			HoldResult holdResult = new HoldResult();
			holdResult.setId(new HoldResultId(bp.getProductId(), holdType));
			holdResult.setHoldReason(holdReason);
			holdResult.setHoldAssociateNo(associateId);
			holdResult.setHoldAssociateName(associateName);
			holdResult.setHoldAssociatePhone(phone);
			holdResult.setProductionDate(productionDate);
			holdResult.setHoldProcessPoint(kickOutProcessPointId);
			holdResult.setReleasePermission(releasePermission);
			
			holdResults.add(holdResult);
		}
		return holdResults;
	}
	
	private void refreshProductTable() {
		List<Map<String, Object>> remainingRecords = getRemainingRecords(getProductStateMap().get(ProductStateEnum.OK));
		getView().getProductPanel().reloadData(remainingRecords);
		getView().getProductPanel().clearSelection();
		dialog.getParentPanel().updateProductRecords();
	}

	public HoldDialog getDialog() {
		return dialog;
	}

	public void setDialog(HoldDialog dialog) {
		this.dialog = dialog;
	}
	
	private List<BaseProduct> getOwnerProducts(List<HoldResult> holdResults){
		List<BaseProduct> ownerProducts = new ArrayList<BaseProduct>();
		for (HoldResult holdResult : holdResults){
			ProductDao<?> productDao = getProductDao(getView().getProductType());
			BaseProduct product = productDao.findBySn(holdResult.getId().getProductId());
			if (product != null && product.getOwnerProductId() != null){
				ProductTypeData ownerData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(getView().getProductType().toString());
				BaseProduct ownerProduct = getProductDao(ownerData.getOwnerProductType()).findBySn(product.getOwnerProductId());
				if (ownerProduct != null) ownerProducts.add(ownerProduct);
			}
		}
		return ownerProducts;
	}
	
	//load all broad cast destinations 
	//(device has auto enabled false to prevent duplicate broadcast on DataCollection complete)
	private void loadBroadcastDestinations(){
		 this.broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class)
					.findAllByProcessPointId(applicationId);
	}
	
	private void invokeBroadcastService(BaseProduct product, int seqNo, String aReason){
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
		dc.put(DataContainerTag.PRODUCT, product);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		dc.put(DataContainerTag.HOLD_COMMENT, aReason);
		dc.put(DataContainerTag.ASSOCIATE_NO, getMainWindow().getUserId());
		getService(BroadcastService.class).broadcast(applicationId, seqNo, dc);
	}
	
	private Map<ProductStateEnum,List<BaseProduct>> getProductStateMap(){
		if (productStateMap == null) {
			productStateMap = new HashMap<ProductStateEnum, List<BaseProduct>>();
			productStateMap.put(ProductStateEnum.OK, new ArrayList<BaseProduct>());
			productStateMap.put(ProductStateEnum.MISSING, new ArrayList<BaseProduct>());
			productStateMap.put(ProductStateEnum.SHIPPED, new ArrayList<BaseProduct>());
			productStateMap.put(ProductStateEnum.FAILED, new ArrayList<BaseProduct>());
		}
		return productStateMap;
	}
	
	private String getProductStateMessage() {
		StringBuilder message = new StringBuilder();
		if (getProductStateMap().get(ProductStateEnum.OK).isEmpty()) {
			message.append("Nothing to process.\n");
		} else {
			message.append("\n" + getProductStateMap().get(ProductStateEnum.OK).size() + " product(s) processed successfully.\n");
		}
		if (getProductStateMap().get(ProductStateEnum.MISSING).isEmpty() && 
			getProductStateMap().get(ProductStateEnum.SHIPPED).isEmpty() &&
			getProductStateMap().get(ProductStateEnum.FAILED).isEmpty()) 
			return message.toString();
		message.append("\nThe following products were skipped:\n");
		for (BaseProduct product : getProductStateMap().get(ProductStateEnum.MISSING)) {
			message.append(product.getProductId() + " - not found\n");
		}
		for (BaseProduct product : getProductStateMap().get(ProductStateEnum.SHIPPED)) {
			message.append(product.getProductId() + " - shipped\n");
		}
		for (BaseProduct product : getProductStateMap().get(ProductStateEnum.FAILED)) {
			message.append(product.getProductId() + " - transaction failed\n");
		}
		return message.toString();
	}
}