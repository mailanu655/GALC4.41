package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.service.IDaoService;

public interface QiStationPreviousDefectDao extends IDaoService<QiStationPreviousDefect, QiStationPreviousDefectId>{ 
	/*
	 * this method is used to fetch all QiStationPreviousDefect by processPoint
	 */
	public List<QiStationPreviousDefect> findAllByProcessPoint(String processPointId);
	
	public long countByProcessPoint(String processPointId);
	public int deleteByProcessPoint(String processPointId);

}
