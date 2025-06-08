package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.service.IDaoService;

public interface QiRepairAreaDao extends IDaoService<QiRepairArea, String> {
	public List<QiRepairArea> findAllBySiteAndPlant(String siteName, String plantName);
	public void updateRepairArea(QiRepairArea repairArea, String oldRepairAreaName);	
	public List<String> findAllBySitePlantLocation(String siteName, String plantName, char location);
	public QiRepairArea findBySiteAndPlant(String site, String plant);
	public QiRepairArea isIntransitRepairAreaExist();
	public List<QiRepairArea> findAllRepairArea();
	public List<String> findAllPlantsBySiteAndLoc(String siteName, char location);
	public List<QiRepairArea> findAllBySite(String siteName);
	public List<QiRepairArea> findRepairAreaBySitePlantDivisionProductId(String siteName, String plantName, String divisionName, String productId);
	public List<QiRepairArea> findRepairAreaBySitePlantDivisionLocation(String siteName, String plantName, String divisionName, char location);
	public List<QiRepairArea> findRepairAreaBySitePlantDivisionPartialName(String siteName, String plantName, String divisionName, String repairAreaName);
	public List<QiRepairArea> findAllAvailRepairAreaBySiteLocation(String siteName, char location);
}
