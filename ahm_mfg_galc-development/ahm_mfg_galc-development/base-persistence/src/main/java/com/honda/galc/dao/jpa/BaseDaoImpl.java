package com.honda.galc.dao.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.validation.ValidationInfoMessage;
import com.honda.galc.common.validation.ValidationType;
import com.honda.galc.dto.DtoFactory;
import com.honda.galc.dto.IDto;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/** * * 
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012
 */
public abstract class BaseDaoImpl<E,K> implements IDaoService<E,K>{

	private static final String CURRENT_TIMESTAMP_QUERY = "select current_timestamp from sysibm.sysdummy1";
	
	private static final String SELECT_ALL_COLUMN_LENGTHS ="select name,length from SYSIBM.SYSCOLUMNS where tbname = ?1 and tbcreator = 'GALADM' ";
	
	private static final String NATIVE_PAGE_CLAUSE = "%s LIMIT %d OFFSET %d";
	
	private static final String LOG_NAME = "JPA Server";

	protected Class<E> entityClass;
	protected String selectClause;
	protected String deleteClause;
	protected String updateClause;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
		selectClause = prepareSelectClause();
		deleteClause = prepareDeleteClause();
		updateClause = prepareUpdateClause();
	}

	/**
	 * save - insert a new entity or update an existing entity
	 */
	@Transactional
	public E save(E entity) { 
		entity = entityManager.merge(entity);
		getLogger().check("Merged entity : " + entity.toString());
		return entity;
	}

	@Transactional
	public E insert(E entity) {
		entityManager.persist(entity);
		getLogger().check("Inserted entity : " + entity.toString());
		return entity;
	}

	@Transactional
	public void insertAll(List<E> entities) {
		for(E entity : entities)
			insert(entity);
	}

	@Transactional
	public E update(E entity) {
		return save(entity);
	}

	@Transactional
	public void updateAll(List<E> entities) {
		saveAll(entities);
	}

	@Transactional
	public void remove(E entity) {
		if (entity == null) return;
		E newEntity = entityManager.merge(entity);
		entityManager.remove(newEntity); 
		getLogger().check("Removed entity : " + entity.toString());
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public E findByKey(K id) { 
		return entityManager.find(entityClass, id); 
	}

	@Transactional
	public void removeByKey(K pk) {
		remove(findByKey(pk));
	}

	@Transactional
	public void removeAll() {
		delete(null);
	}

	@Transactional
	public void removeAll(List<E> entities) {
		for(E entity : entities) 
			remove(entity);
	}

	/**
	 * find the entities with parameters.
	 * equivalent JPQL statement - "select e from E e where e.p1 = :p1 and e.p2 = :p2 "
	 * in which, p1,p2 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "select from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params - parameters for the statement
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findAll(Parameters parameters){
		String sql = prepareSelectSql(parameters);
		Query q = entityManager.createQuery(sql);
		setParameters(q,parameters);
		return q.getResultList();
	}

	/**
	 * find the entities with parameters.
	 * equivalent JPQL statement - "select e from E e where e.p1 = :p1 and e.p2 = :p2 order by e.p3 asc "
	 * in which, p1,p2,p3 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "select from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params      - parameters for the statement
	 * @param orderBy     - field names in the order by clause
	 */
	protected List<E> findAll(Parameters parameters, String[] orderBy){
		return findAll(parameters, orderBy, true);
	}

	/**
	 * find the entities with parameters.
	 * equivalent JPQL statement - "select e from E e where e.p1 = :p1 and e.p2 = :p2 order by e.p3 asc "
	 * in which, p1,p2,p3 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "select from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params      - parameters for the statement
	 * @param orderBy     - field names in the order by clause
	 * @param isAscending - true - ascending , false - descending
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findAll(Parameters parameters, String[] orderBy, boolean isAscending){
		String sql = prepareSelectSql(parameters, orderBy,isAscending);

		Query q = entityManager.createQuery(sql);
		setParameters(q,parameters);
		return q.getResultList();
	}

	/**
	 * find the entities with parameters.
	 * equivalent JPQL statement - "select e from E e where e.p1 = :p1 and e.p2 = :p2 order by e.p3 asc "
	 * in which, p1,p2,p3 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "select from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params      - parameters for the statement
	 * @param orderBy     - field names in the order by clause
	 * @param isAscending - true - ascending , false - descending
	 * @param count       - number of rows to return
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findAll(Parameters parameters, String[] orderBy, boolean isAscending,int count){
		String sql = prepareSelectSql(parameters, orderBy,isAscending);

		Query q = entityManager.createQuery(sql);
		setParameters(q,parameters);
		q.setMaxResults(count);
		return q.getResultList();
	}

	/**
	 * find the first entity with parameters.
	 * equivalent JPQL statement - "select e from E e where e.p1 = :p1 and e.p2 = :p2 "
	 * in which, p1,p2 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "select from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params - parameters for the statement
	 */

	protected E findFirst(Parameters parameters){

		String sql = prepareSelectSql(parameters);
		return getSingleResult(sql,parameters);

	}

	@SuppressWarnings("unchecked")
	private E getSingleResult(String sql, Parameters parameters) {

		Query q = entityManager.createQuery(sql);
		setParameters(q,parameters);
		return (E) getSingleResult(q);

	}

	private Object getSingleResult(Query q) {

		q.setMaxResults(1);
		Object obj = null;
		try{
			obj = q.getSingleResult();
		}catch(NoResultException e) {
		}
		return obj;

	}

	/**
	 * find first entity with parameters.
	 * equivalent JPQL statement - "select e from E e where e.p1 = :p1 and e.p2 = :p2 order by e.p3 asc "
	 * in which, p1,p2,p3 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "select from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params      - parameters for the statement
	 * @param orderBy     - field names in the order by clause
	 * @param isAscending - true - ascending , false - descending
	 */
	protected E findFirst(Parameters parameters, String[] orderBy, boolean isAscending){

		String sql = prepareSelectSql(parameters, orderBy,isAscending);
		return getSingleResult(sql, parameters);

	}

	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<E> findAll() {
		return  entityManager.createQuery(getFindAllSql()).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<E> findAll(int startPosition, int pageSize){
		return entityManager.createQuery(getFindAllSql()).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List<E> findAll(Parameters parameters, int startPosition, int pageSize){
		String sql = prepareSelectSql(parameters);

		Query q = entityManager.createQuery(sql).setFirstResult(startPosition).setMaxResults(pageSize);;
		setParameters(q,parameters);
		return q.getResultList();
	}

	/**
	 * find the page of entities with parameters.
	 *
	 * @param params      - parameters for the statement
	 * @param orderBy     - field names in the order by clause
	 * @param isAscending - true - ascending , false - descending
	 * @param startPosition - the offset for the page
	 * @param pageSize - the limit for the page
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findAll(Parameters parameters, String[] orderBy, boolean isAscending, int startPosition, int pageSize){
		String sql = prepareSelectSql(parameters, orderBy, isAscending);

		Query q = entityManager.createQuery(sql).setFirstResult(startPosition).setMaxResults(pageSize);
		setParameters(q,parameters);
		return q.getResultList();
	}

	/**
	 * delete the entities with parameters.
	 * equivalent JPQL statement - "delete from E e where e.p1 = :p1 and e.p2 = :p2 "
	 * in which, p1,p2 are field names of the entity. if the field name is part of the primary key set the parameters as
	 * id.p1 and id.p2. This makes the equivalent JPQL statement -
	 * "delete from E e where e.id.p1 = :p1 and e.id.p2 = :p2 "
	 *
	 * @param params - parameters for the statement
	 * * @return the number of rows deleted
	 */
	@Transactional
	protected int delete(Parameters params){
		String sql = prepareDeleteSql(params);
		return executeUpdate(sql, params);
	}

	/**
	 * Update entity attributes for selected entities.
	 * 
	 * @param updateParams - attributes and values to be updated
	 * @param whereParams - attributes and values to find entities for update.
	 */
	@Transactional
	public int update(Parameters updateParams, Parameters whereParams){
		String sql = prepareUpdateSql(updateParams, whereParams);
		return executeUpdate(sql, updateParams.putAll(whereParams));
	}

	private String prepareUpdateSql(Parameters updateParams, Parameters whereParams) {
		return updateClause + prepareUpdateClause(updateParams) + prepareWhereClause(whereParams);
	}

	private String prepareUpdateClause(Parameters params) {
		if(params == null || params.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		sb.append(" set ");
		boolean isFirst = true;
		for(Map.Entry<String, Object> entry : params.getParameters().entrySet()) {
			if(isFirst) isFirst = false;
			else sb.append(", ");
			sb.append("e.").append(entry.getKey()).append(" = :").append(getSimpleName(entry.getKey()));
		}
		return sb.toString();
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
		getLogger().check("Executed update query : " + sql + " | Parameters :" + (params == null? "": params.toString()));
		return retValue;
	}

	@Transactional
	public List<E> saveAll(List<E> entities) {
		List<E> results = new ArrayList<E>();
		for(E entity: entities) {
			E e = save(entity);
			results.add(e);
		}
		return results;
	}

	public long count() {
		return (Long) entityManager.createQuery(prepareCountClause()).getSingleResult();
	}

	/**
	 * get count of the entity with parameters
	 * equivalent JPQL statement: " select count(e) from E e where e.p1= :p1 and e.p2 = :p2"
	 *
	 * @param paramters - parameters for the statement
	 * @return
	 */
	protected long count(Parameters params) {
		Query q = entityManager.createQuery(prepareCountClause() + prepareWhereClause(params));
		setParameters(q, params);
		return (Long) q.getSingleResult();
	}
	
	/**
	 * Find count by supplied query and parameters.
	 * @param jpql
	 * @param params
	 * @return
	 */
	protected long count(String jpql, Parameters params) {
		Query q = entityManager.createQuery(jpql);
		setParameters(q, params);
		return (Long) q.getSingleResult();
	}

	/**
     * This method selects count by custom sql query .
     * @param sql - select count query
     * @param params - indexed parameters
     * @return
     */
    protected long countByNativeSql(String sql, Parameters params) {
        Query q = entityManager.createNativeQuery(sql);
        setIntIndexedParameters(q, params);
        Object result = q.getSingleResult(); 
        return Long.valueOf(result.toString()).longValue(); 
    }

	/**
	 * get min of a column
	 * equivalent JPQL statement - "select min(e.p1) from E e"
	 *
	 * @param name --- the name of the column
	 * @param c    --- the type of the column
	 * @return the max value of the column
	 */
	@SuppressWarnings("unchecked")
	protected <T> T min(String name, Class<T> c) {
		Query q = entityManager.createQuery(prepareMinClause(name));
		return (T) getSingleResult(q);
	}

	/**
	 * get min of a column with parameters
	 * equivalent JPQL statement - "select min(e.p1) from E e where e.p2 = :p2"
	 *
	 * @param name      --- the name of the column
	 * @param c         --- the type of the column
	 * @param paramters - parameters for the statement
	 * @return the min value of the column
	 */
	@SuppressWarnings("unchecked")
	protected <T> T min(String name, Class<T> c, Parameters params) {
		Query q = entityManager.createQuery(prepareMinClause(name) + prepareWhereClause(params));
		setParameters(q, params);
		return (T) q.getSingleResult();
	}

	/**
	 * get max of a column
	 * equivalent JPQL statement - "select max(e.p1) from E e"
	 *
	 * @param name --- the name of the column
	 * @param c    --- the type of the column
	 * @return the max value of the column
	 */
	@SuppressWarnings("unchecked")
	protected <T> T max(String name, Class<T> c) {
		Query q = entityManager.createQuery(prepareMaxClause(name));
		return (T) getSingleResult(q);
	}

	/**
	 * get max of a column with parameters
	 * equivalent JPQL statement - "select max(e.p1) from E e where e.p2 = :p2"
	 *
	 * @param name      --- the name of the column
	 * @param c         --- the type of the column
	 * @param paramters - parameters for the statement
	 * @return the max value of the column
	 */
	@SuppressWarnings("unchecked")
	protected <T> T max(String name, Class<T> c, Parameters params) {
		Query q = entityManager.createQuery(prepareMaxClause(name) + prepareWhereClause(params));
		setParameters(q, params);
		return (T) getSingleResult(q);
	}

	/**
	 * run a JPQL query to get a list of entities
	 *
	 * @param queryString
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findAllByQuery(String queryString){
		return entityManager.createQuery(queryString).getResultList();
	}

	/**
	 * run a JPQL query to get one entity
	 *
	 * @param queryString
	 * @return entity
	 */
	@SuppressWarnings("unchecked")
	protected E findFirstByQuery(String queryString) {
		Query q = entityManager.createQuery(queryString);
		return (E) getSingleResult(q);
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

	@SuppressWarnings("unchecked")
	protected <T> T findFirstByQuery(String queryString, Class<T> c, Parameters params) {
		Query q = entityManager.createQuery(queryString);
		setParameters(q, params);
		return (T) getSingleResult(q);	
	}	

	@SuppressWarnings("unchecked")
	protected <T> List<T> findByQuery(String queryString, Class<T> c) {
		Query q = entityManager.createQuery(queryString);
		return q.getResultList();
	}

	/**
	 * run a JPQL named query to get a list of entities
	 *
	 * @param queryString
	 * @param paramters   - parameters for the statement
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findAllByNamedQuery(String namedQuery, Parameters params) {
		Query q = entityManager.createNamedQuery(namedQuery);
		setParameters(q, params);
		return q.getResultList(); 
	}

	/**
	 * run a JPQL query to get a list of object arrays
	 *
	 * @param queryString
	 * @param paramters   - parameters for the statement
	 * @return list of object arrays
	 */
	@SuppressWarnings("unchecked")
	protected List findResultListByQuery(String query, Parameters params) {
		Query q = entityManager.createQuery(query);
		setParameters(q, params);
		return q.getResultList(); 
	}

	/**
	 * run a JPQL named query to get one entity
	 *
	 * @param queryString
	 * @param paramters   - parameters for the statement
	 * @return entity
	 */
	@SuppressWarnings("unchecked")
	protected E findFirstByNamedQuery(String namedQuery, Parameters params) {
		Query q = entityManager.createNamedQuery(namedQuery);
		setParameters(q, params);
		return (E) getSingleResult(q); 
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

	private String prepareSelectSql(Parameters params) {

		return selectClause + prepareWhereClause(params);
	}

	protected final String prepareSelectClause() {
		StringBuilder sb = new StringBuilder();
		return sb.append("select e from ").append(entityClass.getName()).append(" as e").toString();
	}

	protected final String prepareCountClause() {
		StringBuilder sb = new StringBuilder();
		return sb.append("select count(e) from ").append(entityClass.getName()).append(" as e").toString();
	}

	private String prepareMinClause(String name) {
		StringBuilder sb = new StringBuilder();
		return sb.append("select min(e.").append(name).append(") from ").append(entityClass.getName()).append(" e").toString();
	}

	private String prepareMaxClause(String name) {
		StringBuilder sb = new StringBuilder();
		return sb.append("select max(e.").append(name).append(") from ").append(entityClass.getName()).append(" e").toString();
	}

	private String prepareWhereClause(Parameters params){
		if(params == null || params.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		sb.append(" where ");
		boolean isFirst = true;
		for(Map.Entry<String, Object> entry : params.getParameters().entrySet()) {
			if(isFirst) { 
				isFirst = false;
			}
			else { 
				sb.append(" and ");
			}
			sb.append("e.").append(entry.getKey());
			if (entry.getValue() instanceof Collection) {
				sb.append(" in ( :").append(getSimpleName(entry.getKey())).append(" )");
			} else {
				sb.append(" = :").append(getSimpleName(entry.getKey()));
			}
		}
		return sb.toString();
	}

	private String getSimpleName(String name) {
		return name.startsWith("id.") ? name.substring(3) : name;
	}

	private String prepareOrderByClause(String[] orderByItems, boolean isAscending){
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		sb.append("order by ");
		for(String item: orderByItems) {
			if(isFirst) isFirst = false;
			else sb.append(" , ");
			sb.append("e.").append(item);
			if(isAscending) sb.append(" asc");
			else sb.append(" desc");
		}

		return sb.toString();
	}

	private String prepareSelectSql(Parameters parameters, String[] orderByItems, boolean isAscending) {
		return prepareSelectSql(parameters) + " " + prepareOrderByClause(orderByItems,isAscending);
	}

	private String prepareDeleteClause(){
		StringBuilder sb = new StringBuilder();
		return sb.append("delete from ").append(entityClass.getName()).append(" as e").toString();
	}

	private String prepareDeleteSql(Parameters params){
		return deleteClause + prepareWhereClause(params);
	}

	protected String getFindAllSql(){
		StringBuilder sb = new StringBuilder();
		return sb.append("select e from ").append(entityClass.getName()).append(" e").toString();
	}

	private void setParameters(Query q, Parameters params) {
		if(params == null || params.size() == 0) return;
		for(Map.Entry<String, Object> entry : params.getParameters().entrySet()) {
			q.setParameter(getSimpleName(entry.getKey()), entry.getValue());
		}
	}

	private void setIntIndexedParameters(Query q, Parameters params) {
		if(params == null || params.size() == 0) return;
		for(Map.Entry<String, Object> entry : params.getParameters().entrySet()) {
			q.setParameter(Integer.parseInt(getSimpleName(entry.getKey())), entry.getValue());
		}
	}

	/**
	 * run a JPQL query to get a single entity
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	protected E findFirstByQuery(String queryString, Parameters params) {
		return getSingleResult(queryString, params);
	}

	/**
	 * run a JPQL query to get a list of entity
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	protected List<E> findAllByQuery(String queryString, Parameters params){
		Query q = entityManager.createQuery(queryString);
		setParameters(q, params);
		return q.getResultList(); 
	}

	/**
	 * run a JPQL query to get a page of entity
	 * 
	 * @param queryString - JPQL query to run
	 * @param params - parameters for the statement
	 * @param startPosition - the offset for the page
	 * @param pageSize - the limit for the page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	protected List<E> findAllByQuery(String queryString, Parameters params, int startPosition, int pageSize){
		Query q = entityManager.createQuery(queryString).setFirstResult(startPosition).setMaxResults(pageSize);
		setParameters(q, params);
		return q.getResultList(); 
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	protected List<E> findAllByQuery(String queryString, Parameters params,int count){
		Query q = entityManager.createQuery(queryString);
		setParameters(q, params);
		q.setMaxResults(count);
		return q.getResultList(); 
	}

	/**
	 * run a Native query to get a list of entity
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected E findFirstByNativeQuery(String queryString, Parameters params) {
		Query q = entityManager.createNativeQuery(queryString, entityClass);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return (E) getSingleResult(q);
	}

	@SuppressWarnings("unchecked")
	protected List<E> findAllByNativeQuery(String queryString, Parameters params){
		Query q = entityManager.createNativeQuery(queryString, entityClass);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return q.getResultList(); 
	}
	
	/**
	 * Returns a section of results for the given native query.
	 * @param queryString - native query string to run
	 * @param params - parameters for the query
	 * @param limit - the value of the LIMIT clause
	 * @param offset - the value of the OFFSET clause
	 * @return
	 */
	protected List<E> findAllByNativeQuery(String queryString, Parameters params, int limit, int offset) {
		return findAllByNativeQuery(String.format(NATIVE_PAGE_CLAUSE, queryString, limit, offset), params);
	}
	
	@SuppressWarnings("unchecked")
	protected List findResultListByNativeQuery(String queryString, Parameters params){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return q.getResultList(); 
	}
	
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> findResultMapByNativeQuery(String queryString, Parameters params) {
		Query q = entityManager.createNativeQuery(queryString, Map.class);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List findResultListByNativeQuery(String queryString, Parameters params,int count){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		q.setMaxResults(count);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return q.getResultList(); 
	}

	@SuppressWarnings("unchecked")
	protected List<E> findAllByNativeQuery(String queryString, Parameters params, int count){
		Query q = entityManager.createNativeQuery(queryString, entityClass);
		setIntIndexedParameters(q, params);
		q.setMaxResults(count);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
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
		boolean isDto = IDto.class.isAssignableFrom(resultClass);
		Query q = entityManager.createNativeQuery(queryString, isDto ? Map.class : resultClass);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		if(!isDto) return (T) getSingleResult(q);
		else {
			return (T) DtoFactory.getDto(resultClass, (Map<String,Object>)getSingleResult(q));
		}
	}
	
	protected <T> List<T> findAllByNativeQuery(String queryString, Parameters params, Class<T> resultClass){
		return findAllByNativeQuery(queryString, params, resultClass, -1);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> List<T> findAllByNativeQuery(String queryString, Parameters params, Class<T> resultClass, int count){
		boolean isDto = IDto.class.isAssignableFrom(resultClass);
		Query q = entityManager.createNativeQuery(queryString, isDto ? Map.class : resultClass);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		if(count >=0) q.setMaxResults(count);
		if(!isDto) return q.getResultList();
		else return DtoFactory.getDtoList(resultClass,q.getResultList());
	}

	@Transactional
	protected int executeNativeUpdate(String queryString, Parameters params){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		int retValue = q.executeUpdate();
		getLogger().check("Executed update query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return retValue;
	}

	protected int executeNative(String queryString, Parameters params){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		int retValue = q.executeUpdate();
		getLogger().check("Executed update query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return retValue;
	}

	@SuppressWarnings("unchecked")
	protected List<Object[]> executeNative(String queryString){
		Query q = entityManager.createNativeQuery(queryString);
		getLogger().database("Executed native query : " + queryString);
		return q.getResultList(); 
	}

	@SuppressWarnings("unchecked")
	protected List<Object[]> executeNative(Parameters params, String queryString){
		Query q = entityManager.createNativeQuery(queryString);
		setIntIndexedParameters(q, params);
		getLogger().database("Executed native query : " + queryString + " | Parameters : " + (params == null? "": params.toString()));
		return q.getResultList(); 
	}

	@Transactional
	protected int executeNativeUpdate(String queryString){
		Query q = entityManager.createNativeQuery(queryString);
		int retValue = q.executeUpdate();
		getLogger().check("Executed update query : " + queryString);
		return retValue;
	}
	
	private String prepareUpdateClause() {
		StringBuilder sb = new StringBuilder();
		return sb.append("update ").append(entityClass.getName()).append(" e").toString();
	}

	public static String getFieldName(String methodName) {
		String fieldName = "";
		try {
			if (methodName != null)
				if (methodName.startsWith("get")) {
					fieldName = methodName.substring(3, 4).toLowerCase()
							+ methodName.substring(4);
				} else if (methodName.startsWith("is")) {
					fieldName = methodName.substring(2, 3).toLowerCase()
							+ methodName.substring(3);
				}

		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return fieldName;
	}

	public List<ValidationInfoMessage> validate(E clone, K key,
			List<ValidationType> typeList) {
		List<ValidationInfoMessage> validationResults = new ArrayList<ValidationInfoMessage>();
		try {
			Iterator<ValidationType> typeIter = typeList.iterator();

			while (typeIter.hasNext()) {
				List<Field> completeFieldList = new ArrayList<Field>();
				List<Method> completeMethodList = new ArrayList<Method>();

				E original = findByKey(key);
				ValidationType type = typeIter.next();
				if (type.equals(ValidationType.Existence)) {
					if (original == null) {
						ValidationInfoMessage infoMessage = new ValidationInfoMessage(
								ValidationType.NonExistence,
								"Entity does not exist");
						validationResults.add(infoMessage);
					}
					break;
				}
				Class<?> c = original.getClass();
				Method[] subClassMethods = c.getMethods();
				Collections.addAll(completeMethodList, subClassMethods);

				Field[] subClassFields = c.getDeclaredFields();
				Collections.addAll(completeFieldList, subClassFields);
				Class<?> superClass = c.getSuperclass();
				while (superClass != null) {

					Field[] superClassFields = superClass.getDeclaredFields();
					Collections.addAll(completeFieldList, superClassFields);
					superClass = superClass.getSuperclass();
				}

				if (type.equals(ValidationType.Equals)) {

					for (Method method : completeMethodList) {
						String methodName = method.getName();

						if (methodName.startsWith("get")
								|| methodName.startsWith("is")) {

							String fieldName = getFieldName(methodName);
							for (Field field : completeFieldList) {
								if (field.getName().equals(fieldName)) {

									Object cloneFieldValue = null;
									Object originalFieldValue = null;
									cloneFieldValue = method.invoke(clone);
									originalFieldValue = method.invoke(original);
									if ((cloneFieldValue != null)
											&& (!cloneFieldValue
													.equals(originalFieldValue))) {
										ValidationInfoMessage infoMessage = new ValidationInfoMessage(
												ValidationType.Equals,
												fieldName + " does not match");
										getLogger().info(methodName	+ " did not match for the Clone and Original entities.");
										validationResults.add(infoMessage);

									}
								}
							}

						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return validationResults;

	}

	public Timestamp getDatabaseTimeStamp(){
		Query q = entityManager.createNativeQuery(CURRENT_TIMESTAMP_QUERY, Timestamp.class);
		return (Timestamp) getSingleResult(q);
	}

	protected int getInt(Object val) {
		if (val != null) {
			if (val instanceof Integer)
				return (Integer) val;
			if (val instanceof BigDecimal)
				return ((BigDecimal) val).intValue();
			if (val instanceof Double)
				return ((Double) val).intValue();
			if (val instanceof String)
				return Integer.parseInt((String) val);
		} else {
			return 0;
		}
		return 0;
	}

	protected String getString(Object val) {
		if (val != null) {
			return ((String) val).trim();
		}
		return "";
	}

	protected Class<E> getEntityClass() {
		return entityClass;
	}
	
  public List<Object[]> findAllColumnLengths(String tableName) {
		Parameters params = Parameters.with("1", tableName);
		return findAllByNativeQuery(SELECT_ALL_COLUMN_LENGTHS, params,Object[].class);
	}
  
  private Logger getLogger(){
	  return Logger.getLogger(LOG_NAME);
  }
  
  /**
	 * This method will be used to derive referenced field values from different
	 * table for auditing purpose. All the values will be separated by space.
	 * 
	 */
	protected String fetchAuditPrimaryKeyValue(String queryString,Parameters params) {
		StringBuilder auditPrimaryKey = new StringBuilder();
		Object[] queryData = findFirstByNativeQuery(queryString, params, Object[].class);
		if (queryData != null) {
			for (Object columnValue : queryData) {
				if (columnValue != null && columnValue.toString().trim().length() > 0) {
					auditPrimaryKey.append(columnValue.toString().trim()).append(" ");
				}
			}
		}
		return auditPrimaryKey.toString().trim();
	}
	
  	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<E> findAll(String start, int startPosition, int pageSize){
		return entityManager.createQuery(getFindAllBetweenSql(start, null)).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
		
	}

    protected  String getFindAllBetweenSql(String start, String end) {
        StringBuilder sb = new StringBuilder();
		sb.append("select e from ").append(entityClass.getName()).append(" e ");
        sb.append( "where e.createTimestamp > '").append(start).append("' ");
        if(null != end)
        	sb.append(" and e.createTimestamp < '").append(end).append("' ");
        
        sb.append( "order by e.createTimestamp asc");
		return sb.toString();
	}


	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<E> findAll(String start, String end, int startPosition, int pageSize){
		
		return entityManager.createQuery(getFindAllBetweenSql(start, end)).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
		
	}

}
