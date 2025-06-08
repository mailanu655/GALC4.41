package com.honda.galc.dao.jpa.qi;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.dto.qi.QiRepairAreaSapceAssignmentDto;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.service.Parameters;

public class QiRepairAreaSpaceDaoImpl extends BaseDaoImpl<QiRepairAreaSpace, QiRepairAreaSpaceId> implements QiRepairAreaSpaceDao {

	private static final String FIND_ALL_AVAILABLE_BY_REPAIR_AREA_AND_ROW = "select distinct e.REPAIR_AREA_SPACE from galadm.QI_REPAIR_AREA_SPACE_TBX e "
			+ "where e.REPAIR_AREA_NAME = ?1 and e.REPAIR_AREA_ROW = ?2 and e.ACTIVE = 1 and e.PRODUCT_ID IS NULL order by e.REPAIR_AREA_SPACE asc";
	
	
	private static final String FIND_SPACE_ASSIGNMENT = "select ras.REPAIR_AREA_NAME,ras.REPAIR_AREA_ROW,ras.REPAIR_AREA_SPACE,ras.PRODUCT_ID,dr.PRODUCT_TYPE,dr.DEFECT_TYPE_NAME,dr.RESPONSIBLE_PLANT from galadm.QI_REPAIR_AREA_SPACE_TBX ras full outer join galadm.QI_DEFECT_RESULT_TBX dr on ras.DEFECTRESULTID = dr.DEFECTRESULTID where ras.REPAIR_AREA_NAME=?1 and ras.REPAIR_AREA_ROW=?2";
	
	private static final String UPDATE_REPAIR_AREA_SPACE_BY_REPAIR_AREA = "update GALADM.QI_REPAIR_AREA_SPACE_TBX  set REPAIR_AREA_NAME = ?1, UPDATE_USER = ?2" +			
			" where REPAIR_AREA_NAME = ?3";


	private static final String FIND_FIRST_AVAILABLE_BY_ROW = "SELECT * FROM GALADM.QI_REPAIR_AREA_SPACE_TBX S WHERE S.REPAIR_AREA_NAME = ?1 "
			+ "AND S.REPAIR_AREA_ROW  = ?2 AND S.ACTIVE = 1 AND S.PRODUCT_ID IS NULL AND S.DEFECTRESULTID IS NULL  ";
	
	private static final String FIND_PRODUCTS_BY_REPAIR_AREA = "SELECT PRODUCT_ID FROM GALADM.QI_REPAIR_AREA_SPACE_TBX WHERE REPAIR_AREA_NAME = ?1 "
			+ "AND ACTIVE = 1 AND PRODUCT_ID IS NOT NULL ";
	
	private static final String GET_NAQ_PARKING_SPACE = "SELECT * FROM GALADM.QI_REPAIR_AREA_SPACE_TBX S where "
														+ "S.REPAIR_AREA_NAME = ?1 and S.PRODUCT_ID is NULL and S.DEFECTRESULTID is NULL "
														+ "ORDER BY S.REPAIR_AREA_SPACE";


	private static final String	UPDATE_NAQ_PARKING = "UPDATE QI_REPAIR_AREA_SPACE_TBX SET DEFECTRESULTID = ?1 , PRODUCT_ID = ?2 "
													+ "WHERE REPAIR_AREA_NAME = ?3 and REPAIR_AREA_SPACE = ?4 and REPAIR_AREA_ROW = ?5";
	
	private static final String	CLEAR_NAQ_PARKING = "UPDATE QI_REPAIR_AREA_SPACE_TBX SET PRODUCT_ID = NULL, DEFECTRESULTID = NULL "
			+ "WHERE PRODUCT_ID = ?1";
	
	public QiRepairAreaSpace findByProductId(String productId) {
		return findFirst(Parameters.with("productId", productId));
	}

	public List<Integer> findAllAvailableByRepairAreaAndRow(String repairArea, Integer repairAreaRow) {

		Parameters params = Parameters.with("1", repairArea).put("2", repairAreaRow);
		return findAllByNativeQuery(FIND_ALL_AVAILABLE_BY_REPAIR_AREA_AND_ROW, params, Integer.class);

	}

