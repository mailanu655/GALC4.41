package com.honda.galc.dto;

import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.product.InstalledPart;

public class InstalledPartBroadcastContext extends BroadcastContext {

	private static final long serialVersionUID = 1L;
	private InstalledPart installedPart;
	
	public InstalledPart getInstalledPart() {
		return installedPart;
	}
	public void setInstalledPart(InstalledPart installedPart) {
		this.installedPart = installedPart;
	}

	@Override
	public Map<String, String> getAttributes() {
		Map<String, String> attributes = super.getAttributes();
		if(installedPart!=null) {
			attributes.put("PART_NAME", installedPart.getId().getPartName());
			attributes.put("PART_SR_NO", StringUtils.trimToEmpty(installedPart.getPartSerialNumber()));
			attributes.put("PART_STATUS", Integer.toString(installedPart.getInstalledPartStatus().getId()));
			attributes.put("PART_ACTUAL_TIMESTAMP", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(installedPart.getActualTimestamp()));
		}
		return attributes;
	}

}
