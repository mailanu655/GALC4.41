package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.dto.qi.QiInspectionPartDto;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>QiBomQicsPartMappingDao Class description</h3>
 * <p>
 * QiBomQicsPartMappingDao is used to declare the method required for the operation on Bom Part Screen
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
public interface QiBomQicsPartMappingDao extends IDaoService<QiBomQicsPartMapping, String>  {
	public List<QiBomPartDto> findNewBomPartMappingList(List<QiBomPartDto> associatedBomPartList);
	/**
	 * This method is used to find the list of BomQicsPart based on PartName
	 * @param bomQicsPartMappingId
	 * @return
	 */
	public List<QiBomQicsPartMapping> findAllByPartName(String partName);
	/**
	 * This method is used to update Part in BombQicsPartMapping table while updating part on Inspection Part Screen 
	 * @param inspectionPart
	 * @param oldPartName
	 * @param userName
	 */
	public void updatePartInBomQicsPartMapping(QiInspectionPart inspectionPart, String oldPartName,String userName);
	
	/**
	 * This method is used to find All BomQics Parts sorted by mainPartNo
	 * @return QiBomQicsPartMapping
	 */
	public List<QiBomQicsPartMapping> findAllSortedByBomQicsParts();
	/**
	 * This method is used to find mainPartNo by Part1
	 * @param inspectionPartName
	 * @return
	 */
	public List<String> findAllMainPartNoByInspectionPartName(String inspectionPartName);
	
	/**
	 * This method is used to find all the UPC part data by Process Point Id
	 */
	List<QiInspectionPartDto> findAllByProcessPointId(String processPointId);
	long countByMainPartNo(String mainPartNo);
	
}
