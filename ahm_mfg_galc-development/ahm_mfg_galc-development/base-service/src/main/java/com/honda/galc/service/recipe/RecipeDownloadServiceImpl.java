package com.honda.galc.service.recipe;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.InstalledPartHelper;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>RecipeDownloadServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadServiceImpl description </p>
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
 * <TD>Jan 2, 2013</TD>
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
 * @since Jan 2, 2013
 */
public class RecipeDownloadServiceImpl extends RecipeDownloadBase implements RecipeDownloadService {
	protected List<InstalledPart> installedPartList;
	private boolean checkFlag = true;

	public Device execute(Device device) {
		try{
			init(device);
			if(isMultipleProduct()){
				prepareMultipleProductRecipe(device);
			} else
				prepareSingleProductRecipe(device);
		} catch (Throwable te){
			getLogger().error(te, " Exception to collect data for", this.getClass().getSimpleName());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}
		
		populateReply(device); 
		
		return device;
	}

	protected void prepareMultipleProductRecipe(Device device) {
		getLogger().info("start to process multiple product: ", getPropertyBean().getProducts());
		for(String prodName : CommonUtil.splitStringList(getPropertyBean().getProducts())){
			productName = prodName;
			
			prepareSingleProductRecipe(device);
		}
	}
		
	protected void prepareSingleProductRecipe(Device device) {
		try{
			
			if(getHelper().getProperty().isUseNextProductId())
				getNextProduct(device);
			else
				getCurrentProduct(device);
			
			getLotControlRules(product);
			if(hasLotControlRule())
				populateRules(device);
			else 
				getLogger().info("There is no Lot Control Rule defined!");
			
			if(null!=product && !StringUtils.isEmpty(getPropertyBean().getPreviousProcessPointId())
					 && !getPropertyBean().getPreviousProcessPointId().equals(product.getLastPassingProcessPointId()))
				addError(getErrorCodeDescription(), product.getProductId());
			
			//add some special checks when implement Fills - controlled by properties so won't impact exist functions
			if(getPropertyBean().isValidateProducedByBuildResult() && checkProducedByBuildResults())
				addError(RecipeErrorCode.Produced_Ref, product.getProductId());
			
			String processpointId = this.processPointId;
			ProcessPoint processPoint  = ServiceFactory.getDao(ProcessPointDao.class).findById(processpointId);
			executeProductChecks(product, processPoint);
			
			populateCommon();
			populateBuildResults();
			processTask();
			processAddtionalFeatures(device);
			if(isValidForTracking()) trackProduct();
			
			completeSingleProduct();

			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			if(NO_ERROR.equals(context.get(TagNames.ERROR_CODE.name())))
				getLogger().info("Ref#", getRequestProductId(), " - Next Ref# OK.");
			
		}catch(TaskException te){
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(te, te.getMessage(), " ",this.getClass().getSimpleName());
		}catch (Exception e){
			contextPut(TagNames.ERROR_CODE.name(), RecipeErrorCode.System_Err.getCode());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(e, RecipeErrorCode.System_Err.getDescription() + getRequestProductId(), " ", this.getClass().getSimpleName());
		}
	}
	
	private Boolean processStateCheckResults(Map<String, Object> checkResults) {
		boolean resultStatus = true;
		for (Entry<String, Object> e : checkResults.entrySet()) {
			if (ClassUtils.isAssignable(e.getValue().getClass(), Boolean.class)) {
				
				if (checkFlag)
					context.put(e.getKey(), e.getValue());
				resultStatus &= (Boolean) e.getValue();
			} else if (ClassUtils.isAssignable(e.getValue().getClass(), Map.class)) {
				checkFlag = false;
				resultStatus &= processStateCheckResults((Map<String, Object>) e.getValue());
				checkFlag = true;
				context.put(e.getKey(), e.getValue());
			} else
				getLogger().warn("Invalid data type: " + e.getValue().getClass());
		}
		return resultStatus;
	}
		
protected boolean executeProductChecks(BaseProduct product, ProcessPoint processPoint) {
		
		if(processPoint == null) {
			return true;
		}
			
		List<String> productCheckTypes = PropertyService.getPropertyList(processPoint.getProcessPointId(), "PRODUCT_STATE_CHECKS");
		   
	    if(productCheckTypes==null || productCheckTypes.isEmpty()) {
	    	return true;
	    }
	
	    String [] checkTypes =  productCheckTypes.toArray(new String[0]);
		Map<String, Object> checkResults = ProductCheckUtil.check(product, processPoint, checkTypes);
		
		if (checkResults == null || checkResults.isEmpty()) {
			return true;
		}
		
		Boolean cResult = processStateCheckResults(checkResults); 
		context.put(TagNames.CHECK_RESULT.name(), cResult );
		
		String message =  "Failed Product Checks: \n" + ProductCheckUtil.formatTxt(checkResults);
		Logger.getLogger().error(message);

		return false;
	}

