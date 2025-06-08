package com.honda.galc.dao.jpa;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.util.AuditLogUtilRest;

/**
 * 
 * <h3>BaseAuditDaoImpl Class description</h3>
 * <p> BaseAuditDaoImpl description </p>
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
public abstract class BaseAuditDaoImpl<E,K> extends BaseDaoImpl<E,K> {
	
	protected abstract K getId(E entity);
	
	@Transactional
	public List<E> saveAll(List<E> list, String userId, String changeType) {
			// for mbpn maintenance the is no update - checked before save into database
		    // -- need to handle saveAll differently if not checked existing entity before save!
			List<E> saveAll = super.saveAll(list);
			for(E entity : list) {
				AuditLogUtilRest.logInsert(entity, userId, changeType);
			}
			
			return saveAll;
		
	}
	
	@Transactional
	public E insert(E entity, String userId, String changeType) {
		E insert = super.insert(entity);
		System.out.println("mbpndao:" + entity.toString() + " changeTyhpe:" + changeType);
		AuditLogUtilRest.logInsert(entity, userId, changeType);
		return insert;
	}
	
	@Transactional
	public void deleteByKey(K id, String userId, String changeType) {
		E exist = super.findByKey(id);
		super.removeByKey(id);
		AuditLogUtilRest.logDelete(exist, userId, changeType);
		
	}
	
	@Transactional
	public void delete(E entity, String userId, String changeType) {
		super.remove(entity);
		AuditLogUtilRest.logDelete(entity, userId, changeType);
		
	}


	@Transactional
	public E update(E entity, E exist, String userId, String changeType) {
		E saved = save(entity);
		AuditLogUtilRest.logUpdate(exist, saved, userId, changeType);
		return saved;
	}
	

	@Transactional
	public void deleteAll(List<E> list, String userId, String changeType) {
		super.removeAll(list);
		for(E entity: list) {
			AuditLogUtilRest.logDelete(entity, userId, changeType);
		}
	}
	

}
