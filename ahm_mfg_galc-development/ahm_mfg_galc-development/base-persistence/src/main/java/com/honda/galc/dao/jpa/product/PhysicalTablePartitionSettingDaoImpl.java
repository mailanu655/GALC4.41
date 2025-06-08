package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PhysicalTablePartitionSettingDao;
import com.honda.galc.entity.product.PhysicalTablePartitionSetting;
import com.honda.galc.entity.product.PhysicalTablePartitionSettingId;
import com.honda.galc.service.Parameters;

public class PhysicalTablePartitionSettingDaoImpl
		extends
		BaseDaoImpl<PhysicalTablePartitionSetting, PhysicalTablePartitionSettingId>
		implements PhysicalTablePartitionSettingDao {

	
	String TABLE_NAME = "tableName";
	String GET_ALL_PHYSICAL_TABLE = "SELECT ptps FROM PhysicalTablePartitionSetting AS ptps WHERE ptps.id.tableName = :tableName ORDER BY ptps.id.physicalTableOffset";
	
	public List<String> getPhysicalTableNames(String tableName) {
		List<String> tableNames = new ArrayList<String>();
		Parameters params = Parameters.with(TABLE_NAME, tableName);
		List<PhysicalTablePartitionSetting> tableNameLst = findAllByQuery(GET_ALL_PHYSICAL_TABLE, params);
		for (PhysicalTablePartitionSetting tabelname : tableNameLst){
			tableNames.add(tabelname.getPhysicalTableName());
		}
		return tableNames;
	}
}
