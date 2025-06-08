package com.honda.galc.checkers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.property.IpuLabelDateMatchPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Hemant Rajput
 * @date May 01, 2025
 */
public class IpuLabelDateMatchChecker extends AbstractBaseChecker<PartSerialScanData> {
	
	InstalledPartDao installedPartDao;
	IpuLabelDateMatchPropertyBean propertyBean;

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(PartSerialScanData partSerialScanData) {
		String message = null;
		List<CheckResult> checkResults = new ArrayList<CheckResult>();

		try {
			BaseProduct product = null;
			if ((partSerialScanData.getProductType() != null) && (partSerialScanData.getProductId() != null)) {
				product = ProductTypeUtil.getProductDao(partSerialScanData.getProductType()).findBySn(partSerialScanData.getProductId());
			}
			
			if(product != null) {
				String productId = product.getProductId();
				String labelPartName = partSerialScanData.getPartName();
				String labelSn = partSerialScanData.getSerialNumber();
				String ipuIdPartName = getIpuIdPartName(labelPartName);
				if (StringUtils.isBlank(ipuIdPartName)) {
					message = "IPU ID Part Name property is not set correctly for \"IPU to Label Date Check\" strategy";
				}
				
				String ipuSn = getIpuSn(ipuIdPartName, productId);
				if(StringUtils.isBlank(ipuSn)) {
					message = ipuIdPartName + " part serial number missing";
				}
				
				String ipuSnDate = getDateTimeFromLabel(ipuSn.trim());
				String labelSnDate = getDateTimeFromLabel(labelSn.trim());
				
				if (!ipuSnDate.equals(labelSnDate)){
					message = ipuIdPartName + " time [" + ipuSnDate + "] and " + labelPartName + " time [" + labelSnDate + "] don't match";
				}
			}

			if (message != null) {
				CheckResult checkResult = new CheckResult();
				checkResult.setCheckMessage(message);
				checkResult.setReactionType(getReactionType());
				checkResults.add(checkResult);
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Something went wrong while executing the check");
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("Something went wrong while executing the check");
			checkResult.setReactionType(ReactionType.DISPLAY_ERR_MSG);
			checkResults.add(checkResult);
		}
		return checkResults;
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
		return ipuPart == null ? null : ipuPart.getPartSerialNumber();
	}
	
	private String getDateTimeFromLabel(String serialNumber) {
		Integer[] dateTimeChars = getPropertyBean().getLabelDateTimeChars();
		Integer startIndex = dateTimeChars[0];
		Integer endIndex = dateTimeChars[1];
        // Extract the timestamp substring (12 characters)
        String timestamp = serialNumber.substring(startIndex, endIndex);  // "080220231744"

        // Define formatter for input and output
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MMddyyyyHHmm");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        // Parse and reformat
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, inputFormat);
        String formatted = dateTime.format(outputFormat);
        return formatted;
	}
	
	private IpuLabelDateMatchPropertyBean getPropertyBean() {
		if (propertyBean == null) {
			propertyBean = PropertyService.getPropertyBean(IpuLabelDateMatchPropertyBean.class);
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
