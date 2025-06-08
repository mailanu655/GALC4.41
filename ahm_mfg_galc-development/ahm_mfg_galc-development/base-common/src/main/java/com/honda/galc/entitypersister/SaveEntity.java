package com.honda.galc.entitypersister;

import java.io.Serializable;

import com.honda.galc.service.IDaoService;


public class SaveEntity extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = -2830205325901136729L;
	
	public SaveEntity(Object obj, String entityString,IDaoService dao) {
		this.setEntity(obj);
		this.setEntityString(entityString);
		this.setDao(dao);
	}
	
	@Override
	public void process() {
		this.getDao().update(this.getEntity());
		
	}	
}
