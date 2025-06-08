package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ModelGrouping;
import com.honda.galc.entity.product.ModelGroupingId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ModelGroupingDao Class description</h3>
 * <p>
 * ModelGroupingDao class used for Finding/updating the model grouping based on model Group 
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
public interface ModelGroupingDao extends IDaoService<ModelGrouping, ModelGroupingId> {
	/**
	 * This method is used to find all the assigned Mtc model to a particular Model Group
	 * @param Model Group
	 * @return
	 */
	public List<ModelGrouping> findAllByModelGroup(String modelGroup);
	/**
	 * This method is used to find all the assigned Mtc model to a particular Model Group and System
	 * @param modelGroup
	 * @param system
	 * @return
	 */
	public List<ModelGrouping> findAllByModelGroupAndSystem(String modelGroup, String system);
	/**
	 * This method is used to find all the assigned Model Groupings to a particular mtc model, system and product type.
	 * @param mtcModel
	 * @param system
	 * @param productType
	 * @return
	 */
	public List<ModelGrouping> findAllByMtcModelSystemAndProductType(String mtcModel, String system, String productType);
	/**
	 * This method is used to find all QiModelGrouping data based on Mtc Model
	 * @return
	 */
	public ModelGrouping findModelGroupingByMtcModelAndSystem(String mtcModel, String system);
	/**
	 * This method is used to update Model Grouping Id
	 * @param newEntryModelName
	 * @param newEntryModelSystem
	 * @param userId
	 * @param oldEntryModelName
	 * @param oldEntryModelSystem
	 */
	public void updateModelGroupingId(String newEntryModelName, String newEntryModelSystem, String userId, String oldEntryModelName, String oldEntryModelSystem);
	/**
	 * This method is used to find all recent (previous year onward) Model Groups based on System
	 * @param system
	 * @return
	 */
	public List<String> findRecentModelGroupsBySystem(String system);

}
