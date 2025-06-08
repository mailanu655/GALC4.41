package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationEntryScreenId;
import com.honda.galc.service.IDaoService;
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
public interface QiStationEntryScreenDao extends IDaoService<QiStationEntryScreen, QiStationEntryScreenId>{
	/**
	 * This method is used to find EntryScreen by process point
	 */
	public List<QiEntryScreenDto> findAllEntryScreenInfoByProcessPoint(String entryModel,String entryDept,String qicsStation);
	/**
	 * This method is used to find QiStationEntryScreen by EntryScreen
	 */
	public List<QiStationEntryScreen> findAllByEntryScreen(String entryModel, String entryScreen);
	
	/**
	 * This method to find QiStationEntryScreen by Id and EntryScreen
 	 * 
	 */
	public QiStationEntryScreen findStationEntryScreenByIdAndEntryScreen(QiStationEntryScreen qiStationEntryScreen);
	
	public void updateEntryScreenAndModel(QiEntryScreenId newEntryScreenId, String oldEntryScreen, String oldEntryModel, String userId);
	
	public List<QiDefectEntryDto> findAllEntryScreenByProcessPoint(String filterValue, String processPoint, String mtcModel, String entryDept, boolean realProblemScreen);
	
	public List<String> findAllProcessPointByEntryScreen(String entryScreen);
	
	public List<QiStationEntryScreen> findAllByEntryScreenModelAndDept(String entryScreenName,String entryModel,String entryDept,String processPointId);
	
	/**
	 * This method is used to find QiStationEntryScreen by EntryModel
	 */
	public List<QiStationEntryScreen> findAllByEntryModel(String entryModel);
	
	public List<QiStationEntryScreen> findAllByProcessPointAndEntryModel(String processPointId, String entryModel);

	public void removeByProcesspointModelAndDivision(String qicsStation,String entryModel,String entryDept);
	
	public void updateEntryModelName(String newEntryModel, String oldEntryModel, String userId);
	
	public List<QiStationEntryScreen> findAllEntryScreenByProcessPointAndDivision(String processPoint, String division);

	public long countEntryScreenByProcessPointAndDivision(String processPoint, String division);
	public int deleteByProcessPoint(String processPoint);
	List<QiDefectEntryDto> findAllEntryScreenByProcessPointAndPartLocation(String filterValue, String processPoint, String mtcModel, String entryDept,
			boolean realProblemScreen);
	
}