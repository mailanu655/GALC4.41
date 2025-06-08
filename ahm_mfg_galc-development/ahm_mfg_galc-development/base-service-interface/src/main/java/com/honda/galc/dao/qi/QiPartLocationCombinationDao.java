package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.service.IDaoService;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QIPartLocCombMaintDAO</code> is a DAO interface to implement database interaction for Part Location Combination.
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
 * <TD>15/06/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */

public interface QiPartLocationCombinationDao extends IDaoService<QiPartLocationCombination, Integer>{

	public List<QiPartLocationCombination> findFilteredPartLocComb(String partLocCombFilter, String productKind, List<Short> statusList);
	public void updatePartLocCombStatus(Integer partLocationId, short active, String updateUser);
	public List<QiPartLocationCombination> checkPartInPartLocCombination(String partName, String productKind);
	public List<QiPartLocationCombination> checkLocationInPartLocCombination(String locationName, String productKind);
	public List<QiPartLocationCombinationDto> findFullPartNameByFilter(String filterData, short active, String productKind);
	public List<QiPartLocationCombination> findActivePartLocCombByFilter(String partLocCombFilter, String productKind);
	public boolean checkPartLocComb(String part1, String part1Loc1, String part1Loc2, String part2, String part2Loc1, String part2Loc2, String part3, String productKind, Integer partLocationId);
	public List<String> findAllPart1ByProductKind(String productKind);
	public List<QiPartLocationCombination> findAllByImageSectionId(int imageSectionId);
	public List<String> findAllPart1ByProcessPoint(String processPoint, String productKind, String mtcModel, String entryDept);
	public List<String> findAllPart1AndPart2(String processPoint, String productKind, String mtcModel, String entryDept);
	List<String> findAllByAllPartLocation(String processPointId, String productKind, String mtcModel, String entryDept);
	List<QiPartLocationCombination> findAllByPartLocationMatch(String productKind, String partLoc);
	List<QiPartLocationCombinationDto> findUnassignedFullPartName(short active, String productKind);
	List<QiPartLocationCombinationDto> findFullPartNameByWhichFilter(String filterData, short active, int which, String productKind);
}
