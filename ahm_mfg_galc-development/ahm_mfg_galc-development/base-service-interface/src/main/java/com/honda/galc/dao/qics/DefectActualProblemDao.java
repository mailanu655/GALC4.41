package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectActualProblemId;
import com.honda.galc.service.IDaoService;

 /** * *
 * @version 1
 * @author Gangadhararao Gadde
 * @since Aug 02, 2013
 */
public interface DefectActualProblemDao extends IDaoService<DefectActualProblem, DefectActualProblemId> {

	public List<DefectActualProblem> findAllByDefectDescription(String inspectionPartName,String defectTypeName, String secondaryPartName);

	public	List<DefectActualProblem> findAllByDefectResult(String inspectionPartName,String defectTypeName, String secondaryPartName);
	
	public	List<DefectActualProblem> findAllBySelectedValues(String modelCode,String defectTypeName,String inspectionPartName, String secondaryPartName,String actualProblemName);


}
