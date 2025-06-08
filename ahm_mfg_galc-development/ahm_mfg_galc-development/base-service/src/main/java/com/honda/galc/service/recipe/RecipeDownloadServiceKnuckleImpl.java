package com.honda.galc.service.recipe;

import java.util.List;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>RecipeDownloadServiceKnuckleImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadServiceKnuckleImpl description </p>
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
 * <TD>Jan 15, 2013</TD>
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
 * @since Jan 15, 2013
 */
public class RecipeDownloadServiceKnuckleImpl extends RecipeDownloadServiceImpl 
implements RecipeDownloadServiceKnuckle{
	String secondProductId; //this is the right Knuckle Product Id
	SubProduct leftKnuckle;
	SubProduct rightKnuckle;
	
	protected void getNextProduct(Device device) {
		List<String> ids = CommonUtil.splitStringList(getPropertyBean().getProductIdTags());
		requestProductId = device.getDeviceFormat(ids.get(0)).getValue().toString(); //Left Knuckle always
		secondProductId = device.getDeviceFormat(ids.get(1)).getValue().toString();

		if(!validateProductId(requestProductId))
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		else if(!validateProductId(secondProductId) )
			addError(RecipeErrorCode.Invalid_Ref, secondProductId);

		//TODO check if the current product is skipped
		leftKnuckle = getHelper().nextProduct(requestProductId);
		rightKnuckle = getHelper().nextProduct(secondProductId);
		
		if(leftKnuckle == null || rightKnuckle == null)
			addError(RecipeErrorCode.No_Next_Ref, getRequestProductId());
		
		int leftKnPostion = getHelper().getProductionLotPosition(leftKnuckle);
		product = leftKnuckle;
		int rightKnPostion = getHelper().getProductionLotPosition(rightKnuckle);
		
		
		if(leftKnPostion != rightKnPostion)
			addError(RecipeErrorCode.Invalid_Pair, getRequestProductId());
			
		context.put(ids.get(0), leftKnuckle.getProductId());
		context.put(ids.get(1), rightKnuckle.getProductId());

	}
	
	protected String getKdLotNumber() {
		return leftKnuckle.getKdLotNumber();
	}
	
	protected void trackProduct() {
		if(getPropertyBean().isAutoTracking()){
			getTrackingService().track(leftKnuckle, getProcessPointId());
			getTrackingService().track(rightKnuckle, getProcessPointId());
		} else 
			trackKnuckles();
	}

	
	private void trackKnuckles() {
		SubProductDao dao = ServiceFactory.getDao(SubProductDao.class);
		ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		ProcessPoint processPoint = processPointDao.findByKey(getProcessPointId());
		
		leftKnuckle.setTrackingStatus(processPoint.getLineId());
		leftKnuckle.setLastPassingProcessPointId(getProcessPointId());
		
		rightKnuckle.setTrackingStatus(processPoint.getLineId());
		rightKnuckle.setLastPassingProcessPointId(getProcessPointId());
		
		dao.updateTrackingAttributes(leftKnuckle);
		dao.updateTrackingAttributes(rightKnuckle);
	}

	@Override
	protected int getKdLotSize() {
		return super.getKdLotSize()*2;
	}
	
	public String getRequestProductId(){
		return requestProductId + ", " + secondProductId;
	}
	
	protected String getNextProducts() {
		return leftKnuckle.getProductId() + ", " + rightKnuckle.getProductId();
	}
		
	
}
