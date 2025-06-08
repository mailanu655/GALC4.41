package com.honda.galc.dao.jpa.qics;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.InspectionPartDescriptionDao;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.InspectionPartDescriptionId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>InspectionPartDescriptionDaoImpl Class description</h3>
 * <p> InspectionPartDescriptionDaoImpl description </p>
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
 * @author Jeffray Huang<br>
 * Feb 28, 2011
 *
 *
 */
public class InspectionPartDescriptionDaoImpl extends BaseDaoImpl<InspectionPartDescription,InspectionPartDescriptionId> implements InspectionPartDescriptionDao{

	public List<InspectionPartDescription> findAllByDescriptionId(
			int descriptionId) {
		
		return findAll(Parameters.with("descriptionId", descriptionId));
		
	}

	public List<InspectionPartDescription> findAllByPart(String partGroupName,
			String inspectionPartName,String inspectionPartLocationName) {
		
		Parameters params = new Parameters(); 
		if(!StringUtils.isEmpty(partGroupName)) params.put("id.partGroupName",partGroupName);
		if(!StringUtils.isEmpty(inspectionPartName)) params.put("id.inspectionPartName",inspectionPartName);
		if(!StringUtils.isEmpty(inspectionPartLocationName)) params.put("id.inspectionPartLocationName",inspectionPartLocationName);
		
		
		return findAll(params);
		
	}
	
	@Transactional
	public List<InspectionPartDescription> saveAll(List<InspectionPartDescription> inspectionPartDescriptions)  {
		Integer maxId = this.max("descriptionId", Integer.class);
		if (maxId == null) {
			maxId = 0;
		}
		for(InspectionPartDescription item : inspectionPartDescriptions) {
			item.setDescriptionId(++maxId);
		}
		return super.saveAll(inspectionPartDescriptions);
	}

	public List<InspectionPartDescription> findAllByPartGroupName(
			String partGroupName) {
		
		return findAll(Parameters.with("id.partGroupName", partGroupName),new String[]{"id.inspectionPartName"},true);
		
	}
	
	

}
