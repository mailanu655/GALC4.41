package com.honda.galc.client.datacollection.processor;

import java.util.Map;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.PartSerialMatchCheckPairPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 30, 2016
 */

public class PartSerialMatchCheckSerialNumProcessor extends PartSerialNumberProcessor{
	
	
	public PartSerialMatchCheckSerialNumProcessor(ClientContext context) {
		super(context);
	}
	
	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		String productId = getController().getState().getProductId().trim();
		String currentPartName=getController().getState().getCurrentPartName();
		checkPartSerialNumber(partnumber);
		if(isCheckDuplicatePart())
			checkDuplicatePart(partnumber.getPartSn());	
		return partSerialMatchCheck(currentPartName,partnumber,productId,context.getProcessPointId());
	}
	
	public boolean validate(LotControlRule rule,String productId,ProductBuildResult result){
		
		String currentPartName = rule.getPartNameString();
		PartSerialNumber partNumber = new PartSerialNumber(result.getPartSerialNumber());
		
		return partSerialMatchCheck(currentPartName,partNumber,productId,rule.getId().getProcessPointId());
	}


	private boolean partSerialMatchCheck(String currentPartName,PartSerialNumber partNumber, String productId,String componentId){
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		Map<String, String> partSerialMatchCheckPair =PropertyService.getPropertyBean(PartSerialMatchCheckPairPropertyBean.class,componentId ).getPartSerialMatchCheckPair();
		if(partSerialMatchCheckPair!=null && partSerialMatchCheckPair.get(currentPartName)!=null)
		{
			InstalledPart previousSerialNumScanPart=installedPartDao.findById(productId, partSerialMatchCheckPair.get(currentPartName).trim());
			if(previousSerialNumScanPart==null|| previousSerialNumScanPart.getPartSerialNumber()==null || !(previousSerialNumScanPart.getPartSerialNumber().trim().equals(partNumber.getPartSn().trim())))
			{
				throw new TaskException("SerialNumberScan for Part:"+partSerialMatchCheckPair.get(currentPartName)+" is missing or does not match");
			}
		}
		installedPart.setValidPartSerialNumber(true);
		return true;
	}
}
