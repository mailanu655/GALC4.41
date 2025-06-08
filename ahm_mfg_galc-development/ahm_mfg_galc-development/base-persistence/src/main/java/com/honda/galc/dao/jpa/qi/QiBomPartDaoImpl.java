package com.honda.galc.dao.jpa.qi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiBomPartDao;
import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.entity.qi.QiBomPart;
import com.honda.galc.entity.qi.QiBomPartId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QiBomPartDaoImpl Class description</h3>
 * <p>
 * QiBomPartDaoImpl is used to define the methods required for the operation on Bom Part table on screen
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
public class QiBomPartDaoImpl extends BaseDaoImpl<QiBomPart,QiBomPartId> implements QiBomPartDao{
	private static String  FIND_BOM_PART = "select distinct o.main_Part_No,o.dc_Part_Name,o.dc_Part_No from galadm.QI_BOM_Part_TBX o where o.product_kind = ?2 and (o.main_Part_No like ?1 or o.dc_Part_Name like ?1) order by o.main_Part_No";
	private static String  FIND_ASSOCIATED_BOM_PART = "SELECT distinct o.main_Part_No,o.dc_Part_Name,o.dc_Part_No from galadm.QI_BOM_Part_TBX o where o.product_kind = ?2 and o.main_Part_No IN (SELECT e.main_Part_No FROM GALADM.QI_BOM_QICS_PART_MAPPING_TBX e) and (o.main_Part_No like ?1 or o.dc_Part_Name like ?1) order by o.main_Part_No";
	private static String  FIND_NOT_ASSOCIATED_BOM_PART = "SELECT distinct o.main_Part_No,o.dc_Part_Name,o.dc_Part_No from galadm.QI_BOM_Part_TBX o where o.product_kind = ?2 and o.main_Part_No NOT IN (SELECT e.main_Part_No FROM GALADM.QI_BOM_QICS_PART_MAPPING_TBX e) and (o.main_Part_No like ?1 or o.dc_Part_Name like ?1 or o.dc_Part_No like ?1) order by o.main_Part_No";
	private static String MERGE_SQL = "MERGE INTO QI_BOM_PART_TBX AS A USING (VALUES @VALUE_LIST@) AS B (DC_PART_NO, MODEL_CODE, MAIN_PART_NO, DC_PART_NAME, PRODUCT_KIND) ON A.PRODUCT_KIND = B.PRODUCT_KIND and A.DC_PART_NO = B.DC_PART_NO and A.MODEL_CODE = B.MODEL_CODE WHEN MATCHED THEN UPDATE SET A.MAIN_PART_NO = B.MAIN_PART_NO, A.DC_PART_NAME = B.DC_PART_NAME WHEN NOT MATCHED THEN INSERT (DC_PART_NO, MODEL_CODE, MAIN_PART_NO, DC_PART_NAME, PRODUCT_KIND) VALUES (B.DC_PART_NO, B.MODEL_CODE, B.MAIN_PART_NO, B.DC_PART_NAME, B.PRODUCT_KIND)";
	private static String INSERT_SQL = "INSERT INTO QI_BOM_PART_TBX (DC_PART_NO, MODEL_CODE, MAIN_PART_NO, DC_PART_NAME, PRODUCT_KIND) VALUES @VALUE_LIST@";
	
	/**
	 * To Filter the table data on basis of main part no
	 * @param filterValue - User inputs in the filter
	 * @return the number of rows based on filtervalue
	 */
	@SuppressWarnings("unchecked")
	public List<QiBomPartDto> findBomPartsByFilter(String filterValue, String productKind) {
		List<QiBomPartDto> qiBomPartDtoList = new ArrayList<QiBomPartDto>();
		Parameters params = Parameters.with("1", "%" +filterValue+ "%");
		params.put("2", productKind);
		List<Object[]> bomPartList =  findResultListByNativeQuery(FIND_BOM_PART, params);
		setBomPartDtoList(qiBomPartDtoList, bomPartList);
		return qiBomPartDtoList;
	}

	/**
	 * To Filter the table data on basis of main part no for associated Bom Parts
	 * @param filterValue - User inputs in the filter
	 * @return the number of rows based on filtervalue
	 */
	@SuppressWarnings("unchecked")
	public List<QiBomPartDto> findAssociatedBomPartsByFilter(String filterValue, String productKind) {
		List<QiBomPartDto> qiBomPartDtoList = new ArrayList<QiBomPartDto>();
		Parameters params = Parameters.with("1", "%" +filterValue+ "%");
		params.put("2", productKind);
		List<Object[]> associatedBomPartList = findResultListByNativeQuery(FIND_ASSOCIATED_BOM_PART, params);
		setBomPartDtoList(qiBomPartDtoList, associatedBomPartList);
		return qiBomPartDtoList;
	}
	/**
	 * To Filter the table data on basis of main part no for not associated Bom Parts
	 * @param filterValue - User inputs in the filter
	 * @return the number of rows based on filtervalue
	 */
	public List<QiBomPartDto> findNotAssociatedBomPartsByFilter(String filterValue, String productKind) {
		List<QiBomPartDto> qiBomPartDtoList = new ArrayList<QiBomPartDto>();
		Parameters params = Parameters.with("1", "%" +filterValue+ "%");
		params.put("2", productKind);
		List<Object[]> notAssociatedBomPartList = findAllByNativeQuery(FIND_NOT_ASSOCIATED_BOM_PART, params, Object[].class);
		setBomPartDtoList(qiBomPartDtoList, notAssociatedBomPartList);
		return qiBomPartDtoList;
	}

	/**
	 * To set value in DTO
	 * @param qiBomPartDtoList
	 * @param bomPartList
	 */
	private void setBomPartDtoList(List<QiBomPartDto> qiBomPartDtoList,List<Object[]> bomPartList) {
		QiBomPartDto qiBomPartDtoObj;
		if(bomPartList!=null && !bomPartList.isEmpty()){
			for(Object[] qiBomPartObj : bomPartList){
				qiBomPartDtoObj = new QiBomPartDto();
				qiBomPartDtoObj.setMainPartNo(qiBomPartObj[0].toString());
				qiBomPartDtoObj.setDieCastPartName(qiBomPartObj[1].toString());
				qiBomPartDtoObj.setDieCastPartNo(qiBomPartObj[2].toString());
				qiBomPartDtoList.add(qiBomPartDtoObj);
			}
		}
	}
	
	@Transactional
	public void mergeBatch(String values) {
		executeNativeUpdate(MERGE_SQL.replace("@VALUE_LIST@", values), null);
	}
	
	@Transactional
	public void insertBatch(String values) {
		executeNativeUpdate(INSERT_SQL.replace("@VALUE_LIST@", values), null);
	}
}
