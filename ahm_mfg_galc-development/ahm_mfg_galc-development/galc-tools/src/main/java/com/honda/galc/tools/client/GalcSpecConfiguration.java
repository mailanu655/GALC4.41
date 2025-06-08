package com.honda.galc.tools.client;

import java.util.List;

import org.springframework.util.StringUtils;

import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.DataCollectionImage;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.RequiredPart;


/**
 * 
 * @author vec15809
 *
 */
public class GalcSpecConfiguration {
	public static final String CURRENT_VERSION = "1";
	private String processPointId;
	private List<LotControlRule> lotControlRules;
	private List<RequiredPart> requiredParts;
	private List<BuildAttribute> buildAttributes;
	private List<DataCollectionImage> images;
	private String version;
	
	
	public GalcSpecConfiguration() {
		super();
		version = CURRENT_VERSION;
	}
	// -------- getters & setters -----------
	public List<LotControlRule> getLotControlRules() {
		return lotControlRules;
	}

	public List<RequiredPart> getRequiredParts() {
		return requiredParts;
	}

	public List<BuildAttribute> getBuildAttributes() {
		return buildAttributes;
	}

	public String getProcessPointId() {
		return StringUtils.trimWhitespace(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
		
	}

	public void setLotControlRules(List<LotControlRule> rules) {
		this.lotControlRules = rules;
	}

	public void setRequiredParts(List<RequiredPart> requiredPartList) {
		this.requiredParts = requiredPartList;
		
	}

	public void setBuildAttributes(List<BuildAttribute> buildAttributes) {
		this.buildAttributes = buildAttributes;
		
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isCurrentVersion() {
		return CURRENT_VERSION.equals(getVersion());
	}
	
	public List<DataCollectionImage> getImages() {
		return images;
	}
	
	public void setImages(List<DataCollectionImage> images) {
		this.images = images;
	}
	
	public Object wrongVersionMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("This XML for ");
		builder.append(processPointId);
		builder.append(" was built by version ");
		builder.append(getVersion());
		builder.append(". Current version of the tool is ");
		builder.append(CURRENT_VERSION);
		builder.append(". Please use previous version of the tool to restore it.");
		return builder.toString();
	}
	
}

