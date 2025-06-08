package com.honda.galc.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.validation.ValidationInfoMessage;
import com.honda.galc.common.validation.ValidationType;

/**
 * An Interface to define the basic database operations
 *
 * @author is08925
 * @param <E> -- entity class
 * @param <K> -- primary key class
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public interface IDaoService<E, K> extends IService{

    /**
     * save an entity. If the entity is new, insert otherwise update
     *
     * @param entity - entity to be saved
     * @return merged entity
     */
    public E save(E entity);
    
    
    /**
     * save all entities
     *
     * @param entities - entities to be saved
     * @return list of updated entities
     */
    public List<E> saveAll(List<E> entities);

    /**
     * insert a new entity.
     *
     * @param entity - entity to be inserted
     * @return merged entity
     */
    
    public E insert(E entity);
    
    /**
     * insert all entities
     *
     * @param entities - entities to be inserted
     */
    public void insertAll(List<E> entities);

    /**
     * update an existing entity.
     *
     * @param entity - entity to be updated
     * @return merged entity
     */
    
    public E update(E entity);
    
    /**
     * update all entities
     *
     * @param entities - entities to be updated
     */
    public void updateAll(List<E> entities);

    /**
     * remove an entity
     *
     * @param entity - entity to be removed
     */
    public void remove(E entity);

    /**
     * remove an entity by primary key
     *
     * @param id - primary key of the entity
     */
    public void removeByKey(K id);

    /**
     * remove all entities in the table
     */
    public void removeAll();

    /**
     * remove all the enities in the list
     * @param entities
     */
    public void removeAll(List<E> entities);
    
    /**
     * find the entity by primary key
     *
     * @param id - primary key object of the entity
     * @return
     */
    public E findByKey(K id);

    /**
     * find all the entities in the tables
     * equivalent JPQL statement - "select e from E e"
     *
     * @return
     */
    public List<E> findAll();

    /**
     * find all the entities with pagination. This is used for querying large result set  
     * @param startPosition - start position
     * @param pageSize - page size
     * @return
     */
    public List<E> findAll(int startPosition, int pageSize);
    
    public List<E> findAll(String start, int startPosition, int pageSize);
    public List<E> findAll(String start, String end, int startPosition, int pageSize);
    
    /**
     * get the counts of the entity
     *
     * @return counts
     */
    public long count();

    public List<ValidationInfoMessage> validate(E entity,K key,List<ValidationType> typeList);
	
    public Timestamp getDatabaseTimeStamp();
    
    public List<Object[]> findAllColumnLengths(String tableName);
}