	private boolean checkProducedByBuildResults() {
		if(StringUtils.isEmpty(getPropertyBean().getBuildResultProcessPoints()))
				return false;
		String[] split = getPropertyBean().getBuildResultProcessPoints().split(Delimiter.COMMA);
		List<InstalledPart> buildResults = ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndProcessPoint(requestProductId, split[0]);
		return buildResults.size() > 0;
	}

	protected void populateBuildResults() {
		if(StringUtils.isEmpty(getPropertyBean().getBuildResultProcessPoints())) return;
		getLogger().info("populate build results for process points:", getPropertyBean().getBuildResultProcessPoints());
		
		List<LotControlRule> buildResultRules = new ArrayList<LotControlRule>();
		installedPartList = new ArrayList<InstalledPart>();
		for(String ppid : CommonUtil.splitStringList(getPropertyBean().getBuildResultProcessPoints())){
			List<LotControlRule> rules = LotControlRuleCache.getOrLoadLotControlRule(productSpec, StringUtils.trim(ppid));
			for(LotControlRule r : rules){
				if(!StringUtils.isEmpty(getPropertyBean().getBuildResultPartFilter()) && !CommonUtil.isInList(r.getPartNameString(), getPropertyBean().getBuildResultPartFilter()))
					continue;
				
				buildResultRules.add(r);
				
				InstalledPart installedPart = getInstalledPartDao().findByKey(new InstalledPartId(product.getProductId(),r.getPartNameString()));
				if(installedPart != null)
					installedPartList.add(installedPart);
			}
		}
		
		List<String> partNames = getPartNames(buildResultRules);
		getLogger().info("populate build results for :", partNames.toString());		
		getMeasurementsForInstalledParts(installedPartList);
		
		installedPartHelper.emitBuildResultTag(installedPartList, getPropertyBean().getBuildResultTag(), context);
		
	}
	

	protected void completeSingleProduct() {
		if(getPropertyBean().isValidateSkippingProduct()) {
			try{
				if(getPropertyBean().isUseNextProductId())
					getExpectedProductDao().save(new ExpectedProduct(product.getProductId(), processPointId));
				else {

					BaseProduct nextProduct = getProductDao().findNextInprocessProduct(product.getProductId());
					getExpectedProductDao().save(new ExpectedProduct(nextProduct.getProductId(), processPointId));

				}
			} catch(Exception e) {
				getLogger().warn("Failed to save next expected product for ", product.getProductId());
			}

		}
		
		if(!hasLotControlRule() && !hasBuildAttributes() && !hasBuildResults()){
			addError(RecipeErrorCode.No_Recipe_Data, RecipeErrorCode.No_Recipe_Data.getDescription());
		}
		
	}
	
	private boolean hasBuildResults() {
		
		 if(StringUtils.isEmpty(getPropertyBean().getBuildResultProcessPoints()))
			 return false;
		 
		 if(installedPartList == null || installedPartList.size() == 0)
			 return false;
		 
		 return true;
	}

	private boolean hasBuildAttributes() {
		if (StringUtils.isEmpty(getPropertyBean().getBuildAttributes()))
			return false;
		
		String[] split = getPropertyBean().getBuildAttributes().split(Delimiter.COMMA);
		for(String buildattr : split){
		  if(context.get(getFullTag(buildattr)) != null) return true;
		}
		
		return false;
	}


	protected RecipeErrorCode getErrorCodeDescription() {
		return this.getProcessPointId().equals(product.getLastPassingProcessPointId()) ? 
				RecipeErrorCode.Duplicate_Ref : RecipeErrorCode.Produced_Ref;
	}

	protected void trackProduct() {
		if(null==product){
			return;
		}
		
		if(getPropertyBean().isAutoTracking())
			getTrackingService().track(product, getProcessPointId());
		else
			updateLastPassingProcessPoint();
	}


	protected String getNextProducts() {
		return product.getProductId();
	}

	protected boolean isValidForTracking() {
		Object errorCode = context.get(TagNames.ERROR_CODE.name());
		if(errorCode != null && errorCode.toString().equals((RecipeErrorCode.Produced_Ref.getCode()))){
			getLogger().warn("product:", getNextProducts(), " is invalid for tracking - skipped tracking.");
			return false;
		} else
			return true;
	}

	private void updateLastPassingProcessPoint() {
		super.updateLastPassingProcessPoint(product);	
	}

	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		device.populate(data);
		
		return execute(device).toReplyDataContainer(true);
	}
}
