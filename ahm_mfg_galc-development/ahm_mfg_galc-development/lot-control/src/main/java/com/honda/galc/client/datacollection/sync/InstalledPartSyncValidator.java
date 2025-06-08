package com.honda.galc.client.datacollection.sync;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.LotControlRuleFlag;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * <h3>InstalledPartSyncValidator</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InstalledPartSyncValidator description </p>
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
 * Mar 25, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class InstalledPartSyncValidator {
	protected List<LotControlRule> lotControlRules = new ArrayList<LotControlRule>();
	protected String productId;
	protected String productSpecCode;
	protected int partIndex;
	ClientContext context;
	public static final String INVALID_PRODUCT_MARK = "*";
	public static final String EXCEPTION_MARK = "**";
	private TerminalPropertyBean property;
	
	public InstalledPartSyncValidator(ClientContext context) {
		super();
		this.context = context;
		
		init();
	}

	private void init() {
		property = context.getProperty();
		
	}

	public void validateInstalledPart(List<InstalledPart> installedParts){

		try {
			
			if (!validateProductId(installedParts))
				return;
			
			validatePartSerialNumbers(installedParts);
		}catch (ServiceTimeoutException ste) {
			throw ste;
		}catch (ServiceInvocationException sie){
			throw sie;
		}catch (TaskException te){
			Logger.getLogger().warn("[SyncError]" + te.getMessage(), getErrorMessage(installedParts));
			updateInstalledPartsForInvalidProductId(installedParts, EXCEPTION_MARK, "validation exception");
			
		}catch (Exception e) {
			Logger.getLogger().warn(e, "SyncError]" + getErrorMessage(installedParts));
			updateInstalledPartsForInvalidProductId(installedParts, EXCEPTION_MARK, "validation exception");
			
		}

	}
	
	private String getErrorMessage(List<InstalledPart> installedParts) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName()).append(" ");
		sb.append(installedParts.size() > 0 ? installedParts.get(0).getId().getProductId() : "null");
		return sb.toString();
	}

	private void validatePartSerialNumbers(List<InstalledPart> installedParts) {
		for(int i = 0; i < installedParts.size(); i++){
			if(lotControlRules.get(i).getVerificationFlag() == LotControlRuleFlag.ON.getId()){
				validatePartSerialNumber(installedParts.get(i));
			}
		}
	}

	private void validatePartSerialNumber(InstalledPart part) {
		List<InstalledPart> duplicatedParts = context.getDbManager().findDuplicatePartsByPartName(part.getId().getPartName(), part.getPartSerialNumber());
		Set<InstalledPart> myParts = new HashSet<InstalledPart>();

		 
		for(Iterator< InstalledPart > it = duplicatedParts.iterator(); it.hasNext();){
			InstalledPart p = it.next();
			if(p.getId() != null && productId.equals(p.getId().getProductId()))
				myParts.add(p);
		}
		
		//remove part of the same product
		if(myParts.size() > 0)	duplicatedParts.removeAll(myParts);
		
		if(duplicatedParts.size() > 0){
			part.setInstalledPartStatus(InstalledPartStatus.NG);
			part.setInstalledPartReason(StringUtils.isEmpty(part.getInstalledPartReason()) ? "duplicate" : part.getInstalledPartReason() + " : duplicate");
			
			Logger.getLogger().warn("[SyncError] Part:" + part.getPartSerialNumber() + " already installed on:" + getDuplicatedPartProductIds(duplicatedParts));
		}
	}
	
	
	private String getDuplicatedPartProductIds(List<InstalledPart> duplicatedParts) {
		StringBuilder sb = new StringBuilder();
		for(InstalledPart p : duplicatedParts){
			if(sb.length() > 0) sb.append(",");
			sb.append(p.getId().getProductId());
		}
		return sb.toString();
	}

	private boolean validateProductId(List<InstalledPart> installedParts) {
		boolean result = true;
		productId = installedParts.get(0).getId().getProductId();
		
		//Check marked invalid product
		if(productId.startsWith("*"))
			return false;
		
		//Product Id is already check even off line, so this may never happen, just in case...
		if(StringUtils.isEmpty(productId))
		{
			throw new TaskException("Product Id is Empty:" + productId + " - " + getInstalledPartDetails(installedParts));
		}
		
		if(productId.length() != property.getMaxProductSnLength()){
			throw new TaskException("Product Id length error:" + productId.length() + " - " + getInstalledPartDetails(installedParts));
		}
		
		BaseProduct product = getProductIdOnServer();
		
		/*
		 * Need to confirm - for now throw exception and log details. Manually check log and recovery may necessary.
		 */
		if(product == null)
		{
			throw new TaskException("Exception: product not exist:" + productId + " - "+ getInstalledPartDetails(installedParts));
		}
		
		//re-load Lot control rule only if product spec code changed 
		if(!product.getProductSpecCode().equals(productSpecCode)){
			productSpecCode = product.getProductSpecCode();
			lotControlRules = loadLotControlRules();
		}
		
		if(property.isCheckProcessedProduct() && !checkProcessedProduct()){
			result = false;
			String msg = "Exception: product was already processed:" + productId + " - "+ getInstalledPartDetails(installedParts);
			Logger.getLogger().warn(msg, this.getClass().getSimpleName());
			
			updateInstalledPartsForInvalidProductId(installedParts, INVALID_PRODUCT_MARK, "processed product");
		}
		
		return result;
	}

	private void updateInstalledPartsForInvalidProductId(List<InstalledPart> installedParts, String mark, String installedReason) {
		for(InstalledPart p : installedParts){
			String newProductId = mark + p.getId().getProductId().substring(mark.length());
			String msg = "[SyncWarn] Replace productId:" + p.getId().getProductId() + " with " + newProductId + " partName:" + p.getId().getPartName();
			p.getId().setProductId(newProductId);
			p.setInstalledPartReason(StringUtils.isEmpty(p.getInstalledPartReason())? installedReason : p.getInstalledPartReason() + " : " + installedReason);
			Logger.getLogger().warn(msg);
		}
		
	}

	private List<LotControlRule> loadLotControlRules() {
		ProductSpec productSpec = getProductSpec();
		return LotControlPartUtil.getLotControlRuleByProductSpec(productSpec, context.getAllRules());
		
	}

	private boolean checkProcessedProduct() {
		return (!context.getDbManager().isProcessed(productId, getPartNamesString()));
		
	}

	private List<String> getPartNamesString() {
		List<String> list = new ArrayList<String>();
        for (int i = 0; i < lotControlRules.size(); i++) {

            list.add(lotControlRules.get(i).getPartName().getPartName());
        }
        return list;
	}

	protected BaseProduct getProductIdOnServer(){
		return context.getDbManager().confirmProductOnServer(productId);
	}
	
	protected abstract ProductSpec getProductSpec();

	private String getInstalledPartDetails(List<InstalledPart> installedParts) {
		StringBuilder sb = new StringBuilder();
		for(InstalledPart p : installedParts){
			sb.append(p.toString()).append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	//Getters & Setters
	public List<LotControlRule> getLotControlRules() {
		return lotControlRules;
	}
	public void setLotControlRules(List<LotControlRule> lotControlRules) {
		this.lotControlRules = lotControlRules;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductSpecCode() {
		return productSpecCode;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public int getPartIndex() {
		return partIndex;
	}
	public void setPartIndex(int partIndex) {
		this.partIndex = partIndex;
	}
}
