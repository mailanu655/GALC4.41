package com.honda.galc.entity;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>EntityCache Class description</h3>
 * <p> EntityCache description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 24, 2010
 *
 *
 */
	
public class EntityCache<E extends IEntity, K>{
	
	List<E> entityList = new ArrayList<E>();
	protected Class<E> entityClass;
	
	protected IDaoService<E,K> dao;
	
	public EntityCache(IDaoService<E,K> dao) {
		this.dao = dao;
	}
	
	public EntityCache(List<E> entityList,IDaoService<E,K> dao) {
		this(entityList);
		this.dao = dao;
	}
	
	public EntityCache(List<E> entityList) {
		if(entityList != null)
			this.entityList = entityList;
	}
	
	public void addAll(List<E> entities) {
		entityList.addAll(entities);
	}
	
	public boolean contains(K id) {
		
		for(E e : entityList) {
			if(e.getId().equals(id)) return true;
		}
		
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean contains(E e) {
		return contains((K) e.getId());
	}
	
	public E get(K id) {
		for(E e : entityList) {
			if(e.getId().equals(id)) return e;
		}
		return null;
	}
	
	public E findByKey(K id) {
		
		for(E e : entityList) {
			if(e.getId().equals(id)) return e;
		}
		
		if(dao == null) return null;
		
		E e = dao.findByKey(id);
		if(e != null) entityList.add(e);
		return e;
	}
	
	public List<E> getEntityList(){
		return entityList;
	}
	
	
	
}
