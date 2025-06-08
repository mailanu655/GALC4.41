package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.ProcessPointGroupDto;
import com.honda.galc.entity.conf.ProcessPointGroup;
import com.honda.galc.entity.conf.ProcessPointGroupId;
import com.honda.galc.service.IDaoService;

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
public interface ProcessPointGroupDao extends IDaoService<ProcessPointGroup, ProcessPointGroupId> {
    public List<ProcessPointGroupDto> findAllGroupsByProcessPointId(String ppid);
    public int getCount(short categoryCode, String site, List<String> processPointGroupList);
}
