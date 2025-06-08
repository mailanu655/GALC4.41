package com.honda.galc.dao.jpa.qics;

import java.util.List;


import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.PartGroupDao;
import com.honda.galc.entity.qics.PartGroup;


 /** * *
 * @version 1
 * @author Gangadhararao Gadde
 * @since Jan 15,2015
 */
public class PartGroupDaoImpl extends BaseDaoImpl<PartGroup,String> implements PartGroupDao{

	private static String FIND_ALL_WILDCARD = "Select a from PartGroup a where a.partGroupName like ";

	public List<PartGroup> findLikePartGroupName(String wildcard) {
		
		return findAllByQuery(FIND_ALL_WILDCARD + "'" + wildcard + "'"+" order by a.partGroupName");
		
	}



}
