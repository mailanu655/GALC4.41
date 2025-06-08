package com.honda.galc.service.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.property.RecipePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * <h3>RecipeProductSequenceHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeProductSequenceHelper description </p>
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
public class RecipeProductSequenceHelper {
	protected BaseProduct product;
	private ProductType productType;
	protected ProductDao<? extends BaseProduct> productDao;
	protected ProductNumberDef productNumberDef;
	private RecipePropertyBean property;
	private PreProductionLot preProductionLot;
	protected PreProductionLot nextPreProductionLot = null;
	private Logger logger;
	protected String processPointId;
	private ProductStampingSequenceDao productStampingSequenceDao;
	
	static Map<String, RecipeProductSequenceHelper> helperCache = new HashMap<String, RecipeProductSequenceHelper>();
	
	//Preproduction lots/small lots on the same KD lots 
	private List<PreProductionLot> preproductionLotList = new ArrayList<PreProductionLot>();
	
	protected PreProductionLotDao preProductionLotDao;
	

	public RecipeProductSequenceHelper(ProductType productType, RecipePropertyBean property, Logger logger) {
		this.productType = productType;
		this.property = property;
		this.logger = logger;
		init();
	}

	public RecipeProductSequenceHelper(ProductType productType, RecipePropertyBean property, Logger logger, String ppId) {
		this(productType, property, logger);
		this.processPointId = ppId;
	}

	private void init() {
		productDao = ProductTypeUtil.getTypeUtil(productType).getProductDao();
	}

	public static RecipeProductSequenceHelper getHelper(String ppId, ProductType type, RecipePropertyBean property, Logger logger){
		if(!helperCache.keySet().contains(ppId)){
			switch(type){
			case KNUCKLE:
				helperCache.put(ppId, new RecipeKnuckleSequenceHelper(type, property, logger));
				break;
			case FRAME_JPN:
			case FRAME:
				helperCache.put(ppId, new FrameRecipeProductSequenceHelper(type, property, logger, ppId));
				break;
			default:
				helperCache.put(ppId, new RecipeProductSequenceHelper(type, property, logger));
			}
		}  
		return helperCache.get(ppId);
	}
	

	public <T extends BaseProduct> T nextProduct(String productId){
		product = productDao.findByKey(productId);
		nextPreProductionLot = null;
		
		if(product == null)
			return null;
		
		//Need to handle cluster - make sure the production lots are up to date
		syncPreproductionLots();
		
		if(isLastInLot()){
			return this.<T> getStartProductInNextLot(getPreProductionLot());
		} else {
			return this.<T> nextProduct();
		} 
		
	}
	
	public ProductStampingSequence nextProductForVinStamping(String productId){
		product = productDao.findByKey(productId);
		if(product == null) return null;
		
		ProductStampingSequence currentSeq = getProductStampingSequenceDao().findById(product.getProductionLot(),productId);
		
		ProductStampingSequence nextSeq;
		if(getPreProductionLot().getLotSize() == currentSeq.getStampingSequenceNumber()) {
			nextPreProductionLot = findNextProductionLot();
			if(nextPreProductionLot == null) {
				getLogger().info("WARN: The next production lot is null. Current lot:" + preProductionLot.getProductionLot());
				return null;
			}
			nextSeq = getProductStampingSequenceDao().findNextProduct(nextPreProductionLot.getProductionLot(), 0);
			
		} else
			nextSeq = getProductStampingSequenceDao().findNextProduct(product.getProductionLot(), currentSeq.getStampingSequenceNumber());
		
		Boolean isScrapped = checkScrap(nextSeq.getProductId());
		if(isScrapped == true) {
			getLogger().info("This Product[" + nextSeq.getProductId() + "] has been scrapped. Getting next Product.");
			nextSeq = this.nextProductForVinStamping(nextSeq.getProductId());
		}
		
		return nextSeq;
	}

