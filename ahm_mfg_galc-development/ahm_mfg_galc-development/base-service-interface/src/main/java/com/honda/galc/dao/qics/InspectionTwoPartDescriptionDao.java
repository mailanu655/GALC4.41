package com.honda.galc.dao.qics;


import java.util.List;

import com.honda.galc.entity.qics.InspectionTwoPartDescription;
import com.honda.galc.entity.qics.InspectionTwoPartDescriptionId;
import com.honda.galc.service.IDaoService;

/** * *
* @version 
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public interface InspectionTwoPartDescriptionDao extends IDaoService<InspectionTwoPartDescription, InspectionTwoPartDescriptionId> {

	public List<InspectionTwoPartDescription> findByPartGroupInspLocPartName(String partGroupName,String inspecPartLocName,String inspecPartName);
	
	public void saveInspectionTwoPartDescription(InspectionTwoPartDescription inspectionTwoPartDescription);
	
	public List<InspectionTwoPartDescription> findByPartGroupName(String partGroupName);
	
	public List<InspectionTwoPartDescription> findByDescriptionId(int descriptionID);
}
