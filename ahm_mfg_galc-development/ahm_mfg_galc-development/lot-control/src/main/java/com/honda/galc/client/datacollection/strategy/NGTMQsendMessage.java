package com.honda.galc.client.datacollection.strategy;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.SubproductPartSerialNumberProcessor;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.dto.SubAssemblyPartListDto;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.MQMessagingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


/**
 * 
 * <h3>NGTMQsendMessage Class description</h3>
 * <p>
 * NGTMQsendMessage description
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
 * 
 * </TABLE>
 * 
 * @author Deshane Joseph<br>
 *         Apr 12, 2013
 * 
 * 
 */

public class NGTMQsendMessage extends SubproductPartSerialNumberProcessor {

	private static String SUBASSEMBLY_NGT_PART_NAME = "SUBASSEMBLY_NGT_PART_NAME";
	private static String BLANK = "";
	private static String PART_SERIAL_NUMBER = "PART_SERIAL_NUMBER";
	private String NGTPartSerialNumber;
	private String subAssemblyPartName;

	public NGTMQsendMessage(ClientContext context) {
		super(context);
	}

	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		super.confirmPartSerialNumber(partnumber);
		return validate(null,getController().getState().getProductId(),installedPart);
	}

	public boolean validate(LotControlRule lotControlRule, String productId, ProductBuildResult result){
		MQMessagingService messaging = ServiceFactory.getService(MQMessagingService.class);
		DataContainer dc = new DefaultDataContainer(); 
		subAssemblyPartName = PropertyService.getProperty(context.getProcessPointId(),SUBASSEMBLY_NGT_PART_NAME,BLANK);

		dc.put(TagNames.PROCESS_POINT_ID.name(), context.getProcessPointId());
		dc.put(TagNames.PRODUCT_ID.name(), productId);

		if(StringUtils.isEmpty(subAssemblyPartName)){			
			dc.put(PART_SERIAL_NUMBER, parsedPartSerialNumber(result.getPartSerialNumber(),getController().getState().getCurrentPartName()));	
		} else {
			List<SubAssemblyPartListDto> parts = ServiceFactory.getService(InstalledPartDao.class).findSubPartsByProductId(productId);
			getSubAssemblyInfo(parts);
			dc.put(PART_SERIAL_NUMBER, NGTPartSerialNumber);
		}
		messaging.send(dc);
		
		return true;
	}

	private void getSubAssemblyInfo(List<SubAssemblyPartListDto> parts) {
		for(SubAssemblyPartListDto part : parts) {
			if(part.getPartName().equals(subAssemblyPartName)){
				NGTPartSerialNumber = parsedPartSerialNumber(part.getPartSerialNumber(), subAssemblyPartName);	
				return;
			}
		}
	}
	
	private String getParseInfo(String partName){
		return super.getController().getProperty().getNgtParseInfo() != null? super.getController().getProperty().getNgtParseInfo().get(partName):null;
	}
	
	private String parsedPartSerialNumber(String partSerialNumber, String partName){
		String parseInfo = getParseInfo(partName);
		
		if(parseInfo != null){
			String pos[] = parseInfo.split(",");
			int start = Integer.parseInt(pos[0]);
			int len = Integer.parseInt(pos[1]);
			
			if(partSerialNumber.length() >= (start+len)){
				return partSerialNumber.substring(start, start+len);
			}else{
				return "";
			}
		}
		return partSerialNumber;
	}
}