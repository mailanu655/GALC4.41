package com.honda.galc.client.datacollection.observer.knuckles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.SubProductLotCache;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.ProductSequenceManager;
import com.honda.galc.client.datacollection.property.KnucklePropertyBean;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.knuckle.KnuckleLabelPrintingUtil;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Knuckle;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductLot;
import com.honda.galc.notification.service.IKnuckleLoadNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>KnucklesOnSequenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnucklesOnSequenceManager description </p>
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
 * Dec 21, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class KnucklesOnSequenceManager extends ProductSequenceManager
{

	private PreProductionLot preproductionLot;
	private Map<String, SubProductLot> subProductLotMap = new HashMap<String, SubProductLot>();
	private List<ExpectedProduct> expectedProductList;
	private ExpectedProduct leftExpectedProduct;
	private ExpectedProduct rightExpectedProduct;
	private ExpectedProduct currentExpectedProduct;
	private SubProductLot currentSubProductLot;
	private int leftPassingCount;
	private int rightPassingCount;
	private PreProductionLotDao preProductionLotDao;
	private ExpectedProductDao expectedDao;
	private KnucklePropertyBean knucklesProperty;
	private List<SubProductLot> lotGroupList = new ArrayList<SubProductLot>(); //hold small lots
	
	public KnucklesOnSequenceManager(ClientContext context) {
		super(context);
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveNextExpectedProduct(ProcessProduct state) {

		if(StringUtils.isEmpty(state.getExpectedProductId())) return;

		saveNextExpectedState();
		
		//fetch the next expected product
		getExpectedProductId(state);
	}

	
	@Override
	public void saveNextExpectedProduct(String nextProductId) {
		
		saveNextExpectedState();
	}
	
	private void saveNextExpectedState() {
			
			getExpectedDao().save(currentExpectedProduct);
	}

	private ExpectedProductDao getExpectedDao() {
		if(expectedDao == null)
			expectedDao =  ServiceFactory.getDao(ExpectedProductDao.class);
		
		return expectedDao;
		
	}


	@Override
	public void getExpectedProductId(ProcessProduct state) {
		
		preproductionLot = context.getCurrentPreProductionLot();
		
		if(preproductionLot == null) return;	
		if(preproductionLot.getStampedCount() >= preproductionLot.getLotSize()*2){//complete current lot
			finishCurrentLot();
			initializeNewLot();
		}
		
		if(subProductLotMap.isEmpty())
			loadSuProductLots();
		
		String expectedProductId = findExpected() ? nextExpectedProductId() : null;;
		state.setExpectedProductId(expectedProductId);
		
		if(expectedProductId == null)
			Logger.getLogger().warn("WARN: Failed to find the next expected product Id. Check if there is any skiped product in this lot.");
		else
			state.setExpectedSubId(currentSubProductLot.getSubId());
		
		Logger.getLogger().info("PreProductionLot:", preproductionLot == null ? "null" : preproductionLot.getProductionLot(), 
				" ProductSpecCode:", preproductionLot == null ? "null" :preproductionLot.getProductSpecCode(), " Expected product:", 
				state.getExpectedProductId(), " Knuckle Side:", state.getExpectedSubId());
	}

	private void initializeNewLot() {
		preproductionLot = context.getCurrentPreProductionLot();
		
		if(preproductionLot == null){
			Logger.getLogger().warn("Current pre-production lot is null.");
			return;
		}

		//remove this initial status to let re-schedule the lot
		//preproductionLot.setSendStatus(PreProductionLotSendStatus.INPROGRESS);
		//make sure start counter is right
		if(preproductionLot.getStampedCount() < 0) {
			//should nerver happen, just in case log it 
			Logger.getLogger().info("Error PreproductionLot:",preproductionLot.getProductionLot()," initial stamp count less than zero." );
		}

		//getPreProductionLotDao().save(preproductionLot);
		
	}

	private void finishCurrentLot() {
		    //we done for the current lot
			leftPassingCount = 0;
			rightPassingCount = 0;
			
			getExpectedProductList().clear();
			
			preproductionLot = context.getCurrentPreProductionLot();
			preproductionLot.setSendStatus(PreProductionLotSendStatus.DONE);
			updateSendStatus();
			
			//clean up expected
			getExpectedDao().remove(leftExpectedProduct);
			getExpectedDao().remove(rightExpectedProduct);
			
			
			context.setCurrentPreProductionLot(null);
			preproductionLot = null;
			subProductLotMap.clear();
	}


	private String nextExpectedProductId() {
		if(currentSubProductLot == null){
			String msg = "Pre-production lot is missing, may need to restart client.";
			Logger.getLogger().warn(msg);
			//throw new TaskException(msg, "WARN"); 
			return null; 		
		}
		
		if(currentExpectedProduct.getProductId() == null){
			currentExpectedProduct.setProductId(currentSubProductLot.getStartProductId());
			return currentSubProductLot.getStartProductId();
		} else {
			currentExpectedProduct.increaseProductId(getProperty().getSnDigits());
			
			if(!currentSubProductLot.isInLot(currentExpectedProduct.getProductId()))
				Logger.getLogger().warn("Next expected product is out of range. next expected:",
						currentExpectedProduct.getProductId(), " Max Product", currentSubProductLot.getEndProductId());
				
			return currentExpectedProduct.getProductId();
		}
	}

	private boolean findExpected() {
		loadExpected();
		
		if(findExpectedForNewLot()) return true;
			
		SubProductLot leftSubProductLot = subProductLotMap.get(leftExpectedProduct.getProcessPointId());
		SubProductLot rightSubProductLot = subProductLotMap.get(rightExpectedProduct.getProcessPointId());
		countPassingKnuckles(leftSubProductLot, rightSubProductLot);
		
		return findExpectedByPassingCount(leftSubProductLot, rightSubProductLot);

	}

	private boolean findExpectedByPassingCount(SubProductLot leftSubProductLot, SubProductLot rightSubProductLot) {
		
		if(leftPassingCount < rightPassingCount  && leftPassingCount < leftSubProductLot.getSize()){
			currentExpectedProduct = leftExpectedProduct;
			currentSubProductLot = leftSubProductLot;
		} else if(rightPassingCount < leftPassingCount){
			currentExpectedProduct = rightExpectedProduct;
			currentSubProductLot = rightSubProductLot;
		}else{ //equal
			if(leftPassingCount < leftSubProductLot.getSize()) {
				currentExpectedProduct = leftExpectedProduct;
				currentSubProductLot = leftSubProductLot;
				
			} else {//done with this Lot
				return false;
			}
		}
		
		return true;
	}

	private void countPassingKnuckles(SubProductLot leftSubProductLot, SubProductLot rightSubProductLot) {
		if(leftSubProductLot == null) throw new TaskException("ERROR: failed to find sub product lot for " + leftExpectedProduct.getProcessPointId());
		leftPassingCount = getProductSequenceNumber(leftExpectedProduct.getProductId()) - getProductSequenceNumber(leftSubProductLot.getStartProductId()) +1;
		
		if(rightSubProductLot == null) throw new TaskException("ERROR: failed to find sub product lot for " + rightExpectedProduct.getProcessPointId());
		rightPassingCount = getProductSequenceNumber(rightExpectedProduct.getProductId()) - getProductSequenceNumber(rightSubProductLot.getStartProductId()) +1;
		
		//Some how - likely caused by user update in team leader, the expected is not update as in the prepoduction lot,
		//then, correct the result and generate the product from starting of the lot
		if(leftPassingCount < 0 ){
			String msg = " Knucle left side expect product:" + leftExpectedProduct.getProductId() + " is not in current lot:" +
			leftSubProductLot.getProductionLot();
			throw new TaskException(msg, "ERROR");
		} else if(rightPassingCount < 0){
			String msg = " Knucle right side expect product:" + rightExpectedProduct.getProductId() + " is not in current lot:" +
			rightSubProductLot.getProductionLot();
			throw new TaskException(msg, "ERROR");
		}
	}

	private boolean findExpectedForNewLot() {
		if(leftExpectedProduct == null){//new Lot start
			currentSubProductLot = subProductLotMap.get(context.getProcessPointId() + SubProduct.SUB_ID_LEFT);
			leftExpectedProduct = createExpectedProduct(context.getProcessPointId() + SubProduct.SUB_ID_LEFT);
			currentExpectedProduct = leftExpectedProduct;
			return true;
		} else if(rightExpectedProduct == null){
			currentSubProductLot = subProductLotMap.get(context.getProcessPointId() + SubProduct.SUB_ID_RIGHT);
			rightExpectedProduct = createExpectedProduct(context.getProcessPointId() + SubProduct.SUB_ID_RIGHT);
			currentExpectedProduct = rightExpectedProduct;
			leftPassingCount = 1;
			return true;
		}
		return false;
	}

	private ExpectedProduct createExpectedProduct(String processPointId) {
		ExpectedProduct expectedProd = new ExpectedProduct();
		expectedProd.setProcessPointId(processPointId);
		
		return expectedProd;
	}


	private void loadExpected() {
		expectedProductList = context.getDbManager().findAllExpectedProduct();
		leftExpectedProduct = findExpected(expectedProductList, SubProduct.SUB_ID_LEFT);
		rightExpectedProduct = findExpected(expectedProductList, SubProduct.SUB_ID_RIGHT);
		
	}


	private ExpectedProduct findExpected(List<ExpectedProduct> list, String side) {
		if(list == null) return null;
		for(ExpectedProduct expectedProduct : list){
			if(expectedProduct.getProcessPointId().substring(expectedProduct.getProcessPointId().length() -1).equals(side))
				return StringUtils.isEmpty(expectedProduct.getProductId()) ? null : expectedProduct;
		}
		
		//ok, not find expected - system is starting up
	    Logger.getLogger().info("No expected product found for side :" + side);
	    return null;
		
	}

	private int getProductSequenceNumber(String productId) {
		return Integer.parseInt(productId.substring(productId.length() - getProperty().getSnDigits()));
	}

	private void loadSuProductLots() {
		if(preproductionLot == null) return;
		subProductLotMap = SubProductLotCache.getInstance().getSubProductLotMap(preproductionLot.getProductionLot(), context.getProcessPointId());
	}


	private TerminalPropertyBean getProperty() {
		return context.getProperty();
	}
	

	private List<ExpectedProduct> getExpectedProductList() {
		if(expectedProductList == null)
			expectedProductList = new ArrayList<ExpectedProduct>();
		
		if(expectedProductList != null && expectedProductList.size() == 0){
			if(leftExpectedProduct != null)
				expectedProductList.add(leftExpectedProduct);
			
			if(rightExpectedProduct != null)
				expectedProductList.add(rightExpectedProduct);
		}
		
		if(expectedProductList.size() < 2 && !expectedProductList.contains(rightExpectedProduct))
			expectedProductList.add(rightExpectedProduct);
		
		return expectedProductList;
	}

	public PreProductionLotDao getPreProductionLotDao() {
		if(preProductionLotDao == null)
			preProductionLotDao =  ServiceFactory.getDao(PreProductionLotDao.class);
				
		return preProductionLotDao;
	}

	public List<String> getIncomingProducts(DataCollectionState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePreProductionLot() {
		try {
			
			if(context.isRemake()) return;
			if(context.isDisabledExpectedProductCheck())return;
				
			preproductionLot.setStampedCount(preproductionLot.getStampedCount() + 1);
			context.setCurrentPreProductionLot(preproductionLot);

			if (preproductionLot.getSendStatus() != PreProductionLotSendStatus.INPROGRESS){
				preproductionLot.setSendStatus(PreProductionLotSendStatus.INPROGRESS);
				updateSendStatus();
			}
			
			getPreProductionLotDao().updateStampedCount(preproductionLot.getProductionLot(), 
					preproductionLot.getStampedCount());
			
			Logger.getLogger().info("Updated preproduction lot :", preproductionLot.getId(), 
					" stamp count to:" + preproductionLot.getStampedCount());
			
//			if(isStartPrintLabels())
//				printNextKdLotLabels();
			
			
			notifySheduleClient();
				
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to update preproduction lot.");
		}

	}

	private void updateSendStatus() {
		getPreProductionLotDao().updateSendStatus(preproductionLot.getProductionLot(), 
				preproductionLot.getSendStatusId());
	}

	private boolean isProductInCurrentProductionLot() {
		String productId = DataCollectionController.getInstance().getState().getProductId();
		for(SubProductLot lot :subProductLotMap.values()){
			if (lot.isInLot(productId))
				return true;
		}
		
		return false;
		
	}

	private void notifySheduleClient() {
		ServiceFactory.getNotificationService(IKnuckleLoadNotification.class, context.getProcessPointId()).knuckleLoaded(
				preproductionLot.getProductionLot(), preproductionLot.getStampedCount());
		
	}

	private boolean isStartPrintLabels() {
		if (!currentSubProductLot.isLabelPrintingStartPositionSet())
			prepareLabelPrinting(preproductionLot.getKdLot());
			
		if (currentSubProductLot.getLabelPrintingStartPosition() < 0) return false;
		return preproductionLot.getStampedCount() == currentSubProductLot.getLabelPrintingStartPosition();
		
	}

	private void printNextKdLotLabels() {
		PreProductionLot nextLot = getPreProductionLotDao().findByKey(preproductionLot.getNextProductionLot());
		prepareLabelPrinting(nextLot.getKdLot());
		
		Thread t = new Thread(){
			public void run() {
				
				doLabelPrinting();
			}
		};
		
		t.start();
		
	}

	protected void doLabelPrinting() {
		
		List<SubProduct> printList = generateLabelPrintingList();
		try {
			
			logLabelPrinting(printList);
			new KnuckleLabelPrintingUtil().print(printList);
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception on print ", printList.get(0).getKdLotNumber());
			DataCollectionController.getInstance().getState().message(
					new Message("Label Print Error ", "Failed to pring labels."));
		}		
		
	}

	private void logLabelPrinting(List<SubProduct> printList) {
		StringBuilder sb = new StringBuilder();
		
		for(SubProduct product : printList){
			if(sb.length() > 0) sb.append(",");
			sb.append(product.getProductId());
		}
		
		Logger.getLogger().info("Print Labels for " + printList.get(0).getKdLotNumber() + ":" + sb.toString());
	}

	private List<SubProduct> generateLabelPrintingList() {
		List<SubProduct> printList = new ArrayList<SubProduct>();

		for(SubProductLot subLot: lotGroupList){
			printList.addAll(subLot.generatePrintLabelList(Knuckle.class));
		}
		
		return printList;
	}
	
	
	
	private void prepareLabelPrinting(String kdLot) {
		List<PreProductionLot> lotsForSameKdLot = 
			preProductionLotDao.findAllWithSameKdLot(kdLot);

		lotGroupList.clear();
		int smallLotIndex = 0;
		for(PreProductionLot lot : lotsForSameKdLot){
			List<SubProductLot> subLotList = SubProductLotCache.getInstance().getSubProductLots(lot.getProductionLot());
			lotGroupList.addAll(subLotList);

			smallLotIndex++;
			for(SubProductLot subLot: subLotList){
				subLot.setSmallLotIndex(smallLotIndex);
				subLot.setLabelPrintingStartPosition(SubProductLot.INIT_PRINTING);
			}
		}
		
		setStartPringPosition(lotGroupList);
	}

	private void setStartPringPosition(List<SubProductLot> lotsList) {
		int total = 0;
		for(SubProductLot lot : lotsList)
			total += lot.getSize();
		
		
		int markPosition = total - getKnucklesProperty().getKnuckleLabelPrintingStartPosition();
		
		//print on the 1 if total is less than start printing position
		if(markPosition < 0){
			lotsList.get(0).setLabelPrintingStartPosition(1);
			return;
		}
		
		int count  = 0;
		for(SubProductLot lot : lotsList){
			count += lot.getSize();
			if(count >= markPosition){
				lot.setLabelPrintingStartPosition(markPosition - (count - lot.getSize()));
				return;
			}
		}
		
	}
	

	private KnucklePropertyBean getKnucklesProperty() {
		if(knucklesProperty == null)
			knucklesProperty = PropertyService.getPropertyBean(KnucklePropertyBean.class, context.getProcessPointId());
			
		return knucklesProperty;
	}

	public int getLeftPassingCount() {
		return leftPassingCount;
	}

	public int getRightPassingCount() {
		return rightPassingCount;
	}


}
