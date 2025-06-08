package com.honda.galc.service;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qics.DefectResult;

public interface HeadlessNaqService extends IService{
	
	public int saveDefectData(DefectMapDto data);
	public boolean saveDefectData(String processPointId, ProductType productType, List<? extends ProductBuildResult> buildResults);
	public DefectResult setDefectData(QiDefectResult qiDefectResult);
	public void updateLegacyDefectResultResponsibility(QiDefectResult qiDefectResult);
}




