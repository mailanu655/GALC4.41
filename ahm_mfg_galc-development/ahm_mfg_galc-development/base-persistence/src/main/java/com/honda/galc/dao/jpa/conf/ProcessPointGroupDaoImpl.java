package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ProcessPointGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.ProcessPointGroupDto;
import com.honda.galc.entity.conf.ProcessPointGroup;
import com.honda.galc.entity.conf.ProcessPointGroupId;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */
public class ProcessPointGroupDaoImpl extends BaseDaoImpl<ProcessPointGroup, ProcessPointGroupId> implements ProcessPointGroupDao {
	
	private static final String FIND_ALL_GROUPS_BY_PROCESS_POINT_ID = "select rc1.regional_value_name as regional_value_name, " + 
			"rc2.regional_value_name as process_point_group_name " +
			"from galadm.process_point_group_tbx pg left join galadm.regional_code_tbx rc1 on pg.category_code = rc1.regional_value " +
			"left join galadm.regional_code_tbx rc2 on pg.process_point_group_name = rc2.regional_value " +
			"where pg.process_point_id = ?1 and rc1.regional_code_name = ?2 and rc2.regional_code_name = ?3";	
	
	private static final String FIND_COUNT = "select * from galadm.process_point_group_tbx " +
			"where category_code = ?1 and site = ?2 and process_point_group_name in (";
	
    public List<ProcessPointGroupDto> findAllGroupsByProcessPointId(String ppid) {
		Parameters params = Parameters.with("1", ppid)
							.put("2", RegionalCodeName.CATEGORY_CODE.getName())
							.put("3", RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName());
		return findAllByNativeQuery(FIND_ALL_GROUPS_BY_PROCESS_POINT_ID, params, ProcessPointGroupDto.class);
    }
    
    public int getCount(short categoryCode, String site, List<String> processPointGroupList) {
    	String inProcessPointGroup = "'" + StringUtils.join(processPointGroupList, "','") + "'";
		Parameters params = Parameters.with("1", categoryCode).put("2", site);
		return findResultListByNativeQuery(FIND_COUNT + inProcessPointGroup + ")", params).size();
    }
}
