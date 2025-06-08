package com.honda.galc.dao.jpa.qi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationEntryScreenId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationEntryScreenDaoImpl</code> is an implementation class for QiStationEntryScreenDao interface.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 1</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class QiStationEntryScreenDaoImpl extends BaseDaoImpl<QiStationEntryScreen, QiStationEntryScreenId> implements QiStationEntryScreenDao{

	@Autowired
    private	QiImageDao qiImageDao;

	private static final String UPDATE_ENTRY_SCREEN_AND_MODEL_NAME = "update QiStationEntryScreen e "
    		+ "set e.entryScreen=:newEntryScreen, e.id.entryModel=:newEntryModel, e.updateUser=:updateUser where e.entryScreen=:oldEntryScreen and e.id.entryModel=:oldEntryModel";
	
	private static final String FIND_ALL_BY_MODEL_DEPT_PROCESSPOINT = "select ses.ENTRY_MODEL,ses.ENTRY_SCREEN, "
			+ "case when es.IS_IMAGE=1 then es.IMAGE_NAME when es.IS_IMAGE=0 then 'TEXT' end as IMAGE_NAME,ses.ORIENTATION_ANGLE,ses.SEQ ,ses.ALLOW_SCAN "
			+ "from galadm.QI_STATION_ENTRY_SCREEN_TBX ses,galadm.QI_ENTRY_SCREEN_TBX es where es.ENTRY_SCREEN=ses.ENTRY_SCREEN and es.ENTRY_MODEL=ses.ENTRY_MODEL and es.IS_USED=1 "
			+ "and ses.ENTRY_MODEL=?1 and ses.DIVISION_ID=?2 and ses.PROCESS_POINT_ID=?3 order by ses.SEQ asc ";
	
	private static final String FIND_ALL_ENTRY_SCREEN_BY_PROCESSPOINT = "SELECT SEL.ENTRY_SCREEN, SEL.IS_IMAGE, SEL.IMAGE_NAME, SEL.ORIENTATION_ANGLE " + 
			"FROM (SELECT DISTINCT QSES.SEQ, QSES.ENTRY_SCREEN, QES.IS_IMAGE, QES.IMAGE_NAME, QSES.ORIENTATION_ANGLE " + 
			"FROM GALADM.QI_STATION_ENTRY_SCREEN_TBX QSES JOIN GALADM.QI_ENTRY_SCREEN_TBX QES " +
			"ON QSES.DIVISION_ID=?1 AND QSES.PROCESS_POINT_ID=?2 AND QSES.ENTRY_SCREEN=QES.ENTRY_SCREEN " + 
			"AND QSES.ENTRY_MODEL=QES.ENTRY_MODEL AND QES.IS_USED=1 JOIN GALADM.QI_ENTRY_MODEL_GROUPING_TBX EMG " +
			"ON EMG.ENTRY_MODEL=QSES.ENTRY_MODEL AND EMG.MTC_MODEL=?3 JOIN GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX LDC " + 
			"ON LDC.ENTRY_SCREEN=QSES.ENTRY_SCREEN AND LDC.ENTRY_MODEL=QSES.ENTRY_MODEL AND LDC.IS_USED=1 " + 
			"JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC ON LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID " +
			"JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID=RDC.PART_LOCATION_ID " +
			"LEFT OUTER JOIN GALADM.QI_IMAGE_SECTION_TBX QIS ON RDC.PART_LOCATION_ID=QIS.PART_LOCATION_ID AND QIS.IMAGE_NAME=QES.IMAGE_NAME ";
	
    private static final String FIND_PROCESS_POINT_BY_ENTRY_SCREEN = "select distinct PROCESS_POINT_ID from QI_STATION_ENTRY_SCREEN_TBX where ENTRY_SCREEN = ?1 order by PROCESS_POINT_ID ";
    
    private static final String UPDATE_ENTRY_MODEL_NAME = "update GALADM.QI_STATION_ENTRY_SCREEN_TBX e "
    		+ "set e.ENTRY_MODEL = ?1, e.UPDATE_USER =?2 where e.ENTRY_MODEL = ?3";
    
	/**
	 * This method is used to find EntryScreen by process point
	 */
	public List<QiEntryScreenDto> findAllEntryScreenInfoByProcessPoint(String entryModel,String entryDept,String qicsStation){
		return findAllByNativeQuery(FIND_ALL_BY_MODEL_DEPT_PROCESSPOINT, Parameters.with("1", entryModel).put("2", entryDept).put("3", qicsStation), QiEntryScreenDto.class);
	}
	/**
	 * This method is used to find QiStationEntryScreen by EntryScreen
	 */
	public List<QiStationEntryScreen> findAllByEntryScreen(String entryModel, String entryScreen) {						
		return findAll(Parameters.with("entryScreen", entryScreen).put("id.entryModel", entryModel));
	}
	
	public QiStationEntryScreen findStationEntryScreenByIdAndEntryScreen(QiStationEntryScreen qiStationEntryScreen) {
		return findFirst(Parameters.with("id.processPointId", qiStationEntryScreen.getId().getProcessPointId()).put("id.entryModel",qiStationEntryScreen.getId().getEntryModel()).put("id.divisionId",qiStationEntryScreen.getId().getDivisionId()).put("entryScreen", qiStationEntryScreen.getEntryScreen()));
	}

	/**
	 * This method is used to fetch list of all entry screens for a given process point
	 */
	public List<QiDefectEntryDto> findAllEntryScreenByProcessPoint(String filterValue, String processPoint, String mtcModel, String entryDept, boolean realProblemScreen) {
		Parameters params = Parameters.with("1", entryDept)
							.put("2", processPoint)
							.put("3", mtcModel);
		
		StringBuilder query = new StringBuilder(FIND_ALL_ENTRY_SCREEN_BY_PROCESSPOINT);
		boolean hasFilter = false;
		if(!StringUtils.isEmpty(filterValue)) {
			params.put("4", filterValue);
			query.append("WHERE (PLC.INSPECTION_PART_NAME = ?4 OR PLC.INSPECTION_PART2_NAME = ?4) ");
			hasFilter = true;
		}
		if (realProblemScreen) {
			if (hasFilter) {
				query.append("AND LDC.DEFECT_CATEGORY_NAME = 'REAL PROBLEM' ");
			} else {
				query.append("WHERE LDC.DEFECT_CATEGORY_NAME = 'REAL PROBLEM' ");
			}
		}
		query.append("ORDER BY QSES.SEQ ) SEL ");
		List<QiDefectEntryDto> qiDefectEntryDtolst =findAllByNativeQuery(query.toString(), params, QiDefectEntryDto.class); 
		setImageData(qiDefectEntryDtolst);
		return qiDefectEntryDtolst;
	}
	
	private void setImageData(List<QiDefectEntryDto> qiDefectEntryDtolst)  {
		if(qiDefectEntryDtolst == null || qiDefectEntryDtolst.isEmpty())  return;
		//get image_data by additional loop to avoid outer join
		List<String> imageNames = new ArrayList<String>();
		for(QiDefectEntryDto operObj : qiDefectEntryDtolst){
			if (StringUtils.isNotBlank(operObj.getImageName())) {
				imageNames.add(operObj.getImageName());
			}
		}
		if (imageNames.isEmpty()) {
			return;
		}
		List<QiImage> images = getQiImageDao().findAllByImageName(imageNames);
		if (images == null || images.isEmpty()) {
			return;
		}
		Map<String, QiImage> map = new HashMap<String, QiImage>();
		for (QiImage img : images) {
			map.put(img.getImageName(), img);
		}
		for(QiDefectEntryDto dto : qiDefectEntryDtolst){
			if (StringUtils.isNotBlank(dto.getImageName()) && map.get(dto.getImageName()) != null) {
				byte[] data =  map.get(dto.getImageName()).getImageData();
				dto.setImageData( data);
			}
		}
		return;

	}
	
	/**
	 * This method is used to fetch list of all entry screens for a given process point
	 */
	@Override
	public List<QiDefectEntryDto> findAllEntryScreenByProcessPointAndPartLocation(String filterValue, String processPoint, String mtcModel, String entryDept, boolean realProblemScreen) {
		Parameters params = Parameters.with("1", entryDept)
							.put("2", processPoint)
							.put("3", mtcModel);
		
		StringBuilder query = new StringBuilder(FIND_ALL_ENTRY_SCREEN_BY_PROCESSPOINT);
		boolean hasFilter = false;
		if(!StringUtils.isEmpty(filterValue)) {
			params.put("4", filterValue.trim());
			query.append("WHERE trim(BOTH '_' FROM replace(replace(replace(trim(PLC.INSPECTION_PART_NAME) || '_' || trim(nvl(PLC.INSPECTION_PART_LOCATION_NAME,'')) || '_' || trim(nvl(PLC.INSPECTION_PART_LOCATION2_NAME,'')) " +
					" || '_' || trim(nvl(PLC.INSPECTION_PART2_NAME,'')) || '_' || trim(nvl(PLC.INSPECTION_PART2_LOCATION_NAME,'')) || '_' || trim(nvl(PLC.INSPECTION_PART2_LOCATION2_NAME,'')) " +
					" || '_' || trim(nvl(PLC.INSPECTION_PART3_NAME,''))" +
					",'__','_'),'__','_'),'__','_')) = ?4 "); 
			hasFilter = true;
		}
		if (realProblemScreen) {
			if (hasFilter) {
				query.append(" AND LDC.DEFECT_CATEGORY_NAME = 'REAL PROBLEM' ");
			} else {
				query.append(" WHERE LDC.DEFECT_CATEGORY_NAME = 'REAL PROBLEM' ");
			}
		}
		query.append("ORDER BY QSES.SEQ ) SEL ");
		List<QiDefectEntryDto> qiDefectEntryDtolst =findAllByNativeQuery(query.toString(), params, QiDefectEntryDto.class); 
		setImageData(qiDefectEntryDtolst);
		return qiDefectEntryDtolst;
	}
	/**
	 * This method is used to find the list of processPoint based on entryScreen
	 */
	public List<String> findAllProcessPointByEntryScreen(String entryScreen){
		Parameters params = Parameters.with("1", entryScreen);
		return findAllByNativeQuery(FIND_PROCESS_POINT_BY_ENTRY_SCREEN, params, String.class);
	}
	
	public List<QiStationEntryScreen> findAllByEntryScreenModelAndDept(String entryScreenName,String entryModel,String entryDept,String processPointId) {
		Parameters params = Parameters.with("entryScreen", entryScreenName).put("id.entryModel", entryModel).put("id.divisionId", entryDept).put("allowScan", (short)1);
		if(!StringUtils.isEmpty(processPointId)){
			params.put("id.processPointId", processPointId);
		}
		return findAll(params);
	}
	
	@Transactional
	public void updateEntryScreenAndModel(QiEntryScreenId newEntryScreenId, String oldEntryScreen, String oldEntryModel, String userId) {
		Parameters params = Parameters.with("newEntryScreen", newEntryScreenId.getEntryScreen())
				.put("newEntryModel", newEntryScreenId.getEntryModel())
				.put("updateUser", userId)
				.put("oldEntryScreen", oldEntryScreen)
				.put("oldEntryModel", oldEntryModel);
		executeUpdate(UPDATE_ENTRY_SCREEN_AND_MODEL_NAME, params);
	}
	
	public List<QiStationEntryScreen> findAllByEntryModel(String entryModel) {
		return findAll(Parameters.with("id.entryModel", entryModel));
	}
	
	public List<QiStationEntryScreen> findAllByProcessPointAndEntryModel(String processPointId, String entryModel) {
		return findAll(Parameters.with("id.processPointId", processPointId).put("id.entryModel", entryModel));
	}
	
	public List<QiStationEntryScreen> findAllEntryScreenByProcessPointAndDivision(String processPoint, String division) {
		return findAll(Parameters.with("id.processPointId", processPoint).put("id.divisionId", division));
	}
	
	public long countEntryScreenByProcessPointAndDivision(String processPoint, String division) {
		return count(Parameters.with("id.processPointId", processPoint).put("id.divisionId", division));
	}
	
	@Transactional
	public int deleteByProcessPoint(String processPointId) {
		return delete(Parameters.with("id.processPointId", StringUtils.trimToEmpty(processPointId)));
	}
	
	@Transactional
	public void removeByProcesspointModelAndDivision(String qicsStation, String entryModel, String entryDept) {
		delete(Parameters.with("id.processPointId", qicsStation).put("id.entryModel", entryModel).put("id.divisionId", entryDept));
	}
	
	@Transactional
	public void updateEntryModelName(String newEntryModel, String oldEntryModel, String userId) {
		Parameters params = Parameters.with("1", newEntryModel)
				.put("2", userId)
				.put("3", oldEntryModel);
		executeNativeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	
	public QiImageDao getQiImageDao() {
		return qiImageDao;
	}
	public void setQiImageDao(QiImageDao qiImageDao) {
		this.qiImageDao = qiImageDao;
	}
}