package com.honda.galc.client.datacollection.strategy;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.IpuLabelDateMatchPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class IpuLabelDateMatchStrategy extends PartSerialNumberProcessor {
	private static final String LABEL = "Label";
	private static final String VIN = "Vin";
	InstalledPartDao installedPartDao;
	IpuLabelDateMatchPropertyBean propertyBean;
	
	public IpuLabelDateMatchStrategy(ClientContext context) {
		super(context);
	}
	
	@Override
	public boolean confirmPartSerialNumber(PartSerialNumber partnumber){
		String productId = getController().getState().getProductId().trim();
		String currentPartName = getController().getState().getCurrentPartName();
		
		checkPartSerialNumber(partnumber);
		if(isCheckDuplicatePart())
			checkDuplicatePart(partnumber.getPartSn());	
		return serialNumberCheck(partnumber.getPartSn(), productId) &&
				partDateMatchCheck(currentPartName, partnumber.getPartSn(), productId);
	}
	
	private boolean serialNumberCheck(String partSn, String productId) {
		if (!getPropertyBean().isCheckVinSerialNumber())
			return true;

		String vinSerialNumbe = extractSericalNumber(productId, VIN,
				getPropertyBean().getVinSerialNumberPositions());
		String labelSerialNumbe = extractSericalNumber(partSn, LABEL,
				getPropertyBean().getLabelSerialNumberPositions());

		if (!vinSerialNumbe.equals(labelSerialNumbe)) {
			throw new TaskException(
					"Label Serial Number [" + labelSerialNumbe + "] and VIN Serial Number [" + vinSerialNumbe
							+ "] don't match");
		}
		return true;
	}

	private String extractSericalNumber(String str, String name, Integer[] positions) {
		try {
			return str.substring(positions[0] - 1, positions[1]);
		} catch (Exception e) {
			throw new TaskException(
					"Exception to get " + name + " Serial Number from [" + str + "]");
		}

	}
	public boolean validate(LotControlRule lotControlRule, String productId, ProductBuildResult result){
		return serialNumberCheck(result.getPartSerialNumber(), productId) &&
				partDateMatchCheck(result.getPartName(), result.getPartSerialNumber(), productId);
	}
	
	private boolean partDateMatchCheck(String labelPartName, String labelSn, String productId){		
		String ipuIdPartName = getIpuIdPartName(labelPartName);
		if (StringUtils.isBlank(ipuIdPartName)) {
			throw new TaskException("IPU ID Part Name property is not set correctly for \"IPU to Label Date Check\" strategy");
		}
		
		String ipuSn = getIpuSn(ipuIdPartName, productId);
		if(StringUtils.isBlank(ipuSn)) {
			throw new TaskException(ipuIdPartName + " part serial number missing");
		}
		
		String ipuSnDate = getDateFromSerialNumber(ipuSn.trim(), true);
		String labelSnDate = getDateFromSerialNumber(labelSn.trim(), false);
		if (!ipuSnDate.equals(labelSnDate)){
			throw new TaskException(ipuIdPartName + " date [" + ipuSnDate + "] and " + labelPartName + " date [" + labelSnDate + "] don't match");
		}
		installedPart.setValidPartSerialNumber(true);
		return true;
	}
	
	private String getIpuIdPartName(String labelPartName) {
		String ipuIdPartName = getPropertyBean().getIpuIdPartName();
		Map<String, String> labelToIpuPartMap = getPropertyBean().getLabelToIpuIdPartMap();
		if (labelToIpuPartMap != null && labelToIpuPartMap.containsKey(labelPartName))
			ipuIdPartName = labelToIpuPartMap.get(labelPartName);
		return ipuIdPartName;
	}
	
	private String getIpuSn(String ipuPartName, String productId) {
		InstalledPart ipuPart = getInstalledPartDao().findById(productId, ipuPartName.trim());
		if (ipuPart == null) {
			for (InstalledPart installedPart : getController().getState().getProduct().getPartList()) {
				if(!installedPart.getPartName().trim().equals(ipuPartName.trim())) continue;
				ipuPart = installedPart;
				break;
			}
		}
		return ipuPart == null ? null : ipuPart.getPartSerialNumber();
	}
	
	private String getDateFromSerialNumber(String serialNumber, Boolean isIpu) {
		try {
			Integer[] monthChars = isIpu ? getPropertyBean().getIpuIdMonthChars() : getPropertyBean().getLabelMonthChars();
			Integer[] yearChars = isIpu ? getPropertyBean().getIpuIdYearChars() : getPropertyBean().getLabelYearChars();
			StringBuilder month = new StringBuilder();
			for (int monthChar : monthChars) {
				month.append(serialNumber.charAt(monthChar-1));
			}
			StringBuilder year = new StringBuilder();
			for (int yearChar : yearChars) {
				year.append(serialNumber.charAt(yearChar-1));
			}
			return month + "/" + year;
		} catch (Exception e) {
			throw new TaskException("Failed to retrieve date from serial number [" + serialNumber + "]");
		}
	}
	
	private IpuLabelDateMatchPropertyBean getPropertyBean() {
		if (propertyBean == null) {
			propertyBean = PropertyService.getPropertyBean(IpuLabelDateMatchPropertyBean.class, context.getProcessPointId());
		}
		return propertyBean;	
	}
	
	private InstalledPartDao getInstalledPartDao() {
		if (installedPartDao == null) {
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		}
		return installedPartDao;
	}
}
