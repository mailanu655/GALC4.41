package com.honda.galc.client.dc.validator;

import com.honda.galc.client.dc.processor.CylinderShimInstallAbstractProcessor;
import com.honda.galc.client.product.validator.AbstractValidator;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 10, 2014
 */
public class ShimIdExistValidator extends AbstractValidator {
	private CylinderShimInstallAbstractProcessor processor;

	public ShimIdExistValidator(CylinderShimInstallAbstractProcessor processor) {
		super();
		this.processor = processor;
		setDetailedMessageTemplate("Shim ID does not exist in database");
	}

	public boolean execute(String value) {
		if (processor.findShimSizeById(value) < 0) {
			setDetailedMessageTemplate(String.format("Shim ID %s does not exist in database", value));
			return false;
		} else {
			return true;
		}
	}

}
