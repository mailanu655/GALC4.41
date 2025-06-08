package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiIqsCategoryDao;
import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIIqsCategoryDaoImpl Class description</h3>
 * <p> QIIqsCategoryDaoImpl description </p>
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
 * July 7 2016
 *
 *
 */

public class QiIqsCategoryDaoImpl extends BaseDaoImpl<QiIqsCategory, String> implements QiIqsCategoryDao {
	private static String UPDATE_IQS_CATEGORY = "update QI_IQS_CATEGORY_TBX  set IQS_CATEGORY= ?1 , " +
			"UPDATE_USER = ?2 where IQS_CATEGORY= ?3";
	
	/**
	 * To find all IQS Category 
	 */
	public List<QiIqsCategory> findAllIqsCategory(){
		return findAll(null,new String[]{"iqsCategory"},true);
	}
	
	/**
	 * To Update IQS Category 
	 * @param currentIqsCategory
	 * @param userid
	 * @param previousIqsCategory
	 */
	@Transactional
	public void updateIqsCategory(String currentIqsCategory, String userId, String previousIqsCategory) {
		Parameters params = Parameters.with("1", currentIqsCategory)
				.put("2", userId).put("3", previousIqsCategory);
		executeNativeUpdate(UPDATE_IQS_CATEGORY, params);
	}

}
