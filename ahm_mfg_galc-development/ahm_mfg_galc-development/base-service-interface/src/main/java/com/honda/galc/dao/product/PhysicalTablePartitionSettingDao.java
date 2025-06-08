package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.PhysicalTablePartitionSetting;
import com.honda.galc.entity.product.PhysicalTablePartitionSettingId;
import com.honda.galc.service.IDaoService;

public interface PhysicalTablePartitionSettingDao extends IDaoService<PhysicalTablePartitionSetting, PhysicalTablePartitionSettingId>{

	public List<String> getPhysicalTableNames (String tablename);
}
