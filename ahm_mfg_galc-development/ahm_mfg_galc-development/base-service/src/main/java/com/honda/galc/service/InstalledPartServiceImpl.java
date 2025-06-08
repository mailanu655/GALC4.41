package com.honda.galc.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.MCInstalledPartDetailDto;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.StringUtil;

/** * * 
* @version 0.2 
* @author Rakesh 
* @since June 29, 2016
*/
public class InstalledPartServiceImpl implements InstalledPartService {
	
	private static final String DEFAULT_VIOS = "DEFAULT_VIOS";
	private static final String PROPERTY_KEY_OP_TYPE = "MEASUREMENT_TYPE";

	@Autowired
	private InstalledPartDao installedPartDao;
	
	private static final String GET_OUTSTANDING_MCOPERATIONS = "SELECT DISTINCT A.PRODUCT_ID ,B.PROCESS_POINT_ID, PP.PROCESS_POINT_NAME,D.OP_DESC  "+
			" ,A.PRODUCT_SPEC_CODE    ,A.STRUCTURE_REV "+
			" ,A.DIVISION_ID    ,D.OPERATION_NAME    ,D.OP_REV	,D.OP_TYPE, "+
			"  C.OP_SEQ_NUM "+
			" FROM " + "@prodStructTable@ A " +
			" INNER JOIN GALADM.MC_STRUCTURE_TBX B ON A.PRODUCT_SPEC_CODE=B.PRODUCT_SPEC_CODE AND A.STRUCTURE_REV=B.STRUCTURE_REV AND A.DIVISION_ID=B.DIVISION_ID"+
			" INNER JOIN GALADM.MC_OP_REV_PLATFORM_TBX C ON (C.OPERATION_NAME=B.OPERATION_NAME) AND (C.OP_REV=B.OP_REV) AND (C.PDDA_PLATFORM_ID = B.PDDA_PLATFORM_ID)"+
			" INNER JOIN GALADM.MC_OP_REV_TBX D ON (D.OPERATION_NAME=C.OPERATION_NAME) AND (D.OP_REV=C.OP_REV)    "+    
			" LEFT OUTER JOIN GALADM.GAL185TBX E ON D.OPERATION_NAME=E.PART_NAME AND A.PRODUCT_ID=E.PRODUCT_ID  "+
			" LEFT OUTER JOIN GALADM.MC_OP_PART_REV_TBX F ON (F.OPERATION_NAME = B.OPERATION_NAME) AND (F.PART_ID = B.PART_ID) AND (F.PART_REV = B.PART_REV) "+
			" LEFT OUTER JOIN GALADM.MC_OP_MEAS_TBX G ON (G.OPERATION_NAME = B.OPERATION_NAME) AND (G.PART_ID = B.PART_ID) AND (G.PART_REV = B.PART_REV) " +
			" LEFT OUTER JOIN GALADM.GAL198TBX H ON (H.PART_NAME=G.OPERATION_NAME) AND (A.PRODUCT_ID = H.PRODUCT_ID) "+
			" AND (H.MEASUREMENT_SEQUENCE_NUMBER=G.OP_MEAS_SEQ_NUM)  "+
			" JOIN GAL214TBX PP ON PP.PROCESS_POINT_ID=B.PROCESS_POINT_ID AND PP.DIVISION_ID=B.DIVISION_ID "+
			" WHERE (A.PRODUCT_ID = ?1) "+ 
			" AND ((E.INSTALLED_PART_STATUS <> 1 OR E.INSTALLED_PART_STATUS IS NULL) "+
			" OR (H.MEASUREMENT_STATUS <> 1 OR "+
			" (H.MEASUREMENT_STATUS IS NULL AND G.OP_MEAS_SEQ_NUM IS NOT NULL AND D.OP_TYPE in (@opType)))) "+
			" AND (D.OP_CHECK = 1) ";

	
	@Override
	public List<MCInstalledPartDetailDto> getOutstandingMCOperations(
			String productId, String ppList, String filterType,
			String divisionList) {
		String sql = GET_OUTSTANDING_MCOPERATIONS.replace("@prodStructTable@", getProductStructureTable());
		String measurementTypeList = PropertyService.getProperty(DEFAULT_VIOS, PROPERTY_KEY_OP_TYPE, getDefaultMeasurementOperationType());
		measurementTypeList =StringUtil.toSqlInString(measurementTypeList);
		sql =  sql.replace("@opType",measurementTypeList);
		
		//Add single quotes to process point list		
		if ((!StringUtils.isBlank(ppList)) && (!StringUtils.isBlank(filterType))) {
			ppList =StringUtil.toSqlInString(ppList);
			if (filterType.toUpperCase().equals("EXCLUDE")) {
				sql += " AND (B.PROCESS_POINT_ID NOT IN (" + ppList + "))";
			}
			else if (filterType.toUpperCase().equals("INCLUDE")) { 
				sql += " AND (B.PROCESS_POINT_ID IN (" + ppList + "))";
			}
		}	
		//Add single quotes to division list if exists	
		if (!StringUtils.isBlank(divisionList)) {
			divisionList =StringUtil.toSqlInString(divisionList);
			sql += " AND (A.DIVISION_ID IN (" + divisionList + "))";
			
		}	
		sql += " ORDER BY B.PROCESS_POINT_ID,  C.OP_SEQ_NUM ASC ";	
		List<MCInstalledPartDetailDto> installedPartDetailList = installedPartDao.getInstalledPartDetails(sql, productId);
		
		return installedPartDetailList;	
	}

	public String getProductStructureTable() {
		String mode = StringUtils.trimToNull(PropertyService.getProperty(
				ApplicationConstants.DEFAULT_VIOS,
				ApplicationConstants.STRUCTURE_CREATE_MODE,
				StructureCreateMode.DIVISION_MODE.toString()));
		if (mode != null && StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())) {
			return CommonUtil.getTableName(MCProductStructureForProcessPoint.class);
		}
		return CommonUtil.getTableName(MCProductStructure.class);
	}
	
	private static String getDefaultMeasurementOperationType(){
		return OperationType.GALC_MEAS+","+OperationType.GALC_MEAS_MANUAL+","+OperationType.GALC_SCAN_WITH_MEAS+","+OperationType.GALC_SCAN_WITH_MEAS_MANUAL;
	}
}
