package com.honda.galc.dao.jpa.qi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.dto.qi.QiInspectionPartDto;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QiBomQicsPartMappingDaoImpl Class description</h3>
 * <p>
 * QiBomQicsPartMappingDaoImpl is used to define the functionality of all the methods required for the operation on Bom Part Screen
 * </p>
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
 * @author LnTInfotech<br>
 *        MAY 06, 2016
 * 
 */
public class QiBomQicsPartMappingDaoImpl extends BaseDaoImpl<QiBomQicsPartMapping, String> implements QiBomQicsPartMappingDao {
	private static String FIND_NEW_BOM_PART_MAPPING_LIST = "SELECT distinct bom.main_part_no,bom.dc_part_name,mapping.inspection_part_name " +
			"FROM " +
			"galadm.QI_BOM_Part_TBX bom JOIN GALADM.QI_BOM_QICS_PART_MAPPING_TBX mapping ON bom.main_part_no=mapping.main_part_no ";

	private final static String FIND_PARTS_BY_PART_NAME= 	"SELECT mapping.* FROM galadm.QI_BOM_Part_TBX bom JOIN GALADM.QI_BOM_QICS_PART_MAPPING_TBX " 
			+" mapping ON bom.main_part_no=mapping.main_part_no where mapping.INSPECTION_PART_NAME=?1 ";
	
	private final static String UPDATE_PART = "update QI_BOM_QICS_PART_MAPPING_TBX  set INSPECTION_PART_NAME = ?1 , " +
			"UPDATE_USER = ?2 where INSPECTION_PART_NAME= ?3";
	
	private final static String FIND_MAIN_PART_NO_BY_INSPECTION_PART_NAME = "SELECT MAIN_PART_NO FROM galadm.QI_BOM_QICS_PART_MAPPING_TBX WHERE INSPECTION_PART_NAME=?1";
	
	private static final String FIND_INSPECTION_PART_FOR_PROCESS_POINT = "select distinct BQPM.INSPECTION_PART_NAME,BQPM.MAIN_PART_NO  from galadm.QI_STATION_UPC_PART_TBX SUPT, " +
			"galadm.QI_BOM_QICS_PART_MAPPING_TBX BQPM where SUPT.MAIN_PART_NO = BQPM.MAIN_PART_NO and SUPT.PROCESS_POINT_ID = ?1 ORDER BY BQPM.INSPECTION_PART_NAME";
	
	private final static String FIND_AVAILABLE_UPC_PARTS= "SELECT distinct mapping.* FROM GALADM.MBPN_TBX mbpn join GALADM.QI_BOM_QICS_PART_MAPPING_TBX mapping on mbpn.MAIN_NO = mapping.MAIN_PART_NO order by mapping.MAIN_PART_NO ";
	/**
	 * 	Used to get the latest list of BomPart and Inspection Part Association
	 *  @param associatedBomPartList - List of associated bom part list
	 *  @return latest BomPart and Inspection Part Association
	 */
	public List<QiBomPartDto> findNewBomPartMappingList(List<QiBomPartDto> associatedBomPartList){
		QiBomPartDto qiBomPartDto;
		List<QiBomPartDto> qiBomPartDtoList =new ArrayList<QiBomPartDto>();
		Parameters params = new Parameters();
	
		for (QiBomPartDto qiBomPartDtoObj : associatedBomPartList) {
			if(null!=qiBomPartDtoObj){
				StringBuilder findNewBomPartMapping = new StringBuilder(FIND_NEW_BOM_PART_MAPPING_LIST);
				if(!(StringUtils.isEmpty(qiBomPartDtoObj.getMainPartNo())) && StringUtils.isEmpty(qiBomPartDtoObj.getInspectionPart())){
					params = Parameters.with("1", qiBomPartDtoObj.getMainPartNo());
					findNewBomPartMapping.append(" WHERE mapping.main_part_no = ?1");

				}else if(StringUtils.isEmpty(qiBomPartDtoObj.getMainPartNo()) && !(StringUtils.isEmpty(qiBomPartDtoObj.getInspectionPart()))){
					params = Parameters.with("1", qiBomPartDtoObj.getInspectionPart());
					findNewBomPartMapping.append(" WHERE mapping.INSPECTION_PART_NAME = ?1");

				}else if(!(StringUtils.isEmpty(qiBomPartDtoObj.getMainPartNo()))  && !StringUtils.isEmpty(qiBomPartDtoObj.getInspectionPart())){
					params = Parameters.with("1", qiBomPartDtoObj.getMainPartNo()).put("2", qiBomPartDtoObj.getInspectionPart());
					findNewBomPartMapping.append(" WHERE mapping.main_part_no = ?1 AND mapping.INSPECTION_PART_NAME = ?2");
				}
				
				findNewBomPartMapping.append(" ORDER BY bom.main_part_no");
				List<Object[]> bomPartMappingList = findResultListByNativeQuery(findNewBomPartMapping.toString(), params);
				for(Object[] object : bomPartMappingList){
					qiBomPartDto = new QiBomPartDto();
					qiBomPartDto.setMainPartNo(object[0].toString());
					qiBomPartDto.setDieCastPartName(object[1].toString());
					qiBomPartDto.setInspectionPart(object[2].toString());
					qiBomPartDtoList.add(qiBomPartDto);
				}
			}
			
		}
		return qiBomPartDtoList;
	}
	/**
	 * This method is used to find the list of BomQicsPart based on PartName
	 */
	public List<QiBomQicsPartMapping> findAllByPartName(String partName){
		Parameters params = Parameters.with("1", partName);
			return findAllByNativeQuery(FIND_PARTS_BY_PART_NAME,params);
	}
	/**
	 * This method is used to update Part in BombQicsPartMapping table while updating part on Inspection Part Screen 
	 */
	@Transactional
	public void updatePartInBomQicsPartMapping(QiInspectionPart inspectionPart, String oldPartName,String userName){
		Parameters params = Parameters.with("1", inspectionPart.getInspectionPartName())
				.put("2", userName).put("3",inspectionPart.getInspectionPartDescShort()).put("3",oldPartName);
		executeNativeUpdate(UPDATE_PART, params);
	}
	
	/**
	 * This method is used to find All BomQics Parts sorted by mainPartNo 
	 */
	public List<QiBomQicsPartMapping> findAllSortedByBomQicsParts() {
		return findAllByNativeQuery(FIND_AVAILABLE_UPC_PARTS, null);
	}
	/**
	 * This method is used to find mainPartNo by Part1
	 */
	public List<String> findAllMainPartNoByInspectionPartName(String inspectionPartName) {
		Parameters params = Parameters.with("1", inspectionPartName);
		return findAllByNativeQuery(FIND_MAIN_PART_NO_BY_INSPECTION_PART_NAME,params,String.class);
	}
	

	/**
	 * This method is used to find UPCPart by Processpoint
	 */
	@SuppressWarnings("unchecked")
	public List<QiInspectionPartDto> findAllByProcessPointId(String processPointId) {
		return findAllByNativeQuery(FIND_INSPECTION_PART_FOR_PROCESS_POINT,Parameters.with("1", processPointId),QiInspectionPartDto.class);
	}

	/*
	 * this method is used to fetch a count of QiPart mappings by part no.
	 */
	public long countByMainPartNo(String mainPartNo) {
		return count(Parameters.with("mainPartNo",mainPartNo));
	}


}
