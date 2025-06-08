package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.dto.lcvinbom.VinBomPartDto;
import com.honda.galc.entity.lcvinbom.VinBomPart;
import com.honda.galc.entity.lcvinbom.VinBomPartId;
import com.honda.galc.service.IDaoService;

public interface VinBomPartDao extends IDaoService<VinBomPart, VinBomPartId> {
	List<VinBomPartDto> findAllByFilter(VinBomPartDto vinBomPartDtoFilter);
	List<VinBomPartDto> findAllByFilterNative(VinBomPartDto vinBomPartDtoFilter);
	String getSystemNameByPartNumber(String dcPartNumber, String productSpecCode);
	List<VinBomPart> findDistinctPartNumberBySystemName(String letSystemName);
	List<String> findDistinctLetSystemName();
	public List<String> getSystemNamesByProductSpecPartNumber(String productSpec,String dcPartNumber,List<String> sysNameToExclude);
	List<String> getShipStatusByProductId(String productId);
	public List<String> getSystemNamesByProductSpec(String productSpec);
	public List<VinBomPart> findDistinctPartNumberBySystemNameAndProductSpec(String letSystemName, String productSpec);
}
