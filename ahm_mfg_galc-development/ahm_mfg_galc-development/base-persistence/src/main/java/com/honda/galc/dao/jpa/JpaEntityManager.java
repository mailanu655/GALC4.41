package com.honda.galc.dao.jpa;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>JpaEntityManager Class description</h3>
 * <p> JpaEntityManager description </p>
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
 * Jun 4, 2012
 *
 *
 */
  /*   
 * @author Gangadhararao Gadde
 * ver 2
 * 
 *
 *
 */
public class JpaEntityManager {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Transactional
	protected int executeNativeUpdate(String queryString){
		Query q = entityManager.createNativeQuery(queryString);
		int retValue = q.executeUpdate();
		getLogger().check("Executed update query : " + queryString);
		return retValue;
	}
	
	
	@Transactional
	protected int executeNativeUpdate(String queryString, Parameters params){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		int retValue =  q.executeUpdate();
		getLogger().check("Executed update query : " + queryString + " | Parameters : " + params.toString());
		return retValue;
	}
	
	/**
	 * excute update 
	 * @param sql
	 * @param params
	 * @return the number of rows updated
	 */
	@Transactional
	protected int executeUpdate(String sql, Parameters params) {
		
		Query q = entityManager.createQuery(sql);
        setParameters(q,params);
        int retValue = q.executeUpdate();
		getLogger().check("Executed update query : " + sql + " | Parameters : " + params.toString());
		return retValue;
	}


	
	protected void setIntIndexedParameters(Query q, Parameters params) {
		if(params == null || params.size() == 0) return;
		for(Map.Entry<String, Object> entry : params.getParameters().entrySet()) {
			q.setParameter(Integer.parseInt(getSimpleName(entry.getKey())), entry.getValue());
		}
	}
	

	@SuppressWarnings("unchecked")
    protected <T> List<T> findAllByNativeQuery(String queryString, Parameters params, Class<T> resultClass){
		
		Query q = entityManager.createNativeQuery(queryString, resultClass);
		setIntIndexedParameters(q, params);
		return q.getResultList();
		
	}

	   /**
     * run a Native query to get a single entity
     * @param queryString
     * @param params
     * @return
     */
	@SuppressWarnings("unchecked")
    protected <T> T findFirstByNativeQuery(String queryString, Parameters params, Class<T> resultClass) {
		Query q = entityManager.createNativeQuery(queryString, resultClass);
		setIntIndexedParameters(q, params);
		
		return (T) getSingleResult(q);
		
	}
	
    /**
     * run a JPQL query to get one column of the entity
     *
     * @param queryString
     * @param c           -- type of the column
     * @return value of the column
     */

	@SuppressWarnings("unchecked")
   protected <T> T findFirstByQuery(String queryString, Class<T> c) {
		
		Query q = entityManager.createQuery(queryString);
		return (T) getSingleResult(q);
		
	}
	
    /**
     * run a JPQL named query to get a list of entities
     *
     * @param queryString
     * @param paramters   - parameters for the statement
     * @param c           -- type of the column
     * @return value of the column
     */

	@SuppressWarnings("unchecked")
    protected <T> T findFirstByNamedQuery(String namedQuery, Class<T> c, Parameters params) {

		Query q = entityManager.createNamedQuery(namedQuery);
		setParameters(q, params);
		return (T) getSingleResult(q); 
		
	}
	
	

	protected void setParameters(Query q, Parameters params) {
		
		if(params == null || params.size() == 0) return;
		for(Map.Entry<String, Object> entry : params.getParameters().entrySet()) {
			q.setParameter(getSimpleName(entry.getKey()), entry.getValue());
		}
		
	}

	protected String getSimpleName(String name) {
		return name.startsWith("id.") ? name.substring(3) : name;
	}
	
	protected Object getSingleResult(Query q) {
		
		q.setMaxResults(1);
		Object obj = null;
        try{
        	obj = q.getSingleResult();
        }catch(NoResultException e) {
        }
        return obj;
        
	}

	@SuppressWarnings("rawtypes")
	protected List findResultListByNativeQuery(String queryString, Parameters params){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		return q.getResultList(); 
	}
	
	@SuppressWarnings("unchecked")
	protected  List findAllByQuery(String queryString) {

		Query q = entityManager.createQuery(queryString);
		return q.getResultList();
	}
	
	public Long getRowCountByQuery(String entityName, String whereStr) {
		String query ="select count("+ entityName.toLowerCase() +") from " + entityName + " as " + entityName.toLowerCase() + " "+ whereStr;
		Query q = entityManager.createQuery(query);
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	protected List findAllByQuery(String queryString, Parameters params, Integer count){
		Query q = entityManager.createQuery(queryString);
		if(params!=null)
			setParameters(q, params);
		if(count!=null)
			q.setMaxResults(count);
		return q.getResultList(); 
	}
	

}
