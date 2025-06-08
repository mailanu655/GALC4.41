package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TrackException;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationDao;
import com.honda.galc.dao.conf.MCOperationPartDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.MCOperation;
import com.honda.galc.entity.conf.MCOperationPart;
import com.honda.galc.entity.conf.MCOperationPartId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.HeadlessDCInfoCode;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

public class MbpnMadeFromService extends MbpnDataCollector{

	@Autowired
	public PreProductionLotDao preproductionLotDao;
	
	@Autowired
	public MbpnProductDao mbpnProductDao;
	
	@Autowired
	public MCOperationDao mCOperationDao;
	
	@Autowired
	public MCOperationRevisionDao mCOperationRevisionDao;
	
	@Autowired
	public MCOperationPartDao mCOperationPartDao;
	
	@Autowired
	public  MCOperationPartRevisionDao mCOperationPartRevisionDao;
	
	@Autowired
	public DefectResultDao defectResultDao;
	
	@Autowired
	public ProductStampingSequenceDao productStampingSequenceDao;
	
	
	String productId = null;
	String productSpecCode = null;
	String isSaveMbpnProduct = null;
	String planCode = null;
	@Override
	public void collectData() {
		productId = context.getProductId();
		productSpecCode = (String)context.getTagValue(TagNames.PRODUCT_SPEC_CODE.name());
		// GET productionLot
		String productionLot = (String)context.getTagValue(TagNames.PRODUCTION_LOT.name());
		
		planCode = (String)context.getTagValue(TagNames.PLAN_CODE.name());
		String operationName = (String)context.getTagValue(TagNames.PRODUCT_NAME.name());
		String partId = (String)context.getTagValue(TagNames.PART_ID.name());
		
		isSaveMbpnProduct = (String)context.getTagValue(TagNames.IS_SAVE_MBPN_PRODUCT.name());
		
		//check whether product id is in the format of "PLANT(1)-DEPT(1)-YYYYMMDD-HHMMSS", Example: 'YW20131119230759'
		if(!validateMBPNProductId(productId)){
			collectorWork.prepareInfoCode(HeadlessDCInfoCode.INVALID_PRODUCT);
			return;
		}
		//check whether product id has defects 
		if(getProperty().isCheckOutstandingDefect() && getDefectResultList(productId) != null && getDefectResultList(productId).size() >0 ) {
			collectorWork.prepareInfoCode(HeadlessDCInfoCode.OUTSTANDING);
			return;
		}
		
		//check product exist use product checker
		if(!validateProduct(productId, partId, productionLot, planCode, productSpecCode, operationName)){
			return;
		}
		
		// collect install part and measurement of mbpn product
		String mbpnInstallParts = getProperty().getMbpnInstalledPartNames();
		if(StringUtils.isNotEmpty(mbpnInstallParts))
			context.extractResultsForMbpnProduct(convertToList(mbpnInstallParts));
	}

	@Override
	public void saveBuildResults() {
		
		// value of IS_SAVE_MBPN_PRODUCT verify if save
		if(StringUtils.isEmpty(isSaveMbpnProduct) || !isSaveMbpnProduct.equals("1")){
			return;
		}
		
		// save mbpn product
		saveMbpnProduct(productId, productSpecCode);
		// save install parts and measurements
		if(getBuildResults() == null || getBuildResults().size() == 0){
			getLogger().info("There is no build result to save for product:", context.getProductId());
			return;
		}
		
		//remove part place holders
		List<ProductBuildResult> emptyParts = new ArrayList<ProductBuildResult>();
		for(ProductBuildResult part : getBuildResults())
			if(!((InstalledPart)part).hasData()) emptyParts.add(part);
		
		if(emptyParts.size() > 0) //missing data for some parts 
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			
		getBuildResults().removeAll(emptyParts);
		
		saveAllInstalledParts(getInstalledParts());
		collectorWork.prepareInfoCode(HeadlessDCInfoCode.SAVE_SUCCESS);
		getLogger().info("save build results:", getBulidResultLogString(getBuildResults()));
		
	}
	
	
	
