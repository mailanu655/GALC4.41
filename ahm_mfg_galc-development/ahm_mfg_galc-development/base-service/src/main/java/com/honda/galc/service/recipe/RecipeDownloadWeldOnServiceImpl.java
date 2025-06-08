package com.honda.galc.service.recipe;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.recipe.RecipeDownloadWeldOnService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>RecipeDownloadWeldOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadWeldOnServiceImpl description </p>
 * <p> As part of HCM Frame migration project, this class implements existing stand alone Weld On
 *     Vin Request and validation functions. No change on PLC interaction messages and format of messages.  
 * </p>
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
 * <TD>Dec. 18, 2018</TD>
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
 * @since Dec 18, 2018
 */
public class RecipeDownloadWeldOnServiceImpl extends AddionalFeaturesRecipeDownloadServiceImpl implements RecipeDownloadWeldOnService{
	private static final String REQUEST_STATUS = "REQUEST_STATUS";
	protected ProductStampingSequenceDao productStampingSequenceDao;
	protected ProductStampingSequence nextSeq;
	private ExpectedProduct expectedProduct;
	
	private FloorStampInfoCodes stampInfo = FloorStampInfoCodes.REQUEST_VIN_OK;

	@Override
	protected void getNextProduct(Device device)	
	{
		requestProductId = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name())).getValue().toString();
		expectedProduct = getExpectedProductDao().findByKey(getProcessPointId());
		nextSeq = getHelper().nextProductForVinStamping(requestProductId);
		updateStampingInfoCode(stampInfo, requestProductId);
		
		if(!StringUtils.isEmpty(requestProductId)) {
			BaseProduct requestProduct = getProductDao().findByKey(requestProductId);
			if(requestProduct == null) {
				stampInfo = FloorStampInfoCodes.REQUEST_VIN_INVALID;
				updateStampingInfoCode(stampInfo, requestProductId);
				return;
			} else if(expectedProduct == null || StringUtils.isEmpty(expectedProduct.getProductId())) {
				getLogger().warn("Next expected result VIN:" + ((expectedProduct == null)? "null" : expectedProduct.getProductId()));;
				expectedProduct = new ExpectedProduct(requestProductId, getProcessPointId());
			}

			int reqStatus = PropertyService.getPropertyInt(getProcessPointId(), REQUEST_STATUS, 1);
			
			if(nextSeq == null) {
				stampInfo = FloorStampInfoCodes.REQUEST_VIN_NO_NEXT_VIN;
				updateStampingInfoCode(stampInfo, requestProductId);
				return;
			} else if(nextSeq.getSendStatus() == reqStatus) {
				stampInfo = FloorStampInfoCodes.REQUEST_VIN_ALREADY_PROCESSED;
				updateStampingInfoCode(stampInfo, nextSeq.getProductId());
			} else if(expectedProduct != null && !expectedProduct.getProductId().equals(requestProductId)) {
				stampInfo = FloorStampInfoCodes.REQUEST_VIN_SKIPPED;
				updateStampingInfoCode(stampInfo, expectedProduct.getProductId());
			}

			contextPut(TagNames.NEXT_PRODUCT_ID.name(),nextSeq.getProductId());
			contextPut(TagNames.LAYOUT_BODY.name(), nextSeq.isLayoutBody() ? "1" : "0");
			product = ProductTypeUtil.getTypeUtil(productType).getProductDao().findByKey(nextSeq.getProductId());
		} else {
			stampInfo = FloorStampInfoCodes.REQUEST_VIN_RFID_NG;
			updateStampingInfoCode(stampInfo, requestProductId);
		}
		
		
	}
	
	protected void completeSingleProduct() {
		try {
			if (stampInfo != FloorStampInfoCodes.REQUEST_VIN_OK && stampInfo != FloorStampInfoCodes.REQUEST_VIN_SKIPPED)
				return; 
			
			getExpectedProduct().setProductId(nextSeq.getProductId());
			getExpectedProductDao().update(expectedProduct);

			if(!isUpdate()) return;
			
			updateStampingStatus();
			
			if (nextSeq.getSendStatus() == ProductStampingSendStatus.WAITING.getId()) {
				nextSeq.setSendStatus(ProductStampingSendStatus.SENT.getId());
				getProductStampingSequenceDao().update(nextSeq);
				logger.info("updated send status to: " + ProductStampingSendStatus.SENT.getId(), " for vin: ",
						nextSeq.getProductId());
			} else {
				logger.info("send status is already: " + ProductStampingSendStatus.SENT.getId(), " for vin: ",
						nextSeq.getProductId(), " skipped status update.");
			}
			
			if(!nextSeq.isLayoutBody()) {
				try {
					getPreProductionLotDao().updateSendStatus(nextSeq.getProductionLot(),PreProductionLotSendStatus.INPROGRESS.getId());
					getPreProductionLotDao().updateSentTimestamp(nextSeq.getProductionLot());
					logger.info("updated send status to: " + PreProductionLotSendStatus.INPROGRESS.getId()
							+ " for production lot: " + nextSeq.getProductionLot());
				} catch (Exception e) {
					logger.error("Exception to update preproduction lot status for Vin stamping.");
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception to update stamping status:" + e.getMessage());
			e.printStackTrace();
		}
		

	}
	
	
	private ExpectedProduct getExpectedProduct() {
		return expectedProduct == null ? new ExpectedProduct(null, getProcessPointId()) : expectedProduct;
	}

	private boolean isUpdate() {
		DeviceFormat dformat = device.getDeviceFormat(getFullTag(TagNames.UPDATE.name()));
		return (dformat==null)? false : Boolean.valueOf(dformat.getValue().toString());
	}

	private void updateStampingStatus() {
		    ServiceFactory.getDao(FrameDao.class).updateProductStartDate(getProcessPointId(), (String)context.get(TagNames.NEXT_PRODUCT_ID.name()));
			logger.info("updated product start date for vin: ", (String)context.get(TagNames.NEXT_PRODUCT_ID.name()));
	}
	
	protected void populateReply(Device device) {
		super.populateReply(device);
		context.put(TagNames.DEVICE_STRING.name(), getString(device.getReplyDeviceDataFormats()));
		if(device.getReplyDeviceFormat(TagNames.DEVICE_STRING.name()) != null)
			device.getReplyDeviceFormat(TagNames.DEVICE_STRING.name()).setValue(context.get(TagNames.DEVICE_STRING.name()));
	}
	
	private String getString(List<DeviceFormat> formats) {
		StringBuffer sb = new StringBuffer();
		for(DeviceFormat df : formats) {

			if(df.getDeviceTagType() != DeviceTagType.DEVICE) {
				if(df.getOffset() > 0) {
					for(int i = 0; i < df.getOffset(); i++)
						sb.append(getDefaultValue(df.getDeviceDataType()));
				}
				if(df.getValue() != null)
					sb.append(df.getValue());
				else
					sb.append(df.getDefaultValue());

			} 
		}
		return sb.toString();
	}
	
	private Object getDefaultValue(DeviceDataType type) {
		return type==DeviceDataType.STRING? Delimiter.SPACE : type.getDefaultValue();
	}


	//-------- getter&setters -------------
	public ProductStampingSequenceDao getProductStampingSequenceDao() {
		if (productStampingSequenceDao == null)
			productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		return productStampingSequenceDao;
	}

}
