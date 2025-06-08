package com.honda.galc.client.product.validator;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Wade Pei <br>
 * @date Aug 28, 2014
 */
public class PositiveIntegerValidator extends AbstractValidator {

	public PositiveIntegerValidator() {
		setDetailedMessageTemplate("{0} must be positive integer");
	}

	@Override
	public boolean execute(String value) {
		return StringUtils.isNotBlank(value) && value.matches("[1-9]\\d*");
	}

}
