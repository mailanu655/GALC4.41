package com.honda.galc.service;

import java.util.List;

/**
 * 
 * <h3>IAuditDaoService Class description</h3>
 * <p> IAuditDaoService description </p>
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
 * @author Paul Chou<br>
 * Nov. 10, 2023
 *
 *
 */
public interface IAuditDaoService<E, K> {
	
	public E insert(E entity, String userId, String changeType);
	
	public E update(E entity, E exist, String userId, String changeType);
	
	public void deleteByKey (K id, String userId, String changeType);
	
	public void delete (E entity, String userId, String changeType);
	
	public List<E> saveAll(List<E> list, String userId, String changeType);
	
	public void deleteAll(List<E> list, String userId, String changeType);
	
}
