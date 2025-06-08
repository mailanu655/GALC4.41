package com.honda.galc.test.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.service.IDaoService;

import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * 
 * <h3>BaseDaoTest</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BaseDaoTest description </p>
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
 * @author Jeffray Huang
 * Mar 3, 2010
 *
 * @param <T>
 */
public abstract class BaseDaoTest<T extends IDaoService> {
	protected Class<T> daoImplClass;
	protected T daoImpl;
	protected boolean isFindAll = true;
	
	public BaseDaoTest(){
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		daoImplClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
		daoImpl = getDao(daoImplClass);
	}
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
    }
	
	@SuppressWarnings("unchecked")
	@Test 
	public void crudTest(){
		Object entity = createEntity();
		
		// insert
		daoImpl.save(entity);
		// update
		daoImpl.save(updateEntity(entity));
		// find by key
		daoImpl.findByKey(findPrimaryKey(entity));
		// delete
		daoImpl.remove(entity);
		// findAll
		if(isFindAll) daoImpl.findAll();
		// count
		daoImpl.count();
		
	}
	
	private Object findPrimaryKey(Object entity) {
	    Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field currentField : fields){
            try {
            	currentField.setAccessible(true);
                if (currentField.isAnnotationPresent(Id.class) || currentField.isAnnotationPresent(EmbeddedId.class)){
                	return currentField.get(entity);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
	}
	
    abstract Object createEntity();
    	
    abstract Object updateEntity(Object entity);
}
