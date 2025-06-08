package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.entity.pdda.UnitImage;
import com.honda.galc.entity.pdda.UnitImageId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitImageDao extends IDaoService<UnitImage, UnitImageId> {
	public List<UnitImage> findAllUnitImages(int maintenanceId, String[] orderBy);
	public List<PddaUnitImage> findAllUnitImages(int maintenanceId);
	public List<PddaUnitImage> getUnitMainImage(String productId ,String processPoint);
	public List<PddaUnitImage> getUnitCCPImages(String productId ,String processPoint);
}
