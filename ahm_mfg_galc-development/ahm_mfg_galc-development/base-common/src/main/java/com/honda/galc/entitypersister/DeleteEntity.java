package com.honda.galc.entitypersister;

import java.io.Serializable;

import com.honda.galc.service.IDaoService;

public class DeleteEntity extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 8083339777563673277L;
	
	public DeleteEntity(Object obj, String entityString,IDaoService dao) {
		this.setEntity(obj);
		this.setEntityString(entityString);
		this.setDao(dao);
	}	
	
	@Override
	public void process() {
		this.getDao().remove(this.getEntity());
		
	}	
}
