package com.honda.galc.entitypersister;

import java.io.Serializable;
import java.util.ArrayList;


@SuppressWarnings("hiding")
public class EntityList<AbstractEntity> extends ArrayList<AbstractEntity> implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2226582686504642388L;

	private String operatedFor = null;
	private String productId = null;
	
	// variable declared for unique identification, user can store any thing for identification purpose.
	private String partId = null;
	
	public EntityList(String entitiesFor, String productId, String partSnNumber){
		this.setOperatedFor(entitiesFor);
		this.setProductId(productId);
		this.setPartId(partSnNumber);
	}
	
	public EntityList(String entitiesFor, String productId){
		this.setOperatedFor(entitiesFor);
		this.setProductId(productId);
	}
	
	public String getOperatedFor() {
		return operatedFor;
	}

	public void setOperatedFor(String operatedFor) {
		this.operatedFor = operatedFor;
	}

	public void addEntity(AbstractEntity entity) {
		super.add((AbstractEntity) entity);
	};

	public boolean isEntityListEmpty(){
		return (this.size() == 0)? true : false;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getPartId() {
		return partId;
	}


}
