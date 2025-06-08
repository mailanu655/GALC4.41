package com.honda.galc.service.recipe;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 10, 2015
 */
public class AddionalFeaturesRecipeDownloadServiceImpl  extends RecipeDownloadServiceImpl implements AddFeaturesRecipeDownloadService{
	protected BaseProduct requestProduct;


	@Override
	protected void getNextProduct(Device device)	
	{
		requestProductId = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name())).getValue().toString();
		if(!validateProductId(requestProductId))
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		requestProduct=getHelper().getProductDao().findByKey(requestProductId);
		if(getPropertyBean().isNextProductIdStampingSeqBased() && getPropertyBean().getProductType().equals(ProductType.FRAME.name()))
		{
			product = getHelper().nextProduct(requestProductId);
		}else{
			InProcessProduct inProcessProduct=getDao(InProcessProductDao.class).findByKey(requestProductId);
			product=getHelper().getProductDao().findByKey(inProcessProduct.getNextProductId());
		}
		if(product == null){
			addError(RecipeErrorCode.No_Next_Ref, requestProductId);
		}else{
			contextPut(TagNames.NEXT_PRODUCT_ID.name(),product.getProductId());
		}
	}

	@Override
	protected void processAddtionalFeatures(Device device)
	{
		List<DeviceFormat> deviceFormatList=device.getReplyDeviceDataFormats();
		for(DeviceFormat deviceFormat:deviceFormatList)
		{
			String tag=deviceFormat.getTag();
			String[] tagArray = tag.split("\\.");
			if(tag.contains(Delimiter.DOT+TagNames.PART_MASK.name())&&tagArray.length>2)
			{	
				populatePartMask(tagArray[0],tagArray[2]);
			}else if((tag.contains(TagNames.MEASUREMENT_UPPER_LIMIT.name())||tag.contains(TagNames.MEASUREMENT_LOWER_LIMIT.name()))&&tagArray.length>2)
			{ 
				populateMeasurementLimits(tagArray[0],tagArray[2],TagNames.MEASUREMENT_UPPER_LIMIT.name(),TagNames.MEASUREMENT_LOWER_LIMIT.name());	 
			}else if(tag.contains(TagNames.CURRENT_PART_SERIAL_NUMBER.name()))
			{
				populateSerialNumber(tagArray[0],requestProduct,TagNames.CURRENT_PART_SERIAL_NUMBER.name());	 
			}else if(tag.contains(TagNames.CURRENT_MEASUREMENT.name()))
			{
				populateMeasurements(tagArray[0],requestProduct,TagNames.CURRENT_MEASUREMENT.name());	 
			}else if(tag.contains(TagNames.CURRENT_BUILD_ATTRIBUTE.name()))
			{
				populateBuildAttribute(requestProduct, tagArray[0],TagNames.CURRENT_BUILD_ATTRIBUTE.name());
			}else if(tag.contains(TagNames.CURRENT_PRODUCT_RESULT_TIMESTAMP.name()))
			{
				populateProdResultTimestamp(tagArray[0],requestProduct);
			}else if(tag.contains(TagNames.NEXT_PART_SERIAL_NUMBER.name()))
			{
				populateSerialNumber(tagArray[0],product,TagNames.NEXT_PART_SERIAL_NUMBER.name());	 
			}else if(tag.contains(TagNames.NEXT_MEASUREMENT.name()))
			{
				populateMeasurements(tagArray[0],product,TagNames.NEXT_MEASUREMENT.name());	 
			}else if(tag.contains(TagNames.NEXT_BUILD_ATTRIBUTE.name()))
			{
				populateBuildAttribute(product, tagArray[0],TagNames.NEXT_BUILD_ATTRIBUTE.name());
			}else if(tag.contains(TagNames.NEXT_LOT_SIZE.name()))
			{
				populateNextLotSize();
			}else if(tag.contains(TagNames.NEXT_LOT_BUILD_ATTRIBUTE.name()))
			{
				populateNextLotAttributeValue(tagArray[0]);
			}else if(tag.contains(TagNames.NEXT_PRODUCT_RESULT_TIMESTAMP.name()))
			{
				populateProdResultTimestamp(tagArray[0],product);
			}else if(tag.contains(TagNames.STRAGGLER.name()))
			{
				populateStragglerInfo(tagArray[0]);
			}else if(tag.contains(TagNames.CURE_TIME_RESULT.name()))
			{
				populateProductResultCureTimeCheck(tagArray[0]);
			}else if(tag.contains(TagNames.NEXT_LAST_PRODUCT_PROCESS_POINT.name()))
			{
				findLastProductProcessPoint(tagArray[0]);
			}
		}
	}

	private PreProductionLot getNextPreProductionLot(){
		PreProductionLot prodLot=getDao(PreProductionLotDao.class).findByKey(requestProduct.getProductionLot());
		if(prodLot!=null)
			return getDao(PreProductionLotDao.class).findByKey(prodLot.getNextProductionLot());
			return null;
	}

	private void populateNextLotSize() {
		PreProductionLot nextProdLot=getNextPreProductionLot();
		if(nextProdLot!=null)
			contextPut(TagNames.NEXT_LOT_SIZE.name(),nextProdLot.getLotSize());
	}

	private void populateNextLotAttributeValue(String attribute) {
		PreProductionLot nextProdLot=getNextPreProductionLot();
		if(nextProdLot!=null)
		{
			String nextLotProdSpecCode=nextProdLot.getProductSpecCode();
			if(nextLotProdSpecCode!=null)
			{
				BuildAttribute nextLotBuildAttribute = getDao(BuildAttributeDao.class).findById(attribute,nextLotProdSpecCode);
				if(nextLotBuildAttribute!=null)
					contextPut(attribute+Delimiter.DOT+TagNames.NEXT_LOT_BUILD_ATTRIBUTE.name(),nextLotBuildAttribute.getAttributeValue());
			}
		}
	}

	private List<ProductResult> getProductResult(String configuredProcessPointId, BaseProduct baseProduct){
		return getDao(ProductResultDao.class).findByProductAndProcessPoint(new ProductResult(baseProduct.getProductId(),configuredProcessPointId));
	}

	private void populateProdResultTimestamp(String configuredProcessPointId,BaseProduct baseProduct) {
		List<ProductResult> productResultList=getProductResult(configuredProcessPointId,baseProduct);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if(productResultList!=null&&productResultList.size()>0){
			contextPut(configuredProcessPointId+Delimiter.DOT+TagNames.CURRENT_PRODUCT_RESULT_TIMESTAMP.name(),df.format(productResultList.get(0).getActualTimestamp()));
		}
	}

	private void populateStragglerInfo(String configuredProcessPointId) {
		List<Straggler> currentStragglerProductList=getDao(StragglerDao.class).findStragglerProductList(product.getProductId(), configuredProcessPointId);
		if(currentStragglerProductList!=null && currentStragglerProductList.size()>0)
		{
			contextPut(configuredProcessPointId+Delimiter.DOT+TagNames.STRAGGLER.name(),true);
		}else
		{
			contextPut(configuredProcessPointId+Delimiter.DOT+TagNames.STRAGGLER.name(),false);
		}
	}

	private void populateBuildAttribute(BaseProduct baseProduct,String attribute,String tagName) {
		BuildAttribute buildAttribute = getDao(BuildAttributeDao.class).findById(attribute,baseProduct.getProductSpecCode());
		if(buildAttribute!=null)
			contextPut(attribute+Delimiter.DOT+tagName,buildAttribute.getAttributeValue());
	}

	private void populateMeasurements(String partName, BaseProduct baseProduct,String tagName) {
		List<Measurement> measurements = getDao(MeasurementDao.class).findAll(baseProduct.getProductId(), partName);
		for(Measurement measurement: measurements){
			contextPut(partName+Delimiter.DOT+tagName+measurement.getId().getMeasurementSequenceNumber(),measurement.getMeasurementValue());
		}
	}

	private void populateSerialNumber(String partName,BaseProduct baseProduct,String tagName) {
		List<String> partNames = new ArrayList<String>();
		partNames.add(partName);
		List<InstalledPart> installedParts = getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(baseProduct.getProductId(), partNames );
		if(installedParts!=null&&installedParts.size()>0){
			contextPut(partName+Delimiter.DOT+tagName,installedParts.get(0).getPartSerialNumber());
		}
	}

	private void populateMeasurementLimits(String partName,String partId,String upperLimitTagName,String lowerLimitTagName) {
		List<MeasurementSpec> measurementSpecs = getDao(MeasurementSpecDao.class).findAllByPartNamePartId(partName, partId);			
		for(MeasurementSpec measurementSpec : measurementSpecs){						
			contextPut(partName+Delimiter.DOT+upperLimitTagName+measurementSpec.getId().getMeasurementSeqNum()+Delimiter.DOT+partId, measurementSpec.getMaximumLimit());
			contextPut(partName+Delimiter.DOT+lowerLimitTagName+measurementSpec.getId().getMeasurementSeqNum()+Delimiter.DOT+partId, measurementSpec.getMinimumLimit());				
		}
	}

	private void populatePartMask(String partName,String partId) {
		PartSpecId partSpecId=new PartSpecId();
		partSpecId.setPartName(partName);
		partSpecId.setPartId(partId);
		PartSpec partSpec = getDao(PartSpecDao.class).findByKey(partSpecId);
		if(partSpec!=null){
			contextPut(partName+Delimiter.DOT+TagNames.PART_MASK.name()+Delimiter.DOT+partId,partSpec.getPartSerialNumberMask());
		}
	}

	public void populateProductResultCureTimeCheck(String configuredProcessPointId){	
		List<ProductResult> productResultList=getProductResult(configuredProcessPointId,requestProduct);
		if(productResultList!=null&&productResultList.size()>0){
			Integer cureTimeMax = CommonUtil.stringToInteger(getPropertyBean().getProductResultCureTimeMax() == null ? null : getPropertyBean().getProductResultCureTimeMax()) ;
			Integer cureTimeMin = CommonUtil.stringToInteger(getPropertyBean().getProductResultCureTimeMin() == null ? null : getPropertyBean().getProductResultCureTimeMin()) ;
			boolean result = true;
			Long elapsedMs = System.currentTimeMillis() - productResultList.get(0).getActualTimestamp().getTime();					
			if(cureTimeMin != null){
				result = elapsedMs > cureTimeMin * 60 *1000;
			}		
			if(cureTimeMax != null) {
				boolean resultMax = elapsedMs < cureTimeMax * 60 *1000;			
				result &= resultMax;
			}	
			contextPut(configuredProcessPointId+Delimiter.DOT+TagNames.CURE_TIME_RESULT.name(), result);	
		}
	}

	private void findLastProductProcessPoint(String configuredProcessPointId){
		ProductResult productResult=getDao(ProductResultDao.class).findLastProductForProcessPoint(configuredProcessPointId);
		BaseProduct baseProduct=null;
		if(productResult!=null)
		{
			if(getPropertyBean().isNextProductIdStampingSeqBased())
			{
				baseProduct = getHelper().nextProduct(productResult.getProductId());
			}else{
				InProcessProduct inProcessProduct=getDao(InProcessProductDao.class).findByKey(productResult.getProductId());
				if(inProcessProduct!=null)
					baseProduct=getHelper().getProductDao().findByKey(inProcessProduct.getNextProductId());
			}
			if(baseProduct!=null)
				contextPut(configuredProcessPointId+Delimiter.DOT+TagNames.NEXT_LAST_PRODUCT_PROCESS_POINT.name(), baseProduct.getProductId());
		}
	}

}
