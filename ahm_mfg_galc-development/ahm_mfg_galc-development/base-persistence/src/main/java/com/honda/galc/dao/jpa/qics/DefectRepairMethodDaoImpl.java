package com.honda.galc.dao.jpa.qics;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.DefectRepairMethodDao;
import com.honda.galc.entity.qics.DefectRepairMethod;
import com.honda.galc.entity.qics.DefectRepairMethodId;
import com.honda.galc.service.Parameters;
 /** * *
 * @version 1
 * @author Gangadhararao Gadde
 * @since Aug 02, 2013
 */
public class DefectRepairMethodDaoImpl extends BaseDaoImpl<DefectRepairMethod,DefectRepairMethodId> implements DefectRepairMethodDao{

	public List<DefectRepairMethod> findAllByProblem(String modelCode,String inspectionPartName,String defectTypeName, String secondaryPartName) {
		Parameters params = Parameters.with("id.modelCode", modelCode)
		  .put("id.inspectionPartName", inspectionPartName)
		  .put("id.defectTypeName", defectTypeName)
		  .put("id.secondaryPartName",secondaryPartName);
        return findAll(params);
	}
	
	
	
	public List<DefectRepairMethod> findAllBySelectedValues(String modelCode,String defectTypeName,String inspectionPartName, String secondaryPartName,String repairMethodName){
		Parameters params = new Parameters();
		if(modelCode!=null)
		{
			params.put("id.modelCode",modelCode);
		}
		if(defectTypeName!=null)
		{
			params.put("id.defectTypeName",defectTypeName);
		}
		if(inspectionPartName!=null)
		{
			params.put("id.inspectionPartName",inspectionPartName);
		}
		if(secondaryPartName!=null)
		{
			params.put("id.secondaryPartName",secondaryPartName);
		}
		if(repairMethodName!=null&&!repairMethodName.equals("SELECT OR ENTER TEXT"))
		{
			params.put("id.repairMethodName",repairMethodName);
		}		
		return findAll(params);
	}

}
