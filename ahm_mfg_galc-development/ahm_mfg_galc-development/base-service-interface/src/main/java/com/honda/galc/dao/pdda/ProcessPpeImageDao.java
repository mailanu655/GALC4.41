package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.dto.ProcessPpeImageDto;
import com.honda.galc.entity.pdda.ProcessPpeImage;
import com.honda.galc.entity.pdda.ProcessPpeImageId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface ProcessPpeImageDao extends IDaoService<ProcessPpeImage, ProcessPpeImageId> {
	
	public List<ProcessPpeImageDto> findAllPpeImageForProcessPoint(String processPoint);

}