	private void saveMbpnProduct(String productId, String productSpecCode){
		// save mbpn product
		PreProductionLot prodLot = getCurrentProductionLot();
		ProductStampingSequence pss = getNextScheduledProduct(prodLot);

		if (isProductAlreadyAssigned(productId)) {
			updateMbpnProductSpecCodeByProductId(productId, productSpecCode);
		} else {
			createMbpnProduct(productId, pss, StringUtils.trimToEmpty(productSpecCode));
		}
		
		// track product, we can set the property isAutoTracking to true.
		//update ProductStampingSequence send status
		updateProductStampingSequenceStatus(pss);
	}
	
	private boolean isProductAlreadyAssigned(String productId) {
		try {
			MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
			if (mbpnProduct != null) {
				return true;
			}
		} catch (Exception e) {
			getLogger().info("Could not check if MbpnProduct " + productId + " was already created");
			handleException(e);
		}
		return false;
	}
	
	private boolean updateMbpnProductSpecCodeByProductId(String productId, String currentProductSpecCode) {
		try {
			MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
			if (mbpnProduct != null) {
				mbpnProduct.setCurrentProductSpecCode(currentProductSpecCode);
				getMbpnProductDao().update(mbpnProduct);
				return true;
			}
		} catch (Exception e) {
			getLogger().info("Could not check if MbpnProduct " + productId + " was already created");
			handleException(e);
		}
		return false;
	}

	private void createMbpnProduct(String productId, ProductStampingSequence pss, String productSpecCode) {
		try {
			MbpnProduct mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(productId);
			getLogger().info("New MbpnProduct " + productId + " created");

			String currOrderNumber = StringUtils.trimToEmpty(pss.getId().getProductionLot());
			mbpnProduct.setCurrentOrderNo(currOrderNumber);
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
			mbpnProduct.setTrackingStatus(StringUtils.trimToEmpty(getLineId()));
			mbpnProduct.setLastPassingProcessPointId(getProcessPointId());
			mbpnProduct.setTrackingSeq(Double.valueOf(pss.getStampingSequenceNumber().toString()));
			mbpnProduct.setContainerId(null);
			mbpnProduct.setContainerPos(null);
			getMbpnProductDao().save(mbpnProduct);
		} catch (Exception e) {
			getLogger().error("Unable to create MbpnProduct " + productId);
			handleException(e);
		}
	}
	
	public PreProductionLot getCurrentProductionLot(){
		return getPreproductionLotDao().findCurrentPreProductionLotByPlanCode(planCode);
	}
	
