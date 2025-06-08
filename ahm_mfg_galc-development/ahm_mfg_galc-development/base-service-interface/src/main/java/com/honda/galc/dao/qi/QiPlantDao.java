package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Interface description</h3> 
 * <p>
 * <code>QiPlantDao</code> is a DAO interface to implement database interaction for Plant.
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
 * <TD>15/11/2016</TD>
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


public interface QiPlantDao extends IDaoService<QiPlant, QiPlantId> {

	public List<String> findAllPlantList();

	public List<String> findAllPlantBySite(String Site);

	public void updatePlant(QiPlant newPlant, String oldPlantName);

	public void inactivatePlant(QiPlantId id);

	public void updateSiteById(String updateUser, String siteName, QiPlantId id);

	public List<QiPlant> findAllBySite(String siteName);

	public List<Object[]> findPlantCodeProductKind();

	public List<String> findAllActivePlantsBySite(String site);

	public List<QiPlant> findAllActiveBySiteList(List<String> siteList);

	public QiPlant findByPddaPlantCode(String plantLocationCode);

	public List<String> findAllAssignedPlants();

	QiPlant findByPddaPlantCodeAndProdLineNo(String pddaPlantCode, String lineNo);

}
