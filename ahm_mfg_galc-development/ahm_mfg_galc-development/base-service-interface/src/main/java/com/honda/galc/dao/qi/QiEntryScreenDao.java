package com.honda.galc.dao.qi;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiEntryScreenDao</code> is a DAO interface to implement database interaction for Entry Screen.
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
import java.util.List;

import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.service.IDaoService;

public interface QiEntryScreenDao extends IDaoService<QiEntryScreen, QiEntryScreenId>{
	
	
	public List<QiEntryScreenDto> findEntryScreenData(String entryModel,String plant);
	public List<QiEntryScreenDto> findEntryScreenData(String entryModel,String plant, boolean isImage);
	public List<QiEntryScreenDto> getUsedEntryModelData(String entryModel,String plant);
	public List<QiEntryScreenDto> getNotUsedEntryModelData(String entryModel,String plant);
	
	/**
	 * This method is used to get the list of EntryScreens on the basis of entry model
	 * 
	 * @param entryModel
	 * @return
	 */
	public List<String> findEntryScreensByEntryModel( String entryModel);
	
	public void updateAssignImage(String qiEntryScreen, String imageName, String userId);
	
	public void deassignImage(String qiEntryScreen, String userId);
	
	public List<QiEntryScreenDto> findAllEntryScreensByEntryModel(String entryModel);
	
	public List<QiEntryScreenDto> findAllByFilter(String filter,  List<Short> statusList, String siteName);
	
	public void updateByEntryScreen(QiEntryScreen qiEntryScreen, String oldEntryScreen, String oldEntryModel, short oldVersion);
	
	public List<QiEntryScreen> findAllEntryScreenByEntryModel(String entryModel, short isUsed);
	
	public List<String> findAllByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel);
	/**
	 * This method is used to findAll entryScreen based on Model and Dept
	 */
	public List<QiEntryScreenDto> findAllByModelAndDept(String entryModel,String entryDepartment, short version);

	public Long findCountByImageName(String imageName);
	
	public void updateImageName(String newImageName, String updateUser, String oldImageName);

	public List<String> findAllByEntryScreenDefect(String imageName);

	public List<String> findAllByStationEntry(String imageName);

	public void updateEntryScreenForImageName(String imageName, String updateUser, String entryScreen, String entryModel, short version);
	
	public List<String> findAllByPlantProductTypeEntryModelAndEntryDept(String plant, String selectedProductType, String entryModel,String entryDept);

	/**
	 * This method is used to fetch EntryScreen based on Product Type
	 * @param productType
	 * @return
	 */
	public List<String> findAllByProductType(String productType);
	
	
	public List<QiEntryScreenDto> findAllByPlantAndProductType(String plant, String productType, boolean isImage);
	

	/**
	 * This method is used to get the EntryScreen and its details
	 * @param entryModel
	 * @param version
	 * @param screenName
	 */
	public QiEntryScreen findByEntryScreenNameAndIsImage(String entryScreen, String entryModel, short version);
	
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion);
	
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel, short isUsed);
	
	public List<QiEntryScreen> findAllByEntryScreenName(String screenName);
	
	public void removeByEntryModelAndVersion(String entryModel, short version);
	
	public List<String> findAllEntryScreensByPlantAndEntryModelAndPdc(String plant, String entryModel);
	
	public List<QiEntryScreen> findAllScreenUsedInStationByModel(String entryModel, short version);
	
	public void updateScreenIsUsed(String entryModel, String entryScreen, short screenIsUsed);
	
	public List<QiEntryScreen> findAllByEntryModelAndScreen(String entryModel, String entryScreen);
	long countByEntryScreenAndModel(String entryScreen, String entryModel);
	
	public List<QiEntryScreen> findAllEntryScreenByPlantAndEntryModel(String plant, String entryModel);
	long countEntryScreenByPlantAndEntryModel(String plant, String entryModel);
}
