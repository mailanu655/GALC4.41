package com.honda.galc.entitypersister;

import java.io.Serializable;

import com.honda.galc.service.IDaoService;

public class InsertEntity extends AbstractEntity implements Serializable{
	
	
	private static final long serialVersionUID = -4550736308600152730L;
	
	
	public InsertEntity(Object obj, String entityString,IDaoService dao) {
		this.setEntity(obj);
		this.setEntityString(entityString);
		this.setDao(dao);
	}

	@Override
	public void process() {
		this.getDao().save(this.getEntity());
		
	}	
	
	

}
