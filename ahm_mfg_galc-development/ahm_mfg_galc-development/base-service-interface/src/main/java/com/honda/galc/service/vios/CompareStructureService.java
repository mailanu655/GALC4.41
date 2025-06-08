package com.honda.galc.service.vios;

import java.util.List;

import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.service.IService;
import com.honda.galc.vios.dto.StructureCompareDto;




public interface CompareStructureService extends IService {

	public List<StructureCompareDto>  compareStructuresAtProdSpecLevel(List<MCStructure> newStructure, List<MCStructure> oldStructure);
	
}
