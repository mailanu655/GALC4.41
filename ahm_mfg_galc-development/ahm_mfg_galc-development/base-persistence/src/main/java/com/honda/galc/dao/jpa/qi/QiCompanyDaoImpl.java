package com.honda.galc.dao.jpa.qi;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiCompanyDao;
import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiCompanyDaoImpl Class description</h3>
 * <p> QiCompanyDaoImpl description </p>
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
 * @author L&T Infotech<br>
 *
 */

public class QiCompanyDaoImpl extends BaseDaoImpl<QiCompany, String> implements QiCompanyDao {
	
	private final static String UPDATE_COMPANY = "Update galadm.QI_COMPANY_TBX set COMPANY_DESCRIPTION=?1, ACTIVE=?2, UPDATE_USER=?3, COMPANY = ?4 where COMPANY=?5 ";
	  
	  
	/** This method will be used to update Company
	 * 
	 * @param newCompanyName
	 * @param oldCompanyName
	 * @param companyDescription
	 * @param active
	 * @param updateUser
	 */
	@Transactional  
	public void updateCompany(String newCompanyName, String oldCompanyName, String companyDescription, int active, String updateUser) {
	
		Parameters params = Parameters.with("1", companyDescription).put("2", active).put("3", updateUser).put("4", newCompanyName).put("5", oldCompanyName);
		executeNativeUpdate(UPDATE_COMPANY, params);
	}

	/** This method will in-activate the company using companyName
	 * 
	 * @param companyName
	 */
	@Transactional 
	public void inactivateCompany(String companyName) {
		update(Parameters.with("ACTIVE", 0), Parameters.with("COMPANY", companyName));
	}

}
