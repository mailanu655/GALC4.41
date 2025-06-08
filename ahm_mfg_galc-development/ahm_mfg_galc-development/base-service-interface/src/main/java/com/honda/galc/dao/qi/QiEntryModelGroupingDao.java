package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiEntryModelGroupingId;
import com.honda.galc.service.IDaoService;

public interface QiEntryModelGroupingDao extends IDaoService<QiEntryModelGrouping, QiEntryModelGroupingId> {
	/**
	 * This method is used to find all the assigned Mtc model to a particular Entry Model
	 * @param entryModel
	 * @return
	 */
	public List<QiEntryModelGrouping> findAllByEntryModel(String entryModel,short isUsed);
	/**
	 * This method is used to find all QiEntryModelGrouping data based on Mtc Model
	 * @return
	 */
	public QiEntryModelGrouping findByMtcModel(String mtcModel, String productType);
	/**
	 * This method is used to update Entry Model Name
	 * @param newEntryModelName
	 * @param upadteUser
	 * @param oldEntryModelName
	 */
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName,short isUsed);
	
	public List<String> findAllMtcModelByEntryModel(String entryModel, String productType);
	
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion);
	public void removeByEntryModelAndVersion(String entryModel, short version);
	
	/**
	 * This method is used to find all QiEntryModelGrouping data based on Mtc Model and version
	 * @return
	 */
	public QiEntryModelGrouping findByMtcModelAndVersion(String mtcModel, short version);
	
	/**
	 * This method is used to fetch all the MTC assigned based on product type
	 * @param producttype
	 * @return
	 */
	public List<QiEntryModelGrouping> findAllAssignedMtcModelByProductType(String productType);
	
}
