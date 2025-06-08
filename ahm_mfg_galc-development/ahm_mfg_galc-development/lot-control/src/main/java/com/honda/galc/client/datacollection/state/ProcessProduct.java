package com.honda.galc.client.datacollection.state;

import java.util.List;

import com.honda.galc.client.audio.ClipPlayer;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.fsm.IProductIdVerificationEvent;
import com.honda.galc.client.datacollection.fsm.IUserControlEvent;

import com.honda.galc.client.datacollection.processor.ProductIdProcessor;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.util.LotControlPartUtil;
/**
 * <h3>ProducIdVerification</h3>
 * <h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public class ProcessProduct extends DataCollectionState 
implements IUserControlEvent, IProductIdVerificationEvent, IProcessProduct, IProcessProductClassic, IProcessProductHeadless, IProcessProductRepair
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize product Id verification state
	 *
	 */
	public void init(){
		logDebug(this.getClass().getSimpleName()+": init()-PRODUCT --->");
		initState();
	}
	
	/**
	 * Map to product Id received event
	 */
	public void receivedProductId(ProductBean product) {
		setProduct(product);
		clearMessage();
	}
	
	
	/**
	 * Map to product Id OK event
	 * Set product id OK action and notify observers
	 * Transit to part serial number verification state.
	 */
	public void productIdOk(ProductBean product){
		logInfo(this.getClass().getSimpleName()+": ok()");
		setProduct(product);
	}
	
	/**
	 * Map to production Id verification failed event
	 * Notify observers product Id verification failed and error message.
	 * 
	 */
	public void productIdNg(ProductBean product, String msgId, String userMsg){
		logInfo(this.getClass().getSimpleName()+": ng()");
		setProduct(product);
		showMessage(MessageType.ERROR, msgId, userMsg);
	}
	
	
	public void showMessage(MessageType messageType, String messagegId, String messageContent){
		setMessage(new Message(messagegId, messageContent, messageType));
	}
	
	/**
	 * Last step in processing the current product. 
	 * 
	 */
	public void complete(){
		Logger.getLogger().debug(this.getClass().getSimpleName()+": complete() PRODUCT <---");
		
		if(getStateBean().getProduct() != null && !getStateBean().getProduct().isSkipped())
			checkResult(this);
	}
	
	private void checkResult(DataCollectionState state) {
		ProductBean product = state.getProduct();
		for(InstalledPart p : product.getPartList()){
			Logger.getLogger().debug("part:" + p.getId().getPartName() + " : " + p.getPartSerialNumber());
			int torqueCount = 0;
			if(p.getId() == null && p.getMeasurements() == null ) continue;//place holder for skipped part 
			for(Measurement m : p.getMeasurements())
			{
				Logger.getLogger().debug(LotControlPartUtil.toString(m));
				torqueCount++;
			}
		}
		
	}

	/**
	 * Map to user skip part event
	 */
	public void skipPart() {
		// Dummy method, never used.
		logInfo(this.getClass().getSimpleName()+": skipPart()");
	}
	
	public void cancel(){
		logInfo(this.getClass().getSimpleName()+": cancel()");
		DataCollectionController.getInstance(getApplicationId()).getClientContext().setManualRefresh(true);		
		ClipPlayer.getInstance().flushAll();
	}
	

	/**
	 * Set lot control rule from data container
	 * @param argDc
	 * @throws TaskException
	 */
	public void initLotControlRules(String productSpec, List<LotControlRule> rules) throws TaskException{
		if(!LotControlRulesValidator.validate(rules)) {
			//log error
			Logger.getLogger().error("Invalid Lot Control Rule!");
			throw new TaskException("Invalid Lot Control Rule!", this.getClass().getSimpleName());
		}
		super.initLotControlRules(productSpec, rules);
	}
	
	/**
	 * Initialize product Id verification initial state
	 *
	 */
	private void initState() {
		getStateBean().setPreviousProductSkppedByCellOut(getProduct() == null ? false : getProduct().isSkippedByCellOut());
		setProduct(null);
		setExpectedProductId(null);
		setAfOnSeqNo(null);
		setProductCount(0);
		setLotSize(0);
		setSpecChanged(false);
		setCurrentPartIndex(-1);
		setCurrentTorqueIndex(-1);
		clearMessageAndHistory();
		getStateBean().setRepair(false);
		getStateBean().setResetNextExpected(true);
	}
	
	public void reject() {
		// Dummy method, never used.
	}
	
	public void reject1() {
		// Dummy method, never used.
	}

	public void skipCurrentInput() {
	}

	public void continueDelay() {
		clearMessage();
	}

	public boolean isLotControlRuleNotDefined(){
		return ProductIdProcessor.LOT_CONTROL_RULE_NOT_DEFINED.equals(getMessage().getId());
	}
	
	public boolean noDataCollection(){
		return DataCollectionController.getInstance(getApplicationId()).getProperty().isSkipDataCollection();
	}
	
}