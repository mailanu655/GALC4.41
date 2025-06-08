package com.honda.galc.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.product.BaseOrderStructureDao;
import com.honda.galc.dao.product.BaseProductStructureDao;
import com.honda.galc.entity.conf.BaseMCOrderStructure;
import com.honda.galc.entity.conf.BaseMCProductStructure;

public enum StructureCreateMode {
	
	DIVISION_MODE(MCProductStructureDao.class, MCOrderStructureDao.class),  
	PROCESS_POINT_MODE(MCProductStructureForProcessPointDao.class, MCOrderStructureForProcessPointDao.class);

	private Class<? extends BaseProductStructureDao<? extends BaseMCProductStructure, ?>> productStructureDaoClass;
	private Class<? extends BaseOrderStructureDao<? extends BaseMCOrderStructure, ?>> orderStructureDaoClass;
	
	private StructureCreateMode(Class<? extends BaseProductStructureDao<? extends BaseMCProductStructure, ?>> productStructureDao, 
			Class<? extends BaseOrderStructureDao<? extends BaseMCOrderStructure, ?>> orderStructureDao){
		this.productStructureDaoClass = (Class<? extends BaseProductStructureDao<? extends BaseMCProductStructure, ?>>) productStructureDao;
		this.orderStructureDaoClass = (Class<? extends BaseOrderStructureDao<? extends BaseMCOrderStructure, ?>>) orderStructureDao;
	}
	
	public static StructureCreateMode get(String mode) throws IllegalArgumentException {
		if (mode == null) {
			throw new IllegalArgumentException("Not a valid Structure Create Mode");
		}
		return Enum.valueOf(StructureCreateMode.class, StringUtils.trimToEmpty(mode));
	}
	
	public Class<? extends BaseProductStructureDao<? extends BaseMCProductStructure, ?>> getProductStructureDaoClass() {
		return productStructureDaoClass;
	}
	
	public Class<? extends BaseOrderStructureDao<? extends BaseMCOrderStructure, ?>> getOrderStructureDaoClass() {
		return orderStructureDaoClass;
	}
}