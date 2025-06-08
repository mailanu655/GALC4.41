package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.PartSnNoValidate;

public interface PartSerialValidatorService extends IService {
	
	DataContainer validateSerialNumber(DefaultDataContainer data);
	
	public String checkPartSerialNumberMask(PartSnNoValidate partsnValidate);

}
