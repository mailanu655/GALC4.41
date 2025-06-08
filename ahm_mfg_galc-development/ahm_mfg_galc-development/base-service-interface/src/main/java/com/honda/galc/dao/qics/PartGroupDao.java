package com.honda.galc.dao.qics;




import java.util.List;


import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 15, 2015
 */
public interface PartGroupDao extends IDaoService<PartGroup, String> {

	public List<PartGroup> findLikePartGroupName(String likePartGroupName);
	

}
