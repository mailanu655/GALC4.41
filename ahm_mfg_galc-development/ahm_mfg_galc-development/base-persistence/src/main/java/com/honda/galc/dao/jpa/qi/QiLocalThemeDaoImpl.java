package com.honda.galc.dao.jpa.qi;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiLocalThemeDao;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiLocalThemeDaoImpl Class description</h3>
 * <p> QiLocalThemeDaoImpl description </p>
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
 * July 17, 2017
 */
public class QiLocalThemeDaoImpl extends BaseDaoImpl<QiLocalTheme, String> implements QiLocalThemeDao {

	
	private static final String FIND_ALL_LOCAL_THEME_BY_FILTER = "select e from QiLocalTheme e where  e.localTheme like :searchString or e.localThemeDescription like :searchString " ;
	
	private static final String  FIND_ALL_LOCAL_THEME_BY_THEME_AND_DESCRIPTION  = "select e from QiLocalTheme e where (e.localTheme like :searchString  or e.localThemeDescription like :searchString  ";
	
	private final static String UPDATE_LOCAL_THEME = "update QI_LOCAL_THEME_TBX  set LOCAL_THEME_DESCRIPTION = ?1, ACTIVE = ?2 , " +
			"UPDATE_USER = ?3, LOCAL_THEME =?4 where LOCAL_THEME= ?5";

	public List<QiLocalTheme> findAllActiveLocalTheme() {
		return findAll(Parameters.with("active", (short)1), new String[]{"localTheme"}, true);
	}

	public List<QiLocalTheme> findAllByFilter(String filterData) {
		Parameters params = Parameters.with("searchString", "%"+filterData+"%");
		if(filterData.equalsIgnoreCase("ACTIVE") || filterData.equalsIgnoreCase("INACTIVE") ){
			if(filterData.equalsIgnoreCase("ACTIVE"))
				params.put("isActive", (short)1);
			else if (filterData.equalsIgnoreCase("INACTIVE"))
				params.put("isActive", (short) 0);
			return findAllByQuery(FIND_ALL_LOCAL_THEME_BY_FILTER + " or e.active in (:isActive) " +" order by e.localTheme",params);	
		}
		return findAllByQuery(FIND_ALL_LOCAL_THEME_BY_FILTER  +" order by e.localTheme ",params);
	}


	public List<QiLocalTheme> findAllByFilterAndStatus(String filter, short status) {
		Parameters params = Parameters.with("searchString", "%"+filter+"%").put("status",status);
		if(filter.equalsIgnoreCase("ACTIVE") || filter.equalsIgnoreCase("INACTIVE") ){
			if(filter.equalsIgnoreCase("ACTIVE"))
				params.put("isActive", (short) 1);
			else if (filter.equalsIgnoreCase("INACTIVE"))
				params.put("isActive", (short) 0);
			return findAllByQuery( FIND_ALL_LOCAL_THEME_BY_THEME_AND_DESCRIPTION  + " or e.active = :isActive)  and e.active = :status order by e.localTheme ", params);
		}
		return findAllByQuery( FIND_ALL_LOCAL_THEME_BY_THEME_AND_DESCRIPTION  +  ") and e.active = :status order by e.localTheme ", params);
	}
	
	@Transactional  
	public void updateLocalTheme(QiLocalTheme qiLocalTheme) {
		update(qiLocalTheme);
	}
	
	@Transactional
	public void updateLocalTheme(QiLocalTheme qiLocalTheme, String oldLocalTheme) {
		Parameters params = Parameters.with("1", qiLocalTheme.getLocalThemeDescription())
				.put("2", qiLocalTheme.isActive() ? 1 : 0)
				.put("3", qiLocalTheme.getUpdateUser())
				.put("4", qiLocalTheme.getLocalTheme())
				.put("5", oldLocalTheme);
		executeNativeUpdate(UPDATE_LOCAL_THEME, params);
	}

}
