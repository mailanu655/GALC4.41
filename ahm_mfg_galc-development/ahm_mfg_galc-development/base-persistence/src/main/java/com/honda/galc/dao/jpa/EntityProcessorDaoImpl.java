package com.honda.galc.dao.jpa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EntityProcessorDao;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.DeleteEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.InsertEntity;
import com.honda.galc.entitypersister.SaveEntity;
import com.honda.galc.entitypersister.UpdateEntity;

/**
 * @author Fredrick Yessaian
 * @since Nov 01 2012
 * @version 0.1
 */

public class EntityProcessorDaoImpl extends BaseDaoImpl<Object, Object>
		implements EntityProcessorDao<Object, Object>, Serializable {

	private static final long serialVersionUID = 95614362934733572L;
	private static final String LOG_NAME = "JPA_LOG";
	Logger logger;

	@Transactional
	public void ProcessEntityList(List<EntityList<AbstractEntity>> masterList) {

		Iterator<EntityList<AbstractEntity>> masterEntityList = masterList.iterator();

		while (masterEntityList.hasNext()) {

			EntityList<AbstractEntity> entityList = masterEntityList.next();

			getLogger().info("EntityProcessorDaoImpl :: Start :: operation :: " + entityList.getOperatedFor() + " :: for Product :: " + entityList.getProductId());
			if(entityList.getPartId() != null)
				getLogger().info("Processing for :: " + entityList.getPartId());
				
			for (AbstractEntity entity : entityList) {

				getLogger().info("Processing entity :: " + entity.getEntityString());
				
				if (entity instanceof InsertEntity) {
					entityManager.persist(entity.getEntity());
					getLogger().check("Persisted entity : " + entity.toString());
				} else if (entity instanceof UpdateEntity || entity instanceof SaveEntity) {
					entityManager.merge(entity.getEntity());
					getLogger().check("Merged entity : " + entity.toString());
				} else if (entity instanceof DeleteEntity) {
					Object newEntity = entityManager.merge(entity.getEntity());
					entityManager.remove(newEntity);
					getLogger().check("Removed entity : " + entity.toString());
				}
			}
			getLogger().info("EntityProcessorDaoImpl :: End :: For operation :: " + entityList.getOperatedFor() + " for :: " + entityList.getProductId());
			entityList = null;
		}
		masterList = null;
	}
	
	Logger getLogger() {
		return (logger == null) ? logger = Logger.getLogger(LOG_NAME) : logger;
	}
}