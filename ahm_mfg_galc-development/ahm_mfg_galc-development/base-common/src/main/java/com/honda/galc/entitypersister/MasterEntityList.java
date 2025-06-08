package com.honda.galc.entitypersister;

import java.io.Serializable;
import java.util.Stack;


@SuppressWarnings("hiding")
public class MasterEntityList<EntityList> extends Stack<EntityList> implements Serializable{

	private static final long serialVersionUID = -1354742954623120500L;

	public MasterEntityList() {}
	

	public boolean triggerEntityPersistance(){
		return (this.size()==0)? false : true;
	}

}
