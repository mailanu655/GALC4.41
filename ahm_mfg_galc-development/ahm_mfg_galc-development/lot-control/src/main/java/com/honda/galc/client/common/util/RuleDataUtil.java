package com.honda.galc.client.common.util;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;

public class RuleDataUtil {
	
	public static String getPartMasks(LotControlRule r) {
		StringBuilder sb = new StringBuilder();
		for(PartSpec spec : r.getParts()){
			if(sb.length() > 0) sb.append(",");
			sb.append(stripOffWildCardBracket(spec.getPartSerialNumberMask()));

		}
		return sb.toString();
	}
	
	public static String stripOffWildCardBracket(String specString) {
		return StringUtils.remove(StringUtils.remove(specString, "<<"),">>");
	}
	
	public static String getPartMark(LotControlRule r) {
		return (r.getParts() != null && r.getParts().get(0) != null)?
				r.getParts().get(0).getPartMark() : "";
	}
	
	public static String getPartMask(LotControlRule r) {
		return (r.getParts() != null && r.getParts().get(0) != null)?
				stripOffWildCardBracket(r.getParts().get(0).getPartSerialNumberMask()) : "";
	}
	
	public static String getPartNumber(LotControlRule r) {
		return  (r.getParts() != null && r.getParts().get(0) != null)?
				r.getParts().get(0).getPartNumber() : "";
	}
	
	public static Double getMinLimit(LotControlRule r) {
		return  (r.getParts() != null && r.getParts().get(0) != null 
				  && r.getParts().get(0).getNumberMeasurementSpecs() != null
				  && r.getParts().get(0).getNumberMeasurementSpecs().size() > 0) ?
						  r.getParts().get(0).getNumberMeasurementSpecs().get(0).getMinimumLimit() : 0.0;	  
	}
	
	public static Double getMaxLimit(LotControlRule r) {
		return  (r.getParts() != null && r.getParts().get(0) != null 
				  && r.getParts().get(0).getNumberMeasurementSpecs() != null
				  && r.getParts().get(0).getNumberMeasurementSpecs().size() > 0) ?
						  r.getParts().get(0).getNumberMeasurementSpecs().get(0).getMaximumLimit() : 0.0;	  
	}

	public static Integer getTorqueCount(LotControlRule r) {
		return (r.getParts() != null && r.getParts().get(0) != null)? 
				r.getParts().get(0).getMeasurementCount() : 0;
	}
	
	
}
