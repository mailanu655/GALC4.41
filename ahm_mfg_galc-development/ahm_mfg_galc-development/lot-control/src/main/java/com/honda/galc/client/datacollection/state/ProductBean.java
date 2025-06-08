package com.honda.galc.client.datacollection.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.MasterEntityList;
/**
 * <h3>ProductBean</h3>
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
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class ProductBean implements Serializable{
	private static final long serialVersionUID = 1L;

	/* Current Product Id */
	private String productId;
	
	/* Product Spec YMTO/MTOC */
	private String productSpec;
	
	private String subId;
	
	/* Expected Product Id */
	private String expectedProductId;
	
	/* AF ON SEQ NO*/
	private String afOnSequenceNumber;
	
	/* Product Hold Status */
	private String productHoldStatus;
	
	/* indicate if the product id passed validation check */
	private boolean validProductId = false;
	
	private boolean skipped = false;
	
	private String kdLotNumber;
	
	private String productionLot;
	private boolean missingRequiredPart = false;
	
	private BaseProduct product;
	private boolean skippedByCellOut;
	
	/* All the part install in this product */
	private List<InstalledPart> partList = new ArrayList<InstalledPart>();
	private List<InstalledPart> derivedPartList = new ArrayList<InstalledPart>();
	private List<String> missingRequiredPartList;

	private MasterEntityList<EntityList<AbstractEntity>> masterEntityList = new MasterEntityList<EntityList<AbstractEntity>>();
	
	private MasterEntityList<EntityList<AbstractEntity>> afterTrackingMasterEntityList = new MasterEntityList<EntityList<AbstractEntity>>();
	
	public Stack<EntityList<AbstractEntity>> getMasterEntityList() {
		return masterEntityList;
	}
	
	public boolean masterListEntitiesIsEmpty(){
		return masterEntityList.isEmpty();
	}
	
	public void setMasterEntityList(EntityList<AbstractEntity> entityList) {
		if(!entityList.isEntityListEmpty()){
			this.masterEntityList.push(entityList);
		}	
	}
	
	public void clearMasterList(){
		masterEntityList.clear();
	}
	
	public Stack<EntityList<AbstractEntity>> getAfterTrackingMasterEntityList() {
		return afterTrackingMasterEntityList;
	}
	
	public boolean afterTrackingMasterListEntitiesIsEmpty(){
		return afterTrackingMasterEntityList.isEmpty();
	}
	
	public void setAfterTrackingMasterEntityList(EntityList<AbstractEntity> entityList) {
		if(!entityList.isEntityListEmpty()){
			this.afterTrackingMasterEntityList.push(entityList);
		}	
	}
	
	public void clearAfterTrackingMasterList(){
		afterTrackingMasterEntityList.clear();
	}
	
	//Getter & Setters
	public String getExpectedProductId() {
		return expectedProductId;
	}
	public void setExpectedProductId(String expectedProductId) {
		this.expectedProductId = StringUtils.trim(expectedProductId);
	}
	public String getProductHoldStatus() {
		return productHoldStatus;
	}
	public void setProductHoldStatus(String productHoldStatus) {
		this.productHoldStatus = productHoldStatus;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = StringUtils.trim(productId);
	}
	public String getProductSpec() {
		return productSpec;
	}
	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
		Logger.getLogger().info("Product Spec Code is: ", productSpec);
	}
	public boolean isValidProductId() {
		return validProductId;
	}
	public void setValidProductId(boolean validProductId) {
		this.validProductId = validProductId;
	}
	public List<InstalledPart> getPartList() {
		return partList;
	}
	public void setPartList(List<InstalledPart> parts) {
		this.partList = parts;
	}
	public List<InstalledPart> getDerivedPartList() {
		return derivedPartList;
	}
	public void setDerivedPartList(List<InstalledPart> dparts) {
		this.derivedPartList = dparts;
	}
	public boolean isSkipped() {
		return skipped;
	}
	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}
	
	public boolean isMissingRequiredPart() {
		return missingRequiredPart;
	}
	public void setMissingRequiredPart(boolean missingRequiredPart) {
		this.missingRequiredPart = missingRequiredPart;
	}
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("productId:" + productId + ",");
		sb.append("productSpec:" + productSpec + ",");
		sb.append("expectedProdId:" + expectedProductId + ",");
		sb.append("holdstatus:" + productHoldStatus + ",");
		sb.append("skipped:" + skipped + ",");
		sb.append("validProductId:" + validProductId).append(System.getProperty("line.separator"));
		sb.append("parts:[").append(System.getProperty("line.separator"));
		for(InstalledPart p : partList){
			if (p != null ) 
				sb.append(p.toString()).append(System.getProperty("line.separator"));
			else 
				sb.append("null");
		}
		sb.append("]");
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
		
	}
	
	public void resetProductState(){
		productId = null;
		productSpec = null;
		expectedProductId = null;
		productHoldStatus = null;
        validProductId = false;
	}
	
	public void resetAll() {
		resetProductState();
		
	}
	
	public String getSubId() {
		return subId;
	}
	
	public void setSubId(String subId) {
		this.subId = subId;
	}
	
	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	
	public void setMissingRequiredParts(List<String> missingRequiredParts) {
		this.missingRequiredPartList = missingRequiredParts;
		
	}

	public List<String> getMissingRequiredPartList() {
		return missingRequiredPartList;
	}
	
	public String getMissingRequiredPartMessage() {
		final String Missing_Required_Part = "Missing Required Part ";
		String msg = getMissingRequiredPartList().toString();
		return Missing_Required_Part + msg.replace(" ", "");
	}
	
	public String getProductionLot(){
		return productionLot;
	}
	
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
		
	}
	
	public String getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}

	public void setAfOnSequenceNumber(String afOnSeqNo) {
		this.afOnSequenceNumber = afOnSeqNo;
		Logger.getLogger().info("Retrieved AF ON Sequence Number is "+afOnSequenceNumber);
	}
	
	public BaseProduct getBaseProduct(){
		return this.product;
	}
	
	public void setBaseProduct(BaseProduct product) {
		this.product = product;
	}

	public void setSkippedByCellOut(boolean b) {
		this.skippedByCellOut = b;
		
	}
	
	public boolean isSkippedByCellOut() {
		return this.skippedByCellOut;
		
	}

	
}
