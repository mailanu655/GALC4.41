package com.honda.galc.dao.jpa.qi;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiPddaStagingDao;
import com.honda.galc.entity.qi.QiPddaStaging;

/**
 * 
 * <h3>QiPddaStagingDaoImpl Class description</h3>
 * <p> QiPddaStagingDaoImpl is an implementation class for QiPddaStagingDao interface. </p>
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
 * Nov 26, 2016
 */

public class QiPddaStagingDaoImpl extends BaseDaoImpl<QiPddaStaging, Integer> implements QiPddaStagingDao {

	private static final String FIND_ALL_BY_DATA_COL_BY_QICS = "select e from QiPddaStaging e  where e.dataColByQics <> 'Y' order by e.pclToQicsSeq"; 

	/** This method is used to return list of pdda staging table unprocessed data.
	 * 
	 * @return List<QiPddaStaging>
	 */
	public List<QiPddaStaging> findAllByDataColByQics() {
		return findAllByQuery(FIND_ALL_BY_DATA_COL_BY_QICS);
	}
	
}