	private Boolean checkScrap(String nextProductId) {
		Boolean isScrapped = false;
		product = productDao.findByKey(nextProductId);
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		List<ExceptionalOut> scrappedExceptionalOutList =  productCheckUtil.checkScrappedExceptionalOut();
		if(scrappedExceptionalOutList != null && scrappedExceptionalOutList.size() > 0) {
			isScrapped = true;
		}
		return isScrapped;
	}
	
	private void syncPreproductionLots() {
		if(isPreproductionLotsInSync()) return;
		//refresh preproduction lots
		preproductionLotList = getPreProductionLotDao().findAllWithSameKdLotFromProductionLot(product.getProductionLot());
	}

	private boolean isPreproductionLotsInSync() {
		
		if(getPreproductionLotList() == null) return false;
		
		for(PreProductionLot lot : getPreproductionLotList())
			if(lot.getProductionLot().equals(product.getProductionLot()))
					return true;
			
		return false;
	}

	@SuppressWarnings("unchecked")
	protected <T extends BaseProduct> T getStartProductInNextLot(PreProductionLot preProductionLot) {
		
		nextPreProductionLot = findNextProductionLot();
		if(nextPreProductionLot == null) {
			getLogger().info("WARN: The next production lot is null. Current lot:" + preProductionLot.getProductionLot());
			return null;
		}
		T startProduct = (T)productDao.findByKey(getStartProductId(nextPreProductionLot, (product==null? null : product.getSubId())));
		
		getLogger().info("Next production lot:" + nextPreProductionLot.getProductionLot() + " start product Id:" +
				(startProduct == null ? "null" : startProduct.getProductId()));
		
		return startProduct;
	}

	protected PreProductionLot findNextProductionLot() {

		//find next small lot
		PreProductionLot nextLot = nextSmallLot();

		if(nextLot == null){
			//refresh current preproduction lot - update for lot moved after recipe down-loaded for current lot
			preProductionLot = getPreProductionLotDao().findByKey(product.getProductionLot());
			if(preProductionLot.getNextProductionLot() != null){
				nextLot = getPreProductionLotDao().findByKey(preProductionLot.getNextProductionLot());
				preproductionLotList = getPreProductionLotDao().findAllWithSameKdLotFromProductionLot(nextLot.getProductionLot());
			}
		}
		
		return nextLot;
	}


	protected void handleException(String productId, RecipeErrorCode recipeErrorCode) {
		throw new TaskException(recipeErrorCode.getDescription()+ productId, recipeErrorCode.getCode());
	}
	

	protected String getStartProductId(PreProductionLot lot,String subId) {
		return ServiceUtil.isCheckDigitNeeded(getProductType().name(), lot.getProductSpecCode()) ? 
					updateCheckDigit(lot.getStartProductId()) : lot.getStartProductId();
	}
	
	@SuppressWarnings("unchecked")
	private <T extends BaseProduct> T nextProduct() {
		return (T)productDao.findByKey(nextProductId());
	}
	
	protected String nextProductId() {
		String token = getProductNumberDef().getSequence(product.getProductId());
		int newSeq = Integer.parseInt(token) + 1;
		
		return generateProductId(product, token, newSeq);
	}

	protected String generateProductId(BaseProduct productTemplate,String token, int newSeq) {
		String format ="%1$0" + token.length()+ "d";
		String nextId = productTemplate.getProductId().substring(0, getProductNumberDef().getLength() - token.length()) + 
		       String.format(format, newSeq);
		return ServiceUtil.isCheckDigitNeeded(getProductType().name(), getPreProductionLot().getProductSpecCode()) ? 
				updateCheckDigit(nextId) : nextId;
	}
	
	protected String updateCheckDigit(String productId) {
		StringBuilder builder = new StringBuilder();
		builder.append(productId.substring(0,8));
		builder.append(ProductDigitCheckUtil.calculateVinCheckDigit(productId));
		builder.append(productId.substring(9));
		return builder.toString();
	}

	public boolean isLastInLot(){
		int position = getProductionLotPosition(product);
		return position == getPreProductionLot().getLotSize();
	}


