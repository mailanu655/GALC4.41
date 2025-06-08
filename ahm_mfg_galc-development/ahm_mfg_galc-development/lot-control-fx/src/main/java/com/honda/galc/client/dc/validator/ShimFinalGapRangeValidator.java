package com.honda.galc.client.dc.validator;

import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.product.validator.AbstractValidator;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 11, 2014
 */
public class ShimFinalGapRangeValidator extends AbstractValidator {
	private CylinderShimInstallProcessor processor;

	public ShimFinalGapRangeValidator(CylinderShimInstallProcessor processor) {
		super();
		this.processor = processor;
	}

	public boolean execute(String value) {
		double[] range = processor.getTargetGapRange();
		int[] gapRange = new int[2];
		gapRange[0] = (int) Math.ceil(range[0]);
		gapRange[1] = (int) range[1];
		int gap = Integer.parseInt(value);
		if (gapRange[0] <= gap && gap <= gapRange[1]) {
			return true;
		}
		setDetailedMessageTemplate(String.format("{0} exceeds permitted range %d - %d.", gapRange[0], gapRange[1]));
		return false;
	}

}
