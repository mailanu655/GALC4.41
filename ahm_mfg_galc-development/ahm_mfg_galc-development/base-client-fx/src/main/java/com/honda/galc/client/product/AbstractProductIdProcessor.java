package com.honda.galc.client.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * 
 * <h3>AbstractProductIdProcessor Class description</h3>
 * <p> AbstractProductIdProcessor description </p>
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
 * @author Jeffray Huang<br>
 * Feb 02, 2016
 *
 *
 */
public abstract class AbstractProductIdProcessor implements IProductIdProcessor{
	
	protected Map<String, Object> messages;
	
	private ProductController productController;
	
	public AbstractProductIdProcessor(ProductController productController) {
		this.productController = productController;
		EventBusUtil.register(this);
	}
	
	@Override
	public String convert(String inputNumber) {
			return inputNumber;
	}
	
	@Override
	public Map<String,Object> validate(String inputNumber) {
		
		String productId = convert(inputNumber);
		String[] checkTypes = getCheckTypes();
		BaseProduct product = ProductTypeUtil.findProduct(getProductController().getModel().getProductTypeData().getProductTypeName(), productId);
		if (product == null) {
			return new HashMap<String, Object>();
		} else {
			return ProductCheckUtil.check(product, getProductController().getModel().getProcessPoint(), checkTypes);
		}		
	}
	
	@Override
	public void productReset(ProductEvent event) {
		throw new UnsupportedOperationException("productReset is not implemented for " + this.getClass());
	}
	
	private String[] getCheckTypes() {
		ProductCheckPropertyBean propertyBean= PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProductController().getModel().getProcessPointId());
		String[] productInputCheckTypes = propertyBean.getProductInputCheckTypes();
		List<String> types = new ArrayList<String>(Arrays.asList(productInputCheckTypes));
		if(getProductController().getModel().getProperty().isCheckProcessedProduct()) {
			types.add(ProductCheckType.PROCESSED_PRODUCT_CHECK.toString());
		}
		return types.toArray(new String[types.size()]);
	}
	
	protected void removePrefixForFrame() {
		String inputNumber = getProductController().getView().getInputPane().getProductId();		
		removePrefixForFrame(inputNumber);
	}
	
	protected void removePrefixForFrame(String inputNumber) {
		getProductController().getLogger().info(String.format("Start processing InputNumber: %s", inputNumber));
		
		if (inputNumber != null) {
			inputNumber = inputNumber.trim();
			if (getProductController().getModel().getProperty().isRemoveIEnabled()) {
				if (getProductController().getModel().getProductType().equals("FRAME")) {
					inputNumber = removeLeadingVinChars(inputNumber);
				}
			}
			getProductController().getView().getInputPane().setProductId(inputNumber);	
		}
				
	}
	
	protected void removeExtraDataFromScan() {
		String inputNumber = getProductController().getView().getInputPane().getProductId();
		
		if (inputNumber != null) {
			inputNumber = inputNumber.trim();
	
			if (getProductController().getModel().getProductType().equals(ProductType.FRAME.name()) || getProductController().getModel().getProductType().equals(ProductType.ENGINE.name())) {
				inputNumber = removeTrailingVinChars(inputNumber);
			}
			getProductController().getView().getInputPane().setProductId(inputNumber);
		}
	}
	
	@Override
	public boolean validateInputNumber() {
		String inputNumber = getProductController().getView().getInputPane().getProductId();
		messages = validate(inputNumber);
		return messages.isEmpty();
	}
	
	protected BaseProduct validateAndCreateBaseProduct() {
		removePrefixForFrame();
		removeExtraDataFromScan();
		boolean isValidVin = validateInputNumber();
		boolean productIdScanField = PropertyService.getPropertyBoolean(getProductController().getProcessPointId(), "PRODUCT_ID_SEARCH_FROM_SCAN_FIELD", false);
		 
		if(productIdScanField==false) {
	
		
		if(!isValidVin){
			Object firstObject = messages.values().iterator().next();
			Object object = (firstObject instanceof List) ? ((List<?>) firstObject).get(0) : firstObject;
			getProductController().getView().setErrorMessage((ObjectUtils.toString(object)), getProductController().getView().getInputPane().getProductIdField());
			getProductController().getAudioManager().playNGSound();
			return null;
		}else{
			
			return getProductController().getModel().findProduct(getProductController().getView().getInputPane().getProductId());
		}
		
		} else {
			if(!isValidVin){
				getProductController().getProductInputPane().productIdButton.fire();
				return null;
			} else{
				
				return getProductController().getModel().findProduct(getProductController().getView().getInputPane().getProductId());
			} 
		}
		
	}
	
	public String removeLeadingVinChars(String productId){
		String leadingVinChars = getProductController().getView().getApplicationPropertyBean().getLeadingVinCharsToRemove();
		if(StringUtils.isNotBlank(leadingVinChars)){
		String[] vinChars = leadingVinChars.trim().split(",");
		
			for(String c:vinChars){
				if (productId.toUpperCase().startsWith(c)) {
					return productId.substring(c.length());
				}
			}
		}
		return productId;
	}
	
	public String removeTrailingVinChars(String productId){
		int truncAmt = getProductController().getView().getPropertyInt("MAX_PRODUCT_ID_LENGTH",0);
		int lenProdId = productId.length();
		if(lenProdId > truncAmt && truncAmt != 0) {
			if(truncAmt > 255) {
				truncAmt = 255;
			}
			productId = productId.substring(0, truncAmt);
		}
		return productId;
	}
	/**
	 * @return the productController
	 */
	public ProductController getProductController() {
		return productController;
	}

	/**
	 * @param productController the productController to set
	 */
	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
}
