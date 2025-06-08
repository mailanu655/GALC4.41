package com.honda.galc.dao.pdda;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.service.IDaoService;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface ChangeFormDao extends IDaoService<ChangeForm, Integer> {
	
	public int getChangeFormCount();
	
	public List<ChangeForm> getGetAllChangeForms();

	public List<ChangeForm> getChangeFormsForRevId(long revId);
	
	public List<Object[]> getProcessChangeForProcessPoint(String processPoint, int processChangeHistoryDays, int processChangeDisplayRows);
	
	public List<String> getUnprocessedChangeForms(List<Integer> changeFormIds);
	
	public List<PddaPlatformDto> getAllNewPlatforms();

	public List<ChangeForm> findAllByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode);
}
