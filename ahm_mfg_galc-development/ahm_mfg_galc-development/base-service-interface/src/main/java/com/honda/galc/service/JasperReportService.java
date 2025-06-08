package com.honda.galc.service;

import com.honda.galc.data.DataContainer;

public interface JasperReportService extends IService{
	
	public byte[] generateJasperReport(byte[] templateData,DataContainer dc);

}
