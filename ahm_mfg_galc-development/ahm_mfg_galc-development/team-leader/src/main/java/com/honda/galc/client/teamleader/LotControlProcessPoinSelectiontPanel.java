package com.honda.galc.client.teamleader;

import java.util.List;

import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>LotControlProcessPoinSelectiontPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlProcessPoinSelectiontPanel description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 18, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 18, 2011
 */

public class LotControlProcessPoinSelectiontPanel extends ProcessPointSelectiontPanel{
	private static final long serialVersionUID = 1L;
	public LotControlProcessPoinSelectiontPanel(String siteName) {
		super(siteName);
	}
	
	@Override
	protected SortedArrayList<ProcessPoint> getProcessPoints(Division division) {
		List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class)
		.findAllLotControlProcessPointByDivision(division);
    	return new SortedArrayList<ProcessPoint>(processPoints, processPointSortingMethod);
	}

}
