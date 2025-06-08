package com.honda.galc.dao.jpa.qi;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiIncidentDao;
import com.honda.galc.entity.qi.QiIncident;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QiIncidentDaoImpl Class description</h3>
 * <p>
 * QiIncidentDaoImpl contains methods for fetching the list of IncidentId'
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
public class QiIncidentDaoImpl extends BaseDaoImpl<QiIncident,String> implements QiIncidentDao{
	
	private static final String FIND_ALL_INCIDENT_TITLE="select case when INCIDENT_DATE is not null then incident_title || ' (' || TO_CHAR(INCIDENT_DATE,'YYYY-MM-DD') ||')' else INCIDENT_TITLE end from galadm.QI_INCIDENT_TBX order by INCIDENT_DATE desc";
	private static final String FIND_BY_INCIDENT_TITLE_AND_DATE="SELECT i FROM QiIncident i where i.incidentTitle=(:incidentTitle) and i.incidentDate like :incidentDate";
	
	public List<String> findAllIncidentTitle() {
		return findAllByNativeQuery(FIND_ALL_INCIDENT_TITLE,null,String.class);		
	}

	public QiIncident findByIncidentTitleAndDate(String incidentTitle,String incidentDate) {
		return findFirstByQuery(FIND_BY_INCIDENT_TITLE_AND_DATE, QiIncident.class, Parameters.with("incidentTitle", incidentTitle).put("incidentDate", "%"+incidentDate+"%"));
	}
	
}
