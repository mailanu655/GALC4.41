package com.honda.galc.client.datacollection.processor;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;

import com.honda.galc.client.ui.ErrorMessageDialog;
import com.honda.galc.client.ui.ErrorMessageDialog.ButtonOptions;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductAttributeDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductAttribute;
import com.honda.galc.entity.product.ProductAttributeId;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.StragglerService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

/**
 * @version 1.0
 * @author Gangadhararao Gadde 
 * @since May 19, 2014
 */
/**
 * @version 2.0
 * @author Ambica Gawarla
 * @date October 23, 2018
 */
public class PaintOffFrameVinProcessor extends FrameVinProcessor {

	ProcessPoint processPoint;
	String requestName=null;
	String expectedKdLot = null;
	
	public PaintOffFrameVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		getLogger(getProcessPoint().getProcessPointId()).debug("PaintOffFrameVinProcessor : Enter execute method");
		Boolean invokeStragglerService= false;
		try {	
			
				super.confirmProductId(productId);
				Frame frame = getFrame();
				 if(getProcessPoint().getLineId().equals(frame.getTrackingStatus())){
					 String msg="Product "+product.getProductId() +" (KD Lot - "+frame.getKdLotNumber() +" ) already processed at this Processpoint ";
					 handleException(msg);
				}else{
					super.performPreviousLineCheck();
				}
				 
				 isRelinkThenRemove();
				if(!isSameLot()){
					if(!isPreviousLotCompleted()){
						String msg = "Scanned VIN - "+product.getProductId()+" (KD Lot - "+frame.getKdLotNumber() +" ) \n"
								+"Previous KD Lot - "+getProcessPoint().getCurrentKdLot()+" has not been completed and has "+getUprocessedProductCnt() 
								+" unprocessed vins.\nWould you like to mark them all as stragglers?";
						int createStragglers = showColoredMessageDialog(msg, Color.yellow, "Previous Lot Not Completed");
						if(createStragglers == JOptionPane.NO_OPTION){
							invokeStragglerService = false;
							msg="Previous Lot ("+getProcessPoint().getCurrentKdLot()+") has not been completed";
							handleException(msg);
						}else{
							invokeStragglerService = true;
						}
					}
					if(!isLotInCorrectOrderAsPerSchedule()){
						String msg = "Kd Lot (" + frame.getKdLotNumber()+") for scanned Vin - "+product.getProductId()+ " is out of Order."
								+"\n Expected Kd Lot -  "+expectedKdLot
								+ "\nDo you still want to continue processing the Vin?";
						int continueProcessing =showColoredMessageDialog(msg, Color.yellow, "InCorrect Lot Order");
						//display prompt to process if lot is not in correct order if ok process vin if cancel allow user to passbody
						if(continueProcessing == JOptionPane.NO_OPTION){
							msg="Current Kd Lot (" + frame.getKdLotNumber()+") for Vin - "+product.getProductId()+" is out of Order ";
							handleException(msg);
						}
					}
				}
				if(isRelinkMode()){
					int createRelink= JOptionPane.YES_OPTION;
					if(isStraggler()){
						if(isStragglerAsRelink()){
							String msg = "Scanned Vin - "+product.getProductId()+" is a straggler. \nDo you want to process it as Relink?";
							createRelink = showColoredMessageDialog(msg, Color.yellow, "Straggler");
						}else{
							createRelink= JOptionPane.NO_OPTION;
						}
					}
					if(createRelink == JOptionPane.YES_OPTION){
						createRelinkProduct();
					}else{
						String msg="Scanned Vin - "+product.getProductId()+" is a straggler. \nCannot process it as Relink.";
						handleException(msg);
					}
				}
				
				if(invokeStragglerService) {
					getLogger(getProcessPoint().getProcessPointId()).info("Invoking Straggler Service on Kd Lot - "+getProcessPoint().getCurrentKdLot());
					identifyStragglers();
				}
				
				createProductSequence();
				updateKdLot();
				
				
				
				getController().getFsm().productIdOk(product);
			}catch (Exception  e){
				logException(e,"An error occured while executing PaintOffFrameVinProcessor",productId.getProductId());
				return false;
			}
		getLogger(getProcessPoint().getProcessPointId()).info("PaintOffFrameVinProcessor : Exit execute method");
		return true;
	}

	private void isRelinkThenRemove() {
		ProductAttributeId prodAttributeId = new ProductAttributeId();
		prodAttributeId.setAttribute(getRelinkAttributeName());
		prodAttributeId.setProductId(product.getProductId());
		ProductAttribute prodAttribute  =  ServiceFactory.getDao(ProductAttributeDao.class).findByKey(prodAttributeId);
		
		if(prodAttribute!= null){
			getLogger(getProcessPoint().getProcessPointId()).info("Removing Relink Vin - "+ product.getProductId() );
			ServiceFactory.getDao(ProductAttributeDao.class).remove(prodAttribute);
		}else{
			getLogger(getProcessPoint().getProcessPointId()).info("Vin - "+ product.getProductId() +" is not Relink");
		}
		
	}
		

	private void createRelinkProduct() {
		ProductAttributeId prodAttributeId = new ProductAttributeId();
		prodAttributeId.setAttribute(getRelinkAttributeName());
		prodAttributeId.setProductId(product.getProductId());
		
		ProductAttribute prodAttribute = new ProductAttribute();
		prodAttribute.setId(prodAttributeId);
		prodAttribute.setAttributeValue(Boolean.TRUE.toString());
		prodAttribute.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		getLogger(getProcessPoint().getProcessPointId()).info("Saving Relink Product - "+prodAttribute.toString());
		ServiceFactory.getDao(ProductAttributeDao.class).save(prodAttribute);
	}

	private boolean isRelinkMode() {
		ComponentStatus componentStatus = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(context.getProcessPointId(), "OPERATION_MODE");
		if(componentStatus != null){
			String operationMode = componentStatus.getStatusValue();
			return OperationMode.AUTO_RELINK_MODE.getName().equalsIgnoreCase(operationMode) || OperationMode.MANUAL_RELINK_MODE.getName().equalsIgnoreCase(operationMode);
		}
		return false;
	}

	private void updateKdLot() {
		 Frame frame = getFrame();
		 ProcessPoint processPoint = getProcessPoint();
		 processPoint.setCurrentKdLot(frame.getKdLotNumber());
		 processPoint.setCurrentProductionLot(frame.getProductionLot());
		 this.processPoint = ServiceFactory.getDao(ProcessPointDao.class).save(processPoint);
		 getLogger(getProcessPoint().getProcessPointId()).info("Successfully updated the processpoint:"+processPoint.toString() +" Kd Lot ( " + frame.getKdLotNumber()+" ), Prod Lot ( "+ frame.getProductionLot() +" )");
	}

	protected String getUprocessedProductCnt() {
		String lastProcessedProduct = getLastProcessedProduct();
		int schedQty = ServiceFactory.getDao(FrameDao.class).getScheduleQuantity(lastProcessedProduct, getPlanCode(context.getProcessPointId()));
		int passQty = ServiceFactory.getDao(FrameDao.class).getPassQuantity(lastProcessedProduct, context.getProcessPointId());
		
		return String.valueOf(schedQty - passQty);
	}

	protected boolean isLotInCorrectOrderAsPerSchedule() {
		PreProductionLot preProductionLot = getProductionLot(getProcessPoint().getCurrentProductionLot());
		String currKdLot = getProcessPoint().getCurrentKdLot();
		
		boolean kdLotMatches = true;
		while(kdLotMatches){
			String nextProductionLot = preProductionLot.getNextProductionLot();
			if (StringUtils.isEmpty(nextProductionLot)) {
				getLogger(getProcessPoint().getProcessPointId()).info("No next production lot specified for " + preProductionLot.getProductionLot());
				preProductionLot = null;
				kdLotMatches = false;
			} else {
				preProductionLot = getProductionLot(nextProductionLot);
				if(preProductionLot == null || !preProductionLot.getKdLot().equalsIgnoreCase(currKdLot)){
					kdLotMatches = false;
				}
			}
		}
		Frame frame = getFrame();
		if(preProductionLot != null && preProductionLot.getKdLot().equalsIgnoreCase(frame.getKdLotNumber())){
			return true;
		}
		this.expectedKdLot = preProductionLot == null ? null : preProductionLot.getKdLot();
		getLogger(getProcessPoint().getProcessPointId()).info("Kd Lot Number " + frame.getKdLotNumber() + " for VIN " + frame.getProductId() + " is not in correct order as per schedule");
		return false;
	}

	protected boolean isPreviousLotCompleted() {
		return getRemainingQty(getLastProcessedProduct()) > 0? false:true;
	}

	protected boolean isSameLot() {
		Frame frame = getFrame();
		getLogger(getProcessPoint().getProcessPointId()).info("Processing Kd Lot Number : "+ frame.getKdLotNumber());
		if(frame.getKdLotNumber().equalsIgnoreCase(getProcessPoint().getCurrentKdLot())){
			return true;
		}
		getLogger(getProcessPoint().getProcessPointId()).info("Kd Lot Number " + frame.getKdLotNumber() + " for VIN " + frame.getProductId() + " does not match current Kd Lot Number " + getProcessPoint().getCurrentKdLot() + " for process point " + getProcessPoint().getProcessPointId());
		return false;
	}

	public ProcessPoint getProcessPoint() {
		if(this.processPoint == null){
			this.processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(context.getProcessPointId().trim());
		}
		return this.processPoint;	
	}
	
	public Frame getFrame() {
		return ServiceFactory.getDao(FrameDao.class).findByKey(product.getProductId());
	}
	
	public PreProductionLot getProductionLot(String productionLot) {
		return StringUtils.isEmpty(productionLot)?null:ServiceFactory.getDao(PreProductionLotDao.class).findByKey(productionLot);
	}

	public void identifyStragglers() {
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(context.getProcessPointId().trim());
		ServiceFactory.getService(StragglerService.class).identifyStragglers(processPoint, getFrame());
	}
	
	public void logErrorMessage(String message) {
		getLogger(getProcessPoint().getProcessPointId()).error(message);
	}
	
	public void logException(Exception ex, String msg, String productId) {
		ProductBean productBean = new ProductBean();
		productBean.setProductId(productId);
		getController().getFsm().productIdNg(productBean, MESSAGE_ID, ex.getMessage() == null ? msg : ex.getMessage());
		getLogger(getProcessPoint().getProcessPointId()).error(ex, msg);
	}
	
	public boolean createProductSequence() {
		try {
				Sequence nextSeq = ServiceFactory.getDao(SequenceDao.class).getNextSequence(getSequenceName());
				Frame frame = getFrame();
				frame.setAfOnSequenceNumber(nextSeq.getCurrentSeq());
				String text = nextSeq.getCurrentSeq()!= null?StringUtil.padLeft(nextSeq.getCurrentSeq().toString(),5,'0'):"N/A";
				getLogger(getProcessPoint().getProcessPointId()).info("Assigning Sequence - "+text + " to Product - "+ frame.getId());
				
				//update productSequence Tbx
				ProductSequence prodSequence = new ProductSequence();
				ProductSequenceId prodSequenceId = new ProductSequenceId();
				prodSequenceId.setProcessPointId(context.getProcessPointId());
				prodSequenceId.setProductId(frame.getId());
				
				prodSequence.setId(prodSequenceId);
				prodSequence.setReferenceTimestamp(new Timestamp(System.currentTimeMillis()));
				prodSequence.setSequenceNumber(nextSeq.getCurrentSeq());
				prodSequence.setAssociateNo(context.getAppContext().getUserId());
				
				getLogger(getProcessPoint().getProcessPointId()).info("Saving ProductSequence - "+prodSequence.toString());
				ServiceFactory.getDao(ProductSequenceDao.class).save(prodSequence);
				
				getLogger(getProcessPoint().getProcessPointId()).info("Saving PA Off Sequence - "+text +" to Sequence Table");
				ServiceFactory.getDao(SequenceDao.class).save(nextSeq);
				
		} catch (Exception e) {    
			logException(e,"An error occured while assigning PA Off Sequence to ProductId",product.getProductId());
			return false;
		}
		return true;
	}
	
	private String getSequenceName(){
		return getController().getProperty().getSequenceName();
	}
	
	private String getRelinkAttributeName(){
		return getController().getProperty().getRelinkAttributeName().trim();
	}
	
	private Logger getLogger(String processPointId) {
		return Logger.getLogger(PropertyService.getLoggerName(processPointId));
	}
	
	private String getPlanCode(String ProcessPoint) {
		return StringUtils.trimToEmpty(PropertyService.getProperty(ProcessPoint, TagNames.PLAN_CODE.name(), null));
	}
	
	private int getRemainingQty(String vin){
		int schedQty = ServiceFactory.getDao(FrameDao.class).getScheduleQuantity(vin, getPlanCode(context.getProcessPointId()));
		int passQty = ServiceFactory.getDao(FrameDao.class).getPassQuantity(vin, context.getProcessPointId());
		int remainingQty = schedQty - passQty;
		getLogger(getProcessPoint().getProcessPointId()).info(vin + " quantity remaining is " + remainingQty + " (schedule qty " + schedQty + ", pass qty " + passQty + ")");
		return remainingQty ;
	}
	
	protected String getLastProcessedProduct(){
		List<ProcessProductDto> prodList = ServiceFactory.getDao(ProductSequenceDao.class).findAllAlreadyProcessed(context.getProcessPointId(), 1);
		return prodList.size()> 0?prodList.get(0).getVin():null;
	}
	
	protected boolean isStraggler(){
		String productId = product.getProductId();
		String applicationId = context.getAppContext().getApplicationId();
		List<Straggler> currentProductstragglerList = ServiceFactory.getDao(StragglerDao.class).findStragglerProductList(productId, applicationId);	
		if(currentProductstragglerList != null && currentProductstragglerList.size() > 0){
			getLogger(getProcessPoint().getProcessPointId()).info("VIN " + productId + " is a straggler for " + applicationId);
			return true;
		}
		getLogger(getProcessPoint().getProcessPointId()).info("VIN " + productId + " is not a straggler for " + applicationId);
		return false;
	}
	
	private int showColoredMessageDialog(String message, Color color, String title) {
		getLogger(getProcessPoint().getProcessPointId()).info("showing colored message dialog - "+message);
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(ErrorMessageDialog.defaultYesLabel, ErrorMessageDialog.defaultYesLabel);
		options.put(ErrorMessageDialog.defaultNoLabel, ErrorMessageDialog.defaultNoLabel);
		
		final ErrorMessageDialog errorDialog = new ErrorMessageDialog(context.getFrame(), title, message, "#ffff00", "#000000", "",false, ButtonOptions.YES_NO, options);
		errorDialog.setActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				setRequestName(button.getText());
				errorDialog.dispose();
			}
			
		});
		errorDialog.setVisible(true);
		return getRequestName().equalsIgnoreCase(ErrorMessageDialog.defaultYesLabel)? JOptionPane.YES_OPTION:JOptionPane.NO_OPTION ;
	}
	
	private String getRequestName() {
		return this.requestName;
	}

	private void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	
	private boolean isStragglerAsRelink(){
		return PropertyService.getPropertyBoolean(context.getProcessPointId(), "STRAGGLER_AS_RELINK", true);
	}
}
