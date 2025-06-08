package com.honda.galc.service.printing;

import java.util.List;

import com.honda.galc.data.DataContainer;

public interface IPrintAttributeConvertor {
	public DataContainer convertFromPrintAttribute(String clientId,DataContainer dc);
	public DataContainer convertFromDeviceDataFormat(String clientId,DataContainer dc);
	public List<String> parseTemplateData(String templateData);
}
