package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>GpcsDivisionDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> GpcsDivisionDao description </p>
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
 * @author Paul Chou
 * Aug 25, 2010
 *
 */
public interface GpcsDivisionDao extends IDaoService<GpcsDivision, String>{
	public GpcsDivision findByDivision(String devisionId);
	
	public String getGpcsPlantCode(String plantName);
	
	public List<String> getDivIdLst(String gpcsPlantCode, String gpcsLineNo, String gpcsProcessLoc);
	
	public List<Object[]> getGpcsDeptAndLine();
	
	public void saveDivision();
	public GpcsDivision saveDivisionLine(GpcsDivision gpcsDivision);
	
	public void update();
	public GpcsDivision updateDivisionLine(GpcsDivision gpcsDivision);
	
	public void delete();
	public GpcsDivision deleteDivisionLine(GpcsDivision gpcsDivision);
	
	public List<Object[]> findDivisionInfoAndGpcs(String divisionId);
	
    public List<Object[]> findLineInfoAndGpcsData(String lineId);

	public List<Object[]> findProcessPointInfoAndGpcsData(String processPointId);


}
