package com.honda.galc.client.dc.enumtype;

import java.lang.reflect.Constructor;

import com.honda.galc.client.dc.validator.InputValidator;
import com.honda.galc.client.dc.validator.LastTighteningResultValidator;
import com.honda.galc.client.dc.validator.MeasurementValueValidator;
import com.honda.galc.client.dc.validator.PartSnInputValidator;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.openprotocol.model.LastTighteningResult;

public enum InputValidators {

	PART_SN					(PartSerialScanData.class, PartSnInputValidator.class, MCOperationPartRevision.class),
	LAST_TIGHTENING_RESULT 	(LastTighteningResult.class, LastTighteningResultValidator.class, MCOperationMeasurement.class),
	MEASUREMENT_VALUE 		(MeasurementValue.class, MeasurementValueValidator.class, MCOperationMeasurement.class);
	
	Class<? extends InputData> inputDataClass = null;
	Class<? extends InputValidator<? extends InputData>> validatorClass = null;
	Class<?> specificationClass = null;

	private InputValidators(Class<? extends InputData> inputDataClass, 
			Class<? extends InputValidator<? extends InputData>> validatorClass,
			Class<?> specificationClass) {
		this.inputDataClass = inputDataClass;
		this.validatorClass = validatorClass;
		this.specificationClass = specificationClass;
	}
	
	public Class<? extends InputData> getInputDataClass() {
		return inputDataClass;
	}

	public void setInputDataClass(Class<? extends InputData> inputDataClass) {
		this.inputDataClass = inputDataClass;
	}
	
	public Class<? extends InputValidator<? extends InputData>> getValidatorClass() {
		return validatorClass;
	}

	public void setValidatorClass(
			Class<? extends InputValidator<? extends InputData>> validatorClass) {
		this.validatorClass = validatorClass;
	}
	
	public Class<?> getSpecificationClass() {
		return specificationClass;
	}

	public void setSpecificationClass(Class<?> specificationClass) {
		this.specificationClass = specificationClass;
	}
	
	
	public static InputValidators getValidatorType(InputData inputData) {
		for(InputValidators validator : InputValidators.values()) {
			if(validator.getInputDataClass().equals(inputData.getClass())) 
				return validator;
		}
		return null;
	}
	
	public static InputValidator<? extends InputData> getValidator(InputData inputData) {
		for(InputValidators validator : InputValidators.values()) {
			if(validator.getInputDataClass().equals(inputData.getClass())) 
				return createValidator(inputData, validator.getInputDataClass(), validator.getValidatorClass());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected static InputValidator<? extends InputData> createValidator(InputData inputData, 
			Class<? extends InputData> inputDataClass,
			Class<? extends InputValidator<? extends InputData>> inputValidatorClass ) {
		try {
			Class<?>[] parameterTypes = {inputDataClass};
			Object[] parameters = {inputData};
			Constructor<?> constructor = inputValidatorClass.getConstructor(parameterTypes);
			return (InputValidator<? extends InputData>) constructor.newInstance(parameters);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
