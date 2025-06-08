package com.honda.galc.dao.product;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.service.IDaoService;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 03, 2013
 */
public interface LetInspectionProgramDao extends IDaoService<LetInspectionProgram, Integer> {
	
	public Map<String, Integer> loadLetInspectionProgram();
	public List<LetInspectionProgram> findAllLetInspPgmOrderByPgmName();
	public List<Object[]> getLetVinFaultResultData(String productId);
	public LetInspectionProgram findPgmIdByName(String programName);
	public int insertIfNotExists(int id, String name);
}
