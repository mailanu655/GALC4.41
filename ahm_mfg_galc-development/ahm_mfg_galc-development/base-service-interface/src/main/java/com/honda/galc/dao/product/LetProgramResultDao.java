package com.honda.galc.dao.product;

import java.util.List;
import java.util.Map;

import com.honda.galc.data.LetInspectionDownloadDto;
import com.honda.galc.entity.product.LetProgramResult;
import com.honda.galc.entity.product.LetProgramResultId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */

public interface LetProgramResultDao extends
		IDaoService<LetProgramResult, LetProgramResultId> {
	
	public List<Object[]> fetchLetProgramResultDownloadData(LetInspectionDownloadDto dto);
	public List<Object[]> fetchLetProgramResultValueDownloadData(LetInspectionDownloadDto dto);
	public List<Object[]> getLetResultProgramDataForProductId(String productId);

	/**
	 * Method returns data for all programs (required  by YMTO)  that are not in Pass status or are missing.  
	 * @param productId  
	 * @param letCheckType - comma delimited let category type filter. Available values are defined in GAL730TBX.PGM_CATEGORY_NAME. If blank or null category filter is not used.
	 * @return
	 */
	
	public List<Map<String, Object>> findAllOutstandingPrograms(String productId, String letProgramCategory);
	
	/**
	 * If a criteria program corresponding to current inspection program is associated with triggering program (see LetConnectedProgram) and a result is in Pass status 
	 * then this method will verify if triggering program was executed after current program.
	 */	
	public boolean isProgramResultPassTerminated(String productId, int inspectionPgmId);

}
