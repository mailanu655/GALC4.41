package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsNodeId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsNodeDao Class description</h3>
 * <p> GtsNodeDao description </p>
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
 * @author Jeffray Huang<br>
 * May 21, 2015
 *
 *
 */
public interface GtsNodeDao extends IDaoService<GtsNode, GtsNodeId>{
	
	public List<GtsNode> findAll(String trackingArea);
	
	public GtsNode findByNodeName(String trackingArea, String nodeName);
	
	public  int updateNodeStatus(String trackingArea,String nodeName,int status);
	
	public Integer getNextNodeId(String areaName);
	
}
