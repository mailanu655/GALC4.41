package com.honda.galc.dao.jpa.qics;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.InspectionTwoPartDescriptionDao;
import com.honda.galc.entity.qics.InspectionTwoPartDescription;
import com.honda.galc.entity.qics.InspectionTwoPartDescriptionId;
import com.honda.galc.service.Parameters;
/** * *
* @version 1
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public class InspectionTwoPartDescriptionDaoImpl extends BaseDaoImpl<InspectionTwoPartDescription,InspectionTwoPartDescriptionId> implements InspectionTwoPartDescriptionDao{

	public List<InspectionTwoPartDescription> findByPartGroupInspLocPartName(String partGroupName,String inspecPartLocName,String inspecPartName)
	{
		Parameters params = Parameters.with("id.partGroupName", partGroupName).put("id.inspectionPartLocationName", inspecPartLocName).put("id.inspectionPartName",inspecPartName);
		return findAll(params);
	}

	@Transactional
	public void saveInspectionTwoPartDescription(InspectionTwoPartDescription inspectionTwoPartDescription)
	{

		Integer maxDescId = max("descriptionId", Integer.class);
		if (maxDescId == null) {
			maxDescId = 0;
		}	
		inspectionTwoPartDescription.setDescriptionId(++maxDescId);
		save(inspectionTwoPartDescription);
	}
	
	public List<InspectionTwoPartDescription> findByPartGroupName(String partGroupName)
	{
		Parameters params = Parameters.with("id.partGroupName", partGroupName);
		return findAll(params);
	}
	
	public List<InspectionTwoPartDescription> findByDescriptionId(int descriptionID)
	{
		Parameters params = Parameters.with("descriptionID", descriptionID);
		return findAll(params);
	}
	

}
