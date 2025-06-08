package com.honda.galc.dao.jpa.conf;


import java.util.List;

import com.honda.galc.dao.conf.RegionalProcessPointGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.conf.RegionalProcessPointGroupId;
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
public class RegionalProcessPointGroupDaoImpl extends BaseDaoImpl<RegionalProcessPointGroup, RegionalProcessPointGroupId> implements RegionalProcessPointGroupDao {

    private final String FIND_ALL_BY_CODE_AND_MATCHING_TEXT = "select g from RegionalProcessPointGroup g where g.id.categoryCode = :code" +
    				" and upper(g.id.processPointGroupName) like :text";
    
    public List<RegionalProcessPointGroup> findAllByCategoryCode(short code) {
    	return findAll(Parameters.with("id.categoryCode", code));
    }
    
    public List<RegionalProcessPointGroup> findAllByCodeAndMatchingText(short code, String text) {
		Parameters params = Parameters.with("code", code)
									.put("text", "%" + text.toUpperCase() + "%");
    	return findAllByQuery(FIND_ALL_BY_CODE_AND_MATCHING_TEXT, params);
    }
    
	public long findCountByCategoryCode(short categroyCode) {
		Parameters params = Parameters.with("id.categoryCode", categroyCode);
		return count(params);
	}
	
	public long findCountByprocessPointGroupName(String processPointGroupName) {
		Parameters params = Parameters.with("id.processPointGroupName", processPointGroupName);
		return count(params);
	}
}
