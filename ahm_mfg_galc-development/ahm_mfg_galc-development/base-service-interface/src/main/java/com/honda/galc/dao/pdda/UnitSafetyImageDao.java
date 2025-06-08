package com.honda.galc.dao.pdda;

import java.sql.SQLException;
import java.util.List;

import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.entity.pdda.UnitSafetyImage;
import com.honda.galc.entity.pdda.UnitSafetyImageId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitSafetyImageDao extends IDaoService<UnitSafetyImage, UnitSafetyImageId> {

	public List<UnitSafetyImage> findAllSafetyImages(int maintenanceId, String[] orderBy);
	public List<UnitSafetyImage> findAllSafetyImages(int maintenanceId);
	public List<UnitOfOperation> getUnitSafetyImageList(String productId ,String processPoint, String mode) throws SQLException;
}
