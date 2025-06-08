package com.honda.galc.client.dc.validator;

import javafx.scene.control.TextField;

import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.product.validator.AbstractValidator;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 11, 2014
 */
public class ShimBaseGapRangeValidator extends AbstractValidator {
	private TextField shimIdField;
	private CylinderShimInstallProcessor processor;

	public ShimBaseGapRangeValidator(TextField shimIdField, CylinderShimInstallProcessor processor) {
		super();
		this.shimIdField = shimIdField;
		this.processor = processor;
	}

	public boolean execute(String value) {
		double[] gapRange = processor.getGapRangeByShimId(shimIdField.getText());
		int gap = Integer.parseInt(value);
		if (gapRange[0] <= gap && gap <= gapRange[1]) {
			return true;
		}
		setDetailedMessageTemplate(String.format("{0} exceeds permitted range %.0f - %.0f.", gapRange[0], gapRange[1]));
		return false;
	}

}
