package com.honda.galc.client.dc.validator;

import com.honda.galc.client.dc.processor.PistonClearanceProcessor;
import com.honda.galc.client.product.validator.AbstractValidator;
/*   
* @author Jiamei Li<br>
* Jul 10, 2014
*
*
*/
public class PistonClearanceRangeValidator extends AbstractValidator {

	private PistonClearanceProcessor processor;
	public PistonClearanceRangeValidator(PistonClearanceProcessor processor){
		super();
		this.processor = processor;
		setDetailedMessageTemplate("Clearance value is outside of the specified tollerance");
	}
	public boolean execute(String value) {
		double clearanceValue = Double.valueOf(value).doubleValue();
		if(getProcessor().getMaxMeasurementValue() == null || getProcessor().getMinMeasurementValue() == null){
			setDetailedMessageTemplate("No maximum or minimum clearance defined.");
		}
		if( clearanceValue >= getProcessor().getMinMeasurementValue().doubleValue() && clearanceValue <= getProcessor().getMaxMeasurementValue().doubleValue()){
			return true;
		}
		return false;
	}
	public PistonClearanceProcessor getProcessor() {
		return processor;
	}

}
