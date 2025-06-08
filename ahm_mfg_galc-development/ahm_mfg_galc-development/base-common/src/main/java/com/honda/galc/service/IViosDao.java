package com.honda.galc.service;

import java.util.List;

/**
 * 
 * 
 * @version 0.2
 * @author Shambhu Singh
 * @since May 09, 2019
 */
public interface IViosDao<E> {

	/**
	 * find all the entities with id. This is used for querying large result set
	 * 
	 * @param id
	 * @return
	 */
	public List<E> findAllData(String id);
}
