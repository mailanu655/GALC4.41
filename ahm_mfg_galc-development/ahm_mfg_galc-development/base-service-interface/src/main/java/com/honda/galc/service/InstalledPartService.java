package com.honda.galc.service;

import java.util.List;

import com.honda.galc.data.MCInstalledPartDetailDto;

/** * * 
* @version 0.2 
* @author Rakesh 
* @since June 29, 2016
*/
public interface InstalledPartService extends IService{

	public List<MCInstalledPartDetailDto> getOutstandingMCOperations(String productId, String ppList, String filterType, String divisionList);
	
	
}
