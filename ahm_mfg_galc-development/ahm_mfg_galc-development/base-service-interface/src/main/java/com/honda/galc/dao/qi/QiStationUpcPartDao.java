package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiStationUpcPart;
import com.honda.galc.entity.qi.QiStationUpcPartId;
import com.honda.galc.service.IDaoService;

public interface QiStationUpcPartDao extends IDaoService<QiStationUpcPart, QiStationUpcPartId>{
	/**
	 * This method is used to find all the UPC part data by Process Point Id
	 */
	List<QiStationUpcPart> findAllByProcessPointId(String processPointId);
	public long countByProcessPoint(String processPointId);
	public int deleteByProcessPoint(String processPointId);
	public boolean isValidMBPN(String processPointId, String productID);
}
