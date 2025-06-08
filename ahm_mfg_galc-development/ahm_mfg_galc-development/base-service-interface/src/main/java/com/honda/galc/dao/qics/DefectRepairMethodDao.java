package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectRepairMethod;
import com.honda.galc.entity.qics.DefectRepairMethodId;
import com.honda.galc.service.IDaoService;

 /** * *
 * @version 1
 * @author Gangadhararao Gadde
 * @since Aug 02, 2013
 */
public interface DefectRepairMethodDao extends IDaoService<DefectRepairMethod, DefectRepairMethodId> {

	public List<DefectRepairMethod> findAllByProblem(String modelCode,String inspectionPartName,String defectTypeName, String secondaryPartName);
	
	
	public	List<DefectRepairMethod> findAllBySelectedValues(String modelCode,String defectTypeName,String inspectionPartName, String secondaryPartName,String repairMethodName);

}
