package com.honda.galc.service.defect;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.service.IService;


public interface ScrapService extends IService {
	DataContainer scrapProduct(DefaultDataContainer data);
	String getClassName();
}
