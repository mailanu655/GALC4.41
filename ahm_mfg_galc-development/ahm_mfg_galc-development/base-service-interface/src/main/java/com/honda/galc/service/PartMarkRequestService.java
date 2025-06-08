package com.honda.galc.service;

import java.util.List;

import com.honda.galc.data.DataContainer;


public interface PartMarkRequestService  extends IService{
	public DataContainer getPartMarks(String modelYearCode,List<String> partNumbers);
}
