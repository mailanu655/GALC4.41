package com.honda.galc.service.recipe;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.KnuckleDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.property.RecipePropertyBean;

/**
 * 
 * <h3>RecipeKnuckleSequenceHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeKnuckleSequenceHelper description </p>
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
 * <TD>Jan 4, 2013</TD>
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
 * @since Jan 4, 2013
 */
public class RecipeKnuckleSequenceHelper extends RecipeProductSequenceHelper {
	private Map<String, BaseProduct> firstLastMap = new HashMap<String, BaseProduct>();
	private String productionLotStr; 
	private static final String FIRST = "first";
	private static final String LAST = "last";
	
	
	public RecipeKnuckleSequenceHelper(ProductType productType, RecipePropertyBean property, Logger logger) {
		super(productType, property, logger);
	}

	@Override
	public boolean isLastInLot() {
		
		BaseProduct lastProduct = getProperty().isFirstOfSameSubid() ? 
				getLastProduct(product.getProductionLot(), product.getSubId()):
					getLastProduct(product.getProductionLot(), BaseProduct.SUB_ID_RIGHT);
					
		return product.getProductId().equals(lastProduct.getProductId());
	}

	
	@Override
	protected String nextProductId() {
		
		return getProperty().isFirstOfSameSubid() ? 
				super.nextProductId() : nextKnuckleId();
	}

	private String nextKnuckleId() {
		int position = getProductionLotPosition(product);
		String nextSubId = getNextSubId();
		BaseProduct nextStartProduct = getStartProductId(product.getProductionLot(), nextSubId);
		String token = getProductNumberDef().getSequence(nextStartProduct.getProductId());
		
		
		if(BaseProduct.SUB_ID_LEFT.equals(nextSubId)){
			return generateProductId(nextStartProduct, token, Integer.parseInt(token) + position);
		} else {
			return generateProductId(nextStartProduct, token, Integer.parseInt(token) + position -1);
		}
		
	}

	private String getNextSubId() {
		
		return product.getSubId().equals(BaseProduct.SUB_ID_LEFT) ? BaseProduct.SUB_ID_RIGHT : BaseProduct.SUB_ID_LEFT;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	protected BaseProduct getStartProductInNextLot(PreProductionLot preProductionLot){
		
		nextPreProductionLot = findNextProductionLot();
		
		if(nextPreProductionLot == null) return null;
		
		return getProperty().isFirstOfSameSubid() ? 
				getStartProductId(nextPreProductionLot.getProductionLot(), product.getSubId()) :
					getStartProductId(nextPreProductionLot.getProductionLot(), BaseProduct.SUB_ID_LEFT) ;
	}
	
	public KnuckleDao getProductDao(){
		return (KnuckleDao) productDao;
	}
	
	@Override
	protected String getStartProductId(PreProductionLot lot,String subId) {
		return getStartProductId(lot.getProductionLot(), subId).getProductId();
	}
	
	protected BaseProduct getStartProductId(String lot, String subId) {
		checkLotChange(lot);
		if(	firstLastMap.containsKey(FIRST + lot + subId)){
			return firstLastMap.get(FIRST + lot + subId);
		} else {
			BaseProduct firstProd = getProductDao().findFistProduct(lot, subId);
			firstLastMap.put(FIRST + lot + subId, firstProd);
			
			return firstProd;
		}
		
	}
	
	protected BaseProduct getLastProduct(String lot, String subId) {
		checkLotChange(lot);
		
		if(firstLastMap.containsKey(LAST + lot + subId)){
			return firstLastMap.get(LAST + lot + subId);
		} else {
			BaseProduct lastProd = getProductDao().findLastProduct(lot, subId);
			firstLastMap.put(LAST + lot + subId, lastProd);
			
			return lastProd;
		}
		
	}

	private void checkLotChange(String lot) {
		//clear cache when pre-production lot change
		if(!StringUtils.equals(lot, productionLotStr)){
			firstLastMap.clear();
			productionLotStr = lot;
		}
	}

	public int getProductionLotPosition(SubProduct knuckle) {
		if(product == null || knuckle == null) return -1;
		return getProductSequence(knuckle.getProductId()) - getProductSequence(getStartProductId(getEffectivePreProductionLot(), knuckle.getSubId())) +1;
	}

}
