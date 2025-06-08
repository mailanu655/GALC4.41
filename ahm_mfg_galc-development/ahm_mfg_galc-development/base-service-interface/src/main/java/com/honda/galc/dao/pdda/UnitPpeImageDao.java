package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.dto.UnitPpeImageDto;
import com.honda.galc.entity.pdda.UnitPpeImage;
import com.honda.galc.entity.pdda.UnitPpeImageId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitPpeImageDao extends IDaoService<UnitPpeImage, UnitPpeImageId> {
	
	public List<UnitPpeImageDto> findAllUnitPpeImgForProcessPoint(String processPoint);

}
