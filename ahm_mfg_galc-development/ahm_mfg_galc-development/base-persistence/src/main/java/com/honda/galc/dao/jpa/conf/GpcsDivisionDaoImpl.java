package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>GpcsDivisionDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> GpcsDivisionDaoImpl description </p>
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
public class GpcsDivisionDaoImpl extends BaseDaoImpl<GpcsDivision,String> implements GpcsDivisionDao{

    private static final String GET_GPCS_PLANT_CODE = "select gpcs.GPCS_PLANT_CODE from GALADM.GAL211TBX plant " +
    														"inner join GALADM.GAL128TBX div on plant.PLANT_NAME = div.PLANT_NAME " +
    														"inner join GALADM.GAL238TBX gpcs on div.DIVISION_ID = gpcs.DIVISION_ID " +
    														"where plant.PLANT_NAME = ?1 group by gpcs.GPCS_PLANT_CODE for read only";
    
    private static final String GET_DIVISIONS = "SELECT GPCSDIV.DIVISION_ID FROM GAL238TBX GPCSDIV WHERE GPCSDIV.GPCS_PLANT_CODE = ?1 AND GPCSDIV.GPCS_LINE_NO = ?2 AND GPCSDIV.GPCS_PROCESS_LOCATION = ?3";
    
    private static final String FIND_GPCS_DATA = "SELECT Division.SITE_NAME, Division.PLANT_NAME, DivToGPCSDeptLine.DIVISION_ID, Division.DIVISION_NAME, DivToGPCSDeptLine.GPCS_PLANT_CODE, "+
    				 "DivToGPCSDeptLine.GPCS_PROCESS_LOCATION, DivToGPCSDeptLine.GPCS_LINE_NO, (select PLAN_CODE  FROM GAL226TBX WHERE "+ 
    				 "GAL226TBX.PLANT_CODE = DivToGPCSDeptLine.GPCS_PLANT_CODE AND GAL226TBX.LINE_NO = DivToGPCSDeptLine.GPCS_LINE_NO "+
    				 "AND GAL226TBX.PROCESS_LOCATION = DivToGPCSDeptLine.GPCS_PROCESS_LOCATION "+
    				 "FETCH FIRST 1 ROWS ONLY) AS GAL226TBX_Schedule_PLAN_CODE FROM GAL238TBX DivToGPCSDeptLine "+     
    				 "INNER JOIN GAL128TBX Division ON DivToGPCSDeptLine.DIVISION_ID=Division.DIVISION_ID WHERE 1=1 ORDER BY DivToGPCSDeptLine.DIVISION_ID";
    
    
    private static String updateDivisionLineQuery ="GPCS_PLANT_CODE=?1,GPCS_LINE_NO=?2,GPCS_PROCESS_LOCATION=?3 where DIVISION_ID=?4";
    
	
	private static final String FIND_DIVISION_INFO_AND_GPCS = 	"SELECT gpcs.gpcs_process_location, gpcs.gpcs_line_no "+
			"from GAL238TBX gpcs where gpcs.division_id=?1";
    
	private static final String FIND_PROCESS_POINT_INFO_AND_GPCS_DATA= "SELECT process.division_id, gpcs.gpcs_process_location, gpcs.gpcs_line_no  "+
			"from GAL214TBX process join GAL238TBX gpcs on gpcs.division_id=process.division_id where process.process_point_id = ?1";
	
	private static final String FIND_LINE_INFO_AND_GPCS_DATA = "SELECT  line.LINE_ID, line.DIVISION_ID, gpcs.GPCS_PROCESS_LOCATION, gpcs.GPCS_LINE_NO "+
			 " FROM GAL195TBX line JOIN GAL238TBX gpcs ON line.DIVISION_ID = gpcs.DIVISION_ID WHERE LINE_ID=?1";


	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public GpcsDivision findByDivision(String devisionId) {
		return findByKey(devisionId);
	}

	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public GpcsDivision findByKey(String id) {
		return super.findByKey(id);
	}

	public String getGpcsPlantCode(String plantName) {
		Parameters params = Parameters.with("1", plantName);
		return findFirstByNativeQuery(GET_GPCS_PLANT_CODE, params, String.class);
	}
	
	public List<String> getDivIdLst(String gpcsPlantCode, String gpcsLineNo, String gpcsProcessLoc){
		Parameters params = new Parameters();
		params.put("1", gpcsPlantCode);
		params.put("2", gpcsLineNo);
		params.put("3", gpcsProcessLoc);
		
		return  findAllByNativeQuery(GET_DIVISIONS, params, String.class);
	}
	
	
	public List<Object[]> getGpcsDeptAndLine(){
		return  executeNative(FIND_GPCS_DATA);
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDivision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public GpcsDivision saveDivisionLine(GpcsDivision gpcsDivision) {
		return save(gpcsDivision);		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public GpcsDivision updateDivisionLine(GpcsDivision gpcsDivision) {
		Parameters updateParam = new Parameters();

		updateParam.put("gpcsPlantCode", gpcsDivision.getGpcsPlantCode());
		updateParam.put("gpcsLineNo", gpcsDivision.getGpcsLineNo());
		updateParam.put("gpcsProcessLocation", gpcsDivision.getGpcsProcessLocation());
		
		Parameters whereParam = new Parameters();
		whereParam.put("divisionId", gpcsDivision.getDivisionId());
		update(updateParam, whereParam);
		
		return new GpcsDivision();
	}

	public Division updateDivision(Division division) {
		Parameters updateParam = new Parameters();

		updateParam.put("siteName", division.getSiteName());
		updateParam.put("plantName", division.getDivisionName());
		return new Division();
		
	}
	
	@Override
	public void delete() {
 
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public GpcsDivision deleteDivisionLine(GpcsDivision gpcsDivision) {
		remove(gpcsDivision);
		return gpcsDivision;	
	}	

	@Override
	public List<Object[]> findDivisionInfoAndGpcs(String divisionId) {
		Parameters params = Parameters.with("1", divisionId);
		@SuppressWarnings("unchecked")
		List<Object[]> divisionListGpcs = findAllByNativeQuery(FIND_DIVISION_INFO_AND_GPCS,params,Object[].class);
		return divisionListGpcs;
	}
	
	@Override
	public List<Object[]> findProcessPointInfoAndGpcsData(String processPointId) {
		Parameters params = Parameters.with("1", processPointId);
		@SuppressWarnings("unchecked")
		List<Object[]> processPointListGpcsData = findAllByNativeQuery(FIND_PROCESS_POINT_INFO_AND_GPCS_DATA,params,Object[].class);
		return processPointListGpcsData;
	}	
	
	@Override
	public List<Object[]> findLineInfoAndGpcsData(String lineId) {

		Parameters params = Parameters.with("1", lineId);
		
		List<Object[]> lineListGpcsData = findAllByNativeQuery(FIND_LINE_INFO_AND_GPCS_DATA, params, Object[].class);
		return lineListGpcsData;
	}

}
