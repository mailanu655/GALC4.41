package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.entity.lcvinbom.LotPart;
import com.honda.galc.entity.lcvinbom.LotPartId;
import com.honda.galc.service.IDaoService;

public interface LotPartDao extends IDaoService<LotPart, LotPartId> {
	void setInterchangableInactive(String letSystemName, String dcPartNumber);
	List<String> getPartNumbersByProductionLotAndSystemName(String productionLot, String systemName);
	List<LotPart> getByProductionLotAndSystemName(String productionLot, String systemName); 
	void deleteLotParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName);
	void insertLotParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName);
	void insertStragglerLotParts(String dcPartNumber, String productSpecWildcard, List<String> shippedTrackingStatuses,
			String letSystemName, String processPointId);
	void deleteStragglerLotParts(String dcPartNumber, String productSpecWildcard, List<String> shippedTrackingStatuses,
			String letSystemName, String processPointId);
	void deleteStragglerLotPartsWithPartNumber(String dcPartNumber, String productSpecWildcard, List<String> shippedTrackingStatuses,
			String letSystemName, String processPointId);
}
