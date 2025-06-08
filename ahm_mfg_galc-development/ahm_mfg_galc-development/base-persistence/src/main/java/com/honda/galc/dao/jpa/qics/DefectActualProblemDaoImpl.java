package com.honda.galc.dao.jpa.qics;

import java.util.List;


import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.DefectActualProblemDao;
import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectActualProblemId;
import com.honda.galc.service.Parameters;
 /** * *
 * @version 1
 * @author Gangadhararao Gadde
 * @since Aug 02, 2013
 */
public class DefectActualProblemDaoImpl extends BaseDaoImpl<DefectActualProblem,DefectActualProblemId> implements DefectActualProblemDao{

	public List<DefectActualProblem> findAllByDefectDescription(String inspectionPartName,String defectTypeName, String secondaryPartName) {

		Parameters params = Parameters.with("id.inspectionPartName", inspectionPartName)
									  .put("id.defectTypeName", defectTypeName)
									  .put("id.secondaryPartName",secondaryPartName);
		return findAll(params);

	}

	public List<DefectActualProblem> findAllByDefectResult(String inspectionPartName,String defectTypeName, String secondaryPartName) {

		Parameters params = Parameters.with("id.inspectionPartName", inspectionPartName)
									  .put("id.defectTypeName", defectTypeName)
									  .put("id.secondaryPartName", secondaryPartName);
		return findAll(params);
	}
	
	public List<DefectActualProblem> findAllBySelectedValues(String modelCode,String defectTypeName,String inspectionPartName, String secondaryPartName,String actualProblemName){
		
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
		if(actualProblemName!=null&&!actualProblemName.equals("SELECT OR ENTER TEXT"))
		{
			params.put("id.actualProblemName",actualProblemName);
		}
		return findAll(params);
	}

}
