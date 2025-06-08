package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.MassMessageDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MassMessage;
import com.honda.galc.entity.conf.MassMessageId;
import com.honda.galc.service.Parameters;

public class MassMessageDaoImpl extends
		BaseDaoImpl<MassMessage, MassMessageId> implements MassMessageDao {

	private String PLANT_NAME = "plantName";
	private String DEPARTMENT_ID = "departmentId";
	private String LINE_ID = "lineId";
	

	private String GET_LATEST_MESSAGE = "SELECT mmt FROM MassMessage AS mmt WHERE mmt.id.plantName = :plantName " +
			"AND (mmt.id.departmentId = :departmentId OR mmt.id.departmentId = 'NONE') " +
			"AND (mmt.id.lineId = :lineId OR mmt.id.lineId ='NONE') ORDER BY mmt.id.massMessageType";
	
	public List<MassMessage> getLatestMessage(String PlantName,
			String DepartmentId, String LineId) {
		
		Parameters params = new Parameters();
		params.put(PLANT_NAME, PlantName);
		params.put(DEPARTMENT_ID, DepartmentId);
		params.put(LINE_ID, LineId);
		
		return findAllByQuery(GET_LATEST_MESSAGE, params);

	}

}
