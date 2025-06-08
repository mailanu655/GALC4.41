package com.honda.galc.entity.conf;

import javax.persistence.MappedSuperclass;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Fredrick Yessaian
 * @Date Apr 01,2016
 */

@MappedSuperclass()
public abstract class BaseMCProductStructure extends AuditEntry{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract String getMode();
	
	public abstract String getProductSpecCode();
	
	public abstract long getStructureRevision();

}