	public ProductNumberDef getProductNumberDef() {
		if(productNumberDef == null){
			productNumberDef = ProductNumberDef.getProductNumberDef(getProductType()).get(0);
		}
		
		return productNumberDef;
	}
	
	public PreProductionLot nextSmallLot(){
		for(PreProductionLot lot :getPreproductionLotList())
			if(lot.getProductionLot().equals(preProductionLot.getNextProductionLot()))
				return lot;
		
		return null;
	}
	
	public int getProductionLotPosition(BaseProduct prod){
		if(product == null) return -1;
		return getProductSequence(prod.getProductId()) - getStartProductSequence(getEffectivePreProductionLot(), prod.getSubId()) +1;
	}
	
	/**
	 * KD Lot position of the product in the current KD lot
	 * @param productId
	 * @return
	 */
	public int getKdLotPosition(BaseProduct prod) {
		return getPreviousSmallLotSize() + getProductionLotPosition(prod);
	}

	/**
	 * Total lot size of the previous small lots 
	 * @return
	 */
	private int getPreviousSmallLotSize() {
		
		int count = 0;
		
		for(PreProductionLot lot : preproductionLotList){
			
			if(lot.getProductionLot().equals(getEffectivePreProductionLot().getProductionLot()))
				break;
			else
				count += lot.getLotSize();
		}
		
		return count;
	}
	
	// ------------ getters & setters ----------------
	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public ProductType getProductType() {
		return productType;
	}

	public RecipePropertyBean getProperty() {
		return property;
	}
	

	protected PreProductionLotDao getPreProductionLotDao() {
		if(preProductionLotDao == null)
			preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		return preProductionLotDao;
	}

	public ProductDao<? extends BaseProduct> getProductDao() {
		return productDao;
	}

	public PreProductionLot getPreProductionLot() {
		if(product == null)
			return null; // return null if the product is null because the PreProductionLot should match the product's production lot
		if(preProductionLot == null || !preProductionLot.getProductionLot().equals(product.getProductionLot()))
			preProductionLot = getPreProductionLotDao().findByKey(product.getProductionLot());
		return preProductionLot;
	}
	
	
	public PreProductionLot getEffectivePreProductionLot(){
		return getNextPreProductionLot() != null ? getNextPreProductionLot() : getPreProductionLot();
	}
	
	protected Logger getLogger(){
		return this.logger;
	}

	public int getProductSequence(String productId) {
		String token = getProductNumberDef().getSequence(productId);
		return Integer.parseInt(token);
		
	}

	public int getStartProductSequence(PreProductionLot lot, String subId) {
		return getProductSequence(getStartProductId(lot, subId));
	}
	
	public List<PreProductionLot> getPreproductionLotList() {
		return preproductionLotList;
	}

	public void setPreproductionLotList(List<PreProductionLot> preproductionLots) {
		this.preproductionLotList = preproductionLots;
	}

	public void setPreProductionLot(PreProductionLot preProductionLot) {
		this.preProductionLot = preProductionLot;
	}

	public PreProductionLot getNextPreProductionLot() {
		return nextPreProductionLot;
	}

	public void setNextPreProductionLot(PreProductionLot nextPreProductionLot) {
		this.nextPreProductionLot = nextPreProductionLot;
	}

	public int getKdLotSize() {
		int size = 0;
		if(preproductionLotList == null ) return size;
		
		for(PreProductionLot lot:  preproductionLotList)
			size += lot.getLotSize();
		return size;
	}

	public BaseProduct getNextInProcessProduct(String productId) {
		product = productDao.findNextInprocessProduct(productId);
		return product;
	}

	public Integer getLineRefNumber() {
		if(product.getClass() != (Frame.class)) return null;
		return ((Frame)product).getLineRef(getProperty().getLineRefNumberOfDigits());
	}
	
	public BaseProduct getProduct(String productId) {
		product = getProductDao().findByKey(productId);
		return product;
	}

	public ProductStampingSequenceDao getProductStampingSequenceDao() {
		if(productStampingSequenceDao == null)
			productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		return productStampingSequenceDao;
	}
	
	

}
