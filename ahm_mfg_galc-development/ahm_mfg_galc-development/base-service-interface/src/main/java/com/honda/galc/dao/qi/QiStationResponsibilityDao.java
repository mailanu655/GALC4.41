package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.qi.QiStationResponsibility;
import com.honda.galc.entity.qi.QiStationResponsibilityId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>QiStationResponsibilityDao Interface description</h3>
 * <p>
 * QiStationResponsibilityDao
 * </p>
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
 * @author LnTInfotech<br>
 *        June 13, 2017
 * 
 */
public interface QiStationResponsibilityDao extends IDaoService<QiStationResponsibility, QiStationResponsibilityId>{
	
	public void deleteAllByProcessPoint(String processPoint);
	
	public List<QiStationResponsibility> findAllByResponsibleLevel(String responsibleLevel);

	public List<QiStationResponsibility> findAllBySite(String siteName);

	public List<QiStationResponsibility> findAllByPlant(String plantName);

	public List<QiStationResponsibility> findAllByDepartment(String department);
	
	public List<QiStationResponsibilityDto> findAllAssignedRespByProcessPoint(String processPoint);

	public long countAssignedRespByProcessPoint(String processPoint);
	public int deleteByProcessPoint(String processPointId);
	public List<QiStationResponsibility> findAllByProcessPoint(String processPoint);

	long countByResponsibleLevelId(int responsibleLevelId);

	long countByResponsibleLevel2(int responsibleLevelId);

	long countByResponsibleLevel3(int responsibleLevelId);
	
}
