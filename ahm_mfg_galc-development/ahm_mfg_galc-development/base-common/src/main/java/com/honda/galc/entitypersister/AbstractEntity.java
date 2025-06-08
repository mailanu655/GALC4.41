package com.honda.galc.entitypersister;

import java.io.Serializable;

import com.honda.galc.service.IDaoService;

 
public abstract class AbstractEntity implements Serializable{

	private static final long serialVersionUID = -7531433321554145950L;
	private Object object = null;
	private String entityString = null;
	private IDaoService dao=null;
	
	public abstract void process();
	
	public Object getEntity() {
		return object;
	}

	public void setEntity(Object obj) {
		this.object = obj;
	}

	public void setEntityString(String entityString) {
		this.entityString = entityString;
	}

	public String getEntityString() {
		return entityString;
	}
	
	public IDaoService getDao() {
		return dao;
	}

	public void setDao(IDaoService dao) {
		this.dao = dao;
	}
}
