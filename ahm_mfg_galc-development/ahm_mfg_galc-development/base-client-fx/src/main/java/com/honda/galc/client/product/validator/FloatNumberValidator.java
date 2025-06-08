package com.honda.galc.client.product.validator;

/*   
* @author Jiamei Li<br>
* Jul 10, 2014
*
*
*/
public class FloatNumberValidator extends AbstractValidator {

	public FloatNumberValidator() {
		setDetailedMessageTemplate("{0} must be numeric");
	}

	public boolean execute(String value) {
		try {
			Float.valueOf(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