	@Transactional
	public void clearRepairAreaSpace(QiRepairAreaSpaceId id, String updateUser) {
		update(Parameters.with("productId", null).put("defectResultId", null).put("updateUser", updateUser), 
		       Parameters.with("id.repairAreaName", id.getRepairAreaName()).put("id.repairArearRow", id.getRepairArearRow()).put("id.repairArearSpace", id.getRepairArearSpace()));		
	}

	public List<QiRepairAreaSapceAssignmentDto> findAllSpaceAssignmentByRepairAreaNameAndRow(
			QiRepairAreaRowId areaRowId) {
		Parameters params = Parameters.with("1", areaRowId.getRepairAreaName()).put("2", areaRowId.getRepairAreaRow());
		return findAllByNativeQuery(FIND_SPACE_ASSIGNMENT, params, QiRepairAreaSapceAssignmentDto.class);
	}

	public List<QiRepairAreaSpace> findAllByRepairAreaNameAndRow(String repairAreaName, int repairAreaRow) {
		return findAll(Parameters.with("id.repairAreaName",repairAreaName).put("id.repairArearRow", repairAreaRow));
	}

	public boolean isRepairAreaUsed(String repairAreaName) {
		QiRepairAreaSpace qiRepairAreaSpace = findFirst(Parameters.with("id.repairAreaName", repairAreaName)); 
		if (qiRepairAreaSpace != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional
	public void updateAllByRepairArea(String repairMethodName, String oldRepairMethodName, String updateUser) {
		Parameters params = Parameters.with("1", repairMethodName)
				.put("2", updateUser)
				.put("3", oldRepairMethodName);			
		executeNativeUpdate(UPDATE_REPAIR_AREA_SPACE_BY_REPAIR_AREA, params);
	}

	public QiRepairAreaSpace findFirstAvailableByRow(QiRepairAreaRow row) {
		Parameters params = Parameters.with("1",row.getId().getRepairAreaName()).put("2", row.getId().getRepairAreaRow());
		StringBuilder queryString = new StringBuilder(FIND_FIRST_AVAILABLE_BY_ROW);
		if(row.getSpaceFillSequence() == 'D') {
			queryString.append(" ORDER BY S.REPAIR_AREA_SPACE DESC ");
		}
		
		else {
			queryString.append(" ORDER BY S.REPAIR_AREA_SPACE  ");
		}
		
		return findFirstByNativeQuery(queryString.toString(), params, QiRepairAreaSpace.class);
	}

	@Transactional
	public void assignRepairAreaSpaceWithTarget(String productId, long defectResultId, String updateUser, 
			String targetRespDept, String targetRepairArea, QiRepairAreaSpaceId qiRepairAreaSpaceId) {
		update(Parameters.with("productId", productId).put("defectResultId", defectResultId)
				.put("updateUser", updateUser).put("targetRespDept", targetRespDept)
				.put("targetRepairArea", targetRepairArea)
				.put("actualTimestamp", new Timestamp(System.currentTimeMillis())),
				Parameters.with("id.repairAreaName", qiRepairAreaSpaceId.getRepairAreaName())
						.put("id.repairArearRow", qiRepairAreaSpaceId.getRepairArearRow())
						.put("id.repairArearSpace", qiRepairAreaSpaceId.getRepairArearSpace()));
	}
	
	public List<String> getProductsByRepairArea(String repairAreaName){
		return findAllByNativeQuery(FIND_PRODUCTS_BY_REPAIR_AREA, Parameters.with("1", repairAreaName), String.class);
	}

	public QiRepairAreaSpace getNaqParkingSpace(String repairAreaName) {
		Parameters params = Parameters.with("1",repairAreaName);
		StringBuilder queryString = new StringBuilder(GET_NAQ_PARKING_SPACE);
				
		return findFirstByNativeQuery(queryString.toString(), params, QiRepairAreaSpace.class);
	}
	
	@Transactional
	public void updateNAQParking(long defectResultId, String productId, String repairAreaName, int repairAreaSpace,
			int repairAreaRow, String string) {
		Parameters params = Parameters.with("1", defectResultId)
				.put("2", productId)
				.put("3", repairAreaName)
				.put("4",repairAreaSpace)
				.put("5",repairAreaRow);			
		executeNativeUpdate(UPDATE_NAQ_PARKING, params);
	}

	@Transactional
	public void clearNAQParking(String productId) {
		Parameters params = Parameters.with("1", productId);
		executeNativeUpdate(CLEAR_NAQ_PARKING, params);
	}
}
