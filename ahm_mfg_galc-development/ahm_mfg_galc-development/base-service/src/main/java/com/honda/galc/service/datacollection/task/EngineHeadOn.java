package com.honda.galc.service.datacollection.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.HeadHistoryDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.entity.product.Head;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.DiecastUtil;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>EngineHeadOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineHeadOn description </p>
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
 * <TD>Apr 24, 2012</TD>
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
 * @since Apr 24, 2012
 */
public class EngineHeadOn extends CollectorTask{
	
	public enum HeadOnStatus{OK, NG, NR, ND, AP}; //added AP to represent Already Processed
	public final static  String HEAD_STATUS = "HEAD_STATUS";
	private static final String HCM_ASSIGNED_PLANT_CODE = "HCM_ASSIGNED_PLANT_CODE";
	public static String MC_OFF_PPID="MC_OFF_PPID";
	public static List<ProductNumberDef> productNumberDefs = new ArrayList<ProductNumberDef>();
	static{
		productNumberDefs.add(ProductNumberDef.DCH_AN);
		productNumberDefs.add(ProductNumberDef.DCH_JP);
	}
	
	private Head  head;
	private String headId;
	private String model;
	
	
	public EngineHeadOn(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
		headId = (String)context.get(TagNames.PRODUCT_ID.name());
		model = (String)context.get(TagNames.MODEL_CODE.name());
	}
	
	
	public void execute(){
		try {
			if (!checkHead()) return;
			
			if(isReRun())
				context.put(HEAD_STATUS, HeadOnStatus.AP.name());
			else
				context.put(HEAD_STATUS, HeadOnStatus.OK.name());
			
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
		} catch (Exception e) {
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(e, " Exception to execute ", this.getClass().getSimpleName());
		}
	}
	
	
	public boolean isReRun() {
		if(getDao().findByKey(head.getHeadId()) == null){
			getLogger().warn("Engine Head Marriage - head id:", head.getHeadId(), " does not exist in database.");
			context.put(HEAD_STATUS, HeadOnStatus.ND.name());
			return false;
		}
			
		return head.getTrackingStatus().startsWith(getEngineLineMark());
	}


	public String getEngineLineMark() {
		return PropertyService.getProperty(processPointId, TagNames.ENGINE_LINE_PROCESS_POINT_PREFIX.name());
	}


	public boolean checkHead(){
		if(!checkHeadId()){
			//Ok if it is Anna or Jappan head
			if(ProductNumberDef.isNumberValid(headId, productNumberDefs)){
				if(!isRequiredModel()){
					getLogger().info("Head model from other plant does not match requirement.");
					context.put(HEAD_STATUS, HeadOnStatus.NR.name());
					return false;
				}
				
				if(!saveHeadInfo()){
					context.put(HEAD_STATUS, HeadOnStatus.NR.name());
					return false;
				}
				
			} else {
				context.put(HEAD_STATUS, HeadOnStatus.NR.name());
				return false;
			}
		}
		
		if(head != null){
			if(!checkHeadStatus(head)) return false;
		} else if(isHeadFromOtherPlant()){
			/**
			 * Here is the scenario: Head from other plan for example Anna has the length as Hcm head, 
			 * and it has not yet in our data base, however HCM AE already assigned plant code for 
			 * instance plant code as "AE".
			 * 
			 * If this is the case, it'll be DC number only.
			 *  
			 * We support this case by insert Head into database.
			 */

			getLogger().info("received head from other plant. head id:", headId);
			head = createNewHead();
			getDao().save(head);
			return true;
		} 

		if(!checkHeadPssedMcOff()) return false;
		
		head.setDunnage(null);
		getDao().save(head);
		
		return true;
	}
	
	private boolean checkHeadId() {
		head = (Head)DiecastUtil.validateProductId(ProductType.HEAD.name(),headId,context, null);
		
		if(head != null) model = head.getModelCode(); 
		
		logContext();
		return (Boolean)context.get(TagNames.VALID_PRODUCT_NUMBER_FORMAT.name()) ;
	}


	public boolean saveHeadInfo() {
		Head findByKey = getDao().findByKey(headId);
		if(findByKey != null){
			getLogger().info("Head:", headId, " from other plant already exist in database.");
			return false;
		}
		head = createNewHead();
		getDao().save(head);
		
		return true;
	}


	public HeadDao getDao() {
		return ServiceFactory.getDao(HeadDao.class);
	}

	public boolean isRequiredModel(){
		
		String strModels = PropertyService.getProperty("prop_EngineHeadMarriage","HEAD_MODEL_CODE");
		if(StringUtils.isEmpty(model) || StringUtils.isEmpty(strModels))
		{
			getLogger().error("Invalid model information. model:", model, " head model code:", strModels);
			throw new TaskException("Invalid model information.");
		}
		return strModels.contains(model.trim());
	}

	public boolean isHeadFromOtherPlant() {
		return (head == null && isDcNumber(headId) && isAssignedPlantCode(headId));
	}

	public boolean checkHeadStatus(Head head) {
		if(head.isDefect() || head.getHoldStatus() != 0){
			getLogger().info("Invalid Engine Head:", head.getId().toString(), " defect status:", head.getDefectStatus().name(),
					" hold status:" + head.getHoldStatus());
			context.put(HEAD_STATUS, HeadOnStatus.NG.name());
			
			return false;
		}
		
		return true;
	}

	public boolean checkHeadPssedMcOff() {
		String mcOffPpid = null;
		for(String ppId : getMcOffProcessPointIds()){
		    if(ServiceFactory.getDao(HeadHistoryDao.class).hasProductHistory(head.getHeadId(), ppId)){
		    	mcOffPpid = ppId;
		    	break;
		    }
		}
		
		if(StringUtils.isEmpty(mcOffPpid)){
			context.put(HEAD_STATUS, HeadOnStatus.NG.name());
			getLogger().info("Head Id:", head.getHeadId(), " did not passed MC Off. Last passing process point id:",
					head.getLastPassingProcessPointId());

			return false;
		} else {
			getLogger().info("Head Id:", head.getHeadId(), " passed mc off at:", mcOffPpid);
			return true;
		}
	}

	public String[] getMcOffProcessPointIds() {
		String McOffPpid = PropertyService.getProperty(processPointId, MC_OFF_PPID);
		return McOffPpid.split(",");
	}

	public boolean isDcNumber(String headId) {
		return ProductNumberDef.DCH.getLength() == headId.length();
	}

	public Head createNewHead() {
		Head newHead = (Head)ProductTypeUtil.createProduct(ProductType.HEAD.name(), headId);
		newHead.setDcSerialNumber(headId);
		newHead.setMcSerialNumber(headId);
		newHead.setModelCode(model);
		return newHead;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	public boolean isAssignedPlantCode(String headId){
		String plantCode = ProductNumberDef.DCH.getToken(TokenType.PLANT.name(), headId);
		String assingedCode = (String)PropertyService.getProperty("prop_MCDCNumber", HCM_ASSIGNED_PLANT_CODE);
		return plantCode.equals(assingedCode);
	}

}
