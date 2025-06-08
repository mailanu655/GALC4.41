package com.honda.galc.dao.jpa.conf;


import java.util.List;

import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.conf.PreviousLineId;
import com.honda.galc.service.Parameters;


public class PreviousLineDaoImpl extends BaseDaoImpl<PreviousLine, PreviousLineId> implements PreviousLineDao{

	private static final String FIND_ALL_BY_PROCESS_POINT_ID = "SELECT A.* FROM GALADM.GAL236TBX A WHERE A.LINE_ID = (" +
			"SELECT B.LINE_ID FROM GALADM.GAL195TBX B WHERE EXISTS (" +
			"SELECT 1 FROM GALADM.GAL214TBX C WHERE B.LINE_ID = C.LINE_ID AND C.PROCESS_POINT_ID = ?1 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY) ORDER BY PREVIOUS_LINE_ID";

	public List<PreviousLine> findAllByLineId(String lineId) {

		return findAll( Parameters.with("id.lineId",lineId));
		
	}

	@Override
	public List<PreviousLine> findAllByProcessPointId(String processPointId) {
		Parameters params = Parameters.with("1", processPointId);
		List<PreviousLine> result  = findAllByNativeQuery(FIND_ALL_BY_PROCESS_POINT_ID, params);
		return result;
	}

}
