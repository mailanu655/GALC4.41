package com.honda.galc.service.recipe;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>FrameRecipeDownloadServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameRecipeDownloadServiceImpl description </p>
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
 * <TD>Dec. 12, 2017</TD>
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
 * @since Dec 12, 2017
 */
public class RecipeDownloadFrameServiceImpl extends RecipeDownloadServiceImpl implements RecipeDownloadFrameService{
	private FrameLinePropertyBean framePropertyBean;
	private Integer lineRefNumber;
	
	

	protected void getNextProduct(Device device) {
		requestProductId  = getRequestProductId(device);
		
		product = getHelper().getNextInProcessProduct(requestProductId);
		if(product == null){
			addError(RecipeErrorCode.No_Next_Ref, getRequestRef());
		}
	}
	
	protected void getCurrentProduct(Device device) {
		requestProductId  = getRequestProductId(device);

		product = getHelper().getProduct(requestProductId);
		
		if(product == null){
			addError(RecipeErrorCode.No_Next_Ref, requestProductId);
		}
	}


	private String getRequestProductId(Device device) {
		DeviceFormat productIdDF = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name()));
		requestProductId = StringUtils.trimToEmpty(productIdDF == null ? null : (String)productIdDF.getValue());
		
		if(!StringUtils.isEmpty(requestProductId)){
			if(!validateProductId(requestProductId))
				addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		}else{
			lineRefNumber = getLineRefNumber(device);
			if(lineRefNumber == null)
				addError(RecipeErrorCode.Invalid_Ref, RecipeErrorCode.Invalid_Ref.getDescription());
			
			Frame currentFrame = getHelper().findFrameByLineRefNumber(lineRefNumber);
			if(currentFrame == null)
				addError(RecipeErrorCode.Invalid_Ref, lineRefNumber.toString());

			requestProductId = currentFrame == null ? "" : currentFrame.getProductId();
			
		}
			
		if(getPropertyBean().isValidateSkippingProduct() && !validateSkippingProduct(requestProductId))
			addError(RecipeErrorCode.Skipped_Ref, getRequestRef());
		
		if(getPropertyBean().isValidateProductPassedAfOn() && !isPassedAfOn())
			addError(RecipeErrorCode.No_Af_On, getRequestRef());
		
		return requestProductId;
	}
	

	private boolean isPassedAfOn() {
		return ServiceFactory.getDao(ProductHistoryDao.class).hasProductHistory(productId, getFramePropertyBean().getAfOnProcessPointId());
	}

	public FrameLinePropertyBean getFramePropertyBean() {
		if(framePropertyBean == null)
			framePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class, getProcessPointId());
		
		return framePropertyBean;
	}
	
	public Integer getLineRefNumber(Device device) {
		String lineRefNum  = device.getDeviceFormat(getFullTag(TagNames.LINE_REF_NUMBER.name())).getValue().toString();
		return (StringUtils.isEmpty(lineRefNum)) ? null : Integer.parseInt(lineRefNum);
	}

	public void setLineRefNumber(int lineRefNumber) {
		this.lineRefNumber = lineRefNumber;
	}
	
	public FrameRecipeProductSequenceHelper getHelper()  {
		return (FrameRecipeProductSequenceHelper) super.getHelper();
	}
	
	
	public String getRequestRef(){
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.trimToEmpty(requestProductId));

		if(lineRefNumber != null){
			if(sb.length() > 0) sb.append(":");
			sb.append(lineRefNumber);
		}

		return sb.toString();
	}
	
	protected void populateCommon(){
		super.populateCommon();
	    contextPut(TagNames.LINE_REF_NUMBER.name(),getHelper().getLineRefNumber());
	}

}
