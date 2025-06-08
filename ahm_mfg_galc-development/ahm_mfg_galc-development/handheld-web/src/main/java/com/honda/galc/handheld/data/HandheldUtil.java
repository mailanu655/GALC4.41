package com.honda.galc.handheld.data;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;

public class HandheldUtil {

	public static boolean doesRuleCollectASerialNumber(LotControlRule rule) {
		return rule != null
				&& !(rule.getPartMasks().trim().length() == 0)
				&& (rule.getSerialNumberScanType() == PartSerialNumberScanType.PART 
				|| rule.getSerialNumberScanType() == PartSerialNumberScanType.PART_MASK);
	}

	public static boolean doesRuleCollectTorques(LotControlRule rule) {
		return
				rule != null
				&& HandheldUtil.getMeasurementSpecsFrom(rule).size() > 0;
	}

	public static List<MeasurementSpec> getMeasurementSpecsFrom(LotControlRule rule) {
		if (rule == null)
			return new ArrayList<MeasurementSpec>();
		for (PartSpec eachPartSpec : rule.getParts()) {
			if (eachPartSpec.getMeasurementCount() > 0)
				return eachPartSpec.getMeasurementSpecs();
		}
		return new ArrayList<MeasurementSpec>();
	}

}