package com.honda.galc.dao.jpa.mdrs;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.mdrs.ManpowerAssignmentDao;
import com.honda.galc.entity.mdrs.ManpowerAssignment;
import com.honda.galc.entity.mdrs.ManpowerAssignmentId;

public class ManpowerAssignmentDaoImpl 
	extends BaseDaoImpl<ManpowerAssignment, ManpowerAssignmentId> 
		implements ManpowerAssignmentDao {
	private static final String FIND_ALL_DEPT_CODES = "select distinct(dept_code) from vios.manpower_assignment";
	
	private static final String FIND_UNMAPPED_QUARTERS = "select ro.PLANT_LOC_CODE, ro.LINE_NUM, ro.DEPT_CODE, ro.SHIFT_ID, ma.QUARTER " +
			"from vios.MANPOWER_ASSIGNMENT ma left join vios.ROTATION ro on ma.ROTATION_ID = ro.ROTATION_ID and ma.EXTRACT_DATE = ro.EXTRACT_DATE " +
			" group by ro.PLANT_LOC_CODE, ro.LINE_NUM, ro.DEPT_CODE, ro.SHIFT_ID, ma.QUARTER";
	
	public List<String> findDepts() {
		
		return findAllByNativeQuery(FIND_ALL_DEPT_CODES, null, String.class);
		
	}
	
	public List<Object[]> getUnmappedQuarters() {
		return findAllByNativeQuery(FIND_UNMAPPED_QUARTERS, null, Object[].class);
	}

}
