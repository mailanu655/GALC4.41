package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDataId;
import com.honda.galc.entity.qi.QiExternalSystemDefectId;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiExternalSystemDefectIdMapDao Class description</h3>
 * <p> QiExternalSystemDefectIdMapDao used to fire queries to load, reprocess or delete data </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author vcc44349<br>
 * 2019-10-23
 */
public interface QiExternalSystemDefectIdMapDao extends IDaoService<QiExternalSystemDefectIdMap, QiExternalSystemDefectId> {
	
	public QiExternalSystemDefectIdMap findByExternalSystemKey(String extSysName, Long extKey);
	public QiExternalSystemDefectIdMap findByDefectId(Long defectResultId);
	public boolean isExternalSystemIdUsed(short externalSystemId);

}
