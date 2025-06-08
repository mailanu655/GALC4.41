package com.honda.galc.client.datacollection.processor;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.MbpnUtil;

/**
 * 
 * <h3>MbpnProductIdProcessor</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnProductIdProcessor description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Feb 23, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 23, 2017
 */
public class MbpnProductIdProcessor extends ProductIdProcessor {

	private List<MbpnProductType> mbpnProductTypes;
	
	
	public MbpnProductIdProcessor(ClientContext context) {
		super(context);
	}
	
	public Mbpn findProductSpec(String productSpecCode) {
		MbpnDao mbpnSpecDao = ServiceFactory.getDao(MbpnDao.class);
		Mbpn mbpnSpec = mbpnSpecDao.findByProductSpecCode(productSpecCode, context.getProperty().getProductType());
		return mbpnSpec;
		
	}

	@Override
	public String getProductSpecCode(String productId) {
		ProductType productType = context.getProductType();
		MbpnProduct mbpnProduct = (MbpnProduct) ProductTypeUtil.findProduct(productType.toString(), productId);
		return ProductTypeUtil.getProductSpecDao(productType).findByProductSpecCode(mbpnProduct.getModelCode(), productType.toString()).getProductSpecCode();
	}
	
	@SuppressWarnings("unused")
	private boolean isNeedToloadMbpnProductNumberDef(){
		return getController().getState().getProductSpecCode() == null || 
					!getController().getState().getProductSpecCode().equals(product.getProductSpec());
	}
	
	/** NALC-1574-MAX_PRODUCT_SN_LENGTH is set to 17 ( I cannot add both 17 and length 11)*/
	@Deprecated
	public void checkProductIdLength(){
		// Check both product Id length and mask
		if(MbpnUtil.checkProductId(product.getProductId(), getMbpnProductTypes()))
				return;
		
		String msg = "Invalid Product Id: " + product.getProductId() + ", not match to Product Number Defs";
		handleException(msg);
		
		
	}

	protected List<MbpnProductType> getMbpnProductTypes() {
		if(mbpnProductTypes == null || isNeedToloadMbpnProductNumberDef())
			mbpnProductTypes = ServiceFactory.getDao(MbpnProductTypeDao.class).findAllByProductType(context.getProperty().getProductType());
		return mbpnProductTypes;
	}
	
	public void validateProductType(MbpnProduct aproduct){
		
		if(aproduct.getMbpn() == null || aproduct.getMbpn().getMbpnProductType() == null || 
		   !context.getProperty().getProductType().equals(aproduct.getMbpn().getMbpnProductType().getProductType())){
			
			String typeFromProduct = " product type not configured ";
			try {
				typeFromProduct= aproduct.getMbpn().getMbpnProductType().getProductType();
			} catch(Exception e) {
				; //ok if not configured properly
			}
			
			
			String msg = "Invalid Product Id - product type:" + typeFromProduct + "is not matching to the product type:" + context.getProperty().getProductType();
			handleException(msg);
		}
		
	}
	
	/**
	 * Check if the product Id exists in database and is valid.
	 * @return
	 * @throws Exception 
	 */
	protected boolean confirmProdIdOnServer() throws Exception{
		Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");

		BaseProduct aproduct = getProductFromServer();
		product.setProductionLot(aproduct.getProductionLot());
		product.setSubId(aproduct.getSubId());
			
		validateProductType((MbpnProduct)aproduct);
		//must done before productIdOk
		loadLotControlRule();

		validateProduct(aproduct);
		
		doRequiredPartCheck();
		

		Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		return true;
	}
	
}
