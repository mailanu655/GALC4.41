package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.dto.qi.QiDefectCombinationResultDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiPartDefectCombinationDto;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QIPartDefectCombinationDao Class description</h3>
 * <p>
 * QIPartDefectCombination Dao
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
 * @author L&T Infotech<br>
 *         Aug 26, 2016
 * 
 * 
 */
public interface QiPartDefectCombinationDao extends IDaoService<QiPartDefectCombination, Integer> {

	public void updatePartDefectCombStatus(Integer partDefectId, short active,
			String updateUser);

	public List<QiRegionalPartDefectLocationDto> getPartDefectDetails(String filter,
			String productkind);

	public List<QiRegionalPartDefectLocationDto> getAssignedDefectDetails(
			String filter,String productType, String entryScreen, String selectedTextEntryMenu, String entryModel, short isUsedVersion);

	public List<Integer> findPartLocationIdsInPartDefectCombination(
			int partLocationId, String productKind);

	public void inactivatePartinPartDefectCombination(int partLocationId, String productKind,String userId);

	public List<QiPartDefectCombination> findPartLocationIdsInPartDefectCombination(
			List<Integer> partLocIdList, String productKind);

	public boolean checkPartDefectCombination(QiPartDefectCombination comb);

	public List<QiPartDefectCombination> findDefectInPartDefectCombination(
			String defectName, String productKind);

	public List<QiPartDefectCombination> findPartLocCombInPartDefectCombination(
			int partLocId, String productKind);

	public List<QiRegionalPartDefectLocationDto> getAssignedDefectDetails(String filter,String productKind, String entryScreen, String entryModel, short isUsedVersion);
	public List<QiPartDefectCombination> findAllByIqsId(Integer qiIqs);

	public List<QiPartDefectCombination> findAllByThemeName(String themeName);
	
	public List<PdcRegionalAttributeMaintDto> findAllPdcLocalAttributes(String entryScreen, String entryModel, short assigned, String filterValue);
	
	public String fetchAuditPrimaryKeyValue(int regionalDefectCombinationId);

	public List<QiPartDefectCombination> findAllRegionalAttributesByPartDefectId(List<Integer> partDefectIdList);

	public List<QiPartDefectCombination> findAllRegionalAttributesByPartLocationId(List<Integer> partLocationIdList);

	public List<QiRegionalPartDefectLocationDto> getPartDefectDetailsByImage(
			String filter, String productkind, QiEntryScreenDto qiEntryScreenDto);

	public List<QiRegionalPartDefectLocationDto> getPartDefectDetailsByText(
			String filter, String productkind, QiEntryScreenDto qiEntryScreenDto);

	public QiDefectCombinationResultDto findById(int regionalDefectCombinationId);

	public List<QiPartDefectCombination> findAllPLCIdsByPartLocId(List<Integer> partLocationIdList,
			String productKind);
		
	List<QiRegionalAttributeDto> findRegionalAttributeByPartLocationIdList(List<Integer> partLocationIdList, short assigned, String productKind, String searchText, int whichFilter);

	List<QiPartDefectCombinationDto> findPartDefectByPartLocationIdList(List<Integer> partLocationIdList, short active, String productKind, String searchText, int which);
}
