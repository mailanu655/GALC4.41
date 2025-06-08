package com.honda.galc.dao.qi;

import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.service.IDaoService;

public interface QiDefectResultHistDao extends IDaoService<QiDefectResultHist, Integer> {

	public QiDefectResultHist findFirstDefectResultHistory(long defectResultId);
	
}
