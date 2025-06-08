package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiIqsVersionDao;
import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIIqsVersionDaoImpl Class description</h3>
 * <p> QIIqsVersionDaoImpl description </p>
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
 * July 4 2016
 *
 *
 */

public class QiIqsVersionDaoImpl extends BaseDaoImpl<QiIqsVersion, String> implements QiIqsVersionDao {
	
	private static String UPDATE_IQS_VERSION = "update QI_IQS_VERSION_TBX  set IQS_VERSION= ?1 , " +
			"UPDATE_USER = ?2 where IQS_VERSION= ?3";
	
	
	/**
	 * To find all IQS Version 
	 */
	public List<QiIqsVersion> findAllIqsVersion(){
		return findAll(null,new String[]{"iqsVersion"},true);
	}
	/**
	 * To Update IQS Version 
	 * @param currentIqsVersion
	 * @param userId
	 * @param previousIqsCategory
	 */
	@Transactional
	public void updateIqsVersion(String currentIqsVersion, String userId, String previousIqsVersion) {
			Parameters params = Parameters.with("1", currentIqsVersion)
					.put("2", userId).put("3", previousIqsVersion);
			executeNativeUpdate(UPDATE_IQS_VERSION, params);
		}
	
}