	public ProductStampingSequence getNextScheduledProduct(PreProductionLot preProductionLot){
		try {
			return getProductStampingSequenceDao().findNextProduct(preProductionLot.getProductionLot(), PlanStatus.SCHEDULED);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	private String _lineId = "";
	public String getLineId() {
		if (_lineId.equals("")) {
			try {
				ProcessPoint pp = getProcesPointDao().findByKey(getProcessPointId());
				_lineId = pp.getLineId();
			} catch (Exception e) {
				handleException(e);
			}
		}
		return _lineId;
	}
	
	protected void handleException(Exception e) {
		collectorWork.prepareInfoCode(HeadlessDCInfoCode.SAVE_FAILED);
		e.printStackTrace();
		if(e instanceof TrackException){
			throw (TrackException)e;
		}	
	}
	
	private void updateProductStampingSequenceStatus(ProductStampingSequence productSeq){
		try {
			productSeq.setSendStatus(PlanStatus.ASSIGNED.getId());
			getProductStampingSequenceDao().save(productSeq);
			getLogger().info("Updated ProductStampingSequence send status for product " + productSeq.getId().getProductID() + " to " + PlanStatus.ASSIGNED);
		} catch (Exception e) {
			getLogger().error("Could not save ProductStampingSequence " + productSeq);
			e.printStackTrace();
		}
	}
	
	
	public boolean validateMBPNProductId(String productId){
		if(productId == null) return false;
		productId = productId.trim();
		if(productId.length() < 16 || productId.length() >16){
			return false;
		}
		String plantCode = productId.substring(0, 1);//get plant code
		String validPantCodeList = getProperty().getValidPlantCodes(); //get defined plant code from web configor
		if(!validPantCodeList.contains(plantCode)) 
			return false;
		
		String dept = productId.substring(1, 2);//get department
		String validDeptList = getProperty().getValidDepartments();//get department defined in web configor
		if(!validDeptList.contains(dept)) return false;
		
		String datePart = productId.substring(2);
		if(!validateDate(datePart)) return false;
		
		return true;
	}
	/**
	 * validate data format "yyyyMMddHHmmss"
	 * @param dateString
	 * @return
	 */
	private boolean validateDate(String dateString){
		try {
			DateUtils.parseDateStrictly(dateString, new String[]{"yyyyMMddHHmmss"});
			return true;
		} catch (java.text.ParseException e) {
			return false;
		}
	}
	
	public boolean validateProduct(String productId,String partId, String productionLot, String planCode, String prodSpecCode, String operationName){
		
		if(!isProductMadeFrom(operationName)){
			collectorWork.prepareInfoCode(HeadlessDCInfoCode.UNEXPECTED_OP_TYPE);
			return false;
		}
		if(!isPartMaskMatched(prodSpecCode, partId, operationName)){
			collectorWork.prepareInfoCode(HeadlessDCInfoCode.UNMATCHED);
			return false;
		}
		if(!isProductionLotExisted(productionLot, planCode,prodSpecCode)){
			collectorWork.prepareInfoCode(HeadlessDCInfoCode.UNKNOWN_PRODUCTIONLOT);
			return false;
		}
		
		collectorWork.prepareInfoCode(HeadlessDCInfoCode.PRODUCT_OK);
		return true;
		
	}
	
	/*
	 * check if productionLot already exists in gal212tbx
	 */
	public boolean isProductionLotExisted(String productionLot, String planCode, String prodSpecCode){
		PreProductionLot prodLot = this.getPreproductionLotDao().findByPlanCodeAndProdSpecCode(productionLot, planCode, prodSpecCode);
		return prodLot == null ? false : true;
	}
	
	/*
	 * check if MBPN product exists in MBPN_PRODUCT table
	 */
	public boolean isMbpnproductExisted(String productId){
		MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
		return mbpnProduct == null ? false : true;
	}
	
	/*
	 * check if product spec code matches part mask
	 */
	public boolean isPartMaskMatched(String prodSpecCode,String partId, String operationName){
		boolean isMatched = false;
		MCOperationPartRevision mcOPRev = getMCOperationPartRevision(partId, operationName);
		if( mcOPRev == null){
			isMatched = false;
		}
		String partMask = mcOPRev.getPartMask();
		partMask = partMask.trim();
		if (CommonPartUtility.verification(prodSpecCode, partMask, PropertyService.getPartMaskWildcardFormat())) {
			isMatched = true;
		}
		return isMatched;
	}
	
	/*
	 * check operation type
	 * Operation_type: 1. MADE_FROM 2. INSTALLED_PART
	 */
	public boolean isProductMadeFrom(String operationName){
		MCOperationRevision mcOPRev = getMCOperationRevision(operationName);
		if( mcOPRev == null){
			return false;
		}
		
	    OperationType opType = mcOPRev.getType();
	   
		return opType.equals(OperationType.GALC_MADE_FROM) ? true : false;
	}

	/*
	 * get all defects of one product 
	 */
	public List<DefectResult> getDefectResultList(String productId){
		if(productId == null) {
			return null;
		}
		List<DefectResult> defects = new ArrayList<DefectResult>();
		List<DefectResult> defectResults =   getDefectResultDao().findAllByProductId(productId);

		for (DefectResult defect : defectResults) {
			if(defect.isOutstandingStatus())
				defects.add(defect);
		}
			
		return defects;
	}
	
	public MCOperationRevision getMCOperationRevision(String operationName){
		MCOperation mCOperation =getMCOperationDao().findByKey(operationName);
		if(mCOperation == null){
			return null;
		}
		MCOperationRevisionId mCOPRevId = new MCOperationRevisionId();
		mCOPRevId.setOperationName(operationName);
		mCOPRevId.setOperationRevision(mCOperation.getRevision());
		
		MCOperationRevision mCOPRev = getMCOperationRevisionDao().findByKey(mCOPRevId);
		return mCOPRev;
	}
	
	public MCOperationPartRevision getMCOperationPartRevision(String partId, String operationName){
		MCOperationPartId mCOperationPartId = new MCOperationPartId();
		mCOperationPartId.setOperationName(operationName);
		mCOperationPartId.setPartId(partId);
		
		MCOperationPart mCOP = getMCOperationPartDao().findByKey(mCOperationPartId);
		if(mCOP == null){
			return null;
		}
		
		MCOperationPartRevisionId mCOperationPartRevisionId = new MCOperationPartRevisionId();
		mCOperationPartRevisionId.setOperationName(operationName);
		mCOperationPartRevisionId.setPartId(partId);
		mCOperationPartRevisionId.setPartRevision(mCOP.getPartRevision());
		MCOperationPartRevision mCOPRevision = getMCOperationPartRevisionDao().findByKey(mCOperationPartRevisionId);
		if(mCOPRevision == null){
			return null;
		}
		return mCOPRevision;
	}
	

	private List<String> convertToList(String mbpnInstallParts){
		List<String> partList = new ArrayList<String>();
		String[] parts = mbpnInstallParts.split(",");
		for(int i=0; i < parts.length; i++){
			partList.add(parts[i].trim());
		}
		return partList;
	}
	
	public MbpnProductDao getMbpnProductDao(){
		if(mbpnProductDao == null){
			mbpnProductDao = getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}
	
	public MCOperationDao getMCOperationDao(){
		if(mCOperationDao == null){
			mCOperationDao = getDao(MCOperationDao.class);
		}
		return mCOperationDao;
	}
	
	public MCOperationRevisionDao getMCOperationRevisionDao(){
		if(mCOperationRevisionDao == null){
			mCOperationRevisionDao = getDao(MCOperationRevisionDao.class);
		}
		return mCOperationRevisionDao;
	}
	
	public MCOperationPartDao getMCOperationPartDao(){
		if(mCOperationPartDao == null){
			return mCOperationPartDao = getDao(MCOperationPartDao.class);
		}
		return mCOperationPartDao;
	}
	
	public MCOperationPartRevisionDao getMCOperationPartRevisionDao(){
		if(mCOperationPartRevisionDao == null){
			return mCOperationPartRevisionDao = getDao(MCOperationPartRevisionDao.class);
		}
		return mCOperationPartRevisionDao;
	}
	
	public DefectResultDao getDefectResultDao(){
		if(defectResultDao == null){
			return  defectResultDao = getDao(DefectResultDao.class);
		}
		return defectResultDao;
	}
	
	public PreProductionLotDao getPreproductionLotDao() {
		if(preproductionLotDao == null){
			return preproductionLotDao = getDao(PreProductionLotDao.class);
		}
		return preproductionLotDao;
	}

	public ProductStampingSequenceDao getProductStampingSequenceDao() {
		if(productStampingSequenceDao == null){
			return productStampingSequenceDao = getDao(ProductStampingSequenceDao.class);
		}
		return productStampingSequenceDao;
	}
	
}
