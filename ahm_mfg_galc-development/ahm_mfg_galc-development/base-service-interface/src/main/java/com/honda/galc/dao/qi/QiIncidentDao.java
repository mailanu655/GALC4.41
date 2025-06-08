package com.honda.galc.dao.qi;


import java.util.List;
import com.honda.galc.entity.qi.QiIncident;
import com.honda.galc.service.IDaoService;


/**
 * 
 * <h3>QiIncidentDao Interface description</h3>
 * <p>
 * QiIncidentDao interface for QiIncidentDaoImpl
 *  </p>
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
 * 
 */
public interface QiIncidentDao extends IDaoService<QiIncident, String>{

	public List<String> findAllIncidentTitle();

	public QiIncident findByIncidentTitleAndDate(String incidentTitle,String incidentDate);
	
}
