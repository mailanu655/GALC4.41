package com.honda.galc.client.datacollection.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.DataCollectionError;
import com.honda.galc.entity.product.LotControlRule;
/**
 * <h3>DataCollectionStateBean</h3>
 * <h4>
 * Contains satate details. These details is shared by all states.
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
public class DataCollectionStateBean extends Observable implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean resetNextExpected = true;

	//last operation action
	private Action action = Action.INIT;
	private ProductBean product;
	private Message message;
	
	//Product Specification - YMTO or MTOC
	private String productSpecCode;
	private List<LotControlRule> lotControlRules = new ArrayList<LotControlRule>();
	
	//Index of the current processing part
	private int currentPartIndex = -1;
	//Index of current processing torque
	private int currentTorqueIndex = -1;
	
	//Keep track of all errors for current product
	private List<DataCollectionError> errorList = new ArrayList<DataCollectionError>();
	private String expectedProductId;
	private String expectedSubId;
	private String afOnSeqNo;
	private long productCount;
	private long lotSize;
	private int delayCount;
	private boolean specChanged;
	private boolean previousProductSkppedByCellOut;
	private List<String> pullOverProductList;
	private boolean isResetSequence = false;

	//Indicator that the user has chosen the REPAIR option
	private boolean repair;
	
	//Indicator that the broadcast invocation should be skipped
	private boolean skipBroadcast;
	
	public void resetAll(){
		action = Action.INIT;
		message = null;
		lotControlRules.clear();
		errorList.clear();
		repair = false;
		skipBroadcast = false;
		specChanged = false;
	}

	public void clearMessage() {
		message = null;
	}
	
	/**
	 * Notify state change observers
	 */
	public void notifyObservers(Object arg){
		super.notifyObservers(arg);
	}
	
	//Getter & setters
	public int getCurrentPartIndex() {
		return currentPartIndex;
	}

	public void setCurrentPartIndex(int currentPartIndex) {
		this.currentPartIndex = currentPartIndex;
	}
	
	public List<LotControlRule> getLotControlRules() {
		return lotControlRules;
	}

	public void setLotControlRules(List<LotControlRule> lotControlRules) {
		this.lotControlRules = lotControlRules;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public ProductBean getProduct() {
		return product;
	}

	public void setProduct(ProductBean product) {
		this.product = product;
	}
	
	public int getCurrentTorqueIndex() {
		return currentTorqueIndex;
	}

	public void setCurrentTorqueIndex(int currentTorqueIndex) {
		this.currentTorqueIndex = currentTorqueIndex;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public List<DataCollectionError> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<DataCollectionError> errorList) {
		this.errorList = errorList;
	}
	
	public String getExpectedProductId() {
		return expectedProductId;
	}

	public void setExpectedProductId(String expectedProductId) {
		this.expectedProductId = expectedProductId;
	}
	

	public String getExpectedSubId() {
		return expectedSubId;
	}

	public void setExpectedSubId(String expectedSubId) {
		this.expectedSubId = expectedSubId;
	}
	
	public int getDelayCount() {
		return delayCount;
	}

	public void setDelayCount(int delayCount) {
		this.delayCount = delayCount;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("productSpecCode:" + productSpecCode + ",");
		sb.append("action:" + action + ",");
		sb.append("currentPartIndex:" + currentPartIndex + ",");
		sb.append("currentTorqueIndex:" + currentTorqueIndex + ",");
		if(message != null){
			sb.append("msgType:" + message.getType() + ",");
			sb.append("msgId:" + message.getId() + ",");
			sb.append("userMsg:" + message.getDescription());
		}
		sb.append(System.getProperty("line.separator"));
		sb.append("product:" + product);
		sb.append(System.getProperty("line.separator"));
		sb.append("exceptions:" + getErrorList().size());
		return sb.toString();
	}

	public String getAfOnSeqNo() {
		return afOnSeqNo;
	}

	public void setAfOnSeqNo(String afOnSeqNo) {
		this.afOnSeqNo = afOnSeqNo;
	}
	
	
	public long getProductCount() {
		return productCount;
	}

	public void setProductCount(long count) {
		this.productCount = count;
	}

	public boolean isResetNextExpected() {
		return resetNextExpected;
	}

	public void setResetNextExpected(boolean resetNextExpected) {
		this.resetNextExpected = resetNextExpected;
	}	

	public boolean isRepair() {
		return repair;
	}

	public void setRepair(boolean repair) {
		this.repair = repair;
	}

	public boolean isSkipBroadcast() {
		return skipBroadcast;
	}

	public void setSkipBroadcast(boolean skipBroadcast) {
		this.skipBroadcast = skipBroadcast;
	}

	public void setLotSize(long lotSize) {
		this.lotSize = lotSize;
	}

	public long getLotSize() {
		return lotSize;
	}
	
	public boolean isSpecChanged() {
		return specChanged;
	}

	public void setSpecChanged(boolean specChanged) {
		this.specChanged = specChanged;
	}

	public boolean isPreviousProductSkppedByCellOut() {
		return previousProductSkppedByCellOut;
	}

	public void setPreviousProductSkppedByCellOut(boolean previousProductSkppedByCellOut) {
		this.previousProductSkppedByCellOut = previousProductSkppedByCellOut;
	}
	
	public List<String> getPullOverProductList() {
		
		return pullOverProductList;
	}
	
    public void setPullOverProductList(List<String> list) {
		
		this.pullOverProductList = list;
	}
    
    public boolean isResetSequence() {
		return isResetSequence;
	}
	
    public void setResetSequence(boolean isResetSequence) {
		this.isResetSequence = isResetSequence;
	}
	
}