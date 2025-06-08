package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ModelGroup;
import com.honda.galc.entity.product.ModelGroupId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>QiModelGroupDao Class description</h3>
 * <p>
 * QiModelGroupDao class used for Finding/updating the model groups based on product type and Status
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
 *         March 31, 2017
 * 
 */
public interface ModelGroupDao extends IDaoService<ModelGroup, ModelGroupId>{
	
	public List<ModelGroup> findAllByProductType(String selectedProductType);
	
	public List<ModelGroup> findAllByProductTypeAndSystem(String selectedProductType, String selectedSystem);
	
	public List<String> findAllQicsModelGroupsByProductType(String productType);
	
	public List<String> findAllSystems();
	
	/**
	 * This method is used to fetch all the Model Groups based on status and product type
	 * @param producttype
	 * @param status
	 * @return
	 */
	public List<ModelGroup> findAllByStatusAndProductType(String status, String produtType);
	
	/**
	 * This method is used to fetch all the Model Groups based on status, product type and system
	 * @param producttype
	 * @param status
	 * @return
	 */
	public List<ModelGroup> findAllByStatusProductTypeAndSystem(String status, String produtType, String system);
	
	/**
	 * This method is used to update Model Group Id
	 * @param newEntryModelName
	 * @param newEntryModelSystem
	 * @param userId
	 * @param oldEntryModelName
	 * @param oldEntryModelSystem
	 */
	public void updateModelGroupId(String newEntryModelName, String newEntryModelSystem, String userId, String oldEntryModelName, String oldEntryModelSystem);
	
	
}
