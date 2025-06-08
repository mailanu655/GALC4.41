package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.service.IDaoService;

public interface QiEntryModelDao extends IDaoService<QiEntryModel, QiEntryModelId>{
	
	public List<QiEntryModel> findAllByProductType(String selectedProductType);
	
	public List<String> findEntryModelsByProductType(String productType);
	/**
	 * This method is used to fetch all the Entry Models based on status and product type
	 * @param producttype
	 * @param status
	 * @return
	 */
	public List<QiEntryModel> findAllEntryModelByStatus(String status, String produtType);
	/**
	 * This method is used to update Entry Model Name
	 * @param newEntryModelName
	 * @param upadteUser
	 * @param oldEntryModelName
	 */
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName, short isUsed);
	
	/**
	 * Find all by plant and product type.
	 *
	 * @param plant the plant
	 * @param selectedProductType the selected product type
	 * @return the list
	 */
	public List<String> findAllByPlantAndProductType(String plant, String selectedProductType);
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion, String userId);
	
	public List<QiEntryModel> findAllByName(String modelName);
	public int findEntryModelCount(String entryModel);
	public List<QiEntryModel> findAllMtcAssignedModelByProductType(String selectedProductType);
}
