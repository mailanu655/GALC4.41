package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.data.DataContainer;
import com.honda.galc.dto.lcvinbom.VinPartFilterDto;
import com.honda.galc.entity.lcvinbom.VinPart;
import com.honda.galc.entity.lcvinbom.VinPartId;
import com.honda.galc.service.IDaoService;

public interface VinPartDao extends IDaoService<VinPart, VinPartId> {

	List<String> getPartNumbersByProductIdAndSystemName(String productId, String systemName);
	List<VinPartId> getPartNumber(String productId);
	List<VinPartId> getPartNumber(String productId, String systemName);
	void putFlashResults(DataContainer letVinPartDataContainer);
	List<VinPart> filterByCriteria(String productId, String productionLot, String partNumber, String systemName);
	List<VinPartFilterDto> findAllDistinctVinParts();
	void deleteVinParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName);
	void insertVinParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName);
	List<VinPart> filterVinPartsBySysNamePartNumAndProdSpecCode(String partNumber, String productSpecCode, String systemName);
	List<String> getDistinctVinPartSystemNames(String productId);
}
