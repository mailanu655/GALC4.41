package com.honda.galc.entitypersister;

import java.io.Serializable;

import com.honda.galc.service.IDaoService;




public class UpdateEntity extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1363500409276575752L;
	
	public UpdateEntity(Object obj, String entityString,IDaoService dao) {
		this.setEntity(obj);
		this.setEntityString(entityString);
		this.setDao(dao);
		
	}
	
	@Override
	public void process() {
		this.getDao().update(this.getEntity());
		
	}	
}
