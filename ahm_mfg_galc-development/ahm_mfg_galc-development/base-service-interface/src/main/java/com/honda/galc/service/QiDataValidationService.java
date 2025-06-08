package com.honda.galc.service;


import java.util.List;

import com.honda.galc.entity.conf.RegionalProcessPointGroup;

/**
 * 
 * <h3>QiDataValidationService Class description</h3>
 * <p>
 * QiDataValidationService is used to validate regional data.
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
 * 
 */
public interface QiDataValidationService extends IService{
	public String validateByDefectId(List<Integer> partDefectIdList);
	public String validateByLocationId(List<Integer> partLocationCombList);
	public String validateByRepairMethodName(List<String> repairMethodNameList);
	public String validateByImageName(List<String> imageNameList);
	public String validateByPlantName(List<String> plantNameList);
	public String validateBySiteName(List<String> siteNameList);
	public String validateByImageSection(List<String> imageSectionList);
	public String validateByProcessPointGroup(List<RegionalProcessPointGroup> processPointGroupList);
}




